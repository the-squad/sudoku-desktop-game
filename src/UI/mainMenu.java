package UI;

import static UI.gamePlay.*;
import static UI.global.*;

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
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import sudoku.SudokuGenerator;

public class mainMenu {

    // <editor-fold defaultstate="collapsed" desc="Main Panes">
    private GridPane mainMenuContainer;
    private BorderPane rightPartContainer;
    private BorderPane leftPartContainer;
    private GridPane gameModesContainer;
    private GridPane levelsContainer;
    private GridPane savedGamesContainer;
    private BorderPane pageHeaderContainer;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Labels">
    private Label logo;
    private Label logoText;
    private Label version;
    private Label headlineText;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buttons">
    private Button newGameButton;
    static Button loadGameButton;
    private Button checkSudokuButton;
    private Button challangeComputerButton;
    private Button exitButton;
    private Button backButton;
    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    // </editor-fold>

    private int savedGamesNumberGlobal;

    /**
     * Initialize main menu elements
     *
     * @author Muhammad Tarek
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
        leftPartContainer = new BorderPane();
        leftPartContainer.getStyleClass().add("left-part");
        mainMenuContainer.setConstraints(leftPartContainer, 0, 0);
        mainMenuContainer.getChildren().add(leftPartContainer);

        //<editor-fold defaultstate="collapsed" desc="Logo">
        logo = new Label();
        logo.getStyleClass().add("logo");
        leftPartContainer.setTop(logo);
        leftPartContainer.setAlignment(logo, Pos.BOTTOM_CENTER);
        leftPartContainer.setMargin(logo, new Insets(175, 0, 30, 0));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Logo Label">
        logoText = new Label("Sudoku Game");
        logoText.getStyleClass().add("logo-text");
        leftPartContainer.setCenter(logoText);
        leftPartContainer.setAlignment(logoText, Pos.TOP_CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Version Label">
        version = new Label("Version 1.2.1");
        version.getStyleClass().add("version");
        leftPartContainer.setBottom(version);
        leftPartContainer.setAlignment(version, Pos.TOP_CENTER);
        leftPartContainer.setMargin(version, new Insets(0, 0, 30, 0));
        //</editor-fold>

        leftPartContainer.getChildren().addAll();

        //Right part layout
        rightPartContainer = new BorderPane();
        rightPartContainer.setPadding(new Insets(25));
        mainMenuContainer.setConstraints(rightPartContainer, 1, 0);
        mainMenuContainer.getChildren().add(rightPartContainer);

        //Getting number of saved games before initializing buttons
        ArrayList<String> savedGamesTemp = null;
        try {
            savedGamesTemp = database.Select();
        } catch (SQLException ex) {
            savedGamesNumberGlobal = 0;
        }

        if (savedGamesTemp == null) {
            savedGamesTemp = new ArrayList<>();
        }

        savedGamesNumberGlobal = savedGamesTemp.size();

        //Initalize modes
        initializeGameModes();
        initializeLevelsMenu();
        rightPartContainer.setCenter(gameModesContainer);

        return mainMenuContainer;
    }

    /**
     * Initialize game controls buttons
     *
     * @author Muhammad Tarek
     */
    private void initializeGameModes() {
        gameModesContainer = new GridPane();
        gameModesContainer.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[5];
        for (int counter = 0; counter < 5; counter++) {
            rowNo[counter] = new RowConstraints();
            rowNo[counter].setPercentHeight(13);
            gameModesContainer.getRowConstraints().add(rowNo[counter]);
        }

        //<editor-fold defaultstate="collapsed" desc="New Game Button">
        Image newGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameButtonIconView = new ImageView(newGameButtonIcon);
        newGameButton = new Button("       New Game", newGameButtonIconView);
        initButtonStyle(newGameButton, gameModesContainer, 0, newGameButtonIconView, WHITE_BG);

        newGameButton.setOnAction(e -> {
            switchPanes(rightPartContainer, gameModesContainer, levelsContainer);
            playingMode = 1;
            gamePlayContainer.setCenter(loadingIndicator);
            gamePlayContainer.setLeft(null);
            gameTime.setTimer(timerLabel, 0);

            headerCenterAreaContainer.setRight(headerControlsContainer);
            saveGameState = true;

            submitButton.setText("Submit");
            headlineLabel.setText("New Game");
            changeButtonState(DISABLE, saveButton, submitButton);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Load Game Button">
        Image loadGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameButtonIcon);
        loadGameButton = new Button("       Load last game", laodGameIconView);
        initButtonStyle(loadGameButton, gameModesContainer, 1, laodGameIconView, WHITE_BG);

        loadGameButton.setOnAction(e -> {
            initializeSavedGames();
            switchPanes(rightPartContainer, gameModesContainer, savedGamesContainer);
            playingMode = 2;
            gamePlayContainer.setCenter(sudokuCellsContainer);
            gamePlayContainer.setLeft(gameLeftPanelContainer);
            saveGameState = false;

            headerCenterAreaContainer.setRight(headerControlsContainer);
            submitButton.setText("Submit");
            headlineLabel.setText("Loaded Game");
        });

        //Disable the button when there are no saved games
        if (savedGamesNumberGlobal == 0) {
            loadGameButton.setDisable(true);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Check Sudoku Button">
        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        checkSudokuButton = new Button("       Check your Sudoku", checkSudokuIconView);
        initButtonStyle(checkSudokuButton, gameModesContainer, 2, checkSudokuIconView, WHITE_BG);

        checkSudokuButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);
            playingMode = 3;
            gamePlayContainer.setCenter(sudokuCellsContainer);
            gamePlayContainer.setLeft(null);

            headerCenterAreaContainer.setRight(null);
            submitButton.setText("Check");
            headlineLabel.setText("Check your Sudoku");
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Challenge Computer">
        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        challangeComputerButton = new Button("       Challenge Computer", challengeComputerIconView);
        initButtonStyle(challangeComputerButton, gameModesContainer, 3, challengeComputerIconView, WHITE_BG);

        challangeComputerButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);
            playingMode = 4;
            gamePlayContainer.setCenter(sudokuCellsContainer);
            gamePlayContainer.setLeft(null);

            headerCenterAreaContainer.setRight(null);
            submitButton.setText("Challenge");
            headlineLabel.setText("Challenge Computer");
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Exit Button">
        Image exitButtonIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitButtonIconView = new ImageView(exitButtonIcon);
        exitButton = new Button("       Exit", exitButtonIconView);
        initButtonStyle(exitButton, gameModesContainer, 4, exitButtonIconView, WHITE_BG);

        exitButton.setOnAction(e -> {
            System.exit(0);
        });
        //</editor-fold>
    }

