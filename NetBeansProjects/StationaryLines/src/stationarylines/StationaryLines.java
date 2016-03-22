/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stationarylines;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author PerDaniel
 */
public class StationaryLines extends Application {

    boolean running = false;
    boolean stopwatchEnabled = false;
    boolean programStarted = false;
    boolean allowToRun = false;
    boolean isHalfway = true;
    
    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Long> times = new ArrayList<>();
    StopwatchThread sw = new StopwatchThread();
    int countInstructions = 1;
    StationaryLines sl = this;
    private int indexOfLines = 0;
    StackPane root = new StackPane();
    Text textAfterLine = new Text("Please press 's' to start the stopwatch, then press 's' again to stop it");
    Results results = new Results();
    Line currentLine;
    String userID;
    boolean taskDoneBoolean = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Stuff for the scene and canvas
        primaryStage.setTitle("Stationary lines");
        Scene scene;
        scene = new Scene(root, 1920, 1080);
        Canvas canvas = new Canvas(1920, 1080);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
	
        //Textfield to enter the ID of participants, this textfield will be shown every time you start the program
        final TextField enterID = new TextField();
        enterID.setPromptText("Enter userID");
        
        //Instructions
        final Text introduction = new Text("This is the instructions ... Press 'n' for next part");
        final Text instructions = new Text("This is the next part. (Yo, press 'n' again, mate)");
        final Text start = new Text("This is the startscreen. Please press 'j' to start the test");
        final Text halfwayPoint = new Text("You are now halfway through the tasks. Please take a moment to breathe, and press 'j' when you are ready to start again");
        final Text taskDone = new Text("The program is now done! Thank you for participating :) Please press SPACE to close this application.");

        //Stuff for stopwatch
        final Text started = new Text("The stopwatch has been started, press 's' to stop");
        final Text stopped = new Text("The stopwatch has been stopped");

        fillLineArrayList(lines, gc);

        enterID.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    userID = enterID.getText();
                    root.getChildren().add(introduction);
                    root.getChildren().remove(enterID);
                    increaseCountInstructions();
                }
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent ke) {
                if (ke.getText().toLowerCase().equals("s")) {   //either starts or stops the stopwatch, depending on wether it's running or not
                    if (stopwatchEnabled) {
                        if (!running) {
                            running = true;
                            root.getChildren().remove(textAfterLine);
                            sw.startStopwatchThread();
                            root.getChildren().add(started);
                        } else {
                            running = false;
                            sw.stopStopwatchThread();
                            results.addTimeMillis(indexOfLines, (int) sw.getTime());
                            results.addLengthOfLine(indexOfLines, currentLine.getLength());
                            results.addActualTimeMillis(indexOfLines, currentLine.getDuration());
                            results.addHeightPlacement(indexOfLines, currentLine.getHeightPlacement());
                            results.addWidthPlacement(indexOfLines, currentLine.getWidthPlacement());
                            root.getChildren().remove(started);
                            sw = new StopwatchThread();
                            indexOfLines++;
                            if (indexOfLines == (lines.size() / 2)) {
                                disableStopwatch();
                                root.getChildren().add(halfwayPoint);
                            } else if (indexOfLines < lines.size()) {
                                startMainTask(gc);
                            } else {
                                disableStopwatch();
                                root.getChildren().add(taskDone);
                                taskDoneBoolean = true;
                                try {
                                    writeResultsToFile();
                                } catch (FileNotFoundException ex) {
                                    Logger.getLogger(StationaryLines.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                } else if (ke.getText().toLowerCase().equals("p")) { //prints all the times recorded with the stopwatch
                    for (Long s : times) {
                        System.out.println(s);
                    }
                } else if (ke.getText().toLowerCase().equals("n")) {
                    if (countInstructions == 2) {
                        root.getChildren().remove(introduction);
                        root.getChildren().add(instructions);
                        increaseCountInstructions();
                    } else if (countInstructions == 3) {
                        root.getChildren().remove(instructions);
                        root.getChildren().add(start);
                        allowToRun = true;
                        increaseCountInstructions();
                    }
                } else if (ke.getText().toLowerCase().equals("j")) {
                    if (!programStarted && allowToRun) {
                        System.out.println("Hello");
                        programStarted = true;
                        allowToRun = false;
                        root.getChildren().remove(start);
                        startMainTask(gc);
                    } else if (indexOfLines == lines.size() / 2) {
                        if (isHalfway) {
                            isHalfway = false;
                            System.out.println("Hello2");
                            root.getChildren().remove(halfwayPoint);
                            startMainTask(gc);
                        }
                    }
                } else if (ke.getCode() == KeyCode.SPACE) {
                    if (taskDoneBoolean) {
                        Platform.exit();
                    }
                }
            }
        });

        root.getChildren().add(canvas);
        root.getChildren().add(enterID);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void drawFocusingCross(GraphicsContext gc) {
        clearCanvas(gc);
        disableStopwatch();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(945, 540, 975, 540);
        gc.strokeLine(960, 525, 960, 555);
    }

    private synchronized void startMainTask(final GraphicsContext gc) {
        currentLine = lines.get(indexOfLines);
        drawFocusingCross(gc);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
                currentLine.drawLineWithDuration(gc, randInt(1, 10));
            }
        }));
        timeline.play();
    }

    public void clearCanvas(GraphicsContext gc) {
        gc.clearRect(0, 0, 1920, 1080);
    }

    public void clearCanvasEnableStopwatch(GraphicsContext gc) {
        gc.clearRect(0, 0, 1920, 1080);
        enableStopwatch();
        root.getChildren().add(textAfterLine);
    }

    private void fillLineArrayList(ArrayList<Line> lines, GraphicsContext gc) {
        lines.add(new Line(300, gc, this));
        lines.add(new Line(150, gc, this));
        lines.add(new Line(700, gc, this));
        lines.add(new Line(450, gc, this));
        Collections.shuffle(lines);
    }

    private void increaseCountInstructions() {
        countInstructions++;
    }

    private void enableStopwatch() {
        stopwatchEnabled = true;
    }

    void disableStopwatch() {
        stopwatchEnabled = false;
    }

    private void writeResultsToFile() throws FileNotFoundException {
        results.writeResultsToFile(userID);
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive. The
     * difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value. Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
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
