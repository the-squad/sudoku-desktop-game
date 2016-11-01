import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class main extends Application {
    
    Stage mainStage;
    
    int[][] userSudoku; //Reads the Sudoku from the user
    int[][] computerSolution; //Where computer returns the wrong cells
    
    Scene mainMenuScene; //Where the user will start the app
    Scene gameScene; //Where the user will enter or play Sudoku
    
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
        
        intializeGameScene();
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }
    
    private void intializeGameScene() {
        //Creating layouts
        BorderPane gameSceneLayout = new BorderPane();
        BorderPane toolbarLayout = new BorderPane();
        toolbarLayout.getStyleClass().add("toolbar"); //Creating class name for the layout
        
        //Setting toolbar heigt and width
        toolbarLayout.setPrefHeight(150);
        toolbarLayout.setPrefWidth(1000);
        
        //Creating page headline
        Label headline = new Label("Check your Sudoku");
        headline.setId("headline");
        
        //Creating control buttons
        Button back = new Button("");
        back.getStyleClass().add("iconButton"); //Setting class
        back.setId("backButton"); //Setting ID
        
        Button save = new Button("");
        save.getStyleClass().add("iconButton"); //Setting class
        save.setId("saveButton"); //Setting ID
        
        //Setting position
        toolbarLayout.setCenter(headline);
        toolbarLayout.setLeft(back);
        toolbarLayout.setRight(save);

        //Adding everything into the layout
        toolbarLayout.getChildren().addAll();
        
        //Adding the toolbar in the top of the window
        gameSceneLayout.setTop(toolbarLayout);
        gameScene = new Scene(gameSceneLayout, 1000, 600);
        
        //Connecting the stylesheet
        gameScene.getStylesheets().add("/stylesheets/gameSceneStyle.css");
    }
    
    private void intializeSudokuBlock() {
        //TODO
        
        /*
            Generate Sudodku 9x9 cells and add them into gameScene
        */
    }

    public static void main(String[] args) {
        launch(args);
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
    
}
