package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
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
import service.order.OrderServiceImpl;

public class OrderManagementUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    
    private Customer customer;
    private OrderServiceImpl orderService = new OrderServiceImpl();

    // L'ÉCLAT 奢華品牌配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    public OrderManagementUI(Customer customer) {
        this.customer = customer;
        
        // --- 視窗基本設定 ---
        setTitle("L'ÉCLAT - Order Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- 1. 頂部導覽列 (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 700, 60);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitle = new JLabel("ORDER HISTORY");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Didot", Font.BOLD, 20));
        lblTitle.setBounds(20, 15, 200, 30);
        headerPanel.add(lblTitle);

        // 返回主畫面按鈕
        JButton btnBack = new JButton("Back to Main");
        btnBack.addActionListener(e -> {
            MainUI main = new MainUI(customer);
            main.setVisible(true);
            main.setLocationRelativeTo(null);
            dispose();
        });
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(DARK_GREY);
        btnBack.setBounds(550, 15, 120, 30);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnBack);

        // --- 2. 訂單列表 (Table) ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(30, 90, 620, 280);
        scrollPane.setBorder(new LineBorder(new Color(230, 230, 230)));
        contentPane.add(scrollPane);

        String[] columns = {"Order ID", "Date", "Total Amount"};
        
        // 設定表格模型，並禁止使用者直接雙擊修改儲存格文字
        model = new DefaultTableModel(null, columns) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            } 
        };
        
        table = new JTable(model);
        styleTable(table); // 套用美化樣式
        loadOrders();      // 載入該會員的歷史訂單
        scrollPane.setViewportView(table);

        // --- 3. 取消訂單按鈕 (Cancel Order) ---
        JButton btnCancel = new JButton("C A N C E L   O R D E R");
        btnCancel.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order to cancel.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String orderId = (String) table.getValueAt(selectedRow, 0);
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you want to cancel order: " + orderId + "?\nThis action cannot be undone.", 
                "Cancel Confirmation", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                orderService.cancelOrder(orderId);
                JOptionPane.showMessageDialog(this, "Order " + orderId + " has been successfully cancelled.");
                
                // 重新整理表格
                model.setRowCount(0); 
                loadOrders();         
            }
        });
        btnCancel.setBackground(DARK_GREY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCancel.setBounds(30, 390, 250, 40);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnCancel);

        // --- 4. 修改訂單按鈕 (Modify Order) ---
        JButton btnModify = new JButton("M O D I F Y   O R D E R");
        btnModify.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select an order to modify.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String orderId = (String) table.getValueAt(selectedRow, 0);
            
            // 開啟專屬的修改訂單視窗，並把目前的 customer 和 orderId 傳遞過去
            ModifyOrderUI modifyUI = new ModifyOrderUI(customer, orderId);
            modifyUI.setVisible(true);
            modifyUI.setLocationRelativeTo(null);
            
            dispose(); // 關閉當前的管理畫面
        });
        btnModify.setBackground(GOLD); // 金色按鈕凸顯重要性
        btnModify.setForeground(Color.WHITE);
        btnModify.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnModify.setBounds(300, 390, 250, 40);
        btnModify.setFocusPainted(false);
        btnModify.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnModify);
    }

    // ==========================================
    // 輔助方法區 (Helper Methods)
    // ==========================================

    /**
     * 美化 JTable 的外觀
     */
    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(245, 245, 245));
        table.setSelectionForeground(DARK_GREY);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_GREY);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    /**
     * 從資料庫取得該會員的訂單，並填入表格
     */
    private void loadOrders() {
        List<Order> orders = orderService.getOrdersByCustomer(customer.getCustomerId());
        for(Order o : orders) {
            model.addRow(new Object[]{ 
                o.getOrderId(), 
                o.getOrderTime(), 
                "$" + o.getTotalAmount() 
            });
        }
    }
} 