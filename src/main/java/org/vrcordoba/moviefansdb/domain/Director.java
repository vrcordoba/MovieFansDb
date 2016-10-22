package org.vrcordoba.moviefansdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Director.
 */
@Entity
@Table(name = "director")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Director implements CrewMember, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "biography")
    private String biography;

    @NotNull
    @Column(name = "imdb_id", nullable = false)
    private String imdbId;

    @Column(name = "creator")
    private String creator;

    @OneToMany(mappedBy = "director")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Movie> movies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Director name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public Director biography(String biography) {
        this.biography = biography;
        return this;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getImdbId() {
        return imdbId;
    }

    public Director imdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    public Director movies(Set<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public Director addMovies(Movie movie) {
        movies.add(movie);
        movie.setDirector(this);
        return this;
    }

    public Director removeMovies(Movie movie) {
        movies.remove(movie);
        movie.setDirector(null);
        return this;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    public String getCreator() {
        return creator;
    }

    public Director creator(String creator) {
        this.creator = creator;
        return this;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Director director = (Director) o;
        if(director.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, director.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Director{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", biography='" + biography + "'" +
            ", imdbId='" + imdbId + "'" +
            ", creator='" + creator + "'" +
            '}';
    }
}
