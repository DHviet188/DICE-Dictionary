package com.btl.SpeedWord.Logic;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.InputStream;
import java.util.*;

public class Word {
    private static Word instance;
    private Map<String, String> wordMap;
    private List<String> keyList;

    private Word() {
        wordMap = new HashMap<>();
        Parser();
        keyList = new ArrayList<>(wordMap.keySet());
    }

    public static Word getInstance() {
        if (instance == null) {
            instance = new Word();
        }
        return instance;
    }

    private void Parser() {
        try {
            InputStream xmlFile = getClass().getResourceAsStream("word.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            NodeList nodeList = doc.getElementsByTagName("word");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String english = element.getElementsByTagName("english").item(0).getTextContent();
                    String vietnamese = element.getElementsByTagName("vietnamese").item(0).getTextContent();

                    wordMap.put(english, vietnamese);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getKeyList() {
        return keyList;
    }

    public String getWord(String key) {
        return wordMap.get(key);
    }
}
