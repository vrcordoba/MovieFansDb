package org.vrcordoba.moviefansdb.web.rest.util;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtil {

    public static final String IMDB_WMS_SEARCH_URL =
      "http://imdb.wemakesites.net/api/";

    public static final String OMDB_SEARCH_URL =
        "http://www.omdbapi.com/?plot=full&r=json&i=";

    public static String prepareForQuery(String inputString) {
        return inputString.replaceAll("\\p{Space}", "+");
    }

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
