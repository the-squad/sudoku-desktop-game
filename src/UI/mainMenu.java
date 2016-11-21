package UI;

import static UI.gamePlay.*;
import static UI.global.*;
import static UI.main.database;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

public class mainMenu {

    GridPane mainMenuContainer;
    BorderPane rightPartLayout;
    GridPane gameModesLayout;
    GridPane levelsLayout;
    GridPane savedGamesLayout;

    /**
     * Initialize main menu elements
     *
     * @return mainMenuScene
     */
    public GridPane initialize() {
        //Main menu layout
        mainMenuContainer = new GridPane();

        //Creating two columns
        ColumnConstraints leftPart = new ColumnConstraints();
        leftPart.setPercentWidth(35);

        ColumnConstraints rightPart = new ColumnConstraints();
        rightPart.setPercentWidth(65);

        mainMenuContainer.getColumnConstraints().addAll(leftPart, rightPart);

        //Creating 100% height row
        RowConstraints fullHeight = new RowConstraints();
        fullHeight.setPercentHeight(100);

        mainMenuContainer.getRowConstraints().add(fullHeight);

        //Creating left part layout
        BorderPane leftPartLayout = new BorderPane();
        leftPartLayout.getStyleClass().add("left-part");
        mainMenuContainer.setConstraints(leftPartLayout, 0, 0);
        mainMenuContainer.getChildren().add(leftPartLayout);

        //Left part elements
        Label logo = new Label();
        logo.getStyleClass().add("logo");
        leftPartLayout.setTop(logo);
        leftPartLayout.setAlignment(logo, Pos.BOTTOM_CENTER);
        leftPartLayout.setMargin(logo, new Insets(175, 0, 30, 0));

        Label logoText = new Label("Sudodu Game");
        logoText.getStyleClass().add("logo-text");
        leftPartLayout.setCenter(logoText);
        leftPartLayout.setAlignment(logoText, Pos.TOP_CENTER);

        Label version = new Label("Version 0.2");
        version.getStyleClass().add("version");
        leftPartLayout.setBottom(version);
        leftPartLayout.setAlignment(version, Pos.TOP_CENTER);
        leftPartLayout.setMargin(version, new Insets(0, 0, 30, 0));

        leftPartLayout.getChildren().addAll();

        //Right part layout
        rightPartLayout = new BorderPane();
        rightPartLayout.setPadding(new Insets(25));
        mainMenuContainer.setConstraints(rightPartLayout, 1, 0);
        mainMenuContainer.getChildren().add(rightPartLayout);

        //Initalize modes
        initializeGameModes();
        initializeLevelsMenu();
        rightPartLayout.setCenter(gameModesLayout);

        return mainMenuContainer;
    }

    private void initializeGameModes() {
        gameModesLayout = new GridPane();
        gameModesLayout.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[5];
        for (int counter = 0; counter < 5; counter++) {
            rowNo[counter] = new RowConstraints();
            rowNo[counter].setPercentHeight(13);
            gameModesLayout.getRowConstraints().add(rowNo[counter]);
        }

        //New Game Button
        Image newGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameButtonIconView = new ImageView(newGameButtonIcon);
        Button newGameButton = new Button("       New Game", newGameButtonIconView);
        initButtonStyle(newGameButton, gameModesLayout, 0, newGameButtonIconView, WHITE_BG);

        newGameButton.setOnAction(e -> {
            switchPanes(rightPartLayout, gameModesLayout, levelsLayout);
            playingMode = 1;
            gamePlayContainer.setLeft(leftPanelLayout);
            saveButton.setVisible(true);
            saveButton.setDisable(false);
            submitButton.setDisable(true);
        });

        //Load Game Button
        Image loadGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameButtonIcon);
        Button loadGameButton = new Button("       Load last game", laodGameIconView);
        initButtonStyle(loadGameButton, gameModesLayout, 1, laodGameIconView, WHITE_BG);

        loadGameButton.setOnAction(e -> {
            initializeSavedGames();
            switchPanes(rightPartLayout, gameModesLayout, savedGamesLayout);
            playingMode = 2;
            gamePlayContainer.setLeft(leftPanelLayout);
            saveButton.setVisible(true);
            saveButton.setDisable(false);
            submitButton.setDisable(true);
        });

