package org.vrcordoba.moviefansdb.web.rest.clients;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ImdbIdFetcher {

    private final ImdbQueryType imdbQueryType;
    private final String imdbSearchUrl;
    private RestTemplate restTemplate;

    private static final String IMDB_QUERY_FIRST_PART =
        "http://www.imdb.com/xml/find?json=1&nr=1&";
    private static final String IMDB_QUERY_SECOND_PART = "=on&q=";

    public ImdbIdFetcher(final ImdbQueryType imdbQueryType, final String searchTerm) {
        this.imdbQueryType = imdbQueryType;
        imdbSearchUrl = IMDB_QUERY_FIRST_PART
            + this.imdbQueryType.getShortType()
            + IMDB_QUERY_SECOND_PART
            + searchTerm;
        restTemplate = createRestTemplateWithTextHtmlSupport();
    }

    private RestTemplate createRestTemplateWithTextHtmlSupport() {
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

    public Optional<String> fetchId() {
        ObjectNode data = restTemplate.getForObject(imdbSearchUrl, ObjectNode.class);
        Optional<String> fetchedId = fetchPopularId(data);
        if (fetchedId.isPresent()) {
            return fetchedId;
        }
        fetchedId = fetchExactId(data);
        if (fetchedId.isPresent()) {
            return fetchedId;
        }
        fetchedId = fetchSubstringId(data);
        if (fetchedId.isPresent()) {
            return fetchedId;
        }
        return fetchApproxId(data);
    }

    private Optional<String> fetchPopularId(final ObjectNode data) {
        return fetchId(data, "_popular");
    }

    private Optional<String> fetchExactId(final ObjectNode data) {
        return fetchId(data, "_exact");
    }

    private Optional<String> fetchSubstringId(final ObjectNode data) {
        return fetchId(data, "_substring");
    }

    private Optional<String> fetchApproxId(final ObjectNode data) {
        return fetchId(data, "_approx");
    }

    private Optional<String> fetchId(final ObjectNode data, final String category) {
      try {
          return Optional.of(data.get(imdbQueryType.getLongType() + category)
              .get(0)
              .get("id")
              .asText());
      } catch (NullPointerException e) {
          return Optional.empty();
      }
    }
}
