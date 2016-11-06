import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

public class main extends Application {

    Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells

    Scene mainMenuScene; //Where the user will start the app
    Scene gameScene; //Where the user will enter or play Sudoku

    BorderPane gameSceneLayout;

    /*
     Playing mode controls what happens and shows on the gameScene
     1. Checking Sudoku solution
     2. Makes the computer solve the Sudoku
     3. Solving a Sudoku from the computer
     */
    int playingMode;

    @Override
    public void start(Stage primaryStage) {
        initMainMenuScene();

        //Main stage property
        primaryStage.setTitle("Sudoku Game!");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }
    
    private void initMainMenuScene() {
        //Main menu layout
        GridPane mainMenuLayout = new GridPane();
        
        //Creating two columns
        ColumnConstraints leftPart = new ColumnConstraints();
        leftPart.setPercentWidth(35);
        
        ColumnConstraints rightPart = new ColumnConstraints();
        rightPart.setPercentWidth(65);
        
        mainMenuLayout.getColumnConstraints().addAll(leftPart, rightPart);
        
        //Creating 100% height row
        RowConstraints fullHeight = new RowConstraints();
        fullHeight.setPercentHeight(100);
        
        mainMenuLayout.getRowConstraints().add(fullHeight);
        
        //Creating left part layout
        BorderPane leftPartLayout = new BorderPane();
        leftPartLayout.getStyleClass().add("left-part");
        mainMenuLayout.setConstraints(leftPartLayout, 0, 0);
        mainMenuLayout.getChildren().add(leftPartLayout);
        
        //Left part elements
        Label logo = new Label();
        logo.getStyleClass().add("logo");
        leftPartLayout.setTop(logo);
        leftPartLayout.setAlignment(logo, Pos.CENTER);
        
        Label logoText = new Label("Sudodu Game");
        logoText.getStyleClass().add("logo-text");
        leftPartLayout.setCenter(logoText);
        
        Label version = new Label("Version 0.0.1");
        version.getStyleClass().add("version");
        leftPartLayout.setBottom(version);
        
        leftPartLayout.getChildren().addAll();
        
        //Right part layout
        GridPane rightPartLayout = new GridPane();
        rightPartLayout.setAlignment(Pos.CENTER);
        rightPartLayout.setPadding(new Insets(25));
        mainMenuLayout.setConstraints(rightPartLayout, 1, 0);
        mainMenuLayout.getChildren().add(rightPartLayout);
        
        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[6];
        for (int counter = 0; counter < 6; counter++) {
            rowNo[counter] = new RowConstraints();
            
            if (counter == 0)
                rowNo[counter].setPercentHeight(20);
            
            rowNo[counter].setPercentHeight(13);
            rightPartLayout.getRowConstraints().add(rowNo[counter]);
        }
        
        //Creating right part elemens
        Label welcomeText = new Label("Welcome, Muhammad");
        welcomeText.getStyleClass().add("welcome-text");
        rightPartLayout.setConstraints(welcomeText, 0, 0);
        rightPartLayout.getChildren().add(welcomeText);
        
        Image newGameIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameIconView = new ImageView(newGameIcon);
        Button newGame = new Button("       New Game", newGameIconView);
        initButtonStyle(newGame, rightPartLayout, 1, newGameIconView);
        
        Image loadGameIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameIcon);
        Button loadGame = new Button("       New Game", laodGameIconView);
        initButtonStyle(loadGame, rightPartLayout, 2, laodGameIconView);
        
        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        Button checkSudokuGame = new Button("       New Game", checkSudokuIconView);
        initButtonStyle(checkSudokuGame, rightPartLayout, 3, checkSudokuIconView);
        
        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        Button challengeComputerGame = new Button("       New Game", challengeComputerIconView);
        initButtonStyle(challengeComputerGame, rightPartLayout, 4, challengeComputerIconView);
        
        Image exitIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitIconView = new ImageView(exitIcon);
        Button exit = new Button("       New Game", exitIconView);
        initButtonStyle(exit, rightPartLayout, 5, exitIconView);
        
        mainMenuScene = new Scene(mainMenuLayout, 1000, 650);

        //Connecting the stylesheet
        mainMenuScene.getStylesheets().add("/stylesheets/mainMenuSceneStyle.css");
    }

    private void initGameScene() {
        //TODO

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

    private void saveCurrentGame() {
        //TODO

        /*
         1. Read the Sudoku from the screen
         2. Show pop-up telling the user that the game is saved
         */
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
    
    /**
     * Initialize button styles, icons sizes
     * Muhammad Tarek
     * @since 6, November
     * @param button
     * @param layout
     * @param position
     * @param icon 
     */
    private void initButtonStyle(Button button, GridPane layout, int position, ImageView icon) {
        button.getStyleClass().add("icon-text-button");
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        layout.setConstraints(button, 0, position);
        layout.getChildren().add(button);
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
