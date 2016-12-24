package UI;

import static UI.global.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import javafx.util.Duration;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) {
        //Connect to database
        database.DBconnect();

        //Initiliaza screens
        screenContainer = new BorderPane();

        mainMenuContainer = new mainMenu().initialize();
        screenContainer.setCenter(mainMenuContainer);

        //Loading another containers after the main containter is loaded
        Timeline loadOtherContainers = new Timeline();
        KeyFrame loadGamePlay = new KeyFrame(Duration.millis(500), e -> gamePlayContainer = new gamePlay().initialize());
        KeyFrame loadScoreBoard = new KeyFrame(Duration.millis(3000), e -> scorePageContainer = new scoreBoard().initialize());
        loadOtherContainers.getKeyFrames().addAll(loadGamePlay, loadScoreBoard);
        loadOtherContainers.play();

        //Main stage property
        primaryStage.setTitle("Sudoku Game!");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.getIcons().add(new Image("/icons/logo-icon.png"));

        Scene windowScene = new Scene(screenContainer, 1000, 650);
        //Connecting the stylesheet
        windowScene.getStylesheets().add("/stylesheets/mainMenuSceneStyle.css");
        windowScene.getStylesheets().add("/stylesheets/gameSceneStyle.css");
        windowScene.getStylesheets().add("/stylesheets/scoreSceneStyle.css");
        windowScene.getStylesheets().add("/stylesheets/global.css");
        primaryStage.setScene(windowScene);
        primaryStage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }
}
