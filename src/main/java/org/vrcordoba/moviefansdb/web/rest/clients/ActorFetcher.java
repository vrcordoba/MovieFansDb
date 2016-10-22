package org.vrcordoba.moviefansdb.web.rest.clients;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.web.client.RestTemplate;
import org.vrcordoba.moviefansdb.domain.Actor;
import org.vrcordoba.moviefansdb.domain.Movie;
import org.vrcordoba.moviefansdb.repository.ActorRepository;
import org.vrcordoba.moviefansdb.security.SecurityUtils;
import org.vrcordoba.moviefansdb.web.rest.util.RestTemplateUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ActorFetcher {

    private final String name;
    private final ImdbIdFetcher imdbIdFetcher;
    private RestTemplate restTemplate;
    private ActorRepository actorRepository;

    private static final Pattern NAME_EXTRACTOR = Pattern.compile("^(\\w+\\s+\\w+)");

    private static final String IMDB_WMS_SEARCH_URL =
        "http://imdb.wemakesites.net/api/";

    public ActorFetcher(
        final String name,
        final ActorRepository actorRepository) {
        this.name = name;
        imdbIdFetcher = new ImdbIdFetcher(
            ImdbQueryType.ACTOR_DIRECTOR,
            prepareForQuery(name));
        restTemplate = RestTemplateUtil.createRestTemplateWithTextHtmlSupport();
        this.actorRepository = actorRepository;
    }

    private String prepareForQuery(String inputString) {
        return inputString.replaceAll("\\p{Space}", "+");
    }

    public Actor fetch() {
        Optional<String> imdbId = imdbIdFetcher.fetchId();
        Actor actor = null;
        if (imdbId.isPresent()) {
            ObjectNode data = restTemplate.getForObject(
                IMDB_WMS_SEARCH_URL + imdbId.get(),
                ObjectNode.class);
            actor = parseJson(data);
        }
        actorRepository.save(actor);
        return actor;
    }

    private Actor parseJson(final ObjectNode data) {
        Actor actor = new Actor();
        try {
            actor.setName(NAME_EXTRACTOR.matcher(data.get("data").get("title").asText()).group(1));
        } catch (Exception e) {
            actor.setName(name);
        }
        actor.setImdbId(data.get("data").get("id").asText());
        actor.setBiography(data.get("data").get("description").asText());
        actor.setCreator(SecurityUtils.getCurrentUserLogin());
        List<Actor> recoveredActors = actorRepository.findByImdbId(actor.getImdbId());
        if (!recoveredActors.isEmpty()) {
            actor.setId(recoveredActors.get(0).getId());
        }
        return actor;
    }
}
