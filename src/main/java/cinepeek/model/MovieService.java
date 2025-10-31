package cinepeek.model;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MovieService {

    private final String apiKey;
    private final HttpClient client;

    public MovieService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("API_KEY");
        this.client = HttpClient.newHttpClient();
    }

    /**
     * Safely gets a String from a JsonObject, returning null if the key
     * doesn't exist or its value is null.
     */
    private String getSafeString(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsString();
        }
        return null;
    }

    /**
     * Safely gets a float from a JsonObject, returning 0.0f if the key
     * doesn't exist or its value is null.
     */
    private float getSafeFloat(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsFloat();
        }
        return 0.0f;
    }

    /**
     * Safely gets an int from a JsonObject, returning 0 if the key
     * doesn't exist or its value is null.
     */
    private int getSafeInt(JsonObject obj, String key) {
        if (obj.has(key) && !obj.get(key).isJsonNull()) {
            return obj.get(key).getAsInt();
        }
        return 0;
    }

    /**
     * Takes in a JsonObject with relevent data and returns
     * a Movie object.
     *
     */
    public Movie makeMovie(JsonObject movie) throws IOException, InterruptedException {
        int id = movie.get("id").getAsInt();
        String title = getSafeString(movie, "title");
        String releaseDate = getSafeString(movie, "release_date");
        String posterPath = getSafeString(movie, "poster_path");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

        float budget = getSafeFloat(jsonResponse, "budget");
        float revenue = getSafeFloat(jsonResponse, "revenue");
        int runtime = getSafeInt(jsonResponse, "runtime");

        return new Movie(id, title, releaseDate, budget, revenue, runtime, posterPath);
    }

    /**
     * Uses a search query to return an array of up to 6 initialized
     * and defined Movie objects.
     */
    public Movie [] getMovies(String searchQuery) throws IOException, InterruptedException {
        Movie [] movies = new Movie [6];
        String encodeQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
        String url = "https://api.themoviedb.org/3/search/movie?query=" + encodeQuery + "&include_adult=false&language=en-US&page=1";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + apiKey)
                .header("accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        JsonArray results = jsonResponse.getAsJsonArray("results");

        int moviesAdded = 0;
        for (int i = 0; i < Math.min(results.size(), 6); i++) {
            JsonObject jsonMovie = results.get(i).getAsJsonObject();
            try {
                Movie movie = makeMovie(jsonMovie);
                movies[moviesAdded] = movie;
                moviesAdded++;
            } catch (Exception e) {
                System.err.println("Failed to make movie: " + getSafeString(jsonMovie, "title"));
                e.printStackTrace();
            }
        }

        if (moviesAdded < 6) {
            Movie[] validMovies = new Movie[moviesAdded];
            System.arraycopy(movies, 0, validMovies, 0, moviesAdded);
            return validMovies;
        }
        return movies;
    }
}
