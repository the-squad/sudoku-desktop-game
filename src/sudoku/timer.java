package sudoku;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class timer {

    Label timerLabel;
    static Timer timer;
    int time;
    Date gameTime;

    /**
     * Sets timer start point
     * 
     * @param timerLabel
     * @param seconds 
     */
    public void setTimer(Label timerLabel, int seconds) {
        this.timerLabel = timerLabel;
        this.time = seconds;
        gameTime = new Date();
    }

    /**
     * Starts the timer
     */
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

    /**
     * Pauses the timer
     */
    public void pause() {
        timer.cancel();
    }
    
    /**
     * Adds 10 seconds to the timer
     */
    public void addTenSeconds() {
        time += 10;
        timerLabel.setText(gameTime.getMinutes() + ":" + gameTime.getSeconds());
    }
    
    public int getTime() {
        return time;
    }
}
