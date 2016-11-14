package UI;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Class to initialize all global variables and methods
 * @author Muhammad
 */
public class global {
    static BorderPane windowLayout;
    static BorderPane gamePlayContainer;
    static GridPane mainMenuContainer;
    
    //<editor-fold defaultstate="collapsed" desc="Sudoku Arrays">
    static Integer[][] userSudoku = new Integer[9][9]; //Reads the Sudoku from the user
    static Integer[][] computerSolution = new Integer[9][9]; //Where computer returns the wrong cells
    static Boolean[][] markSolution = new Boolean[9][9]; //Where computer returns the wrong cells
//</editor-fold>
    
    public static final int READ_SUDOKU = 1;
    public static final int PRINT_SUDOKU = 2;
    public static final int CLEAR_SUDOKU = 3;
    
    public static final int FADE_IN = 1;
    public static final int FADE_OUT = 2;
    
    static int playingMode;
    
    /**
     *
     * @param parent
     * @param fromChild
     * @param toChild
     */
    public static void switchPanes(BorderPane parent, Object fromChild, Object toChild) {
        fade(fromChild, 350, 0, FADE_OUT);
        parent.setCenter(null);
        parent.setCenter((Node)toChild);
        fade(toChild, 350, 0, FADE_IN);

    }

    public static void fade(Object node, int duration, int delay, int fadeType) {
        FadeTransition fadeAnimation = new FadeTransition(Duration.millis(1000), (Node) node);

        if (delay != 0)
            fadeAnimation.setDelay(Duration.millis(delay));
        
        fadeAnimation.setFromValue(fadeType == 1 ? 0 : 1);
        fadeAnimation.setToValue(fadeType == 1 ? 1 : 0);
     
        fadeAnimation.play();
    }
}
