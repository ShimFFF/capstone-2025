package naeilmolae.domain.member.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import naeilmolae.domain.member.dto.client.ApplePublicKeyResponse;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.AuthErrorStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
public class AppleMemberClient {
    private WebClient webClient;

    public AppleMemberClient(WebClient.Builder webclientBuilder){
        this.webClient = webclientBuilder
                .baseUrl("https://appleid.apple.com/auth")
                .build();
    }

    public String getClientId(final String accessToken) {
        Claims claims = getClaimsBy(accessToken);
        validateClaims(claims);

        return claims.getSubject();
    }

    /*
     * 애플 검증 단계 (https://developer.apple.com/documentation/sign_in_with_apple/sign_in_with_apple_rest_api/verifying_a_user)
     * 1. 서버의 공개 키를 사용하여 JWS E256 서명을 검증
     * 2. 인증에 대한 nonce를 검증
     * 3. iss 필드가 https://appleid.apple.com을 포함하는지 확인
     * 4. aud 필드가 개발자의 클라이언트 ID를 포함하는지 확인
     * 5. 현재 시간을 기준으로 exp 필드가 유효한지 확인
     * */

    //1. Apple의 공개키를 사용하여 identityToken을 검증

    //InvalidKeySpecException, NoSuchAlgorithmException, UnsupportedEncodingException, JsonProcessingException
    public Claims getClaimsBy(String identityToken) {

        if (identityToken == null || !identityToken.contains(".")) {
            throw new RestApiException(AuthErrorStatus.INVALID_APPLE_ID_TOKEN); // 400 Bad Request로 처리
        }

        try {
            ApplePublicKeyResponse response = getAppleAuthPublicKey(); //공개키 가져오기

            //공개 키를 사용하여 identityToken을 검증
            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new RestApiException(AuthErrorStatus.FAILED_GET_APPLE_KEY)); //공개키를 가져오는데 실패

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();
        }catch (InvalidKeySpecException | NoSuchAlgorithmException | UnsupportedEncodingException |
                JsonProcessingException e) {
            throw new RestApiException(AuthErrorStatus.FAILED_GET_APPLE_KEY);
        }
    }

    public ApplePublicKeyResponse getAppleAuthPublicKey() { //Apple의 공개키를 가져오기
        return webClient.get()
                .uri("/keys")
                .retrieve()
                .bodyToMono(ApplePublicKeyResponse.class)
                .block();
    }

    //2. 인증에 대한 nonce를 검증
    //3. iss 필드가 https://appleid.apple.com을 포함하는지 확인
    //4. aud 필드가 개발자의 클라이언트 ID를 포함하는지 확인
    //5. 현재 시간을 기준으로 exp 필드가 유효한지 확인
    public void validateClaims(Claims claims) {
        if (claims == null){
            throw new RestApiException(AuthErrorStatus.FAILED_SOCIAL_LOGIN);
        }

        if (!claims.getIssuer().contains("https://appleid.apple.com"))
            throw new RestApiException(AuthErrorStatus.INVALID_APPLE_ID_TOKEN);

        // 우리 앱으로 발급 받은 것이 맞는 지  (애플 앱 ID가 일치하는 지)
        //if (!claims.getAudience().contains("com.networkingService.umc"))
        //    throw new RestApiException(AuthErrorCode.INVALID_APPLE_ID_TOKEN);

        if (claims.getExpiration().before(new java.util.Date()))
            throw new RestApiException(AuthErrorStatus.INVALID_APPLE_ID_TOKEN);
    }
}

