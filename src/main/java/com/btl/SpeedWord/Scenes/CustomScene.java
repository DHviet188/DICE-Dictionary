package com.btl.SpeedWord.Scenes;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public abstract class CustomScene {
    public abstract Scene getScene();

    public abstract void Init();

    public abstract void Exit();

    public abstract void Pause();

    public abstract void Resume();

    // Hàm để vô hiệu hóa hoặc bật lại tất cả các UI objects
    protected void toggleAllUIObjects(Node node, boolean disable) {
        if (node instanceof Button) {
            ((Button) node).setDisable(disable); // Nếu là Button, vô hiệu hóa hoặc bật lại
        }
        if (node instanceof StackPane) {
            // Nếu là một layout, duyệt qua tất cả các child nodes và thực hiện hành động tương ứng
            ((StackPane) node).getChildren().forEach(child -> toggleAllUIObjects(child, disable));
        }
        // Duyệt qua tất cả các node con của node hiện tại và thực hiện hành động tương ứng
        if (node instanceof javafx.scene.Parent) {
            for (Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                toggleAllUIObjects(child, disable);
            }
        }
    }
}
