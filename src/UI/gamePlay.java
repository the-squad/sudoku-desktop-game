package UI;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class gamePlay {

    Scene gameScene;
    BorderPane gameSceneLayout;

    /*
     Playing mode controls what happens and shows on the gameScene
     1. Checking Sudoku solution
     2. Makes the computer solve the Sudoku
     3. Solving a Sudoku from the computer
     */
    int playingMode;

    /**
     * Initialize game play elements
     *
     * @param primaryStage
     * @param playingMode
     * @return gameScene
     */
    public Scene initialize(Stage primaryStage, int playingMode) {
        this.playingMode = playingMode;
        
        //Main layout
        gameSceneLayout = new BorderPane();

        //Toolbar layout
        BorderPane toolbarLayout = new BorderPane();
        toolbarLayout.getStyleClass().add("toolbar");

        toolbarLayout.setPrefHeight(75);
        toolbarLayout.setPrefWidth(1000);

        //Headline + save button layout
        BorderPane headlineAndSaveLayout = new BorderPane();

        //Toolbar objects
        Label headline = new Label("Check your Sudoku");
        headline.setId("headline");

        headline.setMaxWidth(Double.MAX_VALUE);
        headline.setAlignment(Pos.CENTER);

        Button back = new Button("");
        back.getStyleClass().add("iconButton");
        back.setId("backButton");

        Button save = new Button("");
        save.getStyleClass().add("iconButton");
        save.setId("saveButton");

        Button sumbit = new Button("Sumbit");
        sumbit.getStyleClass().add("iconButton");
        sumbit.setId("sumbitButton");

        //Setting position
        toolbarLayout.setLeft(back);

        headlineAndSaveLayout.setRight(save);
        headlineAndSaveLayout.setMargin(save, new Insets(0, 15, 0, 0));

        headlineAndSaveLayout.setCenter(headline);
        headlineAndSaveLayout.setAlignment(headline, Pos.TOP_CENTER);
        toolbarLayout.setCenter(headlineAndSaveLayout);
        headlineAndSaveLayout.setMargin(headline, new Insets(0, 0, 0, 80));

        toolbarLayout.setRight(sumbit);

        //Adding everything into the layout
        toolbarLayout.getChildren().addAll();

        initSudokuBlock();
        save.setOnAction(e -> {
            showPopup("Game is saved successfuly", 1);
        });

        //Adding the toolbar in the top of the window
        gameSceneLayout.setTop(toolbarLayout);
        gameScene = new Scene(gameSceneLayout, 1000, 650);

        //Connecting the stylesheet
        gameScene.getStylesheets().add("/stylesheets/gameSceneStyle.css");

        return gameScene;
    }

    private void initSudokuBlock() {
        //Sudoku card layout
        BorderPane cardBg = new BorderPane();
        cardBg.getStyleClass().add("card");
        cardBg.setPadding(new Insets(7));

        cardBg.setMaxHeight(475);
        cardBg.setMaxWidth(475);

        //Cells container layout
        GridPane cellsLayout = new GridPane();
        cellsLayout.getStyleClass().add("cells-container");
        cardBg.setCenter(cellsLayout);

        //Cells textfields
        TextField[][] sudokuCells = new TextField[9][9];

        int rowCounter, columnCounter;

        //Creating Sudoku cells
        for (rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (columnCounter = 0; columnCounter < 9; columnCounter++) {
                //Create cells and positioning hem
                sudokuCells[rowCounter][columnCounter] = new TextField();
                cellsLayout.setConstraints(sudokuCells[rowCounter][columnCounter], columnCounter, rowCounter);
                cellsLayout.getChildren().add(sudokuCells[rowCounter][columnCounter]);

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
            }
        }

        gameSceneLayout.setCenter(cardBg);
        gameSceneLayout.setAlignment(cardBg, Pos.CENTER);
        gameSceneLayout.getChildren().addAll();
    }

    private void showPopup(String message, int alertType) {
        //Alert message layout
        GridPane alertLayout = new GridPane();
        alertLayout.setHgap(10);

        gameSceneLayout.setBottom(alertLayout);
        gameSceneLayout.setAlignment(alertLayout, Pos.CENTER);

        //Alert message
        Label alertMessage = new Label(message);
        alertMessage.getStyleClass().add("alert-message");

        if (alertType == 1) {
            alertMessage.getStyleClass().add("alert-message-success");
        } else {
            alertMessage.getStyleClass().add("alert-message-danger");
        }

        alertLayout.setConstraints(alertMessage, 1, 0);
        alertLayout.setMargin(alertMessage, new Insets(10, 0, 0, 0));

        //Alert icon
        Label alertIcon = new Label();
        alertIcon.getStyleClass().add("alert-icon");

        if (alertType == 1) {
            alertIcon.getStyleClass().add("alert-icon-success");
        } else {
            alertIcon.getStyleClass().add("alert-icon-danger");
        }

        alertLayout.setConstraints(alertIcon, 0, 0);

        //Adding the alert in gameScene
        alertLayout.getChildren().addAll(alertIcon, alertMessage);
        alertLayout.setAlignment(Pos.CENTER);

        //Fading animation
        FadeTransition showAlertAnimation = new FadeTransition(Duration.millis(1000), alertLayout);
        showAlertAnimation.setFromValue(0);
        showAlertAnimation.setToValue(1);
        showAlertAnimation.play();

        FadeTransition hideAlertAnimation = new FadeTransition(Duration.millis(1000), alertLayout);
        hideAlertAnimation.setFromValue(1);
        hideAlertAnimation.setToValue(0);

        //Auto hide the alert
        Timeline countDown = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> {
                    hideAlertAnimation.play();
                    gameSceneLayout.setBottom(null);
                }
        ));
        countDown.play();
    }

    private void saveCurrentGame() {
        //TODO

        /*
         1. Read the Sudoku from the screen
         2. Show pop-up telling the user that the game is saved
         */
    }

    private void readSudoku() {
        //TODO

        /*
         Will scan all the 9x9 cells and save them into userSudoku;
         */
    }

    private void printSudoku(int type) {
        //TODO

        /*
         Will print all the new/solved/checked Sudok puzzle
        
         Type:
         1. When the Sudoku is solved by the computer, all cells will be
         locked, and the new numbers will be colored in green
         2. When the Sudoku is checked, only the false values will be 
         etidable and colored in red
         */
    }
    
    private void backToHome() {
        //TODO

        /*
         1. Save the current game only in playing mode
         2. Show pop-up telling the user that the game is saved
         3. Hide gameScene
         4. Show mainMenuScene
         5. Show loadSavedGame button in the mainMenuScene
         */
    }
}
