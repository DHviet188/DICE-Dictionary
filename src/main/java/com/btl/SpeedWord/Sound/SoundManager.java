package com.btl.SpeedWord.Sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, MediaPlayer> soundMap;

    // Private constructor để ngăn việc tạo đối tượng từ bên ngoài class
    private SoundManager() {
        soundMap = new HashMap<>();
        init();
    }

    // Phương thức để trả về instance duy nhất của SoundManager
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void init() {
        addSound("menu", "MilkyWay.mp3", true);
        addSound("click", "click.wav", false);
        addSound("play", "PlayMusic.wav", false);
        addSound("over", "Over.mp3", false);
    }

    // Thêm âm thanh vào Map
    public void addSound(String soundName, String soundFile, boolean inf) {
        Media sound = null;
        try {
            sound = new Media(getClass().getResource(soundFile).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        if (inf) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        soundMap.put(soundName, mediaPlayer);
        soundMap.put(soundName, mediaPlayer);
    }

    // Phát âm thanh dựa trên tên
    public void playSound(String soundName) {
        MediaPlayer mediaPlayer = soundMap.get(soundName);
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
        } else {
            System.out.println("mediaPlayer is null!");
        }
    }

    // Dừng phát âm thanh dựa trên tên
    public void stopSound(String soundName) {
        MediaPlayer mediaPlayer = soundMap.get(soundName);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    // Tạm dừng phát âm thanh dựa trên tên
    public void pauseSound(String soundName) {
        MediaPlayer mediaPlayer = soundMap.get(soundName);
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Kiểm tra xem âm thanh có đang phát hay không dựa trên tên
    public boolean isPlaying(String soundName) {
        MediaPlayer mediaPlayer = soundMap.get(soundName);
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    // Kiểm tra xem âm thanh có tồn tại trong Map hay không
    public boolean containsSound(String soundName) {
        return soundMap.containsKey(soundName);
    }

    public MediaPlayer getSound(String name) {
        return soundMap.get(name);
    }
}