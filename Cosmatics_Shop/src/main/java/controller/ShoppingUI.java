package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.Customer;
import model.Order;
import model.OrderDetail;
import model.Product;
import model.Sales;
import service.order.OrderServiceImpl;
import service.product.ProductServiceImpl;
import service.sales.SalesServiceImpl;

public class ShoppingUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;
    
    // 引入服務層
    private ProductServiceImpl productService = new ProductServiceImpl();
    private OrderServiceImpl orderService = new OrderServiceImpl();
    private SalesServiceImpl salesService = new SalesServiceImpl(); // 新增 Sales 服務
    private Customer customer;
    
    // 新增：業務員選擇下拉選單
    private JComboBox<String> comboSales;
    
    // 奢華配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);
    private final Color LIGHT_BG = new Color(250, 250, 250);

    public ShoppingUI(Customer customer) {
        this.customer = customer;
        
        setTitle("L'ÉCLAT - Collection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // --- 1. Top Bar (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 900, 60);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);
        
        JLabel lblBrand = new JLabel("L'ÉCLAT");
        lblBrand.setForeground(GOLD);
        lblBrand.setFont(new Font("Didot", Font.BOLD, 24));
        lblBrand.setBounds(20, 10, 200, 40);
        headerPanel.add(lblBrand);
        
        // 動態時鐘
        JLabel lblClock = new JLabel();
        lblClock.setForeground(new Color(200, 200, 200));
        lblClock.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblClock.setHorizontalAlignment(SwingConstants.CENTER);
        lblClock.setBounds(300, 20, 250, 20);
        headerPanel.add(lblClock);
        
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            lblClock.setText(sdf.format(new Date()));
        });
        timer.start();
        
        JLabel lblWelcome = new JLabel("Welcome, " + customer.getName());
        lblWelcome.setHorizontalAlignment(SwingConstants.RIGHT);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblWelcome.setBounds(600, 20, 150, 20);
        headerPanel.add(lblWelcome);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            MainUI mainUI = new MainUI(customer);
            mainUI.setVisible(true);
            mainUI.setLocationRelativeTo(null);
            dispose();
        });
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(DARK_GREY);
        btnBack.setBounds(770, 15, 80, 30);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnBack);
        
        // --- 2. Product Table (Left Side) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 100, 580, 400);
        scrollPane.setBorder(new LineBorder(new Color(230,230,230)));
        contentPane.add(scrollPane);
        
        String[] columns = {"Select", "ID", "Product Name", "Category", "Price", "Qty"};
        model = new DefaultTableModel(null, columns) {
            private static final long serialVersionUID = 1L;
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 0) return Boolean.class;
                if(columnIndex == 4) return Double.class;
                if(columnIndex == 5) return Integer.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 5;
            }
        };
        
        table = new JTable(model);
        styleTable(table);
        loadProductData();
        scrollPane.setViewportView(table);
        
        // --- 3. Checkout Panel (Right Side) ---
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setBackground(LIGHT_BG);
        checkoutPanel.setBounds(630, 100, 230, 400);
        checkoutPanel.setLayout(null);
        contentPane.add(checkoutPanel);
        
        JLabel lblSummary = new JLabel("ORDER SUMMARY");
        lblSummary.setFont(new Font("Didot", Font.BOLD, 16));
        lblSummary.setForeground(DARK_GREY);
        lblSummary.setBounds(20, 20, 200, 30);
        checkoutPanel.add(lblSummary);
        
        lblTotal = new JLabel("Total: $0.0");
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTotal.setBounds(20, 60, 200, 30);
        checkoutPanel.add(lblTotal);
        
        JButton btnCalc = new JButton("Update Total");
        btnCalc.addActionListener(e -> calculateTotal());
        btnCalc.setBackground(Color.WHITE);
        btnCalc.setForeground(DARK_GREY);
        btnCalc.setBounds(20, 100, 190, 30);
        btnCalc.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutPanel.add(btnCalc);
        
        // 【新增】選擇業務員 (顧問) 區域
        JLabel lblAdvisor = new JLabel("SELECT ADVISOR:");
        lblAdvisor.setFont(new Font("SansSerif", Font.BOLD, 10));
        lblAdvisor.setForeground(Color.GRAY);
        lblAdvisor.setBounds(20, 160, 190, 20);
        checkoutPanel.add(lblAdvisor);
        
        comboSales = new JComboBox<>();
        comboSales.setBackground(Color.WHITE);
        comboSales.setFont(new Font("SansSerif", Font.PLAIN, 12));
        comboSales.setBounds(20, 180, 190, 30);
        loadSalesData(); // 載入所有業務員名單
        checkoutPanel.add(comboSales);
        
        // 結帳按鈕
        JButton btnCheckout = new JButton("C H E C K O U T");
        btnCheckout.addActionListener(e -> handleCheckout());
        btnCheckout.setBackground(GOLD);
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCheckout.setBounds(20, 340, 190, 40);
        btnCheckout.setFocusPainted(false);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutPanel.add(btnCheckout);
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(false);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_GREY);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBorder(new LineBorder(new Color(230,230,230)));
    }
    
    private void loadProductData() {
        List<Product> list = productService.getAllProducts();
        for(Product p : list) {
            Object[] row = new Object[6];
            row[0] = false;
            row[1] = p.getProductId();
            row[2] = p.getProductName();
            row[3] = p.getCategoryName();
            row[4] = p.getPrice();
            row[5] = 1;
            model.addRow(row);
        }
    }
    
    // 【新增】將資料庫中的業務員載入到下拉選單
    private void loadSalesData() {
        List<Sales> salesList = salesService.getAllSales();
        for (Sales s : salesList) {
            // 格式設定為："名字 (代號)" -> 例如："Aria Montgomery (S002)"
            comboSales.addItem(s.getName() + " (" + s.getSalesId() + ")");
        }
    }
    
    private double calculateTotal() {
        double sum = 0;
        for(int i=0; i<table.getRowCount(); i++) {
            Boolean isChecked = (Boolean) table.getValueAt(i, 0);
            if(isChecked != null && isChecked) {
                Double price = (Double) table.getValueAt(i, 4);
                Integer qty = (Integer) table.getValueAt(i, 5);
                sum += (price * qty);
            }
        }
        lblTotal.setText("Total: $" + sum);
        return sum;
    }
    
    // --- 處理結帳與觸發收據 ---
    private void handleCheckout() {
        double total = calculateTotal();
        if(total == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one item.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Confirm payment of $" + total + "?", "Checkout", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            
            String orderId = "ORD-" + System.currentTimeMillis();
            Order order = new Order();
            order.setOrderId(orderId);
            order.setCustomerId(customer.getCustomerId());
            order.setTotalAmount(total);
            
            // 【關鍵修改】從下拉選單取得被選中的業務員 ID
            String selectedItem = (String) comboSales.getSelectedItem();
            if (selectedItem != null && selectedItem.contains("(")) {
                // 擷取刮號內的代號，例如從 "Aria Montgomery (S002)" 抓出 "S002"
                String salesId = selectedItem.substring(selectedItem.indexOf("(") + 1, selectedItem.indexOf(")"));
                order.setSalesId(salesId);
            } else {
                order.setSalesId("S001"); // 防呆機制
            }
            
            List<OrderDetail> details = new ArrayList<>();
            for(int i=0; i<table.getRowCount(); i++) {
                Boolean isChecked = (Boolean) table.getValueAt(i, 0);
                if(isChecked != null && isChecked) {
                    OrderDetail od = new OrderDetail();
                    od.setOrderId(orderId);
                    od.setProductId((String) table.getValueAt(i, 1));
                    od.setProductName((String) table.getValueAt(i, 2));
                    od.setUnitPrice((Double) table.getValueAt(i, 4));
                    od.setQuantity((Integer) table.getValueAt(i, 5));
                    details.add(od);
                }
            }
            
            orderService.placeOrder(order, details);
            
            showReceiptDialog(order, details);
            
            dispose();
            new ShoppingUI(customer).setVisible(true);
        }
    }

    // ==========================================
    // 結帳後立即彈出的電子收據視窗
    // ==========================================
    private void showReceiptDialog(Order order, List<OrderDetail> details) {
        JDialog receiptDialog = new JDialog(this, "Order Receipt", true);
        receiptDialog.setSize(400, 520); // 稍微加高以容納新資訊
        receiptDialog.setLocationRelativeTo(this);
        receiptDialog.setLayout(null);
        receiptDialog.getContentPane().setBackground(Color.WHITE);

        JTextArea txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReceipt.setEditable(false);
        txtReceipt.setBorder(new EmptyBorder(10, 10, 10, 10));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("               L'ÉCLAT                  \n");
        sb.append("      LUXURY SKINCARE COLLECTION        \n");
        sb.append("========================================\n\n");
        sb.append("Order ID: ").append(order.getOrderId()).append("\n");
        sb.append("Date    : ").append(currentTime).append("\n");
        sb.append("Customer: ").append(customer.getName()).append("\n");
        // 【新增】收據上顯示服務的顧問
        sb.append("Advisor : ").append((String) comboSales.getSelectedItem()).append("\n\n"); 
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-20s %5s %10s\n", "Item", "Qty", "Subtotal"));
        sb.append("----------------------------------------\n");

        for (OrderDetail od : details) {
            String name = od.getProductName();
            if (name.length() > 18) name = name.substring(0, 15) + "...";
            double subtotal = od.getUnitPrice() * od.getQuantity();
            sb.append(String.format("%-20s %5d $%9.2f\n", name, od.getQuantity(), subtotal));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("%-26s $%9.2f\n", "TOTAL AMOUNT:", order.getTotalAmount()));
        sb.append("========================================\n");
        sb.append("      Thank you for your purchase!      \n");

        txtReceipt.setText(sb.toString());

        JScrollPane scroll = new JScrollPane(txtReceipt);
        scroll.setBounds(10, 10, 365, 400);
        scroll.setBorder(new LineBorder(Color.LIGHT_GRAY));
        receiptDialog.add(scroll);

        JButton btnDoPrint = new JButton("P R I N T   R E C E I P T");
        btnDoPrint.setBackground(DARK_GREY);
        btnDoPrint.setForeground(Color.WHITE);
        btnDoPrint.setBounds(10, 420, 365, 40);
        btnDoPrint.setFocusPainted(false);
        btnDoPrint.addActionListener(e -> {
            try {
                boolean complete = txtReceipt.print();
                if (complete) {
                    JOptionPane.showMessageDialog(receiptDialog, "Printing Completed!", "Printer", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(receiptDialog, "Printing Cancelled.", "Printer", JOptionPane.WARNING_MESSAGE);
                }
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(receiptDialog, "Printing Failed: " + pe.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        receiptDialog.add(btnDoPrint);

        receiptDialog.setVisible(true); 
    }
}