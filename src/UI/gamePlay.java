package UI;

import static UI.global.*;
import static UI.mainMenu.emptyIconView;
import static UI.mainMenu.emptyStateLabel;
import static UI.scoreBoard.*;
import static UI.mainMenu.loadGameButton;
import static UI.mainMenu.pageHeaderContainer;
import static UI.mainMenu.savedGamesContainer;
import static UI.mainMenu.savedGamesNumberGlobal;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import sudoku.checker;

public class gamePlay {

    // <editor-fold defaultstate="collapsed" desc="Main Panes">
    private BorderPane gamePlayContainer;
    private BorderPane headerContainer;
    static BorderPane headerCenterAreaContainer;
    static GridPane headerControlsContainer;
    static BorderPane sudokuCellsContainer;
    static BorderPane gameLeftPanelContainer;
    private GridPane gameDetailsContainer;
    private GridPane gameControlsContainer;
    private GridPane sudokuCellsTextfieldsContainer;
    private GridPane alertMessageContainer;
    // </editor-fold>

    private static final TextField[][] sudokuCells = new TextField[9][9];

    // <editor-fold defaultstate="collapsed" desc="Labels">
    static Label headlineLabel;
    static Label levelLabel;
    static Label timerLabel;
    private Label levelHeadlineLabel;
    private Label timerHeadlineLabel;
    private Label hintAlertLabel;
    private Label alertMessageLabel;
    private Label alertHelpMessageLabel;
    private Label alertIcon;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buttons">
    private Button backButton;
    static Button saveButton;
    private Button undoButton;
    private Button redoButton;
    static Button submitButton;
    static Button pauseButton;
    static Button resumeButton;
    static Button hintButton;
    private Button solveButton;
    private Button closePopupButton;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Timelines">
    private Timeline timerStoppedTimeline;
    private Timeline showAlertTimeline;
    private Timeline hideAlertTimeline;
    private Timeline showPopupTimeline;
    private Timeline hidePopupTimeline;
    // </editor-fold>

    static ProgressIndicator loadingIndicator;
    private ContextMenu contextMenu;
    Boolean listenToChange = false;
    Boolean popupState = false;

