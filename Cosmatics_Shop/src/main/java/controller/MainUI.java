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
import model.Customer;

public class MainUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Customer customer;

    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    public MainUI(Customer customer) {
        this.customer = customer;
        setTitle("L'ÉCLAT - Member Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 450);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 600, 80);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblBrand = new JLabel("L'ÉCLAT");
        lblBrand.setForeground(GOLD);
        lblBrand.setFont(new Font("Didot", Font.BOLD, 32));
        lblBrand.setHorizontalAlignment(SwingConstants.CENTER);
        lblBrand.setBounds(0, 20, 600, 40);
        headerPanel.add(lblBrand);

        // Welcome Message
        JLabel lblWelcome = new JLabel("Welcome back, " + customer.getName());
        lblWelcome.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblWelcome.setForeground(DARK_GREY);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setBounds(0, 110, 600, 30);
        contentPane.add(lblWelcome);

        // --- 按鈕 1：創建訂單 (前往購物) ---
        JButton btnShop = new JButton("C R E A T E   O R D E R");
        btnShop.addActionListener(e -> {
            ShoppingUI shop = new ShoppingUI(customer);
            shop.setVisible(true);
            shop.setLocationRelativeTo(null);
            dispose();
        });
        btnShop.setBackground(GOLD);
        btnShop.setForeground(Color.WHITE);
        btnShop.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnShop.setBounds(150, 180, 300, 50);
        btnShop.setFocusPainted(false);
        btnShop.setBorderPainted(false);
        btnShop.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnShop);

        // --- 按鈕 2：查詢與修改訂單 ---
        JButton btnManage = new JButton("M A N A G E   O R D E R S");
        btnManage.addActionListener(e -> {
            OrderManagementUI manage = new OrderManagementUI(customer);
            manage.setVisible(true);
            manage.setLocationRelativeTo(null);
            dispose();
        });
        btnManage.setBackground(DARK_GREY);
        btnManage.setForeground(Color.WHITE);
        btnManage.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnManage.setBounds(150, 250, 300, 50);
        btnManage.setFocusPainted(false);
        btnManage.setBorderPainted(false);
        btnManage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnManage);

        // --- 按鈕 3：登出 ---
        JButton btnLogout = new JButton("Sign Out");
        btnLogout.addActionListener(e -> {
            LoginUI login = new LoginUI();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        });
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(Color.GRAY);
        btnLogout.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnLogout.setBounds(250, 330, 100, 30);
        btnLogout.setBorder(null);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnLogout);
    }
}