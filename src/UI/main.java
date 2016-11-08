package UI;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class main extends Application {
    static BorderPane windowLayout;
    static BorderPane gamePlay;
    static GridPane mainMenu;
    
    static Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    static Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells
    static Boolean[][] markSolution = new Boolean[9][9]; //Where computer returns the wrong cells
    
    static int playingMode;
    
    @Override
    public void start(Stage primaryStage) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                computerSolution[i][j] = i;
            }
        }
         
        //Initiliaza screens
        windowLayout = new BorderPane();
              
        gamePlay = new gamePlay().initialize();
        mainMenu = new mainMenu().initialize();
        
        windowLayout.setCenter(mainMenu);
        
        //Main stage property
        primaryStage.setTitle("Sudoku Game!");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        
        Scene windowScene = new Scene(windowLayout, 1000, 650);
        //Connecting the stylesheet
        windowScene.getStylesheets().add("/stylesheets/mainMenuSceneStyle.css");
        windowScene.getStylesheets().add("/stylesheets/gameSceneStyle.css");
        primaryStage.setScene(windowScene);
        primaryStage.show();       
    }
    
    public static void clearArray() {
        for (int rowCounter = 0; rowCounter < 9; rowCounter++) {
            for (int columnCounter = 0; columnCounter < 9; columnCounter++) {
                userSudoku[rowCounter][columnCounter] = 0;
                computerSolution[rowCounter][columnCounter] = 0;
                markSolution[rowCounter][columnCounter] = Boolean.FALSE;
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
