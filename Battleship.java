import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Battleship includes and runs all the animation and physical aspects of the backends
 * provided in the other files.
 * @author baran_abbasi
 * @version 11.17
 */
public class Battleship extends Application {
    private Scene startScene;
    private Scene gameScene;
    private Scene endScene;
    private ArrayList<ArrayList<Rectangle>> enemyOcean;
    private ArrayList<ArrayList<Rectangle>> playerOcean;
    private BattleshipBackend backend;
    private ArrayList<Rectangle> confettiSet;

    /**
     * The main method that launches the java fx program.
     * @param args a list of arguments to take in
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Battleship");

        Image backgroundImg = new Image("battleshipImage.jpg");
        ImageView background = new ImageView(backgroundImg);

        StackPane layout = new StackPane();
        layout.getChildren().add(background);
        startScene = new Scene(layout, backgroundImg.getWidth(), backgroundImg.getHeight());

        VBox info = new VBox();

        Text firstSceneTitle = new Text("Welcome to Battleship");
        firstSceneTitle.setFont(Font.font("Courier New", FontWeight.BOLD,
                (int) (backgroundImg.getHeight() / 20)));
        firstSceneTitle.setFill(Color.WHITE);

        Button startGameBtn = new Button("Start Game");
        startGameBtn.setOnAction(e -> openGameScreen(primaryStage));

        info.getChildren().addAll(firstSceneTitle, startGameBtn);
        info.setAlignment(Pos.CENTER);

        layout.getChildren().add(info);


        primaryStage.setScene(startScene);

        primaryStage.show();
    }

    /**
     * This method startings the playing field including the enemyocean, plyaerocean
     * and all the available guesses and what not.
     * @param primaryStage the stage that the gamescene will be played on
     */
    private void openGameScreen(Stage primaryStage) {
        System.out.println("now entering game scene~~~");
        backend = new BattleshipBackend();

        BorderPane layout = new BorderPane();
        gameScene = new Scene(layout, 800, 1000);

//_____________________________________________SETTING THE TITLE PANE
        HBox titlePane = new HBox();
        Text title = new Text("Battleship");
        title.setFont(Font.font("Courier New", FontWeight.BOLD,
                (int) (gameScene.getHeight() / 40)));
        titlePane.getChildren().add(title);
        titlePane.setAlignment(Pos.CENTER);

        layout.setTop(titlePane);
        layout.setPadding(new Insets(gameScene.getWidth() / 20));

//_____________________________________________SETTING THE LEFT AND RIGHT PANES
        VBox left = new VBox();
        ImageView leftImg = new ImageView(new Image("battleshipLeft.png",
                gameScene.getWidth() / 2, gameScene.getHeight() / 2,  true, true));
        left.getChildren().add(leftImg);
        left.setAlignment(Pos.CENTER);
        layout.setLeft(left);

        VBox right = new VBox();
        ImageView rightImg = new ImageView(new Image("battleshipRight.png",
                gameScene.getWidth() / 2, gameScene.getHeight() / 2,  true, true));
        right.getChildren().add(rightImg);
        right.setAlignment(Pos.CENTER);
        layout.setRight(right);

//_____________________________________________SETTING THE CENTER PANE
        VBox playingField = new VBox();

//_____________________________________________enemy playing field label
        Label enemyShips = new Label("Enemy Ships:");
        enemyShips.setFont(Font.font("Courier New", FontWeight.BOLD,
                (int) (gameScene.getHeight() / 60)));
        enemyShips.setTextFill(Color.CRIMSON);
        enemyShips.setPadding(new Insets(0, 0, gameScene.getWidth() / 70, 0));
        playingField.getChildren().add(enemyShips);


//_____________________________________________enemy playing field
        enemyOcean = new ArrayList<>();
        VBox enemy = new VBox();

        HBox encol = new HBox();
        encol.getChildren().add(new Rectangle(gameScene.getWidth() / 30,
                gameScene.getWidth() / 30, Color.rgb(255, 245, 252)));
        for (int i = 0; i < 10; i++) {
            StackPane colPane = new StackPane();
            Rectangle num = new Rectangle();
            num.setWidth(gameScene.getWidth() / 30);
            num.setHeight(gameScene.getWidth() / 30);
            num.setFill(Color.rgb(255, 245, 252));
            num.setStroke(Color.rgb(255, 245, 252));
            char c = (char) (i + 65);
            colPane.getChildren().addAll(num, new Text("" + c));
            encol.getChildren().add(colPane);
        }
        encol.setAlignment(Pos.CENTER);
        enemy.getChildren().add(encol);

        for (int i = 1; i <= 10; i++) {
            ArrayList<Rectangle> enRow = new ArrayList<>();
            HBox row = new HBox();

            StackPane pane = new StackPane();
            Rectangle num = new Rectangle();
            num.setWidth(gameScene.getWidth() / 30);
            num.setHeight(gameScene.getWidth() / 30);
            num.setFill(Color.rgb(255, 245, 252));
            num.setStroke(Color.rgb(255, 245, 252));
            pane.getChildren().addAll(num, new Text("" + i));

            row.getChildren().add(pane);
            for (int j = 0; j < 10; j++) {
                Rectangle sq = new Rectangle();
                sq.setWidth(gameScene.getWidth() / 30);
                sq.setHeight(gameScene.getWidth() / 30);
                sq.setFill(new ImagePattern(new Image("ocean.jpeg")));
                sq.setStroke(Color.BLACK);
                char c = (char) ((char) j + 65);
                int r = i;
                sq.setOnMouseEntered(e -> sq.setStroke(Color.RED));
                sq.setOnMouseExited(e -> sq.setStroke(Color.BLACK));
                sq.setOnMouseClicked(e -> {
                    System.out.println("initiating attack sequence");

                    startGamePlay(primaryStage, r, c);
                });

                row.getChildren().add(sq);
                enRow.add(sq);
            }
            enemyOcean.add(enRow);
            row.setAlignment(Pos.CENTER);
            enemy.getChildren().add(row);
        }
        enemy.setAlignment(Pos.CENTER);
        playingField.getChildren().add(enemy);

//_____________________________________________player playing field label
        Label playerShips = new Label("Your Ships:");
        playerShips.setFont(Font.font("Courier New", FontWeight.BOLD,
                (int) (gameScene.getHeight() / 60)));
        playerShips.setTextFill(Color.DARKGREEN);
        playerShips.setPadding(new Insets(gameScene.getWidth() / 70));
        playingField.getChildren().add(playerShips);

//_____________________________________________player playing field
        playerOcean = new ArrayList<>();
        VBox player = new VBox();

        Image ship = new Image("ship.png");
        HBox playcol = new HBox();

        playcol.getChildren().add(new Rectangle(gameScene.getWidth() / 30,
                gameScene.getWidth() / 30, Color.rgb(255, 245, 252)));
        for (int i = 0; i < 10; i++) {
            StackPane colPane = new StackPane();
            Rectangle num = new Rectangle();
            num.setWidth(gameScene.getWidth() / 30);
            num.setHeight(gameScene.getWidth() / 30);
            num.setFill(Color.rgb(255, 245, 252));
            num.setStroke(Color.rgb(255, 245, 252));
            char c = (char) (i + 65);
            colPane.getChildren().addAll(num, new Text("" + c));
            playcol.getChildren().add(colPane);
        }
        playcol.setAlignment(Pos.CENTER);
        player.getChildren().add(playcol);

        for (int i = 1; i <= 10; i++) {
            ArrayList<Rectangle> enRow = new ArrayList<>();
            HBox row = new HBox();

            StackPane pane = new StackPane();
            Rectangle num = new Rectangle();
            num.setWidth(gameScene.getWidth() / 30);
            num.setHeight(gameScene.getWidth() / 30);
            num.setFill(Color.rgb(255, 245, 252));
            num.setStroke(Color.rgb(255, 245, 252));
            pane.getChildren().addAll(num, new Text("" + i));

            row.getChildren().add(pane);
            for (int j = 0; j < 10; j++) {
                Rectangle sq = new Rectangle();
                sq.setWidth(gameScene.getWidth() / 30);
                sq.setHeight(gameScene.getWidth() / 30);
                if (backend.playerHasShip(i, (char) (j + 65))) {
                    sq.setFill(new ImagePattern(ship));
                } else {
                    sq.setFill(new ImagePattern(new Image("ocean.jpeg")));
                }
                sq.setStroke(Color.BLACK);

                row.getChildren().addAll(sq);
                enRow.add(sq);
            }
            playerOcean.add(enRow);
            row.setAlignment(Pos.CENTER);
            player.getChildren().add(row);
        }
        player.setAlignment(Pos.CENTER);
        playingField.getChildren().add(player);

        playingField.setAlignment(Pos.CENTER);

        layout.setCenter(playingField);
//_____________________________________________SET BOTTOM PANE
        HBox guessSelection = new HBox();

        ChoiceBox rowSelection = new ChoiceBox();
        rowSelection.getItems().addAll(
                1, 2, 3, 4, 5,
                6, 7, 8, 9, 10
        );
        rowSelection.setValue(1);

        ChoiceBox colSelection = new ChoiceBox();
        colSelection.getItems().addAll(
                'A', 'B', 'C', 'D', 'E',
                'F', 'G', 'H', 'I', 'J'
        );
        colSelection.setValue('A');

        Button guess = new Button("GUESS");

        EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("initiating attack sequence");
                char c = (char) colSelection.getValue();
                int r = (int) rowSelection.getValue();
                startGamePlay(primaryStage, r, c);
                colSelection.setValue('A');
                rowSelection.setValue(1);
            }
        };

        guess.setOnAction(handler);

        ColorPicker theme = new ColorPicker();

        theme.setOnAction(e -> {
            Color colorPicked = theme.getValue();
            layout.setBackground(new Background(new BackgroundFill(colorPicked, null, null)));
            if ((colorPicked.getBlue() + colorPicked.getGreen() + colorPicked.getRed()) < 1.5) {
                title.setFill(Color.WHITE);
            } else {
                title.setFill(Color.BLACK);
            }
        });

        guessSelection.getChildren().addAll(rowSelection, colSelection, guess, theme);
        guessSelection.setSpacing(gameScene.getWidth() / 50);
        guessSelection.setAlignment(Pos.CENTER);

        layout.setBottom(guessSelection);


        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    /**
     * This method will be called every time the plyaer presses "guess" and will
     * take the appropriate action.
     * @param primaryStage the stage it will be played on
     * @param i the row
     * @param j the column
     */
    private void startGamePlay(Stage primaryStage, int i, char j) {

        GuessOutcome guess = backend.attackEnemy(i, j);
        AudioClip hitSound = new AudioClip(new File("hitSound.mp3").toURI().toString());
        AudioClip missSound = new AudioClip(new File("missSound.mp3").toURI().toString());
        if (guess.getCellState() != CellState.ALREADY_GUESSED) {
            int c = (j) - 65;
            Rectangle hit = enemyOcean.get(i - 1).get(c);
            if (guess.getCellState() == CellState.HIT) {
                hit.setFill(new ImagePattern(new Image("hit.jpeg")));
                hitSound.play();
            } else if (guess.getCellState() == CellState.MISS) {
                hit.setFill(Color.YELLOW);
                missSound.play();
            }

            if (backend.hasPlayerWon()) {
                playWinSequence(primaryStage);
            }

            GuessOutcome attack = backend.attackPlayer();

            Rectangle attacked = playerOcean.get(attack.getRow() - 1).
                    get((char) attack.getColumn() - 65);

            while (attack.getCellState() == CellState.ALREADY_GUESSED) {
                attack = backend.attackPlayer();
            }
            if (attack.getCellState() == CellState.HIT) {
                attacked.setFill(new ImagePattern(new Image("hit.jpeg")));
            } else if (attack.getCellState() == CellState.MISS) {
                attacked.setFill(Color.YELLOW);
            }

            if (backend.hasEnemyWon()) {
                playLostSequence(primaryStage);
            }
        }
    }

    /**
     * This method plays the 'you win" screen when the players win.
     * @param primaryStage the stage that the win scene will be played on
     */
    private void playWinSequence(Stage primaryStage) {
        Group layout = new Group();

        endScene = new Scene(layout, 600, 600);

//-------------------------ADD THE YOU WIN GIF
        Image winner = new Image("youWin.gif", 300, 300, true, true);
        ImageView winnerView = new ImageView(winner);
        winnerView.setTranslateX(300 - winner.getWidth() / 2);
        winnerView.setTranslateY(300 - winner.getHeight() / 2);
        layout.getChildren().add(winnerView);

//-----------------------ADD THE CONFETTI
        createConfetti(endScene);
        layout.getChildren().addAll(confettiSet);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                initiateConfettiFall(endScene);
            }
        };

        primaryStage.setScene(endScene);
        primaryStage.show();
        timer.start();
    }

    /**
     * This method will start the 'you lose' screen when the plyaer has lost.
     * @param primaryStage the stage that the endScene will be played on
     */
    private void playLostSequence(Stage primaryStage) {
        Group layout = new Group();
        endScene = new Scene(layout, 600, 600);

        Image loser = new Image("clown.gif", 300, 300, true, true);
        ImageView loserView = new ImageView(loser);
        loserView.setTranslateX(300 - loser.getWidth() / 2);
        loserView.setTranslateY(300 - loser.getHeight() / 2);

        Text lost = new Text("imagine losing to randome guesses...");
        lost.setFont(Font.font("Courier New", FontWeight.BOLD,
                (int) (gameScene.getHeight() / 60)));
        lost.setTranslateX(280 - loser.getWidth() / 2);
        lost.setTranslateY(275 - loser.getHeight() / 2);

//        Media music = new Media(getClass().getResource("circus.mp3").toExternalForm());
//        MediaPlayer circusMusic = new MediaPlayer(music);

        AudioClip circusMusic = new AudioClip(new File("circus.mp3").toURI().toString());
        circusMusic.play();

        layout.getChildren().addAll(lost, loserView);

        primaryStage.setScene(endScene);
        primaryStage.show();

    }

    private void createConfetti(Scene scene) {
        confettiSet = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 250; i++) {
            Rectangle confetti = new Rectangle(5, 5);
            confetti.setFill(Color.rgb(rand.nextInt(256), rand.nextInt(256),
                    rand.nextInt(256)));
            confetti.setTranslateX(rand.nextInt((int) scene.getWidth()));
            confetti.setTranslateY(rand.nextInt((int) scene.getHeight()));

            confettiSet.add(confetti);
        }
    }

    /**
     * this method moves the confetti created prior.
     * @param scene the scene that they are added to
     */
    private void initiateConfettiFall(Scene scene) {
        Random rand = new Random();

        for (Rectangle confetti : confettiSet) {
            confetti.setTranslateY(confetti.getTranslateY() + 3);

            if (confetti.getTranslateY() > scene.getHeight()) {
                confetti.setTranslateY(0);
                confetti.setTranslateX(rand.nextInt((int) scene.getWidth()));
            }
        }
    }

}