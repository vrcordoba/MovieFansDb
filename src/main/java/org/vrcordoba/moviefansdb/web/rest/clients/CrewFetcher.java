package org.vrcordoba.moviefansdb.web.rest.clients;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.client.RestTemplate;
import org.vrcordoba.moviefansdb.domain.CrewMember;
import org.vrcordoba.moviefansdb.domain.Movie;
import org.vrcordoba.moviefansdb.repository.CrewMemberRepository;
import org.vrcordoba.moviefansdb.security.SecurityUtils;
import org.vrcordoba.moviefansdb.web.rest.util.RestTemplateUtil;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CrewFetcher<C extends CrewMember, R extends JpaRepository> {

    private final Movie movie;
    private final RestTemplate restTemplate;

    public static final Pattern NAME_EXTRACTOR = Pattern.compile("^(\\w+\\s+\\w+)");

    public CrewFetcher(final Movie movie) {
        this.movie = movie;
        restTemplate = RestTemplateUtil.createRestTemplateWithTextHtmlSupport();
    }

    public boolean fetch(
            final String name,
            final C crewMemberToFetch,
            final R repository) {
        ImdbIdFetcher imdbIdFetcher = new ImdbIdFetcher(
            ImdbQueryType.ACTOR_DIRECTOR,
            RestTemplateUtil.prepareForQuery(name));
        Optional<String> imdbId = imdbIdFetcher.fetchId();
        boolean found = imdbId.isPresent();
        if (found) {
            ObjectNode fetchedInformation = restTemplate.getForObject(
                RestTemplateUtil.IMDB_WMS_SEARCH_URL + imdbId.get(),
                ObjectNode.class);
            crewMemberToFetch.setName(name);
            saveFetchedCrewMemberInformation(
                fetchedInformation,
                crewMemberToFetch,
                repository);
        }
        return found;
    }

    private void saveFetchedCrewMemberInformation(
            final ObjectNode fetchedInformation,
            final C crewMemberToFetch,
            final R repository) {
        crewMemberToFetch.setImdbId(fetchedInformation.get("data").get("id").asText());
        crewMemberToFetch.setBiography(fetchedInformation.get("data").get("description").asText());
        crewMemberToFetch.setCreator(SecurityUtils.getCurrentUserLogin());

        Set<Movie> currentMovies = crewMemberToFetch.getMovies();
        currentMovies.add(movie);
        crewMemberToFetch.setMovies(currentMovies);

        List<CrewMember> recoveredCrewMembers = ((CrewMemberRepository)repository).findByImdbId(
            crewMemberToFetch.getImdbId());
        if (!recoveredCrewMembers.isEmpty()) {
            crewMemberToFetch.setId(recoveredCrewMembers.get(0).getId());
        }
        repository.save(crewMemberToFetch);
    }
}