package UI;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class mainMenu {

    /**
     * Initialize main menu elements
     *
     * @return mainMenuScene
     */
    public GridPane initialize() {
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
        leftPartLayout.setAlignment(logo, Pos.BOTTOM_CENTER);
        leftPartLayout.setMargin(logo, new Insets(175, 0, 30, 0));

        Label logoText = new Label("Sudodu Game");
        logoText.getStyleClass().add("logo-text");
        leftPartLayout.setCenter(logoText);
        leftPartLayout.setAlignment(logoText, Pos.TOP_CENTER);

        Label version = new Label("Version 0.0.1");
        version.getStyleClass().add("version");
        leftPartLayout.setBottom(version);
        leftPartLayout.setAlignment(version, Pos.TOP_CENTER);
        leftPartLayout.setMargin(version, new Insets(0, 0, 30, 0));

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

            if (counter == 0) {
                rowNo[counter].setPercentHeight(20);
            }

            rowNo[counter].setPercentHeight(13);
            rightPartLayout.getRowConstraints().add(rowNo[counter]);
        }

        //Creating right part elemens
        Label welcomeText = new Label("Welcome, Muhammad Tarek");
        welcomeText.getStyleClass().add("welcome-text");
        rightPartLayout.setConstraints(welcomeText, 0, 0);
        rightPartLayout.getChildren().add(welcomeText);
        rightPartLayout.setHalignment(welcomeText, HPos.CENTER);

        Image newGameIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameIconView = new ImageView(newGameIcon);
        Button newGame = new Button("       New Game", newGameIconView);
        initButtonStyle(newGame, rightPartLayout, 1, newGameIconView);

        newGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.gamePlay, mainMenuLayout, animation.toGamePlay);
            main.playingMode = 1;
            //gamePlay.printSudoku();
        });

        Image loadGameIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameIcon);
        Button loadGame = new Button("       Load last game", laodGameIconView);
        initButtonStyle(loadGame, rightPartLayout, 2, laodGameIconView);
        
        loadGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.gamePlay, mainMenuLayout, animation.toGamePlay);
            main.playingMode = 2;
        });

        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        Button checkSudokuGame = new Button("       Check your Sudoku", checkSudokuIconView);
        initButtonStyle(checkSudokuGame, rightPartLayout, 3, checkSudokuIconView);
        
        checkSudokuGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.gamePlay, mainMenuLayout, animation.toGamePlay);
            main.playingMode = 3;
        });

        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        Button challengeComputerGame = new Button("       Challenge computer", challengeComputerIconView);
        initButtonStyle(challengeComputerGame, rightPartLayout, 4, challengeComputerIconView);
        
        challengeComputerGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.gamePlay, mainMenuLayout, animation.toGamePlay);
            main.playingMode = 4;
        });

        Image exitIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitIconView = new ImageView(exitIcon);
        Button exit = new Button("       Exit", exitIconView);
        initButtonStyle(exit, rightPartLayout, 5, exitIconView);

        return mainMenuLayout;
    }

    /**
     * Initialize button styles, icons sizes Muhammad Tarek
     *
     * @since 6, November
     * @param button
     * @param layout
     * @param position
     * @param icon
     */
    private void initButtonStyle(Button button, GridPane layout, int position, ImageView icon) {
        animation.fade(button, 300, (position - 1) * 200, animation.fadeIn);

        button.getStyleClass().add("icon-text-button");
        icon.setFitHeight(24);
        icon.setFitWidth(24);
        
        layout.setConstraints(button, 0, position);
        layout.setHalignment(button, HPos.CENTER);
        layout.getChildren().add(button);
    }
}
