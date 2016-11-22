package UI;

import static UI.global.*;
import static UI.scoreBoard.scoreBoardArray;
import static UI.scoreBoard.timeLabel;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyValue;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sudoku.checker;

public class gamePlay {

    // <editor-fold defaultstate="collapsed" desc="Main Panes">
    private BorderPane gamePlayContainer;
    private BorderPane headerContainer;
    private BorderPane headerCenterAreaContainer;
    private BorderPane sudokuCellsContainer;
    static BorderPane gameLeftPanelContainer;
    private GridPane gameDetailsContainer;
    private GridPane gameControlsContainer;
    private GridPane sudokuCellsTextfieldsContainer;
    private GridPane alertMessageContainer;
    // </editor-fold>

    private static final TextField[][] sudokuCells = new TextField[9][9];

    // <editor-fold defaultstate="collapsed" desc="Labels">
    private Label headlineLabel;
    static Label levelLabel;
    static Label timerLabel;
    private Label levelHeadlineLabel;
    private Label timerHeadlineLabel;
    private Label hintAlertLabel;
    private Label alertMessageLabel;
    private Label alertIcon;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buttons">
    private Button backButton;
    static Button saveButton;
    static Button submitButton;
    private Button pauseButton;
    private Button resumeButton;
    private Button hintButton;
    private Button solveButton;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Timelines">
    private Timeline timerStoppedTimeline;
    private Timeline showAlertTimeline;
    private Timeline hideAlertTimeline;
    // </editor-fold>

