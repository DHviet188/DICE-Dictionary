package com.btl.SpeedWord.Core;

import com.btl.SpeedWord.Scenes.MenuScene;
import com.btl.SpeedWord.Scenes.SceneManager;
import com.btl.SpeedWord.Graphics.TextureManager;
import com.btl.SpeedWord.Sound.SoundManager;
import javafx.stage.Stage;

public class Engine {
    public static final int SCREEN_WIDTH = 1018;
    public static final int SCREEN_HEIGHT = 688;
    public static final String Title = "SpeedWord";
    private static Stage stage;
    private static Engine engine;

    public void start() {
        stage = new Stage();
        TextureManager.getTextureManager().ParseTexture("Texture.xml");

        SceneManager.getSceneManager().ChangeScene(MenuScene.getInstance());

        stage.setScene(SceneManager.getSceneManager().getGameScene().peek().getScene());
        stage.setTitle(Title);
        stage.setResizable(false);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }
    
    public static Engine getEngine() {
        if(engine == null) {
            engine = new Engine();
        }
        return engine;
    }
}
