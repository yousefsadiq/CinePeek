package cinepeek.controller;

import cinepeek.model.Movie;
import cinepeek.model.MovieService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class SearchViewController {

    @FXML
    private StackPane rootPane;
    @FXML
    private StackPane loadingPane;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    private SceneManager sceneManager;
    private MovieService movieService;

    /**
     * Initializes the controller with necessary managers/services.
     * This is called by the SceneManager after loading the FXML.
     */
    public void initManager(SceneManager sceneManager, MovieService movieService) {
        this.sceneManager = sceneManager;
        this.movieService = movieService;
    }

    /**
     * Handles the search action from the text field (Enter key) or search button.
     */
    @FXML
    private void onSearch(ActionEvent event) {
        String query = searchField.getText();
        if (query == null || query.trim().isEmpty()) {
            return;
        }

        setLoading(true);

        Task<Movie[]> searchTask = new Task<>() {
            @Override
            protected Movie[] call() throws Exception {
                // This runs on a background thread
                return movieService.getMovies(query);
            }
        };

        searchTask.setOnSucceeded(e -> {
            Movie[] movies = searchTask.getValue();
            Platform.runLater(() -> {
                setLoading(false);
                sceneManager.showResultsScreen(movies, query);
            });
        });

        searchTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                setLoading(false);
                System.err.println("Search failed:");
                searchTask.getException().printStackTrace();
            });
        });

        new Thread(searchTask).start();
    }

    /**
     * Toggles the loading indicator and disables/enables search controls.
     */
    private void setLoading(boolean isLoading) {
        loadingPane.setVisible(isLoading);
        searchField.setDisable(isLoading);
        searchButton.setDisable(isLoading);
    }
}
