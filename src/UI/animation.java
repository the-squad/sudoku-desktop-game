package UI;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

public class animation {

    public static final int FADE_IN = 1;
    public static final int FADE_OUT = 2;

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
