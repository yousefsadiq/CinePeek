package cinepeek.controller;

import cinepeek.model.Movie;
import cinepeek.model.MovieService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ResultsViewController {

    @FXML
    private Label queryLabel;
    @FXML
    private FlowPane resultsBox;
    @FXML
    private Button backButton;

    private SceneManager sceneManager;
    private MovieService movieService;

    /**
     * Initializes the controller with data from the search.
     */
    public void initManager(SceneManager sceneManager, MovieService movieService, Movie[] movies, String query) {
        this.sceneManager = sceneManager;
        this.movieService = movieService;
        queryLabel.setText("Results for \"" + query + "\"");

        populateResults(movies);
    }

    /**
     * Creates and adds the movie poster cards to the resultsBox.
     */
    private void populateResults(Movie[] movies) {
        resultsBox.getChildren().clear();
        boolean resultsFound = false;

        for (Movie movie : movies) {
            if (movie == null) continue;

            VBox movieCard = createMovieCard(movie);
            resultsBox.getChildren().add(movieCard);
            resultsFound = true;
        }

        if (!resultsFound) {
            resultsBox.getChildren().add(new Label("No results found."));
        }
    }

    /**
     * Helper method to create a single clickable movie card.
     */
    private VBox createMovieCard(Movie movie) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("result-card");
        card.setCursor(Cursor.HAND);

        ImageView posterView = new ImageView();
        posterView.setFitWidth(185); // Standard poster width
        posterView.setPreserveRatio(true);
        posterView.getStyleClass().add("poster-image");

        VBox posterPlaceholder = new VBox(posterView);
        posterPlaceholder.getStyleClass().add("poster-placeholder");
        posterPlaceholder.setMinHeight(278); // (185 * 1.5), standard aspect ratio
        posterPlaceholder.setAlignment(Pos.CENTER);

        String posterUrl = "https://image.tmdb.org/t/p/w185" + movie.getPosterPath();
        Image posterImage = new Image(posterUrl, true); // true = background loading
        posterView.setImage(posterImage);

        posterImage.progressProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0) {
                posterPlaceholder.getStyleClass().remove("poster-placeholder");
            }
        });
        posterImage.errorProperty().addListener((obs, wasError, isError) -> {
            if (isError) {
                System.err.println("Failed to load image: " + posterUrl);
            }
        });

        Label titleLabel = new Label(movie.getTitle());
        titleLabel.getStyleClass().add("poster-title");
        titleLabel.setWrapText(true);

        card.getChildren().addAll(posterPlaceholder, titleLabel);

        card.setOnMouseClicked(event -> {
            sceneManager.showMovieScreen(movie);
        });

        return card;
    }

    @FXML
    private void onBack() {
        sceneManager.showSearchScreen();
    }
}
