package com.btl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

public class RandomWordGetAPI {
    
    private static RandomWordGetAPI sendRequest;

    private RandomWordGetAPI() {
    }
    
    public static RandomWordGetAPI getSentRequest() {
        if (sendRequest == null) {
            sendRequest = new RandomWordGetAPI();
        }
        return sendRequest;
    }
    
    public String getARandomWord() {
        ArrayList<String> words = null;
        
        try {
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://random-word-api.herokuapp.com/word"))
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
            
            Type wordsType = new TypeToken<ArrayList<String>>(){}.getType();
            Gson gson = new Gson();
            
            words = gson.fromJson(getResponse.body(), wordsType);
            
        } catch(Exception e) {}
        
        return words.get(0);
    }
    
}