        //Check Sudoku Button
        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        Button checkSudokuButton = new Button("       Check your Sudoku", checkSudokuIconView);
        initButtonStyle(checkSudokuButton, gameModesLayout, 2, checkSudokuIconView, WHITE_BG);

        checkSudokuButton.setOnAction(e -> {
            switchPanes(windowLayout, mainMenuContainer, gamePlayContainer);
            playingMode = 3;
            gamePlayContainer.setLeft(null);
            saveButton.setVisible(false);
            saveButton.setDisable(true);
            submitButton.setDisable(false);
            submitButton.setText("Check");
        });

        //Challange Computer Button
        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        Button challangeComputerButton = new Button("       Challenge computer", challengeComputerIconView);
        initButtonStyle(challangeComputerButton, gameModesLayout, 3, challengeComputerIconView, WHITE_BG);

        challangeComputerButton.setOnAction(e -> {
            switchPanes(windowLayout, mainMenuContainer, gamePlayContainer);
            playingMode = 4;
            gamePlayContainer.setLeft(null);
            saveButton.setVisible(false);
            saveButton.setDisable(true);
            submitButton.setDisable(false);
            submitButton.setText("Challenge");
        });

        //Exit Button
        Image exitButtonIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitButtonIconView = new ImageView(exitButtonIcon);
        Button exitButton = new Button("       Exit", exitButtonIconView);
        initButtonStyle(exitButton, gameModesLayout, 4, exitButtonIconView, WHITE_BG);

