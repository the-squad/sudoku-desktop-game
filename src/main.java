import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class main extends Application {

    Stage mainStage;

    Integer[][] userSudoku; //Reads the Sudoku from the user
    Integer[][] computerSolution; //Where computer returns the wrong cells
    
    TextField[][] sudokuCells; //FIXME

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
        mainStage = primaryStage;

        initGameScene();

        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }

    private void initGameScene() {
        //TODO
        
        //Main layout
        gameSceneLayout = new BorderPane();
        
        
        //Toolbar layout
        BorderPane toolbarLayout = new BorderPane();
        toolbarLayout.getStyleClass().add("toolbar"); //Creating class name for the layout

        toolbarLayout.setPrefHeight(100);
        toolbarLayout.setPrefWidth(1000);
        

        //Toolbar objects
        Label headline = new Label("Check your Sudoku");
        headline.setId("headline");
        
        //headline.setMaxWidth(Double.MAX_VALUE);
        //headline.setAlignment(Pos.CENTER);

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
        toolbarLayout.setCenter(headline);
        BorderPane.setAlignment(headline, Pos.CENTER);
        
        toolbarLayout.setLeft(back);
        
        toolbarLayout.setCenter(save);
        BorderPane.setAlignment(save, Pos.TOP_RIGHT);
        BorderPane.setMargin(save, new Insets(0,20,0,0));
        
        toolbarLayout.setRight(sumbit);     

        //Adding everything into the layout
        toolbarLayout.getChildren().addAll();
        
        initSudokuBlock();
        

        //Adding the toolbar in the top of the window
        gameSceneLayout.setTop(toolbarLayout);
        gameScene = new Scene(gameSceneLayout, 1000, 600);

        //Connecting the stylesheet
        gameScene.getStylesheets().add("/stylesheets/gameSceneStyle.css");
    }

    private void initSudokuBlock() {
        
        GridPane cardBg = new GridPane();
        cardBg.getStyleClass().add("card");
        
        cardBg.setPrefHeight(400);
        cardBg.setPrefWidth(400);
        
        gameSceneLayout.setCenter(cardBg);
        BorderPane.setAlignment(cardBg, Pos.CENTER);

        int rowCounter, columnCounter;
        
        //Creating Sudoku cells
        for (rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (columnCounter = 0; columnCounter < 9; columnCounter++) {
                //TODO
            }
        }
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
        //TODO

        /*
         Alert types:
         1. Danger: In the solving mode, if the user entered wrong Sudoku
         2. Success: When a game is saved
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

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
