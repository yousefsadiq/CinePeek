package cinepeek;

import cinepeek.controller.SceneManager;
import cinepeek.model.MovieService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for CinePeek.
 * Sets up the primary stage and initializes the first scene
 * through the SceneManager.
 */
public class CinePeekApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        MovieService movieService = new MovieService();
        SceneManager sceneManager = new SceneManager(primaryStage, movieService);

        primaryStage.setTitle("CinePeek");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        sceneManager.showSearchScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

