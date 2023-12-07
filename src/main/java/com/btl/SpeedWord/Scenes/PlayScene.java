/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord.Scenes;

import com.btl.SpeedWord.Logic.Point;
import com.btl.SpeedWord.Sound.SoundManager;
import com.btl.SpeedWord.Widgets.ButtonManager;
import com.btl.SpeedWord.Core.Engine;
import com.btl.SpeedWord.Graphics.TextureManager;
import com.btl.SpeedWord.Logic.TurnPlay;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

public class PlayScene extends CustomScene {
    private static PlayScene s_Instance;
    private Scene scene;
    private Timeline timeline;
    private Text scoreText;
    private Integer score;
    private Integer heart;

    public void Init() {
        // Load font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("FontPoint.ttf"), 90);

        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("play"));
        root.getChildren().add(ButtonManager.getInstance().getButton("home", 450, -270));
        root.getChildren().add(ButtonManager.getInstance().getButton("1", -200, -130, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("2", -200, -30, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("3", -200, 70, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("4", -200, 170, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("5", -200, 270, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("6", 200, -130, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("7", 200, -30, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("8", 200, 70, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("9", 200, 170, 250, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("10", 200, 270, 250, 80));

        // Score init
        score = 0;
        scoreText = new Text(score.toString());
        scoreText.setFont(customFont);
        scoreText.setTranslateX(0);
        scoreText.setTranslateY(-250);
        root.getChildren().add(scoreText);

        // Heart init
        heart = 3;
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("heart_3", -330, -260));

        // Timer Bar
        {
            ProgressBar progressBar = new ProgressBar(1); // Giá trị ban đầu là 1.0 (toàn bộ thanh)
            progressBar.getStyleClass().add("progress-bar"); // Thêm lớp CSS vào ProgressBar
            progressBar.setMinWidth(Engine.SCREEN_WIDTH);
            progressBar.setMinHeight(10);
            progressBar.setTranslateX(0);
            progressBar.setTranslateY(-Engine.SCREEN_HEIGHT / 2 + 5);

            timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, e -> {
                        double progress = progressBar.getProgress();
                        if (progress > 0) {
                            progressBar.setProgress(Math.max(0, progress - 0.00016)); // Giảm giá trị progress mỗi 0.01 giây
                        } else {
                            gameOver();
                            timeline.stop();
                        }
                    }),
                    new KeyFrame(Duration.seconds(0.01)) // Cập nhật mỗi 0.01 giây
            );

            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            root.getChildren().add(progressBar);
        }

        // Logic
        TurnPlay.getTurnPlay().start();

        scene = new Scene(root, Engine.SCREEN_WIDTH, Engine.SCREEN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("Button.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("TimerBar.css").toExternalForm());

        SoundManager.getInstance().playSound("play");
    }

    public void gameOver() {
        for (Integer i = 1; i <= 10; i++) {
            ButtonManager.getInstance().getButton(i.toString()).setDisable(true);
        }

        timeline.stop();
        SoundManager.getInstance().stopSound("play");
        SoundManager.getInstance().playSound("over");

        Task<Boolean> task = new Task<>() {
            protected Boolean call() throws Exception {
                Point.getInstance().insertData(score);
                return true;
            }
        };
        new Thread(task).start();

        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("over"));
        ((StackPane) PlayScene.getInstance().getScene().getRoot()).getChildren().add(root);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            // Hành động cần thực hiện sau độ trễ ở đây
            root.getChildren().add(ButtonManager.getInstance().getButton("home", -100, 50));
            root.getChildren().add(ButtonManager.getInstance().getButton("restart", 100, 50));
        }));

        // Bắt đầu Timeline
        timeline.play();
    }

    public Text getScoreText() {
        return scoreText;
    }

    public void setScoreText(Text scoreText) {
        this.scoreText = scoreText;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public void Exit() {
        timeline.stop();
        SoundManager.getInstance().stopSound("play");
        SoundManager.getInstance().stopSound("over");
    }

    @Override
    public void Pause() {

    }

    @Override
    public void Resume() {

    }

    public static PlayScene getInstance() {
        if (s_Instance == null) {
            s_Instance = new PlayScene();
        }
        return s_Instance;
    }
}
