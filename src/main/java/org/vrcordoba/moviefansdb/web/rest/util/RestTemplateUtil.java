package org.vrcordoba.moviefansdb.web.rest.util;

import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {

    public static RestTemplate createRestTemplateWithTextHtmlSupport() {
        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        for(HttpMessageConverter<?> converter : converters) {
            if(converter instanceof MappingJackson2HttpMessageConverter) {
                try {
                    ((AbstractHttpMessageConverter<Object>) converter).setSupportedMediaTypes(
                        Collections.singletonList(MediaType.TEXT_HTML));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return template;
    }
}
