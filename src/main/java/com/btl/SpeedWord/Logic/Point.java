package com.btl.SpeedWord.Logic;

import com.btl.Database;
import com.btl.SpeedWord.Scenes.MenuScene;
import com.btl.getAccountData;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Point {
    private static Point instance;
    private ArrayList<String> username;
    private ArrayList<String> highscore;
    private ArrayList<String> timeList;

    public static Point getInstance() {
        if (instance == null) {
            instance = new Point();
        }
        return instance;
    }

    public void insertData(int score) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Kết nối đến cơ sở dữ liệu
            connection = Database.connectDb();

            // Truy vấn để chèn dữ liệu vào bảng
            String insertQuery = "SELECT highScore FROM game_points WHERE username = ?";

            // Tạo PreparedStatement
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, getAccountData.username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int currentHighScore = resultSet.getInt("highScore");

                // So sánh với highScore hiện tại từ cơ sở dữ liệu và ghi đè nếu cần
                if (score > currentHighScore) {
                    // Cập nhật highScore trong cơ sở dữ liệu
                    String updateQuery = "UPDATE game_points SET highScore = ?, time = ? WHERE username = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, score);
                    updateStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    updateStatement.setString(3, getAccountData.username);

                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        MenuScene.getInstance().Score();
                        System.out.println("Đã ghi đè highScore thành công!");
                    } else {
                        System.out.println("Lỗi khi cố gắng ghi đè highScore.");
                    }
                } else {
                    System.out.println("Không cần ghi đè highScore.");
                }
            } else {
                // Chưa có dữ liệu cho người dùng này, chèn mới vào
                String insertQuery1 = "INSERT INTO game_points (username, highScore, time) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery1);
                insertStatement.setString(1, getAccountData.username);
                insertStatement.setInt(2, score);
                insertStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    MenuScene.getInstance().Score();
                    System.out.println("Dữ liệu được chèn thành công!");
                } else {
                    System.out.println("Lỗi khi cố gắng chèn dữ liệu.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng PreparedStatement và Connection
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllData() {
        username = new ArrayList<>();
        highscore = new ArrayList<>();
        timeList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Kết nối đến cơ sở dữ liệu
            connection = Database.connectDb();

            // Truy vấn để lấy tất cả dữ liệu từ bảng
            String selectQuery = "SELECT * FROM game_points LIMIT 6";

            // Tạo PreparedStatement
            preparedStatement = connection.prepareStatement(selectQuery);

            // Thực thi truy vấn và nhận kết quả
            resultSet = preparedStatement.executeQuery();

            // Xử lý kết quả trả về
            while (resultSet.next()) {
                Integer gamePoints = resultSet.getInt("highScore");
                String retrievedUsername = resultSet.getString("username");
                Timestamp time = resultSet.getTimestamp("time");

                // Xử lý dữ liệu lấy được tại đây
                username.add(retrievedUsername);
                highscore.add(gamePoints.toString());
                timeList.add(time.toString());
            }
            System.out.println("Dữ liệu được lấy thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng ResultSet, PreparedStatement và Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getUsername() {
        return username;
    }

    public ArrayList<String> getHighscore() {
        return highscore;
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }
}
