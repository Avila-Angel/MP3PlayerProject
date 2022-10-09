package com.example.mp3playerproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Button playButton, pauseButton, resetButton, nextButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;

    private Media media;
    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;

    private ArrayList<File> songs; // will be our playlist containing our songs

    private int songNumber; // will keep track of what song number we're on
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200}; // song speeds

    private Timer timer; // timer to keep track of our progressbar and update it
    private TimerTask task;
    private boolean running; // set this to true or false determining whether our player is playing or not

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        songs = new ArrayList<File>();

        directory = new File("Music");

        files = directory.listFiles(); // gets all files within Music directory and stores them

        if (files != null) {
            // System.out.println(file);
            for(File file : files) {
                songs.add(file);
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName()); // will change song label to current playing song name

        for (int speed : speeds) {
            speedBox.getItems().add(speed + "%"); // adds speeds to speedBox
        }

        speedBox.setOnAction(this::changeSpeed); // reference to changeSpeed method

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // when you adjust knob on changeListener, this method will be called
            mediaPlayer.setVolume(volumeSlider.getValue() * 0.01); // allows us changes volume
        });
        //songProgressBar.setStyle("-fx-accent: #00FF00");
    }

    public void playMedia() {
        beginTimer(); // starts timer when play button is pressed, which starts progressBar
        changeSpeed(null); // this will retain speed selected when changing songs

        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01); // adjust volume slider when changing songs
        mediaPlayer.play(); // play song
    }

    public void pauseMedia() {
        cancelTimer(); // stops time when pause button is pressed, which stops progressBar
        mediaPlayer.pause(); // pauses song
    }

    public void resetMedia() {
        songProgressBar.setProgress(0); // resets progress bar when song is reset
        mediaPlayer.seek(Duration.seconds(0)); // resets song to start at 0
    }

    public void previousMedia() {
        songProgressBar.setProgress(0);
        cancelTimer();
        beginTimer();
        // automatically plays song after pressing next
        if (songNumber > 0) {
            songNumber--;

            mediaPlayer.stop();

            if (running) { // resets timer/progressBar when previous button is pressed
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());
            playMedia();

        }
        else {
            // will go back to first song at playlist after reaching the end of your playlist
            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if (running) { // resets timer/progressBar when previous button is pressed
                cancelTimer();
            }


            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
            playMedia();
        }
    }

    public void nextMedia() {
        // automatically plays song after pressing next
        if(songNumber < songs.size() - 1) {

            songNumber++;

            mediaPlayer.stop();

            if(running) {
                cancelTimer();
            }
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            playMedia();

        }
        else {
            // will go back to first song at playlist after reaching the end of your playlist
            songNumber = 0;

            mediaPlayer.stop();

            if(running) {
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }
    public void changeSpeed(ActionEvent event) {
        if(speedBox.getValue() == null) { // sets initial speedValue to 100%
            mediaPlayer.setRate(1);
        } else {
            // mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()) * 0.01); doesn't allow us to change with the % next to the values
           mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01); // allows us to change speed with %
        }
    }
    public void beginTimer() {
        timer = new Timer();

        task = new TimerTask() {

            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                System.out.println(current/end * 100);
                songProgressBar.setProgress(current/end); // associates the duration of the song to progressBar

                if(current/end == 1) { // cancels timer after song is finished
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }
}