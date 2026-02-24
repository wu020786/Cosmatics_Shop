package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import service.customer.CustomerServiceImpl;

public class RegisterUI extends JFrame {
    private JPanel contentPane;
    private JTextField nameField, numberField, userField, addressField;
    private JPasswordField passField;
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);
    private CustomerServiceImpl customerService = new CustomerServiceImpl();

    public RegisterUI() {
        setTitle("L'ÉCLAT - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 650);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JLabel lblTitle = new JLabel("JOIN L'ÉCLAT");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Didot", Font.BOLD, 24));
        lblTitle.setForeground(DARK_GREY);
        lblTitle.setBounds(0, 30, 434, 40);
        contentPane.add(lblTitle);
        
        int y = 100;
        nameField = addField("FULL NAME", y); y+=70;
        numberField = addField("PHONE NUMBER", y); y+=70;
        userField = addField("USERNAME", y); y+=70;
        passField = addPasswordField("PASSWORD", y); y+=70;
        addressField = addField("ADDRESS", y);
        
        JButton btnReg = new JButton("R E G I S T E R");
        btnReg.addActionListener(e -> {
            String user = userField.getText();
            if(customerService.isUsernameTaken(user)) {
                JOptionPane.showMessageDialog(null, "Username already taken!");
                return;
            }
            Customer c = new Customer();
            c.setCustomerId("C" + System.currentTimeMillis());
            c.setName(nameField.getText());
            c.setNumber(numberField.getText());
            c.setUsername(user);
            c.setPassword(new String(passField.getPassword()));
            c.setAddress(addressField.getText());
            c.setLevel(1);
            customerService.addCustomer(c);
            JOptionPane.showMessageDialog(null, "Welcome to L'ÉCLAT.");
            LoginUI login = new LoginUI();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        });
        btnReg.setBackground(GOLD);
        btnReg.setForeground(Color.WHITE);
        btnReg.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnReg.setBounds(75, 500, 280, 45);
        btnReg.setFocusPainted(false);
        btnReg.setBorderPainted(false);
        contentPane.add(btnReg);
        
        JButton btnBack = new JButton("Back to Login");
        btnBack.addActionListener(e -> {
            LoginUI login = new LoginUI();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
            dispose();
        });
        btnBack.setBounds(75, 560, 280, 20);
        btnBack.setBorder(null);
        btnBack.setBackground(Color.WHITE);
        contentPane.add(btnBack);
    }
    
    private JTextField addField(String labelText, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 10));
        label.setForeground(Color.GRAY);
        label.setBounds(75, y, 200, 14);
        contentPane.add(label);
        JTextField field = new JTextField();
        field.setBounds(75, y+20, 280, 30);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentPane.add(field);
        return field;
    }
    private JPasswordField addPasswordField(String labelText, int y) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 10));
        label.setForeground(Color.GRAY);
        label.setBounds(75, y, 200, 14);
        contentPane.add(label);
        JPasswordField field = new JPasswordField();
        field.setBounds(75, y+20, 280, 30);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentPane.add(field);
        return field;
    }
}