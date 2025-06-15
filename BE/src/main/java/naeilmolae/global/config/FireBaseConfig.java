package naeilmolae.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FireBaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FireBaseConfig.class);

    @Value("${firebase.service-account.path-ios}")
    private Resource serviceAccountIos;

    @Value("${firebase.service-account.path-android}")
    private Resource serviceAccountAndroid;

    @PostConstruct
    public void initializeFirebaseApps() throws IOException {

        // Firebase App for iOS
        String iosAppName = "ios";
        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals(iosAppName))) {
            FirebaseOptions optionsIos = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountIos.getInputStream()))
                    .build();

            FirebaseApp.initializeApp(optionsIos, iosAppName);
            logger.info("Initialized Firebase App for iOS: {}", iosAppName);
        }

        // Firebase App for Android
        String androidAppName = "android";
        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals(androidAppName))) {
            FirebaseOptions optionsAndroid = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountAndroid.getInputStream()))
                    .build();

            FirebaseApp.initializeApp(optionsAndroid, androidAppName);
            logger.info("Initialized Firebase App for Android: {}", androidAppName);
        }
    }
}
