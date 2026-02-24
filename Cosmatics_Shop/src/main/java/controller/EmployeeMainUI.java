package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Sales;

public class EmployeeMainUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Sales currentSales;

    // L'ÉCLAT 奢華配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    public EmployeeMainUI(Sales sales) {
        this.currentSales = sales;

        // --- 視窗基本設定 ---
        setTitle("L'ÉCLAT - Employee Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 480);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- 1. 頂部導覽列 (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 600, 90);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblBrand = new JLabel("L'ÉCLAT");
        lblBrand.setForeground(GOLD);
        lblBrand.setFont(new Font("Didot", Font.BOLD, 32));
        lblBrand.setHorizontalAlignment(SwingConstants.CENTER);
        lblBrand.setBounds(0, 15, 600, 40);
        headerPanel.add(lblBrand);
        
        JLabel lblPortal = new JLabel("E M P L O Y E E   P O R T A L");
        lblPortal.setForeground(Color.WHITE);
        lblPortal.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblPortal.setHorizontalAlignment(SwingConstants.CENTER);
        lblPortal.setBounds(0, 55, 600, 20);
        headerPanel.add(lblPortal);

        // --- 2. 歡迎訊息與所屬專櫃 ---
        JLabel lblWelcome = new JLabel("Welcome, " + currentSales.getName());
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblWelcome.setForeground(DARK_GREY);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setBounds(0, 120, 600, 30);
        contentPane.add(lblWelcome);
        
        JLabel lblBranch = new JLabel("Branch: " + currentSales.getAddress());
        lblBranch.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblBranch.setForeground(Color.GRAY);
        lblBranch.setHorizontalAlignment(SwingConstants.CENTER);
        lblBranch.setBounds(0, 150, 600, 20);
        contentPane.add(lblBranch);

        // --- 3. 按鈕：訂單查詢與管理 ---
        JButton btnManageOrders = new JButton("O R D E R   M A N A G E M E N T");
        btnManageOrders.addActionListener(e -> {
            // 跳轉到綜合訂單查詢系統
            EmployeeOrderUI orderUI = new EmployeeOrderUI(currentSales);
            orderUI.setVisible(true);
            orderUI.setLocationRelativeTo(null);
            dispose(); // 關閉原本的 Dashboard 畫面
        });
        btnManageOrders.setBackground(DARK_GREY);
        btnManageOrders.setForeground(Color.WHITE);
        btnManageOrders.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnManageOrders.setBounds(150, 210, 300, 50);
        btnManageOrders.setFocusPainted(false);
        btnManageOrders.setBorderPainted(false);
        btnManageOrders.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnManageOrders);

        // --- 4. 按鈕：業績查詢 ---
        JButton btnPerformance = new JButton("S A L E S   P E R F O R M A N C E");
        btnPerformance.addActionListener(e -> {
        	// 【關鍵：判斷身分】如果是 S001，就開排行榜；其他人就開個人儀表板
            if ("S001".equals(currentSales.getSalesId())) {
                ManagerPerformanceUI managerUI = new ManagerPerformanceUI(currentSales);
                managerUI.setVisible(true);
                managerUI.setLocationRelativeTo(null);
            } else {
                SalesPerformanceUI perfUI = new SalesPerformanceUI(currentSales);
                perfUI.setVisible(true);
                perfUI.setLocationRelativeTo(null);
            }
            dispose(); 
            
        });
        btnPerformance.setBackground(GOLD);
        btnPerformance.setForeground(Color.WHITE);
        btnPerformance.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPerformance.setBounds(150, 280, 300, 50);
        btnPerformance.setFocusPainted(false);
        btnPerformance.setBorderPainted(false);
        btnPerformance.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnPerformance);

        // --- 5. 按鈕：登出 ---
        JButton btnLogout = new JButton("Sign Out");
        btnLogout.addActionListener(e -> {
            // 返回登入畫面
            LoginUI login = new LoginUI();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        });
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.GRAY);
        btnLogout.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnLogout.setBounds(250, 360, 100, 30);
        btnLogout.setBorder(null);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnLogout);
    }
}