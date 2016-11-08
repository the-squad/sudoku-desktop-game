package UI;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class mainMenu {

    GridPane mainMenuLayout;
    BorderPane rightPartLayout;
    GridPane gameModesLayout;
    GridPane levelsLayout;
    GridPane savedGamesLayout;

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

        Label version = new Label("Version 0.1");
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
        initializeGameModes();
        initializeLevelsMenu();
        initializeSavedGames();
        rightPartLayout.setCenter(gameModesLayout);

        return mainMenuLayout;
    }

    private void initializeGameModes() {
        gameModesLayout = new GridPane();
        gameModesLayout.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints rowNo[] = new RowConstraints[5];
        for (int counter = 0; counter < 5; counter++) {
            rowNo[counter] = new RowConstraints();
            rowNo[counter].setPercentHeight(13);
            gameModesLayout.getRowConstraints().add(rowNo[counter]);
        }

        //New Game Button
        Image newGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/new-game.png"));
        ImageView newGameButtonIconView = new ImageView(newGameButtonIcon);
        Button newGameButton = new Button("       New Game", newGameButtonIconView);
        initButtonStyle(newGameButton, gameModesLayout, 0, newGameButtonIconView);

        newGameButton.setOnAction(e -> {
            animation.switchPanes(rightPartLayout, gameModesLayout, levelsLayout);
            main.playingMode = 1;
        });

        //Load Game Button
        Image loadGameButtonIcon = new Image(getClass().getResourceAsStream("/icons/load-game.png"));
        ImageView laodGameIconView = new ImageView(loadGameButtonIcon);
        Button loadGameButton = new Button("       Load last game", laodGameIconView);
        initButtonStyle(loadGameButton, gameModesLayout, 1, laodGameIconView);

        loadGameButton.setOnAction(e -> {
            animation.switchPanes(rightPartLayout, gameModesLayout, savedGamesLayout);
            main.playingMode = 2;
        });

        //Check Sudoku Button
        Image checkSudokuIcon = new Image(getClass().getResourceAsStream("/icons/check-sudoku.png"));
        ImageView checkSudokuIconView = new ImageView(checkSudokuIcon);
        Button checkSudokuButton = new Button("       Check your Sudoku", checkSudokuIconView);
        initButtonStyle(checkSudokuButton, gameModesLayout, 2, checkSudokuIconView);

        checkSudokuButton.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 3;
        });

        //Challange Computer Button
        Image challengeComputerIcon = new Image(getClass().getResourceAsStream("/icons/challenge-computer.png"));
        ImageView challengeComputerIconView = new ImageView(challengeComputerIcon);
        Button challangeComputerButton = new Button("       Challenge computer", challengeComputerIconView);
        initButtonStyle(challangeComputerButton, gameModesLayout, 3, challengeComputerIconView);

        challangeComputerButton.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
            main.playingMode = 4;
        });

        //Exit Button
        Image exitButtonIcon = new Image(getClass().getResourceAsStream("/icons/exit.png"));
        ImageView exitButtonIconView = new ImageView(exitButtonIcon);
        Button exitButton = new Button("       Exit", exitButtonIconView);
        initButtonStyle(exitButton, gameModesLayout, 4, exitButtonIconView);

        exitButton.setOnAction(e -> {
            System.exit(0);
        });
    }

    private void initializeLevelsMenu() {
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

        //Back Button
        Button back = new Button("");
        back.getStyleClass().add("iconButton");
        back.setId("backButtonDark");
        backArrowAndText.setLeft(back);
        backArrowAndText.setAlignment(back, Pos.CENTER);
        backArrowAndText.setMargin(back, new Insets(0, -80, 0, -65));

        back.setOnAction(e -> animation.switchPanes(rightPartLayout, levelsLayout, gameModesLayout));

        //Headline
        Label headlineText = new Label("Choose game level");
        headlineText.getStyleClass().add("headline-text");
        backArrowAndText.setCenter(headlineText);
        backArrowAndText.setAlignment(headlineText, Pos.CENTER);

        //Level buttons
        Button easyButton = new Button("Easy");
        initButtonStyle(easyButton, levelsLayout, 1, null);

        easyButton.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
        });

        Button mediumButton = new Button("Medium");
        initButtonStyle(mediumButton, levelsLayout, 2, null);

        mediumButton.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
        });

        Button hardButton = new Button("Hard");
        initButtonStyle(hardButton, levelsLayout, 3, null);

        hardButton.setOnAction(e -> {
            animation.switchPanes(main.windowLayout, main.mainMenu, main.gamePlay);
        });
    }

    private void initializeSavedGames() {
        int gamesNumber = 20;

        savedGamesLayout = new GridPane();
        savedGamesLayout.setAlignment(Pos.CENTER);

        //Creating custom rows
        RowConstraints headlineRow = new RowConstraints();
        headlineRow.setPercentHeight(20);
        savedGamesLayout.getRowConstraints().add(headlineRow);

        RowConstraints gamesRow = new RowConstraints();
        gamesRow.setPercentHeight(65);
        savedGamesLayout.getRowConstraints().add(gamesRow);

        //BorderPane to hold text and back arrow
        BorderPane backArrowAndText = new BorderPane();
        savedGamesLayout.setConstraints(backArrowAndText, 0, 0);
        savedGamesLayout.getChildren().add(backArrowAndText);
        savedGamesLayout.setHalignment(backArrowAndText, HPos.CENTER);

        //Creating elemens
        Button back = new Button("");
        back.getStyleClass().add("iconButton");
        back.setId("backButtonDark");
        backArrowAndText.setLeft(back);
        backArrowAndText.setAlignment(back, Pos.CENTER);
        backArrowAndText.setMargin(back, new Insets(0, -170, 0, -10));

        back.setOnAction(e -> animation.switchPanes(rightPartLayout, savedGamesLayout, gameModesLayout));

        //Headline
        Label headlineText = new Label("Choose a game");
        headlineText.getStyleClass().add("headline-text");
        backArrowAndText.setCenter(headlineText);
        backArrowAndText.setAlignment(headlineText, Pos.CENTER);

        //Container
        GridPane gamesContainer = new GridPane();
        gamesContainer.setVgap(20);

        //Scrollbar
        ScrollPane scrollPane = new ScrollPane(gamesContainer);
        scrollPane.getStyleClass().add("scroll-panel");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMinWidth(360);
        savedGamesLayout.setMargin(scrollPane, new Insets(0, 0, 0, 60));

        savedGamesLayout.setConstraints(scrollPane, 0, 1);
        savedGamesLayout.getChildren().add(scrollPane);

        //Game blocks
        GridPane gameBlock[] = new GridPane[gamesNumber];

        for (int counter = 0; counter < gamesNumber; counter++) {
            gameBlock[counter] = new GridPane();
            gameBlock[counter].getStyleClass().add("game-block");
            gameBlock[counter].setId("#" + counter); //Change to game ID

            //Container
            BorderPane gameBlockLayout = new BorderPane();
            gameBlock[counter].setConstraints(gameBlockLayout, 0, 0);
            gameBlock[counter].getChildren().add(gameBlockLayout);

            //Details container
            GridPane detailsLayout = new GridPane();
            gameBlockLayout.setLeft(detailsLayout);
            gameBlockLayout.setMargin(detailsLayout, new Insets(0, 130, 0, 0));

            //Custom row constraints
            RowConstraints firstRow = new RowConstraints();
            firstRow.setPercentHeight(50);
            detailsLayout.getRowConstraints().add(firstRow);

            RowConstraints secondRow = new RowConstraints();
            secondRow.setPercentHeight(50);
            detailsLayout.getRowConstraints().add(secondRow);

            //Game title
            Label gameTitle = new Label("25-5-2016"); //Change to game titles
            gameTitle.getStyleClass().add("game-title");
            detailsLayout.setConstraints(gameTitle, 0, 0);
            detailsLayout.getChildren().add(gameTitle);

            //Game level
            Label gameLevel = new Label("Easy"); //Change to game level
            gameLevel.getStyleClass().add("game-level");
            detailsLayout.setConstraints(gameLevel, 0, 1);
            detailsLayout.getChildren().add(gameLevel);
            detailsLayout.setValignment(gameLevel, VPos.BOTTOM);

            //Game timer
            Label gameTimer = new Label("3:25"); //Change to game time
            gameTimer.getStyleClass().add("game-time");
            detailsLayout.setConstraints(gameTimer, 1, 1);
            detailsLayout.getChildren().add(gameTimer);
            detailsLayout.setValignment(gameTimer, VPos.BOTTOM);

            //Delete game button
            Button deleteGame = new Button();
            deleteGame.getStyleClass().add("delete-button");
            gameBlockLayout.setRight(deleteGame);
            gameBlockLayout.setMargin(deleteGame, new Insets(3, 0, 0, 0));

            //Adding blocks to the container
            gamesContainer.setConstraints(gameBlock[counter], 0, counter);
            gamesContainer.getChildren().add(gameBlock[counter]);
        }
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
        animation.fade(button, 300, position * 200, animation.FADE_IN);

        button.getStyleClass().add("icon-text-button");

        if (icon != null) {
            icon.setFitHeight(24);
            icon.setFitWidth(24);
        }

        layout.setConstraints(button, 0, position);
        layout.setHalignment(button, HPos.CENTER);
        layout.setValignment(button, VPos.CENTER);
        layout.getChildren().add(button);
    }
}