        exitButton.setOnAction(e -> {
            System.exit(0);
        });
    }

    private void initializeLevelsMenu() {
        levelsLayout = new GridPane();
        levelsLayout.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[6];
        for (int counter = 0; counter < 6; counter++) {
            rowNo[counter] = new RowConstraints();

            if (counter == 0) {
                rowNo[counter].setPercentHeight(20);
            }

            rowNo[counter].setPercentHeight(13);
            levelsLayout.getRowConstraints().add(rowNo[counter]);
        }

        //BorderPane to hold text and back arrow
        BorderPane backArrowAndText = new BorderPane();
        levelsLayout.setConstraints(backArrowAndText, 0, 0);
        levelsLayout.getChildren().add(backArrowAndText);
        levelsLayout.setHalignment(backArrowAndText, HPos.CENTER);

        //Back Button
        Button backButton = new Button("");
        backButton.getStyleClass().add("button-icon--dark");
        backButton.getStyleClass().add("back-icon--dark");
        backArrowAndText.setLeft(backButton);
        backArrowAndText.setAlignment(backButton, Pos.CENTER);
        backArrowAndText.setMargin(backButton, new Insets(0, -80, 0, -65));

        backButton.setOnAction(e -> switchPanes(rightPartLayout, levelsLayout, gameModesLayout));

        //Headline
        Label headlineText = new Label("Choose game level");
        headlineText.getStyleClass().add("headline-text");
        backArrowAndText.setCenter(headlineText);
        backArrowAndText.setAlignment(headlineText, Pos.CENTER);

        //Level buttons
        Button easyButton = new Button("Easy");
        initButtonStyle(easyButton, levelsLayout, 1, null, WHITE_BG);

        easyButton.setOnAction(e -> {
            switchPanes(windowLayout, mainMenuContainer, gamePlayContainer);

            ArrayList<String> sudokuGame = null;
            try {
                sudokuGame = database.Select("Easy", 0);
                sudokuIdOriginal = sudokuGame.get(0).split(",")[1];
                
                levelLabel.setText(easyButton.getText());
                gameTime.setTimer(timerLabel, 0);
                gameTime.start();
            } catch (SQLException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            splitSudoku(sudokuGame.get(0));
            gamePlay.sudokuOperation(PRINT_SUDOKU);
            sudokuOperation(COUNT_SUDOKU);
        });

        Button mediumButton = new Button("Medium");
        initButtonStyle(mediumButton, levelsLayout, 2, null, WHITE_BG);

        mediumButton.setOnAction(e -> {
            switchPanes(windowLayout, mainMenuContainer, gamePlayContainer);

            ArrayList<String> sudokuGame = null;
            try {
                sudokuGame = database.Select("Medium", 0);
                sudokuIdOriginal = sudokuGame.get(0).split(",")[1];
                
                levelLabel.setText(mediumButton.getText());
                gameTime.setTimer(timerLabel, 0);
                gameTime.start();
            } catch (SQLException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            splitSudoku(sudokuGame.get(0));
            sudokuOperation(PRINT_SUDOKU);
            sudokuOperation(COUNT_SUDOKU);
        });

        Button hardButton = new Button("Hard");
        initButtonStyle(hardButton, levelsLayout, 3, null, WHITE_BG);

        hardButton.setOnAction(e -> {
            switchPanes(windowLayout, mainMenuContainer, gamePlayContainer);

            ArrayList<String> sudokuGame = null;
            try {
                sudokuGame = database.Select("Hard", 0);
                sudokuIdOriginal = sudokuGame.get(0).split(",")[1];
                
                gameTime.setTimer(timerLabel, 0);
                gameTime.start();
                levelLabel.setText(hardButton.getText());
            } catch (SQLException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            splitSudoku(sudokuGame.get(0));
            gamePlay.sudokuOperation(PRINT_SUDOKU);
            sudokuOperation(COUNT_SUDOKU);
        });
    }

    public void initializeSavedGames() {
        ArrayList<String> savedGames = null;

        try {
            savedGames = main.database.Select(null, 1);
        } catch (SQLException ex) {
            Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (savedGames == null) {
            savedGames = new ArrayList<>();
        }

        int gamesNumber = savedGames.size();

        savedGamesLayout = new GridPane();
        savedGamesLayout.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints headlineRow = new RowConstraints();
        headlineRow.setPercentHeight(20);
        savedGamesLayout.getRowConstraints().add(headlineRow);

        RowConstraints gamesRow = new RowConstraints();
        gamesRow.setPercentHeight(65);
        savedGamesLayout.getRowConstraints().add(gamesRow);

        //BorderPane to hold text and back arrow
        BorderPane backArrowAndText = new BorderPane();
        savedGamesLayout.setConstraints(backArrowAndText, 0, 0);
        savedGamesLayout.getChildren().add(backArrowAndText);
        savedGamesLayout.setHalignment(backArrowAndText, HPos.CENTER);

        //Creating elemens
        Button backButton = new Button("");
        backButton.getStyleClass().add("button-icon--dark");
        backButton.getStyleClass().add("back-icon--dark");
        backArrowAndText.setLeft(backButton);
        backArrowAndText.setAlignment(backButton, Pos.CENTER);
        backArrowAndText.setMargin(backButton, new Insets(0, -170, 0, -10));

        backButton.setOnAction(e -> {
            switchPanes(rightPartLayout, savedGamesLayout, gameModesLayout);
            savedGamesLayout.getChildren().clear();
        });

        //Headline
        Label headlineText = new Label("Choose a game");
        headlineText.getStyleClass().add("headline-text");
        backArrowAndText.setCenter(headlineText);
        backArrowAndText.setAlignment(headlineText, Pos.CENTER);

        //Container
        GridPane gamesContainer = new GridPane();
        gamesContainer.setVgap(20);

        //Scrollbar
        ScrollPane scrollPane = new ScrollPane(gamesContainer);
        scrollPane.getStyleClass().add("scroll-panel");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMinWidth(360);
        savedGamesLayout.setMargin(scrollPane, new Insets(0, 0, 0, 60));

        savedGamesLayout.setConstraints(scrollPane, 0, 1);
        savedGamesLayout.getChildren().add(scrollPane);

        GridPane gameBlock[] = new GridPane[gamesNumber];

        for (int counter = 0; counter < gamesNumber; counter++) {
            String[] data = savedGames.get(counter).split(",");

            gameBlock[counter] = new GridPane();
            gameBlock[counter].getStyleClass().add("game-block");
            gameBlock[counter].setAlignment(Pos.CENTER_LEFT);
            gameBlock[counter].setId("#" + data[0]); //Change to game ID

            //Container
            BorderPane gameBlockLayout = new BorderPane();
            gameBlock[counter].setConstraints(gameBlockLayout, 0, 0);
            gameBlock[counter].getChildren().add(gameBlockLayout);

            //Details container
            GridPane detailsLayout = new GridPane();
            gameBlockLayout.setLeft(detailsLayout);
            gameBlockLayout.setMargin(detailsLayout, new Insets(0, 130, 0, 0));

            //Custom row constraints
            RowConstraints firstRow = new RowConstraints();
            firstRow.setPercentHeight(50);
            detailsLayout.getRowConstraints().add(firstRow);

            RowConstraints secondRow = new RowConstraints();
            secondRow.setPercentHeight(50);
            detailsLayout.getRowConstraints().add(secondRow);

            //Game title
            Button gameTitle = new Button(data[4]);
            gameTitle.getStyleClass().add("button-transparent--dark");
            detailsLayout.setConstraints(gameTitle, 0, 0);
            detailsLayout.getChildren().add(gameTitle);

            //Game level
            Label gameLevel = new Label(data[3]);
            gameLevel.getStyleClass().add("game-level");
            detailsLayout.setConstraints(gameLevel, 0, 1);
            detailsLayout.getChildren().add(gameLevel);
            detailsLayout.setValignment(gameLevel, VPos.BOTTOM);

            //Formating the time
            String time = data[2];
            SimpleDateFormat sdf = new SimpleDateFormat("ss");
            Date dateObj = null;
            try {
                dateObj = sdf.parse(time);
            } catch (ParseException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Game timer
            Label gameTimer = new Label(dateObj.getMinutes() + ":" + dateObj.getSeconds());
            gameTimer.getStyleClass().add("game-time");
            detailsLayout.setConstraints(gameTimer, 0, 1);
            detailsLayout.getChildren().add(gameTimer);
            detailsLayout.setValignment(gameTimer, VPos.BOTTOM);
            detailsLayout.setMargin(gameTimer, new Insets(0, 0, 0, 70));

            //Delete game button
            Button deleteButton = new Button();
            deleteButton.getStyleClass().add("button-icon--dark");
            deleteButton.getStyleClass().add("delete-icon");
            gameBlockLayout.setRight(deleteButton);
            gameBlockLayout.setMargin(deleteButton, new Insets(4, 0, 0, 0));

            //Adding blocks to the container
            gamesContainer.setConstraints(gameBlock[counter], 0, counter);
            gamesContainer.getChildren().add(gameBlock[counter]);

            //Switching scenes and printing the Sudoku
            gameTitle.setOnAction(e -> {
                //Printing Sudoku and saving Sudoku ID
                sudokuId = data[0];
                sudokuIdOriginal = data[6];
                splitSudoku(data[1]);
                sudokuOperation(PRINT_SUDOKU);
                sudokuOperation(COUNT_SUDOKU);

                switchPanes(windowLayout, mainMenuContainer, gamePlayContainer);

                levelLabel.setText(data[3]);
                timerLabel.setText(gameTimer.getText());

                gameTime.setTimer(timerLabel, Integer.parseInt(data[2]));
                gameTime.start();

                //Clear container
                savedGamesLayout.getChildren().clear();
                switchPanes(rightPartLayout, savedGamesLayout, gameModesLayout);
            });

            //Deleting the game
            deleteButton.setOnAction(e -> {
                String gameID = deleteButton.getParent().getParent().getId();
                Object gameBlockObject = deleteButton.getParent().getParent();
                int gameBlockNumber = gamesContainer.getRowIndex((Node) gameBlockObject);
                fade(gameBlockObject, 200, 0, FADE_OUT);

                for (int blockCounter = gameBlockNumber; blockCounter < gamesNumber - 1; blockCounter++) {
                    //Creating timeline animation
                    Timeline updateGameTimeline = new Timeline();

                    KeyValue fromKeyValue = new KeyValue(gameBlock[blockCounter + 1].translateYProperty(), gameBlock[blockCounter + 1].getTranslateY());
                    KeyValue toKeyValue = new KeyValue(gameBlock[blockCounter + 1].translateYProperty(), gameBlock[blockCounter + 1].getTranslateY() - 20 - gameBlock[blockCounter + 1].getHeight());

                    KeyFrame startMove = new KeyFrame(Duration.ZERO, fromKeyValue);
                    KeyFrame finishMove = new KeyFrame(Duration.millis(300), toKeyValue);

                    updateGameTimeline.getKeyFrames().addAll(startMove, finishMove);
                    updateGameTimeline.play();
                }

                try {
                    main.database.deleteGame(Integer.parseInt(gameID.replace("#", "")));
                } catch (SQLException ex) {

                }
            });
        }
    }

    /**
     *
     * @param Sudoku
     */
    private void splitSudoku(String Sudoku) {
        int charptr = 0;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                computerSolution[row][column] = Integer.parseInt(Sudoku.charAt(charptr) + "");
                charptr++;
            }
        }
    }
}
