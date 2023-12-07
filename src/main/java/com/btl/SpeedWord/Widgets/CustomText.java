package com.btl.SpeedWord.Widgets;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CustomText {
    private static CustomText instance;

    public Text createText(String content, Font font, int x, int y) {
        Text text = new Text(content);
        text.setFont(font);
        text.setTranslateX(x);
        text.setTranslateY(y);
        return text;
    }

    public static CustomText getInstance() {
        if (instance == null) {
            instance = new CustomText();
        }
        return instance;
    }
}
