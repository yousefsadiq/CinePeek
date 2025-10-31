package cinepeek.controller;

import cinepeek.model.Movie;
import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MovieViewController {

    @FXML
    private Label titleLabel;
    @FXML
    private StackPane cardDeckPane;
    @FXML
    private Button backButton;

    private SceneManager sceneManager;
    private Movie movie;
    private List<Node> factCards;
    private int currentCardIndex = 0;

    /**
     * Initializes the controller with the selected movie.
     */
    public void initManager(SceneManager sceneManager, Movie movie) {
        this.sceneManager = sceneManager;
        this.movie = movie;
        titleLabel.setText(movie.getTitle());

        buildFactCards();
    }

    /**
     * Creates all the fun fact cards and adds them to the deck.
     */
    private void buildFactCards() {
        factCards = new ArrayList<>();

        // 1. Revenue Card
        if (movie.getRevenue() > 0) {
            factCards.add(createFactCard(
                    movie.getTitle(),
                    "Has a lifetime revenue of",
                    formatCurrency(movie.getRevenue()),
                    "revenue-icon"
            ));
        }

        // 2. Runtime Card
        if (movie.getRuntime() > 0) {
            factCards.add(createFactCard(
                    movie.getTitle(),
                    "Has a total runtime of",
                    movie.getRuntime() + " minutes",
                    "runtime-icon"
            ));
        }

        // 3. Release Date
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            try {
                LocalDate releaseDate = LocalDate.parse(movie.getReleaseDate());
                int yearsOld = LocalDate.now().getYear() - releaseDate.getYear();
                factCards.add(createFactCard(
                        movie.getTitle(),
                        "Turns",
                        yearsOld + " years old this year",
                        "calendar-icon"
                ));
            } catch (Exception e) {
                System.err.println("Could not parse release date: " + movie.getReleaseDate());
            }
        }

        // 4. Budget Card
        if (movie.getBudget() > 0) {
            factCards.add(createFactCard(
                    movie.getTitle(),
                    "Was made with a budget of",
                    formatCurrency(movie.getBudget()),
                    "budget-icon"
            ));
        }

        // Add a "No facts" card if list is empty
        if (factCards.isEmpty()) {
            factCards.add(createFactCard(
                    movie.getTitle(),
                    "Has no fun facts available.",
                    "Sorry!",
                    "default-icon"
            ));
        }

        Collections.shuffle(factCards);

        factCards.add(0, createRestartCard());

        cardDeckPane.getChildren().addAll(factCards);
        currentCardIndex = factCards.size() - 1;
    }

    /**
     * Helper to create a single fact card.
     */
    private StackPane createFactCard(String title, String factIntro, String factValue, String iconStyleClass) {
        StackPane card = new StackPane();
        card.getStyleClass().add("fact-card");
        card.setCursor(Cursor.HAND);

        Label iconPlaceholder = new Label();
        iconPlaceholder.getStyleClass().addAll("fact-icon-placeholder", iconStyleClass);

        VBox textContent = new VBox(5);
        textContent.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("fact-card-title");

        Label introLabel = new Label(factIntro);
        introLabel.getStyleClass().add("fact-card-intro");

        Label valueLabel = new Label(factValue);
        valueLabel.getStyleClass().add("fact-card-value");

        textContent.getChildren().addAll(titleLabel, introLabel, valueLabel);

        card.getChildren().addAll(iconPlaceholder, textContent);
        card.setOnMouseClicked(event -> drawCard());

        return card;
    }

    /**
     * Creates a special card to restart the deck.
     */
    private StackPane createRestartCard() {
        StackPane card = new StackPane();
        card.getStyleClass().add("fact-card-restart");
        card.setCursor(Cursor.HAND);

        Label restartLabel = new Label("End of facts!\n(Click to restart)");
        restartLabel.getStyleClass().add("fact-card-intro");
        restartLabel.setAlignment(Pos.CENTER);

        card.getChildren().add(restartLabel);

        card.setOnMouseClicked(event -> {
            cardDeckPane.getChildren().clear();
            buildFactCards();
        });

        return card;
    }

    /**
     * Animates the top card off-screen.
     */
    private void drawCard() {
        if (currentCardIndex <= 0) {
            System.out.println("End of deck");
            return; // Don't remove the last (restart) card
        }

        Node topCard = factCards.get(currentCardIndex);
        currentCardIndex--;

        // Animate card flying off
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), topCard);
        tt.setToX(-600); // Fly off to the left
        tt.setToY(50);

        FadeTransition ft = new FadeTransition(Duration.millis(300), topCard);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ParallelTransition parallelTransition = new ParallelTransition(topCard, tt, ft);
        parallelTransition.setOnFinished(event -> {
            cardDeckPane.getChildren().remove(topCard);
        });

        parallelTransition.play();
    }


    @FXML
    private void onBack() {
        sceneManager.showResultsScreen(sceneManager.getLastResults(), sceneManager.getLastQuery());
    }

    private String formatCurrency(float amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(amount);
    }
}
