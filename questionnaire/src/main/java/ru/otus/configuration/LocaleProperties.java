package ru.otus.configuration;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Data
@Configuration
@ConfigurationProperties(prefix = "locale")
public class LocaleProperties {

    private String country;
    private String language;

    public Locale getLocale() {
        return StringUtils.isEmpty(country) || StringUtils.isEmpty(language)
                ? Locale.US
                : new Locale(language, country);
    }
}