    /**
     * Initialize game play elements
     *
     * @return gamePlayLayout
     */
    public BorderPane initialize() {
        //Main layout
        gamePlayContainer = new BorderPane();

        //Toolbar layout
        headerContainer = new BorderPane();
        headerContainer.getStyleClass().add("toolbar");
        headerContainer.setPrefHeight(75);
        gamePlayContainer.setTop(headerContainer);

        //Headline + controls buttons layout
        headerCenterAreaContainer = new BorderPane();

        //<editor-fold defaultstate="collapsed" desc="headline">
        headlineLabel = new Label("Check your Sudoku");
        headlineLabel.setId("headline");
        headlineLabel.setMaxWidth(Double.MAX_VALUE);
        headlineLabel.setAlignment(Pos.CENTER);
        headerCenterAreaContainer.setCenter(headlineLabel);
        headerCenterAreaContainer.setAlignment(headlineLabel, Pos.TOP_CENTER);
        headerContainer.setCenter(headerCenterAreaContainer);
        headerCenterAreaContainer.setMargin(headlineLabel, new Insets(0, 0, 0, 80));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Back Button">
        backButton = new Button("");
        backButton.getStyleClass().add("button-icon--white");
        backButton.getStyleClass().add("back-icon--white");
        headerContainer.setLeft(backButton);

        backButton.setOnAction(e -> {
            switchPanes(screenContainer, gamePlayContainer, mainMenuContainer);
            resetGamePlayScene();
        });
        //</editor-fold>

        //Redo, undo and save buttons layout
        headerControlsContainer = new GridPane();
        headerControlsContainer.setHgap(10);
        headerCenterAreaContainer.setRight(headerControlsContainer);
        headerCenterAreaContainer.setMargin(headerControlsContainer, new Insets(0, 15, 0, -125));

        //<editor-fold defaultstate="collapsed" desc="Save Button">
        saveButton = new Button("");
        saveButton.getStyleClass().add("button-icon--white");
        saveButton.getStyleClass().addAll("save-icon");
        headerControlsContainer.setConstraints(saveButton, 2, 0);

        saveButton.setOnAction(e -> {
            saveCurrentGame();
            if (savedGamesNumberGlobal == 0) {
                pageHeaderContainer.setTranslateY(-28);
                savedGamesContainer.getChildren().remove(emptyIconView);
                savedGamesContainer.getChildren().remove(emptyStateLabel);
            }
            showPopup("Game is saved successfuly", "You can always complete your game anytime you want", MESSAGE_SUCCESS);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Undo Button">
        undoButton = new Button("");
        undoButton.getStyleClass().add("button-icon--white");
        undoButton.getStyleClass().addAll("undo-icon");
        undoButton.setDisable(true);
        headerControlsContainer.setConstraints(undoButton, 0, 0);

        undoButton.setOnAction(e -> {
            listenToChange = false;

            sudokuCells[history.get(undoHistoryMoveNumber)[0]][history.get(undoHistoryMoveNumber)[1]].setText(history.get(undoHistoryMoveNumber)[2] + "");

            undoHistoryMoveNumber--;
            redoHistoryMoveNumber--;

            redoButton.setDisable(false);
            if (undoHistoryMoveNumber == -1) {
                undoButton.setDisable(true);
            }
            if (!sudokuOperation(CHECK_SUDOKU)) {
                hintButton.setDisable(false);
            } else {
                hintButton.setDisable(true);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Redo Button">
        redoButton = new Button("");
        redoButton.getStyleClass().add("button-icon--white");
        redoButton.getStyleClass().addAll("redo-icon");
        redoButton.setDisable(true);
        headerControlsContainer.setConstraints(redoButton, 1, 0);

        redoButton.setOnAction(e -> {
            listenToChange = false;

            sudokuCells[history.get(redoHistoryMoveNumber)[0]][history.get(redoHistoryMoveNumber)[1]].setText(history.get(redoHistoryMoveNumber)[3] + "");

            undoHistoryMoveNumber++;
            redoHistoryMoveNumber++;

            undoButton.setDisable(false);
            if (redoHistoryMoveNumber == history.size()) {
                redoButton.setDisable(true);
            }
            if (!sudokuOperation(CHECK_SUDOKU)) {
                hintButton.setDisable(false);
            } else {
                hintButton.setDisable(true);
            }
        });
        //</editor-fold>

        headerControlsContainer.getChildren().addAll(undoButton, redoButton, saveButton);

        //<editor-fold defaultstate="collapsed" desc="Submit Button">
        submitButton = new Button("Submit");
        submitButton.getStyleClass().add("button-transparent");
        headerContainer.setRight(submitButton);

        submitButton.setOnAction((event) -> {
            try {
                if (playingMode != CHALLENGE_MODE) {
                    if (sudokuOperation(CHECK_SUDOKU)) {
                        sudokuOperation(READ_SUDOKU);

                        if (isSudokuValid(userSudoku)) {
                            changeButtonState(DISABLE, submitButton, hintButton, pauseButton, solveButton, saveButton, undoButton, redoButton);
                            timerStoppedTimeline.play();

                            Timeline gameSuccessTimeline = new Timeline();

                            for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                                for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                                    if (sudokuCells[rowCounter][columnCounter].isEditable()) {
                                        //Only mark user input in green
                                        sudokuCells[rowCounter][columnCounter].setEditable(false);
                                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-danger");
                                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");
                                    }
                                }
                            }

                            //Show score board only in this case
                            if (playingMode == NEW_GAME_MODE || playingMode == LOAD_GAME_MODE) {
                                timeLabel.setText(timerLabel.getText());

                                KeyFrame goToScoreBoard = new KeyFrame(Duration.millis(2000), e -> {
                                    switchPanes(screenContainer, gamePlayContainer, scorePageContainer);
                                    resetGamePlayScene();
                                });

                                gameSuccessTimeline.getKeyFrames().add(goToScoreBoard);

                                ArrayList<String> bestScores = null;

                                bestScores = database.bestFiveTimes(levelLabel.getText());

                                if (bestScores.size() > 0) {
                                    for (int counter = 0; counter < bestScores.size(); counter++) {
                                        String playerName = bestScores.get(counter).split(",")[0];
                                        String time = bestScores.get(counter).split(",")[2];

                                        SimpleDateFormat sdf = new SimpleDateFormat("ss");
                                        Date dateObj = null;
                                        try {
                                            dateObj = sdf.parse(time);
                                        } catch (ParseException ex) {
                                            showPopup("Error occurred", "", MESSAGE_DANGER);
                                        }

                                        scoreBoardArray[counter].setText((counter + 1) + ".    " + playerName + "      " + dateObj.getMinutes() + ":" + dateObj.getSeconds());
                                    }
                                    scoreHeadlineLabel.setText(gameTime.getTime() < Integer.parseInt(bestScores.get(0).split(",")[2]) ? "NEW BEST SCORE" : "YOUR TIME");
                                }

                                gameSuccessTimeline.play();
                                gamePlayContainer.requestFocus();
                            }
                        }
                    } else {
                        showPopup("Sudoku isn't completed", "Please make sure to fill all Sudoku cells", MESSAGE_DANGER);
                    }
                } else if (playingMode == CHALLENGE_MODE) {
                    sudokuOperation(READ_SUDOKU);
                    Sudoku.setSudoku(userSudoku);
                    Sudoku.setUserSudoku(userSudoku);

                    if (Sudoku.solveSudoku()) {
                        computerSolution = Sudoku.getSudokuSolution();

                        if (isSudokuValid(computerSolution)) {
                            for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                                for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                                    sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                                    sudokuCells[rowCounter][columnCounter].setEditable(false);
                                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");

                                    submitButton.setDisable(true);
                                }
                            }
                            gamePlayContainer.requestFocus();
                        } else {
                            showPopup("Sudoku isn't valid", "Please, Enter a valid Sudoku", MESSAGE_DANGER);
                        }
                    } else {
                        showPopup("Sudoku can't be solved", "Please, Enter a valid Sudoku", MESSAGE_DANGER);
                    }
                }
            } catch (InterruptedException | SQLException ex) {
                showPopup("Error Happened", "Please, Try Again!", MESSAGE_DANGER);
            }
        });
        //</editor-fold>

        initSudokuBlock();

        //Left panel
        gameLeftPanelContainer = new BorderPane();
        gameLeftPanelContainer.setPadding(new Insets(30, 0, 40, 10));
        gamePlayContainer.setMargin(gameLeftPanelContainer, new Insets(0, -150, 0, 0));
        gamePlayContainer.setLeft(gameLeftPanelContainer);

        gameDetailsContainer = new GridPane();
        gameLeftPanelContainer.setTop(gameDetailsContainer);

        gameControlsContainer = new GridPane();
        gameControlsContainer.setVgap(10);
        gameLeftPanelContainer.setBottom(gameControlsContainer);

        //<editor-fold defaultstate="collapsed" desc="Level Headline Label">
        levelHeadlineLabel = new Label("LEVEL");
        levelHeadlineLabel.getStyleClass().add("text");
        levelHeadlineLabel.getStyleClass().add("text--headline");
        gameDetailsContainer.setConstraints(levelHeadlineLabel, 0, 0);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Label Label">
        levelLabel = new Label();
        levelLabel.getStyleClass().add("text");
        levelLabel.getStyleClass().add("text--normal");
        gameDetailsContainer.setConstraints(levelLabel, 0, 1);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Time Headline Label">
        timerHeadlineLabel = new Label("TIME");
        timerHeadlineLabel.getStyleClass().add("text");
        timerHeadlineLabel.getStyleClass().add("text--headline");
        gameDetailsContainer.setConstraints(timerHeadlineLabel, 0, 2);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Time Label">
        timerLabel = new Label();
        timerLabel.getStyleClass().add("text");
        timerLabel.getStyleClass().add("text--normal");
        gameDetailsContainer.setConstraints(timerLabel, 0, 4);

        //Timer label animation 
        timerStoppedTimeline = new Timeline(new KeyFrame(Duration.seconds(0.5), (ActionEvent event) -> {
            fade(timerLabel, 100, 0, (timerLabel.getOpacity() == 0 ? FADE_IN : FADE_OUT));
        }));
        timerStoppedTimeline.setCycleCount(Timeline.INDEFINITE);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Hint Alert Label">
        hintAlertLabel = new Label("+10 Seconds");
        hintAlertLabel.getStyleClass().add("alert-text");
        hintAlertLabel.setOpacity(0);
        gameDetailsContainer.setConstraints(hintAlertLabel, 0, 5);
        hintAlertLabel.setTranslateY(50);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Show Hint Alert Timeline">
        showAlertTimeline = new Timeline();

        KeyValue fromOpacity = new KeyValue(hintAlertLabel.opacityProperty(), 1);
        KeyValue toOpacity = new KeyValue(hintAlertLabel.opacityProperty(), 0);
        KeyValue fromPosition = new KeyValue(hintAlertLabel.translateYProperty(), hintAlertLabel.getTranslateY());
        KeyValue toPosition = new KeyValue(hintAlertLabel.translateYProperty(), hintAlertLabel.getTranslateY() - 100);

        KeyFrame showFrame = new KeyFrame(Duration.millis(50), e -> hintAlertLabel.setOpacity(1));
        KeyFrame startMoving = new KeyFrame(Duration.ZERO, fromPosition);
        KeyFrame finishMoving = new KeyFrame(Duration.millis(450), toPosition);
        KeyFrame startOpacity = new KeyFrame(Duration.millis(350), fromOpacity);
        KeyFrame finishOpacity = new KeyFrame(Duration.millis(450), toOpacity);
        KeyFrame resetOpacity = new KeyFrame(Duration.millis(455), e -> hintAlertLabel.setOpacity(0));
        KeyFrame resetPosition = new KeyFrame(Duration.millis(455), e -> hintAlertLabel.setTranslateY(hintAlertLabel.getTranslateY() + 100));

        showAlertTimeline.getKeyFrames().addAll(showFrame, startMoving, finishMoving, startOpacity, finishOpacity);
        //</editor-fold>

        gameDetailsContainer.getChildren().addAll(levelHeadlineLabel, levelLabel, timerHeadlineLabel, timerLabel, hintAlertLabel);

        //<editor-fold defaultstate="collapsed" desc="Pause Button">
        Image pauseButtonIcon = new Image(getClass().getResourceAsStream("/icons/pause.png"));
        ImageView pauseButtonIconView = new ImageView(pauseButtonIcon);
        pauseButton = new Button("       Pause", pauseButtonIconView);
        initButtonStyle(pauseButton, gameControlsContainer, 0, pauseButtonIconView, TRANSPARENT_BG);

        pauseButton.setOnAction(e -> {
            //Hiding Sudoku card and pause button and showing resume button
            gameTime.pause();
            timerStoppedTimeline.play();

            fade(sudokuCellsContainer, 250, 0, FADE_OUT);
            gamePlayContainer.setCenter(null);
            gamePlayContainer.setCenter(resumeButton);
            fade(resumeButton, 250, 0, FADE_IN);
            fade(pauseButton, 250, 0, FADE_OUT);
            changeButtonState(DISABLE, hintButton, solveButton, saveButton, undoButton, redoButton, submitButton);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Resume Button">
        resumeButton = new Button();
        resumeButton.getStyleClass().add("button-icon--big");
        resumeButton.getStyleClass().add("resume-icon");

        resumeButton.setOnAction(e -> {
            //Hiding resume button and showing Sudoku card and pause button
            gameTime.start();
            timerStoppedTimeline.stop();
            timerLabel.setOpacity(1);

            fade(resumeButton, 250, 0, FADE_OUT);
            gamePlayContainer.setCenter(null);
            gamePlayContainer.setCenter(sudokuCellsContainer);
            fade(sudokuCellsContainer, 250, 0, FADE_IN);
            fade(pauseButton, 250, 0, FADE_IN);

            changeButtonState(ENABLE, solveButton, saveButton, submitButton);
            if (!sudokuOperation(CHECK_SUDOKU)) {
                hintButton.setDisable(false);
            }
            if (undoHistoryMoveNumber != -1) {
                undoButton.setDisable(false);
            }
            if (redoHistoryMoveNumber != history.size()) {
                redoButton.setDisable(false);
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Hint Button">
        Image hintButtonIcon = new Image(getClass().getResourceAsStream("/icons/hint.png"));
        ImageView hintButtonIconView = new ImageView(hintButtonIcon);
        hintButton = new Button("       Hint", hintButtonIconView);
        initButtonStyle(hintButton, gameControlsContainer, 1, hintButtonIconView, TRANSPARENT_BG);

        hintButton.setOnAction(e -> {
            sudokuOperation(READ_SUDOKU);
            if (playingMode == NEW_GAME_MODE) {
                Sudoku.setSudoku(computerSolution);
            } else {
                Sudoku.setSudoku(loadedGameSudoku);
            }

            Sudoku.setUserSudoku(userSudoku);

            int[] hintDetails = Sudoku.hint();
            if (hintDetails == null) {
                hintButton.setDisable(true);
            } else {
                sudokuCells[hintDetails[0]][hintDetails[1]].setText(hintDetails[2] + "");
                //Clearign any history moves if the user made a move and there are redo moves to make
                if (redoHistoryMoveNumber != history.size()) {
                    for (int counter = history.size() - 1; counter >= redoHistoryMoveNumber; counter--) {
                        history.remove(counter);
                        redoButton.setDisable(true);
                    }
                }

                //Saving current move into an arraylist
                history.add(new Integer[]{hintDetails[0], hintDetails[1], 0, hintDetails[2]});
                undoHistoryMoveNumber++;
                redoHistoryMoveNumber++;
                undoButton.setDisable(false);

                gameTime.addTenSeconds();
                showAlertTimeline.play();
                if (sudokuOperation(CHECK_SUDOKU)) {
                    hintButton.setDisable(true);
                }
            }

        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Solve Button">
        Image solveButtonIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView solveButtonIconView = new ImageView(solveButtonIcon);
        solveButton = new Button("       Solve", solveButtonIconView);
        initButtonStyle(solveButton, gameControlsContainer, 2, solveButtonIconView, TRANSPARENT_BG);

        solveButton.setOnAction(e -> {
            sudokuOperation(READ_SUDOKU);
            if (playingMode == NEW_GAME_MODE) {
                Sudoku.setSudoku(computerSolution);
            } else {
                Sudoku.setSudoku(loadedGameSudoku);
            }
            Sudoku.setUserSudoku(userSudoku);
            Sudoku.solveSudoku();
            computerSolution = Sudoku.getSudokuSolution();

            try {
                if (isSudokuValid(computerSolution)) {
                    changeButtonState(DISABLE, submitButton, hintButton, pauseButton, solveButton, saveButton, redoButton, undoButton);

                    gameTime.pause();
                    timerStoppedTimeline.play();

                    for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                        for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                            sudokuCells[rowCounter][columnCounter].setEditable(false);
                            sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");
                        }
                    }
                    gamePlayContainer.requestFocus();
                } else {
                    showPopup("The solution we made was wrong!", "Try again!", MESSAGE_DANGER);
                }
            } catch (InterruptedException ex) {
                showPopup("Error occured!", "Try again!", MESSAGE_DANGER);
            }
        });
        //</editor-fold>

        //Loading icon
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setStyle(" -fx-progress-color: rgb(65, 131, 215);");
        loadingIndicator.setMaxHeight(75);
        loadingIndicator.setMaxWidth(75);
        loadingIndicator.setVisible(true);

        gamePlayContainer.setOnMousePressed((MouseEvent event) -> {
            gameControlsContainer.requestFocus();
        });

        //<editor-fold defaultstate="collapsed" desc="Keyboard Shortcuts">
        final KeyCombination saveGameCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        final KeyCombination undoGameCombination = new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN);
        final KeyCombination redoGameCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        final KeyCombination hintGameCombination = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN);
        final KeyCombination solveGameCombination = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        final KeyCombination goBackCombination = new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_DOWN);
        final KeyCombination resumeAndPauseCombination = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);

        gamePlayContainer.addEventHandler(KeyEvent.KEY_PRESSED,
                (Event event) -> {
                    if (playingMode == NEW_GAME_MODE || playingMode == LOAD_GAME_MODE) {
                        if (saveGameCombination.match((KeyEvent) event)) {
                            saveButton.fire();
                        } else if (undoGameCombination.match((KeyEvent) event)) {
                            undoButton.fire();
                        } else if (redoGameCombination.match((KeyEvent) event)) {
                            redoButton.fire();
                        } else if (hintGameCombination.match((KeyEvent) event)) {
                            hintButton.fire();
                        } else if (solveGameCombination.match((KeyEvent) event)) {
                            solveButton.fire();
                        } else if (goBackCombination.match((KeyEvent) event)) {
                            backButton.fire();
                        } else if (resumeAndPauseCombination.match((KeyEvent) event)) {
                            if (submitButton.isDisabled()) {
                                resumeButton.fire();
                            } else {
                                pauseButton.fire();
                            }
                        }

                    }
                }
        );

        gamePlayContainer.setOnKeyPressed(
                (final KeyEvent keyEvent) -> {
                    if (null != keyEvent.getCode()) {
                        switch (keyEvent.getCode()) {
                            case ENTER:
                                submitButton.fire();
                                //Stop letting it do anything else
                                keyEvent.consume();
                                break;
                            case F1:
                                gameTime.pause();
                                switchPanes(screenContainer, gamePlayContainer, shortcutHelpContainer);
                                keyEvent.consume();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        //</editor-fold>

        return gamePlayContainer;
    }

    /**
     * Create Sudoku cells, 9x9 textfields
     */
    private void initSudokuBlock() {
        //Sudoku card layout
        sudokuCellsContainer = new BorderPane();
        sudokuCellsContainer.getStyleClass().add("card");
        sudokuCellsContainer.setPadding(new Insets(7));
        sudokuCellsContainer.setMaxHeight(475);
        sudokuCellsContainer.setMaxWidth(475);

        //Cells container layout
        sudokuCellsTextfieldsContainer = new GridPane();
        sudokuCellsTextfieldsContainer.getStyleClass().add("cells-container");
        sudokuCellsContainer.setCenter(sudokuCellsTextfieldsContainer);

        //Unreal menu to override default menu
        ContextMenu hiddenMenu = new ContextMenu();
        hiddenMenu.hide();
        int rowCounter, columnCounter;

        //<editor-fold defaultstate="collapsed" desc="Sudoku Cells">
        for (rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (columnCounter = 0; columnCounter < 9; columnCounter++) {
                //Create cells and positioning hem
                sudokuCells[rowCounter][columnCounter] = new TextField();
                sudokuCellsTextfieldsContainer.setConstraints(sudokuCells[rowCounter][columnCounter], columnCounter, rowCounter);
                sudokuCellsTextfieldsContainer.getChildren().add(sudokuCells[rowCounter][columnCounter]);

                sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell");
                sudokuCells[rowCounter][columnCounter].setContextMenu(contextMenu);

                //If the cell is No.2 or No.5 on any column it will have right border
                if (columnCounter == 2 || columnCounter == 5) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-right");
                }

                //If the cell is No.3 or No.6 on any row it will have top border
                if (rowCounter == 3 || rowCounter == 6) {
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top");
                    //Because the previus line of code override the right border
                    if (columnCounter == 2 || columnCounter == 5) {
                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("border-top-right");
                    }
                }

                sudokuCells[rowCounter][columnCounter].setContextMenu(hiddenMenu);

                TextField currentField = sudokuCells[rowCounter][columnCounter];
                int currentFieldRowNumber = rowCounter;
                int currentFieldColumnNumber = columnCounter;

                sudokuCells[rowCounter][columnCounter].setOnKeyPressed((KeyEvent ke) -> {
                    listenToChange = true;
                });

                final KeyCombination hintCellCombination = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN);
                final KeyCombination highlightCellsCombination = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);

                sudokuCells[rowCounter][columnCounter].addEventHandler(KeyEvent.KEY_PRESSED, (Event event) -> {
                    if (playingMode == NEW_GAME_MODE || playingMode == LOAD_GAME_MODE) {
                        if (hintCellCombination.match((KeyEvent) event)) {
                            sudokuOperation(READ_SUDOKU);
                            if (playingMode == NEW_GAME_MODE) {
                                Sudoku.setSudoku(computerSolution);
                            } else {
                                Sudoku.setSudoku(loadedGameSudoku);
                            }
                            Sudoku.setUserSudoku(userSudoku);
                            currentField.setText(Sudoku.hint(currentFieldRowNumber, currentFieldColumnNumber) + "");
                        } else if (highlightCellsCombination.match((KeyEvent) event)) {
                            highlightCell(currentField.getText());
                        }
                    }
                });

                //Adding listener to validate the Sudoku input
                sudokuCells[rowCounter][columnCounter].textProperty().addListener((observable, oldVal, newVal) -> {
                    if (currentField.getLength() > 1) {
                        currentField.setText(oldVal);
                    } else if (!isInputValid(currentField.getText())) {
                        currentField.setText("");
                    } else //Only save in history if the listenToChange == true
                     if (listenToChange && playingMode == NEW_GAME_MODE || playingMode == LOAD_GAME_MODE) {
                            //Clearign any history moves if the user made a move and there are redo moves to make
                            if (redoHistoryMoveNumber != history.size()) {
                                redoButton.setDisable(true);

                                for (int counter = history.size() - 1; counter >= redoHistoryMoveNumber; counter--) {
                                    history.remove(counter);
                                }
                            }

                            //Saving current move into an arraylist
                            history.add(new Integer[]{currentFieldRowNumber, currentFieldColumnNumber, Integer.parseInt("".equals(oldVal) ? "0" : oldVal), Integer.parseInt("".equals(newVal) ? "0" : newVal)});
                            undoHistoryMoveNumber++;
                            redoHistoryMoveNumber++;
                            undoButton.setDisable(false);

                            if (!sudokuOperation(CHECK_SUDOKU)) {
                                hintButton.setDisable(false);
                            } else {
                                hintButton.setDisable(true);
                            }
                        }
                    currentField.getStyleClass().remove("cell-danger");
                });
            }

            gamePlayContainer.setCenter(sudokuCellsContainer);
            gamePlayContainer.setAlignment(sudokuCellsContainer, Pos.CENTER);
            gamePlayContainer.getChildren().addAll();
        }
        //</editor-fold>
    }

    /**
     * Shows a popup message
     *
     * @param message, what to show
     * @param helpText, help text to tell the user what to do
     * @param alertType, success or danger
     */
    private void showPopup(String message, String helpText, int alertType) {
        //Alert message layout
        alertMessageContainer = new GridPane();
        alertMessageContainer.setHgap(10);
        alertMessageContainer.setPrefHeight(50);
        alertMessageContainer.setPadding(new Insets(0, 0, 0, 50));
        alertMessageContainer.getStyleClass().add(alertType == 1 ? "alert-success" : "alert-danger");

        //Creating row constraints
        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(3);
        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(20);
        ColumnConstraints thirdColumn = new ColumnConstraints();
        thirdColumn.setPercentWidth(70);
        ColumnConstraints forthColumn = new ColumnConstraints();
        alertMessageContainer.getColumnConstraints().addAll(firstColumn, secondColumn, thirdColumn, forthColumn);

        gamePlayContainer.setBottom(alertMessageContainer);
        gamePlayContainer.setAlignment(alertMessageContainer, Pos.CENTER_LEFT);

        //<editor-fold defaultstate="collapsed" desc="Alert Message Icon">
        alertIcon = new Label();
        alertIcon.getStyleClass().add("alert-icon");
        alertIcon.getStyleClass().add(alertType == 1 ? "alert-icon-success" : "alert-icon-danger");
        alertMessageContainer.setConstraints(alertIcon, 0, 0);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Alert Message">
        alertMessageLabel = new Label(message);
        alertMessageLabel.getStyleClass().add("alert-message");
        alertMessageLabel.getStyleClass().add(alertType == 1 ? "alert-message-success" : "alert-message-danger");
        alertMessageContainer.setConstraints(alertMessageLabel, 1, 0);
        alertMessageContainer.setMargin(alertMessageLabel, new Insets(10, 0, 0, 0));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Alert Help Message">
        alertHelpMessageLabel = new Label(helpText);
        alertHelpMessageLabel.getStyleClass().add("alert-help");
        alertMessageContainer.setConstraints(alertHelpMessageLabel, 2, 0);
        alertMessageContainer.setMargin(alertHelpMessageLabel, new Insets(10, 0, 0, 0));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Close Button">
        closePopupButton = new Button("");
        closePopupButton.getStyleClass().add("button-icon--white");
        closePopupButton.getStyleClass().addAll("close-icon");
        alertMessageContainer.setConstraints(closePopupButton, 3, 0);

        closePopupButton.setOnAction(e -> {
            hidePopupTimeline.play();
        });
        //</editor-fold> 

        //Adding the alert in gameScene
        alertMessageContainer.getChildren().addAll(alertIcon, alertMessageLabel, alertHelpMessageLabel, closePopupButton);
        alertMessageContainer.setAlignment(Pos.CENTER_LEFT);

        //Fading animation
        showPopupTimeline = new Timeline();

        KeyValue fromPosition = new KeyValue(alertMessageContainer.translateYProperty(), 50);
        KeyValue toPosition = new KeyValue(alertMessageContainer.translateYProperty(), 0);

        KeyFrame startMove = new KeyFrame(Duration.ZERO, fromPosition);
        KeyFrame finishMove = new KeyFrame(Duration.millis(200), toPosition);

        showPopupTimeline.getKeyFrames().addAll(startMove, finishMove);
        popupState = true;
        showPopupTimeline.play();

        hidePopupTimeline = new Timeline();

        KeyValue fromPositionReverse = new KeyValue(alertMessageContainer.translateYProperty(), 0);
        KeyValue toPositionReverse = new KeyValue(alertMessageContainer.translateYProperty(), 50);

        KeyFrame startMoveReverse = new KeyFrame(Duration.ZERO, fromPositionReverse);
        KeyFrame finishMoveReverse = new KeyFrame(Duration.millis(200), toPositionReverse);
        KeyFrame clear = new KeyFrame(Duration.millis(201), e -> gamePlayContainer.setBottom(null));

        hidePopupTimeline.getKeyFrames().addAll(startMoveReverse, finishMoveReverse, clear);

        //Auto hide the alert
        hideAlertTimeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> {
                    hidePopupTimeline.play();
                    popupState = false;
                }
        ));
        hideAlertTimeline.play();
    }

    /**
     * Save current game into database and shows alert when it fails
     */
    private void saveCurrentGame() {
        sudokuOperation(READ_SUDOKU);
        if (saveGameState) {
            try {
                sudokuIdOriginal = Integer.toString(database.saveOriginalSudoku(sudokuGame, levelLabel.getText()));
                saveGameState = false;
            } catch (SQLException ex) {
                Logger.getLogger(gamePlay.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String currentSudokuGame = "";

        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                currentSudokuGame += userSudoku[rowCounter][columnCounter];
            }
        }

        try {
            sudokuId = database.saveGame(currentSudokuGame, gameTime.getTime(), Integer.parseInt(sudokuIdOriginal), Integer.parseInt(sudokuId)) + "";
            loadGameButton.setDisable(false);
            loadGameButton.setStyle("-fx-opacity: 1;");
        } catch (SQLException ex) {
            showPopup("Game isn't saved", "Try Again!", MESSAGE_DANGER);
        }
    }

    /**
     * Checks if there any duplicate in the Sudoku
     *
     * @throws InterruptedException
     * @param sudoku, 2D Sudoku array
     */
    private Boolean isSudokuValid(Integer[][] sudoku) throws InterruptedException {
        Sudoku.setSudoku(sudoku);
        Sudoku.setUserSudoku(sudoku);
        Sudoku.initSudokuWrongCells();

        checker Checker = new checker();
        Checker.check();
        markSolution = Sudoku.getsudokuWrongCells();

        Boolean isSudoku = true;

        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-danger");

                if (markSolution[rowCounter][columnCounter]) {
                    if (playingMode != CHALLENGE_MODE) {
                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-danger");
                    }
                    isSudoku = false;
                }
            }
        }
        return isSudoku;
    }

