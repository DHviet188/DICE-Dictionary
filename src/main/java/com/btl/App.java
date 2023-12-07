package com.btl;

import com.btl.SpeedWord.Core.Engine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class App extends Application {
    private double x = 0;
    private double y = 0;

    /**
     * start function.
     * @param stage stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        Parent root = loader.load();
        loginController controller = loader.getController();
                
        Scene scene = new Scene(root);
        
        root.setOnMousePressed((MouseEvent event) ->{
           x = event.getSceneX();
           y = event.getSceneY();
        });
        
        root.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
            
            stage.setOpacity(.8);
        });
        
        root.setOnMouseReleased((MouseEvent event) ->{
            stage.setOpacity(1);
        });
        
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        controller.getWelcomeStage().setOnHidden(e -> stage.show());
    }

    /**
     * main function.
     * @param args array 
     */
    public static void main(String[] args) {
        launch();
    }

}