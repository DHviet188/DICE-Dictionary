/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord.Widgets;

import com.btl.SpeedWord.Graphics.TextureManager;
import com.btl.SpeedWord.Logic.TurnPlay;
import com.btl.SpeedWord.Scenes.*;
import com.btl.SpeedWord.Sound.SoundManager;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;

import java.util.HashMap;
import java.util.Map;

public class ButtonManager {
    private static ButtonManager s_Instance;
    private Map<String, Button> buttonMap;
    private Map<String, Boolean> isPressButton;

    public ButtonManager() {
        buttonMap = new HashMap<>();
        isPressButton = new HashMap<>();
        buttonInit();
    }

    public void buttonInit() {
        createTextureButton("play", new String[]{"play_n", "play_h", "play_p"}, () -> {
            SceneManager.getSceneManager().ChangeScene(PlayScene.getInstance());
            SceneManager.getSceneManager().Load();
        } );
        createTextureButton("score", new String[]{"score_n", "score_h", "score_p"}, () -> {
            MenuScene.getInstance().OpenScore();
        });
        createTextureButton("option", new String[]{"option_n", "option_h", "option_p"}, () -> {
            MenuScene.getInstance().OpenOption();
        });
        createTextureButton("exit", new String[]{"exit_n", "exit_h", "exit_p"}, () -> {
            MenuScene.getInstance().ExitGame();
        });
        createTextureButton("home", new String[]{"home_n", "home_h", "home_p"}, () -> {
            SceneManager.getSceneManager().ChangeScene(MenuScene.getInstance());
            SceneManager.getSceneManager().Load();
        });
        createTextureButton("restart", new String[]{"restart_n", "restart_h", "restart_p"}, () -> {
            SceneManager.getSceneManager().ChangeScene(PlayScene.getInstance());
            SceneManager.getSceneManager().Load();
        });
        createTextureButton("exitOption", new String[]{"x_n", "x_h", "x_p"}, () -> {
            MenuScene.getInstance().ExitOption();
        });
        createTextureButton("exitScore", new String[]{"x_n", "x_h", "x_p"}, () -> {
            MenuScene.getInstance().ExitScore();
        });

        for (int i = 1; i <= 10; i++) {
            Integer id = i;
            isPressButton.put(id.toString(), false);
            Button tmp = new Button();
            tmp.getStyleClass().add("button");
            tmp.setOnMouseClicked(event -> {
                tmp.getStyleClass().add("button-click");
                SoundManager.getInstance().playSound("click");
                TurnPlay.getTurnPlay().buttonPressed(tmp);
            });
            buttonMap.put(id.toString(), tmp);
        }
    }

    public void createTextureButton(String name, String[] buttonTexture, Runnable action) {
        Button button = new Button();
        button.setGraphic(TextureManager.getTextureManager().GetImageView(buttonTexture[0]));
        button.getStyleClass().add("transparent-button");
        button.setOnMouseEntered(event -> button.setGraphic(TextureManager.getTextureManager().GetImageView(buttonTexture[1])));
        button.setOnMouseExited(event -> button.setGraphic(TextureManager.getTextureManager().GetImageView(buttonTexture[0])));
        button.setOnMouseClicked(event -> {
            button.setGraphic(TextureManager.getTextureManager().GetImageView(buttonTexture[2]));
            SoundManager.getInstance().playSound("click");
            action.run();
        });
        buttonMap.put(name, button);
    }

    public Button getButton(String id) {
        return buttonMap.get(id);
    }
    public Button getButton(String id, int x, int y) {
        Button button = buttonMap.get(id);
        button.setTranslateX(x);
        button.setTranslateY(y);
        return button;
    }

    public Button getButton(String id, int x, int y, int width, int height) {
        Button button = buttonMap.get(id);
        button.setTranslateX(x);
        button.setTranslateY(y);
        button.setPrefSize(width, height);
        return button;
    }

    public void setButtonText(String id, String text) {
        buttonMap.get(id).setText(text);
    }

    public static ButtonManager getInstance() {
        if (s_Instance == null) {
            s_Instance = new ButtonManager();
        }
        return s_Instance;
    }
}