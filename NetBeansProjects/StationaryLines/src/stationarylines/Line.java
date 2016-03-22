package stationarylines;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author PerDaniel
 */
public class Line {

    private final int length;
    GraphicsContext gc;
    StationaryLines sl;
    private int heightPlacement;
    private int widthPlacement;
    private int duration;

    public Line(int length, GraphicsContext gc, StationaryLines sl) {
        this.length = length;
        heightPlacement = randInt(200, 880);
        widthPlacement = randInt(500, 1420);
        this.gc = gc;
        this.sl = sl;

        //If the widthPlacement has a value that causes the line to be outside our designated area, we change it to fit exactly
        if (widthPlacement > 1420 - length) {
            widthPlacement = 1420 - length;
        }
    }

    public int getHeightPlacement() {
        return heightPlacement;
    }

    public int getWidthPlacement() {
        return widthPlacement;
    }

    public int getLength() {
        return length;
    }
    
    public int getDuration(){
        return duration;
    }

    public void drawLineWithDuration(final GraphicsContext gc, long durationSeconds) {
        duration=(int) durationSeconds;
        sl.clearCanvas(gc);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(3);
        gc.strokeLine(widthPlacement, heightPlacement, widthPlacement + length, heightPlacement);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(durationSeconds), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                sl.clearCanvasEnableStopwatch(gc);
            }
        }));
        timeline.play();
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
