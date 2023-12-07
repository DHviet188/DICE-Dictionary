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
import com.btl.SpeedWord.Widgets.CustomText;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Stack;

public class MenuScene extends CustomScene {
    private static MenuScene s_Instance;
    private Scene scene;
    private boolean pause;
    private Slider musicSlider;
    private Slider SFXSlider;

    private MenuScene() {
        musicSlider = new Slider();
        musicSlider.setMin(0);
        musicSlider.setMax(1);
        musicSlider.setValue(0.5);

        SFXSlider = new Slider();
        SFXSlider.setMin(0);
        SFXSlider.setMax(1);
        SFXSlider.setValue(0.5);
    }

    public void Init() {
        pause = false;
        StackPane root = new StackPane();
        root.getChildren().add(TextureManager.getTextureManager().GetImageView("menu", 0, 0));
        root.getChildren().add(ButtonManager.getInstance().getButton("play", 0, 0));
        root.getChildren().add(ButtonManager.getInstance().getButton("score", 0, 80));
        root.getChildren().add(ButtonManager.getInstance().getButton("option", 0, 160));
        root.getChildren().add(ButtonManager.getInstance().getButton("exit", 0, 240));

        scene = new Scene(root, Engine.SCREEN_WIDTH, Engine.SCREEN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("transparentButton.css").toExternalForm());
        Option();

        SoundManager.getInstance().playSound("menu");
    }

    @Override
    public void Exit() {
        SoundManager.getInstance().stopSound("menu");
    }

    @Override
    public void Pause() {
        toggleAllUIObjects(scene.getRoot(), true);
    }

    @Override
    public void Resume() {
        toggleAllUIObjects(scene.getRoot(), false);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    public static MenuScene getInstance() {
        if (s_Instance == null) {
            s_Instance = new MenuScene();
        }
        return s_Instance;
    }

    public void Option() {
        SoundManager.getInstance().getSound("menu").volumeProperty().bind(musicSlider.valueProperty());
        SoundManager.getInstance().getSound("play").volumeProperty().bind(musicSlider.valueProperty());
        musicSlider.setTranslateX(20);
        musicSlider.setTranslateY(-15);
        musicSlider.setMaxSize(300, 50);

        SoundManager.getInstance().getSound("click").volumeProperty().bind(SFXSlider.valueProperty());
        SFXSlider.setTranslateX(20);
        SFXSlider.setTranslateY(90);
        SFXSlider.setPrefWidth(300);
        SFXSlider.setMaxSize(300, 50);

        StackPane stackPane = new StackPane(TextureManager.getTextureManager().GetImageView("option"), ButtonManager.getInstance().getButton("exitOption", 230, -140), musicSlider, SFXSlider);
        stackPane.setVisible(false);
        stackPane.setId("option");
        ((StackPane) scene.getRoot()).getChildren().add(stackPane);
    }

    public void Score() {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(TextureManager.getTextureManager().GetImageView("score"));
        Task<Boolean> task = new Task<>() {
            protected Boolean call() throws Exception {
                Point.getInstance().getAllData();
                ArrayList<String> username = Point.getInstance().getUsername();
                ArrayList<String> highscore = Point.getInstance().getHighscore();
                ArrayList<String> timeList = Point.getInstance().getTimeList();
                Integer order = 1;
                Font font = new Font("Arial", 20);
                Platform.runLater(() -> {
                    int posY = -100;
                    for (int i = 0; i < username.size(); i++) {
                        Text name = CustomText.getInstance().createText(username.get(i), font, -200, posY);
                        name.setFill(Color.WHITE);
                        Text score = CustomText.getInstance().createText(highscore.get(i), font, -100, posY);
                        score.setFill(Color.WHITE);
                        Text time = CustomText.getInstance().createText(timeList.get(i), font, 100, posY);
                        time.setFill(Color.WHITE);
                        posY += 50;

                        stackPane.getChildren().add(name);
                        stackPane.getChildren().add(score);
                        stackPane.getChildren().add(time);
                    }
                    stackPane.getChildren().add(ButtonManager.getInstance().getButton("exitScore", 250, -150));
                });
                return true;
            }
        };
        new Thread(task).start();

        stackPane.setVisible(false);
        stackPane.setId("score");
        ((StackPane) scene.getRoot()).getChildren().add(stackPane);
    }

    public void OpenScore() {
        Score();
        scene.lookup("#score").setVisible(true);
    }

    public void ExitScore() {
        scene.lookup("#score").setVisible(false);
    }

    public void OpenOption() {
        scene.lookup("#option").setVisible(true);
    }

    public void ExitOption() {
        scene.lookup("#option").setVisible(false);
    }

    public void ExitGame() {
        Engine.getStage().close();
    }
}