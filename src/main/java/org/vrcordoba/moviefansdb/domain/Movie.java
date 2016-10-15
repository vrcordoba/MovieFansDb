package org.vrcordoba.moviefansdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "imdb_id", nullable = false)
    private String imdbId;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "plot")
    private String plot;

    @DecimalMin(value = "0")
    @DecimalMax(value = "10")
    @Column(name = "rating")
    private Float rating;

    @Column(name = "genre")
    private String genre;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "movie_cast",
               joinColumns = @JoinColumn(name="movies_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="casts_id", referencedColumnName="ID"))
    private Set<Actor> casts = new HashSet<>();

    @ManyToOne
    private Director director;

    @OneToMany(mappedBy = "movie")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Review> reviews = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User creator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public Movie imdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public Movie title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public Movie date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPlot() {
        return plot;
    }

    public Movie plot(String plot) {
        this.plot = plot;
        return this;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public Float getRating() {
        return rating;
    }

    public Movie rating(Float rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public Movie genre(String genre) {
        this.genre = genre;
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Actor> getCasts() {
        return casts;
    }

    public Movie casts(Set<Actor> actors) {
        this.casts = actors;
        return this;
    }

    public Movie addCast(Actor actor) {
        casts.add(actor);
        actor.getMovies().add(this);
        return this;
    }

    public Movie removeCast(Actor actor) {
        casts.remove(actor);
        actor.getMovies().remove(this);
        return this;
    }

    public void setCasts(Set<Actor> actors) {
        this.casts = actors;
    }

    public Director getDirector() {
        return director;
    }

    public Movie director(Director director) {
        this.director = director;
        return this;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public Movie reviews(Set<Review> reviews) {
        this.reviews = reviews;
        return this;
    }

    public Movie addReviews(Review review) {
        reviews.add(review);
        review.setMovie(this);
        return this;
    }

    public Movie removeReviews(Review review) {
        reviews.remove(review);
        review.setMovie(null);
        return this;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public User getCreator() {
        return creator;
    }

    public Movie creator(User user) {
        this.creator = user;
        return this;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        if(movie.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Movie{" +
            "id=" + id +
            ", imdbId='" + imdbId + "'" +
            ", title='" + title + "'" +
            ", date='" + date + "'" +
            ", plot='" + plot + "'" +
            ", rating='" + rating + "'" +
            ", genre='" + genre + "'" +
            '}';
    }
}
