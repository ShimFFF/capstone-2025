package naeilmolae.domain.member.strategy.context;

import lombok.RequiredArgsConstructor;
import naeilmolae.domain.member.domain.LoginType;
import naeilmolae.domain.member.dto.response.MemberLoginResponseDto;
import naeilmolae.domain.member.strategy.LoginStrategy;
import naeilmolae.domain.member.strategy.impl.AnonymousLoginStrategy;
import naeilmolae.domain.member.strategy.impl.AppleLoginStrategy;
import naeilmolae.domain.member.strategy.impl.KakaoLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginContext {

    private final Map<LoginType, LoginStrategy> strategyMap;

    @Autowired
    public LoginContext(List<LoginStrategy> strategies) {
        this.strategyMap = new HashMap<>();
        strategies.forEach(strategy -> {
            if (strategy instanceof KakaoLoginStrategy) {
                strategyMap.put(LoginType.KAKAO, strategy);
            } else if (strategy instanceof AnonymousLoginStrategy) {
                strategyMap.put(LoginType.ANOYMOUS, strategy);
            } else if (strategy instanceof AppleLoginStrategy) {
                strategyMap.put(LoginType.APPLE, strategy);
            } else {
                throw new IllegalArgumentException("Unsupported login strategy: " + strategy.getClass().getName());
            }
        });
    }

    public MemberLoginResponseDto executeStrategy(String accessToken, LoginType loginType) {
        LoginStrategy strategy = strategyMap.get(loginType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported login type: " + loginType);
        }
        return strategy.login(accessToken);
    }
}
