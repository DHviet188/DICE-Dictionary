/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord.Scenes;

import com.btl.SpeedWord.Core.Engine;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import java.util.Stack;

public class SceneManager {
    private static SceneManager sceneManager;
    private Stack<CustomScene> gameScene = new Stack<>();

    public void PushScene(CustomScene sc) {
        if (!gameScene.empty()) {
            gameScene.peek().Pause();
        }
        gameScene.push(sc);
        gameScene.peek().Init();
    }

    public void PopScene() {
        if (gameScene.empty()) {
            Engine.getStage().close();
        } else {
            gameScene.pop().Exit();
            gameScene.peek().Resume();
        }
    }

    public void ChangeScene(CustomScene sc) {
        if(!gameScene.empty()) {
            gameScene.pop().Exit();
        }
        gameScene.push(sc);
        gameScene.peek().Init();
    }

    public void Load() {
        Engine.getStage().setScene(gameScene.peek().getScene());
    }

    public static SceneManager getSceneManager() {
        if(sceneManager == null) {
            sceneManager = new SceneManager();
        }
        return sceneManager;
    }

    public Stack<CustomScene> getGameScene() {
        return gameScene;
    }

}