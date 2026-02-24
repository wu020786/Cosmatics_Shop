package controller;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.Customer;
import model.Order;
import model.OrderDetail;
import model.Product;
import service.order.OrderServiceImpl;
import service.product.ProductServiceImpl;

public class ModifyOrderUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTotal;
    
    private ProductServiceImpl productService = new ProductServiceImpl();
    private OrderServiceImpl orderService = new OrderServiceImpl();
    private Customer customer;
    private String targetOrderId; // 要修改的訂單編號
    
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    public ModifyOrderUI(Customer customer, String orderId) {
        this.customer = customer;
        this.targetOrderId = orderId;
        
        setTitle("L'ÉCLAT - Modify Order: " + orderId);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 900, 60);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);
        
        JLabel lblBrand = new JLabel("MODIFY ORDER");
        lblBrand.setForeground(GOLD);
        lblBrand.setFont(new Font("Didot", Font.BOLD, 24));
        lblBrand.setBounds(20, 10, 300, 40);
        headerPanel.add(lblBrand);
        
        JButton btnBack = new JButton("Cancel / Back");
        btnBack.addActionListener(e -> {
            new OrderManagementUI(customer).setVisible(true);
            dispose();
        });
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(DARK_GREY);
        btnBack.setBounds(750, 15, 120, 30);
        headerPanel.add(btnBack);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 100, 580, 400);
        scrollPane.setBorder(new LineBorder(new Color(230,230,230)));
        contentPane.add(scrollPane);
        
        String[] columns = {"Select", "ID", "Product Name", "Category", "Price", "Qty"};
        model = new DefaultTableModel(null, columns) {
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 0) return Boolean.class;
                if(columnIndex == 4) return Double.class;
                if(columnIndex == 5) return Integer.class;
                return String.class;
            }
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 5;
            }
        };
        table = new JTable(model);
        styleTable(table);
        loadProductDataAndMergeWithOrder(); // 載入並比對原有訂單
        scrollPane.setViewportView(table);
        
        JPanel checkoutPanel = new JPanel();
        checkoutPanel.setBackground(new Color(250, 250, 250));
        checkoutPanel.setBounds(630, 100, 230, 400);
        checkoutPanel.setLayout(null);
        contentPane.add(checkoutPanel);
        
        lblTotal = new JLabel("Total: $0.0");
        lblTotal.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblTotal.setBounds(20, 70, 200, 30);
        checkoutPanel.add(lblTotal);
        
        JButton btnCalc = new JButton("Recalculate");
        btnCalc.addActionListener(e -> calculateTotal());
        btnCalc.setBackground(Color.WHITE);
        btnCalc.setForeground(DARK_GREY);
        btnCalc.setBounds(20, 110, 190, 30);
        checkoutPanel.add(btnCalc);
        
        JButton btnSave = new JButton("S A V E   C H A N G E S");
        btnSave.addActionListener(e -> handleSaveChanges());
        btnSave.setBackground(GOLD);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnSave.setBounds(20, 340, 190, 40);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        checkoutPanel.add(btnSave);
        
        // 初始化計算一次總額
        calculateTotal();
    }
    
    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_GREY);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
    
    // 【核心邏輯】：把所有商品列出來，並跟這筆訂單原有的明細做比對
    private void loadProductDataAndMergeWithOrder() {
        List<Product> allProducts = productService.getAllProducts();
        List<OrderDetail> existingDetails = orderService.getOrderDetails(targetOrderId);
        
        for(Product p : allProducts) {
            boolean isChecked = false;
            int qty = 1;
            
            // 檢查這個商品是不是已經在原訂單裡面
            for(OrderDetail od : existingDetails) {
                if(od.getProductId().equals(p.getProductId())) {
                    isChecked = true;
                    qty = od.getQuantity();
                    break;
                }
            }
            
            model.addRow(new Object[]{ isChecked, p.getProductId(), p.getProductName(), p.getCategoryName(), p.getPrice(), qty });
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
    
    private void handleSaveChanges() {
        double total = calculateTotal();
        if(total == 0) {
            JOptionPane.showMessageDialog(this, "Order cannot be empty. If you want to cancel, please use the Cancel button in the previous menu.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Save changes? New total is $" + total, "Confirm Modify", JOptionPane.YES_NO_OPTION);
        if(confirm == JOptionPane.YES_OPTION) {
            Order updatedOrder = new Order();
            updatedOrder.setOrderId(targetOrderId);
            updatedOrder.setTotalAmount(total);
            
            List<OrderDetail> newDetails = new ArrayList<>();
            for(int i=0; i<table.getRowCount(); i++) {
                Boolean isChecked = (Boolean) table.getValueAt(i, 0);
                if(isChecked != null && isChecked) {
                    OrderDetail od = new OrderDetail();
                    od.setOrderId(targetOrderId);
                    od.setProductId((String) table.getValueAt(i, 1));
                    od.setProductName((String) table.getValueAt(i, 2));
                    od.setUnitPrice((Double) table.getValueAt(i, 4));
                    od.setQuantity((Integer) table.getValueAt(i, 5));
                    newDetails.add(od);
                }
            }
            
            orderService.modifyOrder(updatedOrder, newDetails);
            JOptionPane.showMessageDialog(this, "Order successfully updated!");
            
            new OrderManagementUI(customer).setVisible(true);
            dispose();
        }
    }
}