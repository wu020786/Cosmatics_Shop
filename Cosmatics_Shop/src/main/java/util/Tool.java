package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tool {
	
	
	 public static void main(String[] args) {
	        System.out.println("正在嘗試連線到資料庫...");
	        Connection conn = getConn();
	        if (conn != null) {
	            System.out.println("太棒了！成功連線到 cosmetics_shop 資料庫！");
	            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
	        } else {
	            System.out.println("連線失敗...請看上方的紅色錯誤訊息來除錯。");
	        }
	    }
	
	
	
    private static final String URL = "jdbc:mysql://localhost:3306/cosmetics_shop?serverTimezone=Asia/Taipei&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";  

    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("【錯誤】找不到 MySQL 驅動程式！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("【錯誤】資料庫連線失敗！");
            e.printStackTrace();
        }
        return conn;
    }

   
}