    /**
     * Initialize game levels buttons
     *
     * @author Muhammad Tarek
     */
    private void initializeLevelsMenu() {
        levelsContainer = new GridPane();
        levelsContainer.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[6];
        for (int counter = 0; counter < 6; counter++) {
            rowNo[counter] = new RowConstraints();

            if (counter == 0) {
                rowNo[counter].setPercentHeight(20);
            }

            rowNo[counter].setPercentHeight(13);
            levelsContainer.getRowConstraints().add(rowNo[counter]);
        }

        //BorderPane to hold text and back arrow
        pageHeaderContainer = new BorderPane();
        levelsContainer.setConstraints(pageHeaderContainer, 0, 0);
        levelsContainer.getChildren().add(pageHeaderContainer);
        levelsContainer.setHalignment(pageHeaderContainer, HPos.CENTER);

        //Back Button
        backButton = new Button("");
        backButton.getStyleClass().add("button-icon--dark");
        backButton.getStyleClass().add("back-icon--dark");
        pageHeaderContainer.setLeft(backButton);
        pageHeaderContainer.setAlignment(backButton, Pos.CENTER);
        pageHeaderContainer.setMargin(backButton, new Insets(0, -80, 0, -65));

        backButton.setOnAction(e -> switchPanes(rightPartContainer, levelsContainer, gameModesContainer));

        //Headline
        headlineText = new Label("Choose game level");
        headlineText.getStyleClass().add("headline-text");
        pageHeaderContainer.setCenter(headlineText);
        pageHeaderContainer.setAlignment(headlineText, Pos.CENTER);

        //<editor-fold defaultstate="collapsed" desc="Easy Level Button">
        easyButton = new Button("Easy");
        initButtonStyle(easyButton, levelsContainer, 1, null, WHITE_BG);

        easyButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    sudokuGame = generator.MakeSudoku(SudokuGenerator.EASY);
                    assignSudoku(sudokuGame, null);
                    return null;
                }
            };

            Thread generateSudokuThread = new Thread(task);
            generateSudokuThread.setDaemon(true);
            generateSudokuThread.start();

            task.setOnSucceeded((WorkerStateEvent t) -> {
                finishLoading();
                levelLabel.setText(easyButton.getText());
                sudokuOperation(PRINT_SUDOKU);
            });

            switchPanes(rightPartContainer, levelsContainer, gameModesContainer);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Medium Level Button">
        mediumButton = new Button("Medium");
        initButtonStyle(mediumButton, levelsContainer, 2, null, WHITE_BG);

        mediumButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    sudokuGame = generator.MakeSudoku(SudokuGenerator.MEDIUM);
                    assignSudoku(sudokuGame, null);
                    return null;
                }
            };

            Thread generateSudokuThread = new Thread(task);
            generateSudokuThread.setDaemon(true);
            generateSudokuThread.start();

            task.setOnSucceeded((WorkerStateEvent t) -> {
                finishLoading();
                levelLabel.setText(mediumButton.getText());
                sudokuOperation(PRINT_SUDOKU);
            });

            switchPanes(rightPartContainer, levelsContainer, gameModesContainer);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Hard Level Button">
        hardButton = new Button("Hard");
        initButtonStyle(hardButton, levelsContainer, 3, null, WHITE_BG);

        hardButton.setOnAction(e -> {
            switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    sudokuGame = generator.MakeSudoku(SudokuGenerator.HARD);
                    assignSudoku(sudokuGame, null);
                    return null;
                }
            };

            Thread generateSudokuThread = new Thread(task);
            generateSudokuThread.setDaemon(true);
            generateSudokuThread.start();

            task.setOnSucceeded((WorkerStateEvent t) -> {
                finishLoading();
                levelLabel.setText(hardButton.getText());
                sudokuOperation(PRINT_SUDOKU);
            });

            switchPanes(rightPartContainer, levelsContainer, gameModesContainer);
        });
        //</editor-fold>
    }

    /**
     * Initialize and load saved games
     *
     * @author Muhammad Tarek
     */
    public void initializeSavedGames() {
        //ArrayList<String> savedGames = null;
        ArrayList<String> savedGames = null;
        try {
            savedGames = database.Select();
        } catch (SQLException ex) {
            Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (savedGames == null) {
            savedGames = new ArrayList<>();
        }

        int savedGamesNumber = savedGames.size();
        savedGamesNumberGlobal = savedGamesNumber;

        savedGamesContainer = new GridPane();
        savedGamesContainer.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints headlineRow = new RowConstraints();
        headlineRow.setPercentHeight(20);
        savedGamesContainer.getRowConstraints().add(headlineRow);

        RowConstraints gamesRow = new RowConstraints();
        gamesRow.setPercentHeight(65);
        savedGamesContainer.getRowConstraints().add(gamesRow);

        //BorderPane to hold text and back arrow
        pageHeaderContainer = new BorderPane();
        savedGamesContainer.setConstraints(pageHeaderContainer, 0, 0);
        savedGamesContainer.getChildren().add(pageHeaderContainer);
        savedGamesContainer.setHalignment(pageHeaderContainer, HPos.CENTER);

        //Creating elemens
        backButton = new Button("");
        backButton.getStyleClass().add("button-icon--dark");
        backButton.getStyleClass().add("back-icon--dark");
        pageHeaderContainer.setLeft(backButton);
        pageHeaderContainer.setAlignment(backButton, Pos.CENTER);
        pageHeaderContainer.setMargin(backButton, new Insets(0, -170, 0, -10));

        backButton.setOnAction(e -> {
            switchPanes(rightPartContainer, savedGamesContainer, gameModesContainer);
            savedGamesContainer.getChildren().clear();
        });

        //Headline
        headlineText = new Label("Choose a game");
        headlineText.getStyleClass().add("headline-text");
        pageHeaderContainer.setCenter(headlineText);
        pageHeaderContainer.setAlignment(headlineText, Pos.CENTER);

        //Container
        GridPane savedGamesLayout = new GridPane();
        savedGamesLayout.setVgap(20);

        //Scrollbar
        ScrollPane scrollPane = new ScrollPane(savedGamesLayout);
        scrollPane.getStyleClass().add("scroll-panel");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMinWidth(360);
        savedGamesContainer.setMargin(scrollPane, new Insets(0, 0, 0, 60));

        savedGamesContainer.setConstraints(scrollPane, 0, 1);
        savedGamesContainer.getChildren().add(scrollPane);

        GridPane gameBlock[] = new GridPane[savedGamesNumber];

        for (int counter = 0; counter < savedGamesNumber; counter++) {
            String[] data = savedGames.get(counter).split(",");

            gameBlock[counter] = new GridPane();
            gameBlock[counter].getStyleClass().add("game-block");
            gameBlock[counter].setAlignment(Pos.CENTER_LEFT);
            gameBlock[counter].setId(data[0]); //Change to game ID

            //Container
            BorderPane savedGameBlockContainer = new BorderPane();
            gameBlock[counter].setConstraints(savedGameBlockContainer, 0, 0);
            gameBlock[counter].getChildren().add(savedGameBlockContainer);

            //Details container
            GridPane savedGameDetailsContainer = new GridPane();
            savedGameBlockContainer.setLeft(savedGameDetailsContainer);
            savedGameBlockContainer.setMargin(savedGameDetailsContainer, new Insets(0, 130, 0, 0));

            //Custom row constraints
            RowConstraints firstRow = new RowConstraints();
            firstRow.setPercentHeight(50);
            savedGameDetailsContainer.getRowConstraints().add(firstRow);

            RowConstraints secondRow = new RowConstraints();
            secondRow.setPercentHeight(50);
            savedGameDetailsContainer.getRowConstraints().add(secondRow);

            //<editor-fold defaultstate="collapsed" desc="Saved Game Title">
            Button savedGameTitle = new Button(data[4]);
            savedGameTitle.getStyleClass().add("button-transparent--dark");
            savedGameDetailsContainer.setConstraints(savedGameTitle, 0, 0);
            savedGameDetailsContainer.getChildren().add(savedGameTitle);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Saved Game Lavel Label">
            Label savedGameLevelLabel = new Label(data[3]);
            savedGameLevelLabel.getStyleClass().add("game-level");
            savedGameDetailsContainer.setConstraints(savedGameLevelLabel, 0, 1);
            savedGameDetailsContainer.getChildren().add(savedGameLevelLabel);
            savedGameDetailsContainer.setValignment(savedGameLevelLabel, VPos.BOTTOM);
            //</editor-fold>

            //Formating the time
            String time = data[2];
            SimpleDateFormat sdf = new SimpleDateFormat("ss");
            Date dateObj = null;
            try {
                dateObj = sdf.parse(time);
            } catch (ParseException ex) {
                Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }

            //<editor-fold defaultstate="collapsed" desc="Saved Game Time Label">
            Label savedGameTimeLabel = new Label(dateObj.getMinutes() + ":" + dateObj.getSeconds());
            savedGameTimeLabel.getStyleClass().add("game-time");
            savedGameDetailsContainer.setConstraints(savedGameTimeLabel, 0, 1);
            savedGameDetailsContainer.getChildren().add(savedGameTimeLabel);
            savedGameDetailsContainer.setValignment(savedGameTimeLabel, VPos.BOTTOM);
            savedGameDetailsContainer.setMargin(savedGameTimeLabel, new Insets(0, 0, 0, 70));
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Saved Game Delete Button">
            Button savedGameDeleteButton = new Button();
            savedGameDeleteButton.getStyleClass().add("button-icon--dark");
            savedGameDeleteButton.getStyleClass().add("delete-icon");
            savedGameBlockContainer.setRight(savedGameDeleteButton);
            savedGameBlockContainer.setMargin(savedGameDeleteButton, new Insets(4, 0, 0, 0));
            //</editor-fold>

            //Adding blocks to the container
            savedGamesLayout.setConstraints(gameBlock[counter], 0, counter);
            savedGamesLayout.getChildren().add(gameBlock[counter]);

            //Switching scenes and printing the Sudoku
            savedGameTitle.setOnAction(e -> {
                //Printing Sudoku and saving Sudoku ID
                sudokuId = data[0];
                sudokuIdOriginal = data[6];
                assignSudoku(data[1], null);
                assignSudoku(data[5], markSolution);
                sudokuOperation(PRINT_SUDOKU);

                switchPanes(screenContainer, mainMenuContainer, gamePlayContainer);

                levelLabel.setText(data[3]);
                timerLabel.setText(savedGameTimeLabel.getText());

                gameTime.setTimer(timerLabel, Integer.parseInt(data[2]));
                gameTime.start();

                //Clear container
                savedGamesContainer.getChildren().clear();
                if (sudokuOperation(CHECK_SUDOKU)) {
                    hintButton.setDisable(true);
                }
                switchPanes(rightPartContainer, savedGamesContainer, gameModesContainer);
            });

            //Deleting the game
            savedGameDeleteButton.setOnAction(e -> {
                String gameID = savedGameDeleteButton.getParent().getParent().getId();
                Object gameBlockObject = savedGameDeleteButton.getParent().getParent();
                int gameBlockNumber = savedGamesLayout.getRowIndex((Node) gameBlockObject);
                fade(gameBlockObject, 200, 0, FADE_OUT);
                System.out.println(gameID);
                savedGamesNumberGlobal--;

                for (int blockCounter = gameBlockNumber; blockCounter < savedGamesNumber - 1; blockCounter++) {
                    //Creating timeline animation
                    Timeline updateGameTimeline = new Timeline();

                    KeyValue fromKeyValue = new KeyValue(gameBlock[blockCounter + 1].translateYProperty(), gameBlock[blockCounter + 1].getTranslateY());
                    KeyValue toKeyValue = new KeyValue(gameBlock[blockCounter + 1].translateYProperty(), gameBlock[blockCounter + 1].getTranslateY() - 20 - gameBlock[blockCounter + 1].getHeight());

                    KeyFrame startMove = new KeyFrame(Duration.millis(100), fromKeyValue);
                    KeyFrame finishMove = new KeyFrame(Duration.millis(300), toKeyValue);

                    if (blockCounter == savedGamesNumber) {
                        int lastGame = savedGamesNumber;
                        KeyFrame removeBottomSpace = new KeyFrame(Duration.millis(501), ea -> {
                            savedGamesLayout.getRowConstraints().get(lastGame).setMaxHeight(0);
                        });
                    }

                    updateGameTimeline.getKeyFrames().addAll(startMove, finishMove);
                    updateGameTimeline.play();
                }

                try {
                    database.deleteGame(Integer.parseInt(gameID.replace("#", "")));
                } catch (SQLException ex) {

                }
                if (savedGamesNumberGlobal == 0) {
                    switchPanes(rightPartContainer, savedGamesContainer, gameModesContainer);
                    savedGamesContainer.getChildren().clear();
                    loadGameButton.setDisable(true);
                }
            });
        }
    }

    /**
     * Takes an array from Sudoku class copy it to another 2D array
     *
     * @author Muhammad Tarek, Mustaga Magdy
     * @param Sudoku
     */
    private void assignSudoku(String Sudoku, Boolean[][] originalSudoku) {
        int charptr = 0;

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (originalSudoku != null) {
                    originalSudoku[row][column] = Integer.parseInt(Sudoku.charAt(charptr) + "") == 0 ? Boolean.FALSE : Boolean.TRUE;
                } else {
                    computerSolution[row][column] = Integer.parseInt(Sudoku.charAt(charptr) + "");
                }

                charptr++;
            }
        }
    }

    private void finishLoading() {
        Timeline showAndHideTimeline = new Timeline();

        //Creating all key values for the animation
        KeyValue loadingOpacityStart = new KeyValue(loadingIndicator.opacityProperty(), 1);
        KeyValue loadingOpacityEnd = new KeyValue(loadingIndicator.opacityProperty(), 0);

        KeyValue panelOpacityStart = new KeyValue(loadingIndicator.opacityProperty(), 0);
        KeyValue panelOpacityEnd = new KeyValue(loadingIndicator.opacityProperty(), 1);

        KeyValue sudokuOpacityStart = new KeyValue(sudokuCellsContainer.opacityProperty(), 0);
        KeyValue sudokuOpacityEnd = new KeyValue(sudokuCellsContainer.opacityProperty(), 1);

        //Clearing the setCenter
        KeyFrame clear = new KeyFrame(Duration.millis(151), e -> {
            gamePlayContainer.setCenter(null);
        });

        KeyFrame startFadeOut = new KeyFrame(Duration.millis(0), loadingOpacityStart);
        KeyFrame finishFadeOut = new KeyFrame(Duration.millis(200), loadingOpacityEnd);
        KeyFrame addingToCenter = new KeyFrame(Duration.millis(209), e -> {
            gamePlayContainer.setCenter(sudokuCellsContainer);
        });
        KeyFrame addingToLeft = new KeyFrame(Duration.millis(209), e -> {
            gamePlayContainer.setLeft(gameLeftPanelContainer);
        });
        KeyFrame enablingButtons = new KeyFrame(Duration.millis(211), e -> changeButtonState(ENABLE, saveButton, submitButton));
        KeyFrame startFadeIn = new KeyFrame(Duration.millis(210), panelOpacityStart);
        KeyFrame finishFadeIn = new KeyFrame(Duration.millis(510), panelOpacityEnd);
        KeyFrame startFadeIn2 = new KeyFrame(Duration.millis(210), sudokuOpacityStart);
        KeyFrame finishFadeIn2 = new KeyFrame(Duration.millis(510), sudokuOpacityEnd);
        KeyFrame startTimer = new KeyFrame(Duration.millis(1000), e -> gameTime.start());

        showAndHideTimeline.getKeyFrames().addAll(startFadeOut, finishFadeOut, clear, addingToCenter, addingToLeft, enablingButtons, startFadeIn, finishFadeIn, startFadeIn2, finishFadeIn2, startTimer);
        showAndHideTimeline.play();
    }
}
