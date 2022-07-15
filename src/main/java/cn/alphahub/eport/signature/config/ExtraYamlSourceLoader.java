package cn.alphahub.eport.signature.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 加载classpath下的yaml文件到spring环境
 *
 * @author weasley
 * @version 1.0
 * @date 2022/7/12
 */
@Component
@AutoConfigureBefore({MailProperties.class})
public class ExtraYamlSourceLoader {

    /**
     * 使用YamlPropertiesFactoryBean加载yaml配置文件
     *
     * @return dttPropertySourcesPlaceholderConfigurer
     */
    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean factoryBean = new YamlPropertiesFactoryBean();
        factoryBean.setResources(new ClassPathResource("mail/email-config.yml"));
        configurer.setProperties(Objects.requireNonNull(factoryBean.getObject()));
        return configurer;
    }
}