    /**
     * Make some operations on Sudoku cells 1. Read Sudoku cells 2. Print Sudoku
     * cells 3. Clear all Sudoku cells and arrays 4. Check if their any Sudoku
     * cell is empty
     *
     * @param opType
     */
    static Boolean sudokuOperation(int opType) {
        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                switch (opType) {
                    //Read Sudoku
                    case 1:
                        userSudoku[rowCounter][columnCounter] = Integer.parseInt("".equals(sudokuCells[rowCounter][columnCounter].getText()) ? "0" : sudokuCells[rowCounter][columnCounter].getText());
                        break;
                    //Print Sudoku
                    case 2:
                        if (computerSolution[rowCounter][columnCounter] != 0) {
                            if (playingMode == NEW_GAME_MODE) {
                                sudokuCells[rowCounter][columnCounter].setEditable(false);
                            }
                            if (playingMode == LOAD_GAME_MODE) {
                                if (markSolution[rowCounter][columnCounter]) {
                                    sudokuCells[rowCounter][columnCounter].setEditable(false);
                                }
                            }
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                        }
                        break;
                    //Clear Sudoku fields and array
                    case 3:
                        sudokuCells[rowCounter][columnCounter].setText("");
                        sudokuCells[rowCounter][columnCounter].setEditable(true);
                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-danger");
                        sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-success");

                        userSudoku[rowCounter][columnCounter] = 0;
                        computerSolution[rowCounter][columnCounter] = 0;
                        markSolution[rowCounter][columnCounter] = Boolean.FALSE;
                        break;
                    //Check if the Sudoku cells are filled
                    case 4:
                        if ("".equals(sudokuCells[rowCounter][columnCounter].getText())) {
                            return false;
                        }
                    default:
                        break;
                }
            }
        }
        return true;
    }

    /**
     * Check that the input is integer
     *
     * @param input, Sudoku cells data
     * @return true/false
     */
    private boolean isInputValid(String input) {
        int checkInput;

        try {
            checkInput = Integer.parseInt(input);
            if (checkInput <= 0 || checkInput > 9) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void highlightCell(String numberMatch) {
        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                sudokuCells[rowCounter][columnCounter].getStyleClass().remove("cell-focus");
                if (sudokuCells[rowCounter][columnCounter].getText().equals(numberMatch) && !"".equals(sudokuCells[rowCounter][columnCounter].getText())) {
                    TextField cell = sudokuCells[rowCounter][columnCounter];
                    Timeline hightlightTimeline = new Timeline();

                    KeyFrame startHighlight = new KeyFrame(Duration.ZERO, e -> cell.getStyleClass().add("cell-focus"));
                    KeyFrame finishHighlight = new KeyFrame(Duration.millis(4000), e -> cell.getStyleClass().remove("cell-focus"));

                    hightlightTimeline.getKeyFrames().addAll(startHighlight, finishHighlight);
                    hightlightTimeline.play();
                }
            }
        }

    }

    private void resetGamePlayScene() {
        sudokuOperation(CLEAR_SUDOKU);

        //Reseting timer
        if (playingMode == NEW_GAME_MODE || playingMode == LOAD_GAME_MODE) {
            gameTime.pause();
            timerStoppedTimeline.stop();
            timerLabel.setOpacity(1);
            timerLabel.setText("");
        }

        //Enabling and disabling buttons
        changeButtonState(ENABLE, pauseButton, hintButton, solveButton, submitButton);
        changeButtonState(DISABLE, undoButton, redoButton);

        //Reseting history
        history.clear();
        undoHistoryMoveNumber = -1;
        redoHistoryMoveNumber = 0;

        //Hide pop-up if it still visible
        if (popupState == true) {
            hidePopupTimeline.play();
        }

        //Reseting current sudoku ID
        sudokuId = "0";

        //Disabling sudoku cells to listen to any input
        listenToChange = false;

        //Making sure that the sudoku block is visible
        gamePlayContainer.setCenter(sudokuCellsContainer);
        sudokuCellsContainer.setOpacity(1);
        resumeButton.setOpacity(0);
        pauseButton.setOpacity(1);
    }
}
