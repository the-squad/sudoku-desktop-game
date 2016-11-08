package UI;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sudoku.Database;

public class main extends Application {
    static BorderPane windowLayout;
    static BorderPane gamePlay;
    static GridPane mainMenu;
    
    //<editor-fold defaultstate="collapsed" desc="Sudoku Arrays">
    static Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    static Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells
    static Boolean[][] markSolution = new Boolean[9][9]; //Where computer returns the wrong cells
//</editor-fold>
    
    static int playingMode;
    static String sudokuId;
    
    static Database database = new Database();
    
    @Override
    public void start(Stage primaryStage) {
        //Connect to database
        database.DBconnect();
         
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
