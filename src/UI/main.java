package UI;

import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.stage.Stage;

public class main extends Application {

    Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells
    
    @Override
    public void start(Stage primaryStage) {
        //Main stage property
        primaryStage.setTitle("Sudoku Game!");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(new mainMenu().initialize(primaryStage));
        primaryStage.show();
    }
    
    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
