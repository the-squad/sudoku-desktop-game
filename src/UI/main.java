package UI;

import static UI.global.windowLayout;
import static UI.global.gamePlayContainer;
import static UI.global.mainMenuContainer;
import static UI.global.scorePageContainer;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sudoku.Database;

public class main extends Application {

    static String sudokuId;

    static Database database = new Database();

    @Override
    public void start(Stage primaryStage) {
        //Connect to database
        database.DBconnect();

        //Initiliaza screens
        windowLayout = new BorderPane();

        mainMenuContainer = new mainMenu().initialize();
        gamePlayContainer = new gamePlay().initialize();
        scorePageContainer = new scoreBoard().initialize();

        windowLayout.setCenter(scorePageContainer); //Change to mainMenu later

        //Main stage property
        primaryStage.setTitle("Sudoku Game!");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);

        Scene windowScene = new Scene(windowLayout, 1000, 650);
        //Connecting the stylesheet
        windowScene.getStylesheets().add("/stylesheets/mainMenuSceneStyle.css");
        windowScene.getStylesheets().add("/stylesheets/gameSceneStyle.css");
        windowScene.getStylesheets().add("/stylesheets/scoreSceneStyle.css");
        primaryStage.setScene(windowScene);
        primaryStage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
