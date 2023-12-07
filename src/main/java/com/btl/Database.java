package com.btl;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    
    /**
     * developing
     * @return 
     */
    public static Connection connectDb() {
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            Connection connect = DriverManager.getConnection("jdbc:mysql://db4free.net/dicedatabase", "dicename", "dicename");
            
            return connect;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
