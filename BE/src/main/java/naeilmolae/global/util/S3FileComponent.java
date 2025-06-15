package naeilmolae.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import naeilmolae.global.common.exception.RestApiException;
import naeilmolae.global.common.exception.code.status.GlobalErrorStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3FileComponent {
    private final AmazonS3Client amazonS3Client; // AmazonS3Client 객체 주입
    @Value("${cloud.aws.s3.bucket}") // S3 버킷 이름
    private String bucket;

    public String uploadFile(String category, MultipartFile multipartFile) {
        // 파일명
        String fileName = createFileName(category, Objects.requireNonNull(multipartFile.getOriginalFilename()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        // 바이트 배열로 파일 내용 읽기
        byte[] bytes;
        try {
            bytes = multipartFile.getBytes();
        } catch (IOException e) {
            // 파일 읽기 실패
            log.error(e.getMessage());
            throw new RestApiException(GlobalErrorStatus._FALIED_READ_FILE);
        }

        // Content-Length 설정
        objectMetadata.setContentLength(bytes.length);

        // ByteArrayInputStream 생성
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // S3에 업로드
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonS3Exception e) {
            // S3 업로드 실패 시 에러 코드 분기
            log.error(e.getMessage());
            throw new RestApiException(GlobalErrorStatus._S3_UPLOAD_ERROR);
        }

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 파일명 생성
     * @param category
     * @param originalFileName
     * @return 작명된 파일 이름
     */
    public String createFileName(String category, String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(".");
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String random = String.valueOf(UUID.randomUUID());

        // 파일명 생성, 같은 파일명이 있을 경우 덮어쓰기 방지
        if(category.isEmpty()){
            return fileName + "_" + random + fileExtension;
        }
        return category + "/" + fileName + "_" + random + fileExtension;
    }

    /**
     * Pre-signed URL 생성
     * @param fileName
     * @param contentType
     * @return
     */
    public URL generatePresignedUrl(String fileName, String contentType) {
        // URL 만료 시간 (10분 후)
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60 * 10); // 10 minutes

        // Pre-signed URL 생성 요청
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT) // 업로드를 위한 PUT 요청
                        .withExpiration(expiration)
                        .withContentType(contentType); // Content-Type 지정

        return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /**
     * 파일 삭제
     * @param fileUrl
     */
    public void deleteFile(String fileUrl) {
        String[] deleteUrl = fileUrl.split("/", 4);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, deleteUrl[3]));
    }

    public byte[] getVoiceFile(String fileUrl) {
        return null;
    }
}
