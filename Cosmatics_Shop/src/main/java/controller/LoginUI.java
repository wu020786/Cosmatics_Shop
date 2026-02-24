package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Customer;
import model.Sales;
import service.customer.CustomerServiceImpl;
import service.sales.SalesServiceImpl;

public class LoginUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    // L'ÉCLAT 奢華配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);
    
    // 引入服務層 (商業邏輯)
    private CustomerServiceImpl customerService = new CustomerServiceImpl();
    private SalesServiceImpl salesService = new SalesServiceImpl();

    /**
     * 啟動應用程式
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                LoginUI frame = new LoginUI();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null); // 讓視窗在螢幕正中央開啟
            } catch (Exception e) { 
                e.printStackTrace(); 
            }
        });
    }

    /**
     * 建立登入視窗
     */
    public LoginUI() {
        setTitle("L'ÉCLAT - System Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 550);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- 1. 品牌標題區 (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBounds(0, 40, 434, 100);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblBrand = new JLabel("L'ÉCLAT");
        lblBrand.setHorizontalAlignment(SwingConstants.CENTER);
        lblBrand.setForeground(DARK_GREY);
        lblBrand.setFont(new Font("Didot", Font.BOLD, 40));
        lblBrand.setBounds(0, 10, 434, 50);
        headerPanel.add(lblBrand);

        JLabel lblSlogan = new JLabel("LUXURY SKINCARE COLLECTION");
        lblSlogan.setHorizontalAlignment(SwingConstants.CENTER);
        lblSlogan.setForeground(GOLD);
        lblSlogan.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSlogan.setBounds(0, 60, 434, 20);
        headerPanel.add(lblSlogan);

        // --- 2. 帳號與密碼輸入框 ---
        JLabel lblUser = new JLabel("USERNAME");
        lblUser.setForeground(DARK_GREY);
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblUser.setBounds(75, 170, 100, 14);
        contentPane.add(lblUser);

        usernameField = new JTextField();
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameField.setBounds(75, 190, 280, 35);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        usernameField.setBackground(Color.WHITE);
        contentPane.add(usernameField);

        JLabel lblPass = new JLabel("PASSWORD");
        lblPass.setForeground(DARK_GREY);
        lblPass.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblPass.setBounds(75, 250, 100, 14);
        contentPane.add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField.setBounds(75, 270, 280, 35);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        passwordField.setBackground(Color.WHITE);
        contentPane.add(passwordField);

        // --- 3. 客戶登入按鈕 (Customer Sign In) ---
        JButton btnLogin = new JButton("S I G N   I N");
        btnLogin.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            
            // 呼叫 CustomerService 驗證
            Customer c = customerService.login(user, pass);
            if(c != null) {
                // 登入成功，跳轉到客戶主畫面
                MainUI mainUI = new MainUI(c);
                mainUI.setVisible(true);
                mainUI.setLocationRelativeTo(null);
                dispose(); // 關閉登入視窗
            } else {
                // 登入失敗
                JOptionPane.showMessageDialog(this, "Invalid Customer Credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(DARK_GREY);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnLogin.setBounds(75, 340, 280, 45);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnLogin);

        // --- 4. 註冊帳號按鈕 (Create an Account) ---
        JButton btnRegister = new JButton("Create an Account");
        btnRegister.addActionListener(e -> {
            // 跳轉到註冊畫面
            RegisterUI reg = new RegisterUI();
            reg.setVisible(true);
            reg.setLocationRelativeTo(null);
            dispose(); // 關閉登入視窗
        });
        btnRegister.setForeground(DARK_GREY);
        btnRegister.setBackground(Color.WHITE);
        btnRegister.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btnRegister.setBounds(75, 400, 280, 30);
        btnRegister.setBorder(null);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnRegister);
        
        // --- 5. 員工專屬入口按鈕 (Employee Portal) ---
        JButton btnEmployeeLogin = new JButton("E M P L O Y E E   P O R T A L");
        btnEmployeeLogin.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
            
            // 呼叫 SalesService 驗證員工身分
            Sales sales = salesService.login(user, pass);
            
            if (sales != null) {
                // 【已經修改】員工登入成功，直接開啟員工專屬大廳
                EmployeeMainUI employeeUI = new EmployeeMainUI(sales);
                employeeUI.setVisible(true);
                employeeUI.setLocationRelativeTo(null);
                dispose();
            } else {
                // 員工登入失敗
                JOptionPane.showMessageDialog(this, "Invalid Employee Credentials or Access Denied.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnEmployeeLogin.setForeground(GOLD); // 使用金色字體，低調奢華
        btnEmployeeLogin.setBackground(Color.WHITE);
        btnEmployeeLogin.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnEmployeeLogin.setBounds(75, 450, 280, 20);
        btnEmployeeLogin.setBorder(null);
        btnEmployeeLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnEmployeeLogin);
    }
}