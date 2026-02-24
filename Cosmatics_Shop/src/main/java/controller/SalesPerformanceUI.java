package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.Order;
import model.Sales;
import service.order.OrderServiceImpl;

public class SalesPerformanceUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    
    private Sales currentSales;
    private OrderServiceImpl orderService = new OrderServiceImpl();

    // L'ÉCLAT 奢華配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    public SalesPerformanceUI(Sales sales) {
        this.currentSales = sales;
        
        setTitle("L'ÉCLAT - Sales Performance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- 1. Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 800, 60);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitle = new JLabel("PERFORMANCE DASHBOARD");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Didot", Font.BOLD, 20));
        lblTitle.setBounds(20, 15, 350, 30);
        headerPanel.add(lblTitle);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> {
            new EmployeeMainUI(currentSales).setVisible(true);
            dispose();
        });
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(DARK_GREY);
        btnBack.setBounds(610, 15, 150, 30);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnBack);

        // --- 2. 獲取業務員業績資料 ---
        List<Order> myOrders = orderService.getOrdersBySales(currentSales.getSalesId());
        double totalRevenue = 0;
        for (Order o : myOrders) {
            totalRevenue += o.getTotalAmount();
        }
        int orderCount = myOrders.size();
        double aov = (orderCount > 0) ? (totalRevenue / orderCount) : 0.0;

        // --- 3. 數據卡片區 (Data Cards) ---
        // 卡片 1: 總業績 (Total Revenue)
        JPanel cardRevenue = new JPanel();
        cardRevenue.setBackground(GOLD);
        cardRevenue.setBounds(30, 90, 220, 100);
        cardRevenue.setLayout(null);
        contentPane.add(cardRevenue);
        
        JLabel lblRevTitle = new JLabel("TOTAL REVENUE");
        lblRevTitle.setForeground(Color.WHITE);
        lblRevTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblRevTitle.setBounds(15, 15, 150, 20);
        cardRevenue.add(lblRevTitle);
        
        JLabel lblRevData = new JLabel(String.format("$%,.2f", totalRevenue));
        lblRevData.setForeground(Color.WHITE);
        lblRevData.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblRevData.setBounds(15, 45, 200, 40);
        cardRevenue.add(lblRevData);

        // 卡片 2: 成交單數 (Total Orders)
        JPanel cardOrders = new JPanel();
        cardOrders.setBackground(DARK_GREY);
        cardOrders.setBounds(280, 90, 220, 100);
        cardOrders.setLayout(null);
        contentPane.add(cardOrders);
        
        JLabel lblOrdTitle = new JLabel("TOTAL ORDERS");
        lblOrdTitle.setForeground(Color.WHITE);
        lblOrdTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblOrdTitle.setBounds(15, 15, 150, 20);
        cardOrders.add(lblOrdTitle);
        
        JLabel lblOrdData = new JLabel(String.valueOf(orderCount));
        lblOrdData.setForeground(Color.WHITE);
        lblOrdData.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblOrdData.setBounds(15, 45, 200, 40);
        cardOrders.add(lblOrdData);

        // 卡片 3: 客單價 (Average Order Value - AOV)
        JPanel cardAov = new JPanel();
        cardAov.setBackground(new Color(240, 240, 240));
        cardAov.setBounds(530, 90, 220, 100);
        cardAov.setBorder(new LineBorder(Color.LIGHT_GRAY));
        cardAov.setLayout(null);
        contentPane.add(cardAov);
        
        JLabel lblAovTitle = new JLabel("AVG ORDER VALUE");
        lblAovTitle.setForeground(DARK_GREY);
        lblAovTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblAovTitle.setBounds(15, 15, 150, 20);
        cardAov.add(lblAovTitle);
        
        JLabel lblAovData = new JLabel(String.format("$%,.2f", aov));
        lblAovData.setForeground(GOLD);
        lblAovData.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblAovData.setBounds(15, 45, 200, 40);
        cardAov.add(lblAovData);

        // --- 4. 歷史訂單列表 (My Recent Sales) ---
        JLabel lblTableTitle = new JLabel("MY RECENT SALES RECORDS");
        lblTableTitle.setForeground(DARK_GREY);
        lblTableTitle.setFont(new Font("Didot", Font.BOLD, 14));
        lblTableTitle.setBounds(30, 220, 300, 30);
        contentPane.add(lblTableTitle);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 260, 720, 270);
        scrollPane.setBorder(new LineBorder(new Color(230,230,230)));
        contentPane.add(scrollPane);

        String[] columns = {"Order ID", "Date", "Customer ID", "Amount"};
        model = new DefaultTableModel(null, columns) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        
        // 將訂單資料填入表格
        for(Order o : myOrders) {
            model.addRow(new Object[]{ o.getOrderId(), o.getOrderTime(), o.getCustomerId(), String.format("$%,.2f", o.getTotalAmount()) });
        }
        
        scrollPane.setViewportView(table);
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_GREY);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
}