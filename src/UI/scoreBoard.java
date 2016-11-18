package UI;

import static UI.global.windowLayout;
import static UI.global.mainMenuContainer;
import static UI.global.switchPanes;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Muhammad
 */
public class scoreBoard {

    GridPane scorePageContainer;

    public GridPane initialize() {
        //Score page layout
        scorePageContainer = new GridPane();
        scorePageContainer.getStyleClass().add("score-page");
        scorePageContainer.setAlignment(Pos.CENTER);

        Label headline = new Label("Time");
        headline.getStyleClass().add("score-text");
        headline.getStyleClass().add("page-headline");
        headline.setPadding(new Insets(0 ,0 ,5, 0));
        centerObject(headline);
        scorePageContainer.setConstraints(headline, 0, 0);

        Label timeLabel = new Label("1:00");
        timeLabel.getStyleClass().add("score-text");
        timeLabel.getStyleClass().add("game-score-time");
        centerObject(timeLabel);
        scorePageContainer.setConstraints(timeLabel, 0, 1);

        GridPane saveDataLayout = new GridPane();
        centerObject(saveDataLayout);
        scorePageContainer.setConstraints(saveDataLayout, 0, 2);

        Label inputLabel = new Label("Enter you name:");
        inputLabel.getStyleClass().add("input-label");
        inputLabel.getStyleClass().add("score-text");
        saveDataLayout.setConstraints(inputLabel, 0, 0);
        
        TextField nameTextField = new TextField();
        nameTextField.getStyleClass().add("name-text-field");
        saveDataLayout.setConstraints(nameTextField, 1, 0);
        
        Button nameSumbitButton = new Button("SAVE");
        nameSumbitButton.getStyleClass().add("score-button");
        nameSumbitButton.setPadding(new Insets(5, 20, 5, 20));
        nameSumbitButton.setTranslateX(20);
        saveDataLayout.setConstraints(nameSumbitButton, 2, 0);

        saveDataLayout.getChildren().addAll(inputLabel, nameTextField, nameSumbitButton);

        Label boardHeadline = new Label("Time Score Board");
        boardHeadline.getStyleClass().add("score-text");
        boardHeadline.getStyleClass().add("page-headline");
        boardHeadline.setPadding(new Insets(80 ,0 ,15, 0));
        scorePageContainer.setConstraints(boardHeadline, 0, 3);
        centerObject(boardHeadline);

        scorePageContainer.getChildren().addAll(headline, timeLabel, saveDataLayout, boardHeadline);

        Label scoreBoardArray[] = new Label[5];

        for (int counter = 0; counter < 5; counter++) {
            scoreBoardArray[counter] = new Label(counter + 1 + ".        1:00        Muhammad Tarek");
            scoreBoardArray[counter].getStyleClass().add("score-line");
            scoreBoardArray[counter].getStyleClass().add("score-text");
            centerObject(scoreBoardArray[counter]);
            
            if (counter < 4)
                scoreBoardArray[counter].setPadding(new Insets(5, 0, 5, 0));
            else
                scoreBoardArray[counter].setPadding(new Insets(5, 0, 30, 0));
            
            scorePageContainer.setConstraints(scoreBoardArray[counter], 0, 4 + counter + 1);
            scorePageContainer.getChildren().add(scoreBoardArray[counter]);
        } 

        Button backToMenuButton = new Button("BACK TO MAIN MENU");
        backToMenuButton.getStyleClass().add("score-button");
        backToMenuButton.setId("back--button");
        backToMenuButton.setPadding(new Insets(10, 20, 10, 20));
        scorePageContainer.setConstraints(backToMenuButton, 0, 10);
        centerObject(backToMenuButton);
        scorePageContainer.getChildren().add(backToMenuButton);
        
        backToMenuButton.setOnAction(e -> switchPanes(windowLayout, scorePageContainer, mainMenuContainer));

        return scorePageContainer;
    }

    private void centerObject(Object node) {
        scorePageContainer.setHalignment((Node) node, HPos.CENTER);
    }
}
