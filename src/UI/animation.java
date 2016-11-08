package UI;

import java.util.Timer;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class animation {

    public static final int fadeIn = 1;
    public static final int fadeOut = 2;

    public static final int toGamePlay = 1;
    public static final int toMenu = 2;

    /**
     *
     * @param parent
     * @param borderChild
     * @param gridChild
     * @param toScreen
     */
    public static void switchPanes(BorderPane parent, BorderPane borderChild, GridPane gridChild, int toScreen) {
        fade((toScreen == 1 ? gridChild : borderChild), 350, 0, fadeOut);
        parent.setCenter(null);
        parent.setCenter((toScreen == 1 ? borderChild : gridChild));
        fade((toScreen == 1 ? borderChild : gridChild), 350, 0, fadeIn);

    }

    public static void fade(Object node, int duration, int delay, int fadeType) {
        FadeTransition fadeAnimation = new FadeTransition(Duration.millis(1000), (Node) node);

        fadeAnimation.setDelay(Duration.millis(delay));
        fadeAnimation.setFromValue(fadeType == 1 ? 0 : 1);
        fadeAnimation.setToValue(fadeType == 1 ? 1 : 0);
     
        fadeAnimation.play();
    }
}
