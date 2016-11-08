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

    GridPane mainMenuLayout;
    BorderPane rightPartLayout;
    GridPane gameModes;
    GridPane levelsLayout;

    /**
     * Initialize main menu elements
     *
     * @return mainMenuScene
     */
    public GridPane initialize() {
        //Main menu layout
        mainMenuLayout = new GridPane();

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
        rightPartLayout = new BorderPane();
        rightPartLayout.setPadding(new Insets(25));
        mainMenuLayout.setConstraints(rightPartLayout, 1, 0);
        mainMenuLayout.getChildren().add(rightPartLayout);

        //Initalize modes
        rightPartLayout.setCenter(initializeGameModes());
        initializeLevelsMenu();

        return mainMenuLayout;
    }

    private GridPane initializeGameModes() {
        gameModes = new GridPane();
        gameModes.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[6];
        for (int counter = 0; counter < 6; counter++) {
            rowNo[counter] = new RowConstraints();

            if (counter == 0) {
                rowNo[counter].setPercentHeight(20);
            }

            rowNo[counter].setPercentHeight(13);
            gameModes.getRowConstraints().add(rowNo[counter]);
        }

        //Creating right part elemens
        Label welcomeText = new Label("Welcome, Muhammad Tarek");
        welcomeText.getStyleClass().add("welcome-text");
        gameModes.setConstraints(welcomeText, 0, 0);
        gameModes.getChildren().add(welcomeText);
        gameModes.setHalignment(welcomeText, HPos.CENTER);

        Image newGameIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameIconView = new ImageView(newGameIcon);
        Button newGame = new Button("       New Game", newGameIconView);
        initButtonStyle(newGame, gameModes, 1, newGameIconView);

        newGame.setOnAction(e -> {
            animation.switchPanes(rightPartLayout, gameModes, levelsLayout);
            main.playingMode = 1;
            //gamePlay.printSudoku();
        });

        Image loadGameIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameIcon);
        Button loadGame = new Button("       Load last game", laodGameIconView);
        initButtonStyle(loadGame, gameModes, 2, laodGameIconView);

        loadGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 2;
        });

        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        Button checkSudokuGame = new Button("       Check your Sudoku", checkSudokuIconView);
        initButtonStyle(checkSudokuGame, gameModes, 3, checkSudokuIconView);

        checkSudokuGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 3;
        });

        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        Button challengeComputerGame = new Button("       Challenge computer", challengeComputerIconView);
        initButtonStyle(challengeComputerGame, gameModes, 4, challengeComputerIconView);

        challengeComputerGame.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 4;
        });

        Image exitIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitIconView = new ImageView(exitIcon);
        Button exit = new Button("       Exit", exitIconView);
        initButtonStyle(exit, gameModes, 5, exitIconView);

        return gameModes;
    }

    private GridPane initializeLevelsMenu() {
        levelsLayout = new GridPane();
        levelsLayout.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[6];
        for (int counter = 0; counter < 6; counter++) {
            rowNo[counter] = new RowConstraints();

            if (counter == 0) {
                rowNo[counter].setPercentHeight(20);
            }

            rowNo[counter].setPercentHeight(13);
            levelsLayout.getRowConstraints().add(rowNo[counter]);
        }

        //BorderPane to hold text and back arrow
        BorderPane backArrowAndText = new BorderPane();
        levelsLayout.setConstraints(backArrowAndText, 0, 0);
        levelsLayout.getChildren().add(backArrowAndText);
        levelsLayout.setHalignment(backArrowAndText, HPos.CENTER);

        //Creating elemens
        Button back = new Button("");
        back.getStyleClass().add("iconButton");
        back.setId("backButtonDark");
        backArrowAndText.setLeft(back);
        backArrowAndText.setAlignment(back, Pos.CENTER);
        
        back.setOnAction(e -> animation.switchPanes(rightPartLayout, levelsLayout, gameModes));

        Label welcomeText = new Label("Choose game level");
        welcomeText.getStyleClass().add("welcome-text");
        backArrowAndText.setCenter(welcomeText);
  

        Button easy = new Button("Easy");
        initButtonStyle(easy, levelsLayout, 1, null);

        easy.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 1;
            //gamePlay.printSudoku();
        });

        Button medium = new Button("Medium");
        initButtonStyle(medium, levelsLayout, 2, null);

        easy.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 1;
            //gamePlay.printSudoku();
        });

        Button hard = new Button("Hard");
        initButtonStyle(hard, levelsLayout, 3, null);

        easy.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 1;
            //gamePlay.printSudoku();
        });

        return null;
    }

    private GridPane initializeSavedGames() {
        return null;
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
        animation.fade(button, 300, (position - 1) * 200, animation.FADE_IN);

        button.getStyleClass().add("icon-text-button");

        if (icon != null) {
            icon.setFitHeight(24);
            icon.setFitWidth(24);
        }

        layout.setConstraints(button, 0, position);
        layout.setHalignment(button, HPos.CENTER);
        layout.getChildren().add(button);
    }
}
