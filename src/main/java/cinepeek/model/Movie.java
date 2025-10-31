package cinepeek.model;

/**
 * Model class for a Movie object.
 */
public class Movie {

    private final int id;
    private final String title;
    private final String releaseDate;
    private final float budget;
    private final float revenue;
    private final int runtime;
    private final String posterPath;


    public Movie(int id, String title, String releaseDate, float budget, float revenue, int runtime, String posterPath) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.budget = budget;
        this.revenue = revenue;
        this.runtime = runtime;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getBudget() {
        return budget;
    }

    public float getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

}
