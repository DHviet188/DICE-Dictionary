package com.btl;

public class HomeRankingRow {
    private String username;
    private int gameScore;

    public HomeRankingRow(String username, int gameScore) {
        this.username = username;
        this.gameScore = gameScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }
}
