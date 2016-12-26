package UI;

import static UI.global.gamePlayContainer;
import static UI.global.screenContainer;
import static UI.global.switchPanes;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

public class shortcutsHelp {

    private GridPane shortcutHelpContainer;

    public GridPane initialize() {
        shortcutHelpContainer = new GridPane();
        shortcutHelpContainer.getStyleClass().add("shortcuts-overlay");
        shortcutHelpContainer.setPadding(new Insets(50, 75, 50, 75));
        shortcutHelpContainer.setVgap(20);

        ColumnConstraints firstColumn = new ColumnConstraints();
        firstColumn.setPercentWidth(50);

        ColumnConstraints secondColumn = new ColumnConstraints();
        secondColumn.setPercentWidth(50);

        shortcutHelpContainer.getColumnConstraints().addAll(firstColumn, secondColumn);

        Button closeButton = new Button("");
        closeButton.getStyleClass().add("button-icon");
        closeButton.getStyleClass().add("close-icon-dark");
        closeButton.getStyleClass().add("button-icon--dark");
        closeButton.setTranslateX(100);
        closeButton.setOnAction(e -> switchPanes(screenContainer, shortcutHelpContainer, gamePlayContainer));
        shortcutHelpContainer.setConstraints(closeButton, 1, 0);

        Label shortcutsHeadline = new Label("Keyboard Shortcuts");
        shortcutsHeadline.getStyleClass().add("shortcuts-headline");
        shortcutHelpContainer.setConstraints(shortcutsHeadline, 0, 0);
        shortcutHelpContainer.setValignment(shortcutsHeadline, VPos.CENTER);

        shortcutHelpContainer.getChildren().addAll(shortcutsHeadline, closeButton);

        GridPane shortcutsLine[] = new GridPane[9];

        for (int counter = 0; counter < 9; counter++) {
            ColumnConstraints firstKeyColumn = new ColumnConstraints();
            firstKeyColumn.setPercentWidth(18);
            
            ColumnConstraints plusColumn = new ColumnConstraints();
            plusColumn.setPercentWidth(5);
            
            ColumnConstraints secondKeyColumn = new ColumnConstraints();
            secondKeyColumn.setPercentWidth(25);

            shortcutsLine[counter] = new GridPane();
            shortcutsLine[counter].getColumnConstraints().addAll(firstKeyColumn, plusColumn, secondKeyColumn);
        }

        createShortcutHelp(shortcutsLine[0], 1, "Ctr", "L", "Highlight similar cells (must be used inside a cell)");
        createShortcutHelp(shortcutsLine[1], 2, "Ctr", "H", "Hint, use it when a cell is focused it will hint this cell");
        createShortcutHelp(shortcutsLine[2], 3, "Ctr", "S", "Save the current game");
        createShortcutHelp(shortcutsLine[3], 4, "Ctr", "U", "Undo");
        createShortcutHelp(shortcutsLine[4], 5, "Ctr", "R", "Redo");
        createShortcutHelp(shortcutsLine[5], 6, "Ctr", "Space", "Pause/Resume");
        createShortcutHelp(shortcutsLine[6], 7, "Ctr", "F", "Solve the sudoku");
        createShortcutHelp(shortcutsLine[7], 8, "Ctr", "Backspace", "Back to main menu");
        createShortcutHelp(shortcutsLine[8], 9, "Enter", null, "Submit your answer");

        return shortcutHelpContainer;
    }

    private void createShortcutHelp(GridPane line, int lineNumber, String firstKey, String secondKey, String helpStatement) {
        Label firstKeyLabel = new Label(firstKey);
        firstKeyLabel.getStyleClass().add("keyboard-key");
        line.setConstraints(firstKeyLabel, 0, 0);
        line.setHalignment(firstKeyLabel, HPos.CENTER);

        Label plusLabel = new Label("+");
        plusLabel.getStyleClass().add("help-text");
        line.setConstraints(plusLabel, 1, 0);
        line.setMargin(plusLabel, new Insets(0, 5, 0, 5));
        line.setHalignment(plusLabel, HPos.CENTER);

        Label secondKeyLabel = new Label(secondKey);
        secondKeyLabel.getStyleClass().add("keyboard-key");
        line.setConstraints(secondKeyLabel, 2, 0);
        line.setMargin(secondKeyLabel, new Insets(0, 0, 0, 8));

        Label helpLineLabel = new Label(helpStatement);
        helpLineLabel.getStyleClass().add("help-text");
        line.setConstraints(helpLineLabel, 3, 0);
        helpLineLabel.setMinWidth(400);
        helpLineLabel.setTextAlignment(TextAlignment.LEFT);
        line.setMargin(helpLineLabel, new Insets(0, 0, 0, -25));
        line.setHalignment(helpLineLabel, HPos.LEFT);
        
        if (secondKey == null) {
            plusLabel.setVisible(false);
            secondKeyLabel.setVisible(false);
        }

        line.getChildren().addAll(firstKeyLabel, plusLabel, secondKeyLabel, helpLineLabel);

        shortcutHelpContainer.setConstraints(line, 0, lineNumber);
        shortcutHelpContainer.getChildren().add(line);
    }
}
