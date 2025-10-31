package cinepeek.controller;

import cinepeek.model.Movie;
import cinepeek.model.MovieService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Manages scene navigation for the application.
 * Loads FXML views, injects dependencies into controllers,
 * and switches the primary stage's scene.
 */
public class SceneManager {

    private final Stage primaryStage;
    private final MovieService movieService;
    private Movie[] lastResults;
    private String lastQuery;

    public SceneManager(Stage primaryStage, MovieService movieService) {
        this.primaryStage = primaryStage;
        this.movieService = movieService;
    }

    /**
     * Loads and displays the main Search View.
     * This method creates the *initial* scene.
     */
    public void showSearchScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cinepeek/SearchView.fxml"));
            Parent root = loader.load();

            SearchViewController controller = loader.getController();
            controller.initManager(this, movieService);

            if (primaryStage.getScene() == null) {
                Scene scene = new Scene(root, 800, 600);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/cinepeek/styles.css")).toExternalForm());
                primaryStage.setScene(scene);
            } else {
                primaryStage.getScene().setRoot(root);
            }

        } catch (IOException e) {
            System.err.println("Failed to load Search View:");
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays the Results View with the given movies.
     */
    public void showResultsScreen(Movie[] movies, String query) {
        this.lastResults = movies;
        this.lastQuery = query;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cinepeek/ResultsView.fxml"));
            Parent root = loader.load();

            ResultsViewController controller = loader.getController();
            controller.initManager(this, movieService, movies, query);

            primaryStage.getScene().setRoot(root);

        } catch (IOException e) {
            System.err.println("Failed to load Results View:");
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays the Movie "Fun Fact" Card View.
     */
    public void showMovieScreen(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cinepeek/MovieView.fxml"));
            Parent root = loader.load();

            MovieViewController controller = loader.getController();
            controller.initManager(this, movie);

            primaryStage.getScene().setRoot(root);

        } catch (IOException e) {
            System.err.println("Failed to load Movie View:");
            e.printStackTrace();
        }
    }

    public Movie[] getLastResults() {
        return lastResults;
    }

    public String getLastQuery() {
        return lastQuery;
    }
}