    /**
     * Initialize game play elements
     *
     * @author Muhammad Tarek
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

        //Headline + saveButton button layout
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
            saveCurrentGame();
            sudokuOperation(CLEAR_SUDOKU);
            gameTime.pause();
            fade(resumeButton, 250, 0, FADE_OUT);
            gamePlayContainer.setCenter(null);
            gamePlayContainer.setCenter(sudokuCellsContainer);
            fade(sudokuCellsContainer, 250, 0, FADE_IN);
            fade(resumeButton, 250, 0, FADE_OUT);
            fade(pauseButton, 250, 0, FADE_IN);
            pauseButton.setDisable(false);
            hintButton.setDisable(false);
            solveButton.setDisable(false);
            timerLabel.setOpacity(1);
            timerStoppedTimeline.stop();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Save Button">
        saveButton = new Button("");
        saveButton.getStyleClass().add("button-icon--white");
        saveButton.getStyleClass().addAll("save-icon");
        headerCenterAreaContainer.setRight(saveButton);
        headerCenterAreaContainer.setMargin(saveButton, new Insets(0, 15, 0, 0));

        saveButton.setOnAction(e -> {
            saveCurrentGame();
            showPopup("Game is saved successfuly", MESSAGE_SUCCESS);
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Submit Button">
        submitButton = new Button("Submit");
        submitButton.getStyleClass().add("button-transparent");
        headerContainer.setRight(submitButton);

        submitButton.setOnAction((event) -> {
            try {
                if (playingMode != 4) {
                    if (sudokuOperation(CHECK_SUDOKU)) {
                        sudokuOperation(READ_SUDOKU);
                        
                        if (checkSudoku(userSudoku)) {
                            Timeline gameSuccessTimeline = new Timeline();

                            for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                                for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                                    if (!sudokuCells[rowCounter][columnCounter].isDisable()) {
                                        //Only mark user input in green
                                        sudokuCells[rowCounter][columnCounter].setDisable(true);
                                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");
                                    }
                                }
                            }

                            //Show score board only in this case
                            if (playingMode == 1 || playingMode == 2) {
                                timeLabel.setText(timerLabel.getText());

                                KeyFrame goToScoreBoard = new KeyFrame(Duration.millis(2000), e -> {
                                    switchPanes(screenContainer, gamePlayContainer, scorePageContainer);
                                });

                                gameSuccessTimeline.getKeyFrames().add(goToScoreBoard);

                                ArrayList<String> bestScores = null;

                                bestScores = database.highfive(levelLabel.getText());

                                for (int counter = 0; counter < bestScores.size(); counter++) {
                                    String playerName = bestScores.get(counter).split(",")[0];
                                    String time = bestScores.get(counter).split(",")[2];

                                    SimpleDateFormat sdf = new SimpleDateFormat("ss");
                                    Date dateObj = null;
                                    try {
                                        dateObj = sdf.parse(time);
                                    } catch (ParseException ex) {
                                        Logger.getLogger(mainMenu.class.getName()).log(Level.SEVERE, null, ex);
                                    }

                                    scoreBoardArray[counter].setText((counter + 1) + "." + "    " + playerName + "      " + dateObj.getMinutes() + ":" + dateObj.getSeconds());
                                }

                                gameSuccessTimeline.play();

                                gameTime.pause();
                            }
                        }
                    } else {
                        showPopup("There are missing fields", MESSAGE_DANGER);
                    }
                } else {
                    sudokuOperation(READ_SUDOKU);
                    Sudoku.setSudoku(userSudoku);
                    Sudoku.setUserSudoku(userSudoku);
                    Sudoku.solveSudoku();
                    computerSolution = Sudoku.getSudokuSolution();

                    if (checkSudoku(computerSolution)) {
                        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                        for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                            sudokuCells[rowCounter][columnCounter].setDisable(true);
                            sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");

                            submitButton.setDisable(true);
                        }
                    }
                    }
                    
                }
            } catch (InterruptedException | SQLException ex) {
                Logger.getLogger(gamePlay.class.getName()).log(Level.SEVERE, null, ex);
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
            fade(sudokuCellsContainer, 250, 0, FADE_OUT);
            gamePlayContainer.setCenter(null);
            gamePlayContainer.setCenter(resumeButton);
            fade(resumeButton, 250, 0, FADE_IN);
            fade(pauseButton, 250, 0, FADE_OUT);

            gameTime.pause();
            pauseButton.setDisable(true);
            hintButton.setDisable(true);
            solveButton.setDisable(true);
            timerStoppedTimeline.play();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Resume Button">
        resumeButton = new Button();
        resumeButton.getStyleClass().add("button-icon--big");
        resumeButton.getStyleClass().add("resume-icon");

        resumeButton.setOnAction(e -> {
            //Hiding resume button and showing Sudoku card and pause button
            fade(resumeButton, 250, 0, FADE_OUT);
            gamePlayContainer.setCenter(null);
            gamePlayContainer.setCenter(sudokuCellsContainer);
            fade(sudokuCellsContainer, 250, 0, FADE_IN);
            fade(pauseButton, 250, 0, FADE_IN);

            gameTime.start();
            pauseButton.setDisable(false);
            if (!hintButton.isDisabled()) {
                hintButton.setDisable(false);
            }
            solveButton.setDisable(false);
            timerLabel.setOpacity(1);
            timerStoppedTimeline.stop();
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Hint Button">
        Image hintButtonIcon = new Image(getClass().getResourceAsStream("/icons/hint.png"));
        ImageView hintButtonIconView = new ImageView(hintButtonIcon);
        hintButton = new Button("       Hint", hintButtonIconView);
        initButtonStyle(hintButton, gameControlsContainer, 1, hintButtonIconView, TRANSPARENT_BG);

        hintButton.setOnAction(e -> {
            sudokuOperation(READ_SUDOKU);
            Sudoku.setSudoku(computerSolution);
            Sudoku.setUserSudoku(userSudoku);

            try {
                int[] hintDetails = Sudoku.hint();
                sudokuCells[hintDetails[0]][hintDetails[1]].setText(hintDetails[2] + "");
                gameTime.addTenSeconds();
                showAlertTimeline.play();
            } catch (Exception a) {
                hintButton.setDisable(true);
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
            Sudoku.setSudoku(computerSolution);
            Sudoku.setUserSudoku(userSudoku);
            Sudoku.solveSudoku();
            computerSolution = Sudoku.getSudokuSolution();

            for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
                for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                    sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                    sudokuCells[rowCounter][columnCounter].setDisable(true);
                    sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-success");

                    submitButton.setDisable(true);
                    hintButton.setDisable(true);
                    pauseButton.setDisable(true);
                    solveButton.setDisable(true);
                    submitButton.setDisable(true);
                    saveButton.setDisable(true);
                    timerStoppedTimeline.play();
                    gameTime.pause();
                }
            }
        });
        //</editor-fold>

        return gamePlayContainer;
    }

    /**
     * Create Sudoku cells
     *
     * @author Muhammad Tarek
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

        int rowCounter, columnCounter;

        //<editor-fold defaultstate="collapsed" desc="Sudoku Cells">
        for (rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (columnCounter = 0; columnCounter < 9; columnCounter++) {
                //Create cells and positioning hem
                sudokuCells[rowCounter][columnCounter] = new TextField();
                sudokuCellsTextfieldsContainer.setConstraints(sudokuCells[rowCounter][columnCounter], columnCounter, rowCounter);
                sudokuCellsTextfieldsContainer.getChildren().add(sudokuCells[rowCounter][columnCounter]);

                sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell");

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

                TextField currentField = sudokuCells[rowCounter][columnCounter];

                //Adding listener to validate the Sudoku input
                sudokuCells[rowCounter][columnCounter].textProperty().addListener((observable, oldVal, newVal) -> {
                    if (currentField.getLength() > 1) {
                        currentField.setText(oldVal);
                    } else if (!checkInput(currentField.getText())) {
                        currentField.setText("");
                    }
                });
            }

            gamePlayContainer.setCenter(sudokuCellsContainer);
            gamePlayContainer.setAlignment(sudokuCellsContainer, Pos.CENTER);
            gamePlayContainer.getChildren().addAll();
        }
        //</editor-fold>
    }

    /**
     * @author Muhammad Tarek
     * @param message
     * @param alertType
     */
    private void showPopup(String message, int alertType) {
        //Alert message layout
        alertMessageContainer = new GridPane();
        alertMessageContainer.setHgap(10);

        gamePlayContainer.setBottom(alertMessageContainer);
        gamePlayContainer.setAlignment(alertMessageContainer, Pos.CENTER);

        //<editor-fold defaultstate="collapsed" desc="Alert Message">
        alertMessageLabel = new Label(message);
        alertMessageLabel.getStyleClass().add("alert-message");
        alertMessageLabel.getStyleClass().add(alertType == 1 ? "alert-message-success" : "alert-message-danger");
        alertMessageContainer.setConstraints(alertMessageLabel, 1, 0);
        alertMessageContainer.setMargin(alertMessageLabel, new Insets(10, 0, 0, 0));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Alert Message Icon">
        alertIcon = new Label();
        alertIcon.getStyleClass().add("alert-icon");
        alertIcon.getStyleClass().add(alertType == 1 ? "alert-icon-success" : "alert-icon-danger");
        alertMessageContainer.setConstraints(alertIcon, 0, 0);
        //</editor-fold>

        //Adding the alert in gameScene
        alertMessageContainer.getChildren().addAll(alertIcon, alertMessageLabel);
        alertMessageContainer.setAlignment(Pos.CENTER);

        //Fading animation
        fade(alertMessageContainer, 1000, 0, FADE_IN);

        //Auto hide the alert
        hideAlertTimeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> {
                    fade(alertMessageContainer, 0, 1000, 1);
                    gamePlayContainer.setBottom(null);
                }
        ));
        hideAlertTimeline.play();
    }

    /**
     * Save current game into database
     */
    private void saveCurrentGame() {
        sudokuOperation(READ_SUDOKU);
        String sudokuGame = "";

        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                sudokuGame += userSudoku[rowCounter][columnCounter];
            }
        }

        try {
            database.saveGame(sudokuGame, gameTime.getTime(), Integer.parseInt(sudokuIdOriginal), Integer.parseInt(sudokuId));
        } catch (SQLException ex) {
            Logger.getLogger(gamePlay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @author @throws InterruptedException
     */
    private Boolean checkSudoku(Integer[][] sudoku) throws InterruptedException {
        Sudoku.setSudoku(sudoku);
        Sudoku.initSudokuWrongCells();

        checker Checker = new checker();
        Checker.check();
        markSolution = Sudoku.getsudokuWrongCells();

        Boolean isSudoku = true;

        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                if (markSolution[rowCounter][columnCounter]) {
                    if (playingMode != 4)
                        sudokuCells[rowCounter][columnCounter].getStyleClass().add("cell-danger");
                    isSudoku = false;
                }
            }
        }
        return isSudoku;
    }

    /**
     * @author Muhammad Tarek, Mustafa Magdy
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
                            sudokuCells[rowCounter][columnCounter].setText(computerSolution[rowCounter][columnCounter] + "");
                            sudokuCells[rowCounter][columnCounter].setDisable(true);
                        }
                        break;
                    //Clear Sudoku fields and array
                    case 3:
                        sudokuCells[rowCounter][columnCounter].setText("");
                        sudokuCells[rowCounter][columnCounter].setDisable(false);
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
     *
     * @param input
     * @return
     */
    private boolean checkInput(String input) {
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
}
