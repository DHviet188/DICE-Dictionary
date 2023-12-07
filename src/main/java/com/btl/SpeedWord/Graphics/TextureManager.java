/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.btl.SpeedWord.Graphics;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    private static TextureManager textureManager;
    private Map<String, ImageView> textureMap;

    public TextureManager() {
        textureMap = new HashMap<>();
    }

    public void ParseTexture(String xmlSource) {
        try {
            InputStream xmlFile = getClass().getResourceAsStream(xmlSource);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList textureList = doc.getElementsByTagName("texture");

            for (int temp = 0; temp < textureList.getLength(); temp++) {
                Element texture = (Element) textureList.item(temp);
                String source = texture.getAttribute("source");
                String id = texture.getAttribute("id");

                LoadTexture(source, id);

                /*System.out.println("Texture Source: " + source);
                System.out.println("Texture ID: " + id);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LoadTexture(String src, String id) {
        InputStream inputStream = getClass().getResourceAsStream(src);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        textureMap.put(id, imageView);
    }

    public ImageView GetImageView(String id, int width, int height, int x, int y) {
        ImageView imageView = textureMap.get(id);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        return imageView;
    }

    public ImageView GetImageView(String id, int x, int y) {
        ImageView imageView = textureMap.get(id);
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
        return imageView;
    }

    public ImageView GetImageView(String id) {
        ImageView imageView = textureMap.get(id);
        return imageView;
    }

    public static TextureManager getTextureManager() {
        if (textureManager == null) {
            textureManager = new TextureManager();
        }
        return textureManager;
    }
}