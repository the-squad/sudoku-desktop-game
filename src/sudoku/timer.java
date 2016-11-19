package sudoku;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 *
 * @author Mohamed
 */
public class timer {

    Label timerLabel;
    static Timer timer;
    int time;
    Date gameTime;

    public void setTimer(Label timerLabel, int seconds) {
        this.timerLabel = timerLabel;
        this.time = seconds;
        gameTime = new Date();
    }

    public void start() {
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    time++;
                    gameTime.setTime(time * 1000);
                    timerLabel.setText(gameTime.getMinutes() + ":" + gameTime.getSeconds());
                });
            }
        };
        timer = new java.util.Timer(true);
        timer.schedule(t, 0, 1000);
    }

    public void pause() {
        timer.cancel();
    }
    
    public int getTime() {
        return time;
    }
}
