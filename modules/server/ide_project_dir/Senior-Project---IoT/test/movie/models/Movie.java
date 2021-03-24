package movie.models;

public class Movie {
    public final String title, description, genre;
    public final double rating;
    public final long id;

    public Movie(String title, String description, String genre, double rating, long id) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.id = id;
    }
}
