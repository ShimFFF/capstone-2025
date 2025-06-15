package naeilmolae.global.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/message"); // i18n 폴더의 message.properties 지정
        messageSource.setDefaultEncoding("UTF-8"); // UTF-8 인코딩 설정
        return messageSource;
    }
}
