package controller;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import model.Sales;
import service.order.OrderServiceImpl;

public class EmployeeOrderUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    
    private Sales currentSales;
    private OrderServiceImpl orderService = new OrderServiceImpl();

    // L'ÉCLAT 奢華配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    private JLabel lblTotalQty;
    private JLabel lblTotalAmount;
    
    // 將下拉選單提升為全域變數，方便互相連動
    private JComboBox<String> comboFilter;
    private JComboBox<String> comboKeyword;

    public EmployeeOrderUI(Sales sales) {
        this.currentSales = sales;
        
        setTitle("L'ÉCLAT - Order Query System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 650);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- 1. Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREY);
        headerPanel.setBounds(0, 0, 950, 60);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitle = new JLabel("ORDER QUERY SYSTEM");
        lblTitle.setForeground(GOLD);
        lblTitle.setFont(new Font("Didot", Font.BOLD, 20));
        lblTitle.setBounds(20, 15, 300, 30);
        headerPanel.add(lblTitle);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> {
            new EmployeeMainUI(currentSales).setVisible(true);
            dispose();
        });
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(DARK_GREY);
        btnBack.setBounds(760, 15, 150, 30);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnBack);

        // --- 2. 雙重連動篩選列 (Filter Bar) ---
        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(new Color(250, 250, 250));
        filterPanel.setBounds(20, 80, 890, 60);
        filterPanel.setBorder(new LineBorder(new Color(230, 230, 230)));
        filterPanel.setLayout(null);
        contentPane.add(filterPanel);
        
        JLabel lblFilter = new JLabel("Filter By:");
        lblFilter.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFilter.setBounds(20, 20, 60, 20);
        filterPanel.add(lblFilter);

        // 主選單：選擇欄位
        String[] filterOptions = {"All Fields (全部)", "Order ID (訂單)", "Customer (顧客)", "Sales (業務)", "Category (分類)", "Product (產品)", "Date (時間)"};
        comboFilter = new JComboBox<>(filterOptions);
        comboFilter.setBackground(Color.WHITE);
        comboFilter.setBounds(80, 15, 150, 30);
        filterPanel.add(comboFilter);

        JLabel lblKeyword = new JLabel("Select / Type:");
        lblKeyword.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblKeyword.setBounds(240, 20, 90, 20);
        filterPanel.add(lblKeyword);

        // 次選單：取代原本的 TextField，變成動態連動選單 (且支援手動打字)
        comboKeyword = new JComboBox<>(new String[]{""});
        comboKeyword.setBackground(Color.WHITE);
        comboKeyword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboKeyword.setBounds(330, 15, 250, 30);
        comboKeyword.setEditable(true); // 讓使用者可以拉選單，也可以直接打字！
        filterPanel.add(comboKeyword);
        
        JButton btnSearch = new JButton("S E A R C H");
        btnSearch.addActionListener(e -> filterData());
        btnSearch.setBackground(DARK_GREY);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnSearch.setBounds(600, 15, 100, 30);
        btnSearch.setFocusPainted(false);
        filterPanel.add(btnSearch);

        JButton btnReset = new JButton("R E S E T");
        btnReset.addActionListener(e -> {
            comboFilter.setSelectedIndex(0);
            comboKeyword.setSelectedItem("");
            filterData();
        });
        btnReset.setBackground(Color.WHITE);
        btnReset.setForeground(DARK_GREY);
        btnReset.setFont(new Font("SansSerif", Font.BOLD, 10));
        btnReset.setBounds(710, 15, 100, 30);
        filterPanel.add(btnReset);

        // --- 3. 綜合資料表格 ---
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 160, 890, 380);
        scrollPane.setBorder(new LineBorder(new Color(230,230,230)));
        contentPane.add(scrollPane);

        String[] columns = {"Order ID", "Date", "Customer", "Sales", "Category", "Product Name", "Qty", "Subtotal"};
        model = new DefaultTableModel(null, columns) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 6) return Integer.class;
                if(columnIndex == 7) return Double.class;
                return String.class;
            }
        };
        table = new JTable(model);
        styleTable(table);
        loadComprehensiveData();
        
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        scrollPane.setViewportView(table);

        // --- 4. 加入下拉選單的連動事件 ---
        // 當第一個選單改變時，自動更新第二個選單的內容
        comboFilter.addActionListener(e -> {
            updateKeywordDropdown();
            filterData();
        });
        
        // 為了支援動態打字篩選，我們監聽 combobox 內建的 TextField
        JTextField keywordEditor = (JTextField) comboKeyword.getEditor().getEditorComponent();
        keywordEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterData();
            }
        });
        comboKeyword.addActionListener(e -> filterData());

        // --- 5. 底部統計區 (Footer Summary) ---
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBounds(20, 550, 890, 50);
        footerPanel.setLayout(null);
        contentPane.add(footerPanel);
        
        lblTotalQty = new JLabel("Total Quantity: 0");
        lblTotalQty.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTotalQty.setForeground(DARK_GREY);
        lblTotalQty.setBounds(500, 10, 150, 30);
        footerPanel.add(lblTotalQty);
        
        lblTotalAmount = new JLabel("Total Amount: $0.00");
        lblTotalAmount.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTotalAmount.setForeground(GOLD);
        lblTotalAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotalAmount.setBounds(650, 10, 230, 30);
        footerPanel.add(lblTotalAmount);
        
        updateSummary(); // 初始化統計
    }

    // ==========================================
    // 輔助方法區
    // ==========================================

    private void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setGridColor(new Color(240, 240, 240));
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(DARK_GREY);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private void loadComprehensiveData() {
        List<Object[]> records = orderService.getAllOrderDetailsWithInfo();
        for(Object[] row : records) {
            model.addRow(row);
        }
    }
    
    /**
     * 【核心功能】根據主選單的選擇，動態抓取表格內的資料，填入次選單
     */
    private void updateKeywordDropdown() {
        int selectedIndex = comboFilter.getSelectedIndex();
        comboKeyword.removeAllItems();
        comboKeyword.addItem(""); // 預設空白選項
        
        if (selectedIndex == 0) return; // "All Fields" 不抓取特定資料
        
        // 對應表格欄位: 1(Order ID)=0, 2(Customer)=2, 3(Sales)=3, 4(Category)=4, 5(Product)=5, 6(Date)=1
        int colIndex = -1;
        switch(selectedIndex) {
            case 1: colIndex = 0; break;
            case 2: colIndex = 2; break;
            case 3: colIndex = 3; break;
            case 4: colIndex = 4; break;
            case 5: colIndex = 5; break;
            case 6: colIndex = 1; break;
        }
        
        // 使用 TreeSet 來自動排序並過濾重複的資料
        Set<String> uniqueValues = new TreeSet<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, colIndex);
            if (val != null) {
                // 如果是日期格式，只取前面的年月日來做分類會比較實用
                if(colIndex == 1) {
                    uniqueValues.add(val.toString().split(" ")[0]); 
                } else {
                    uniqueValues.add(val.toString());
                }
            }
        }
        
        // 將去重複並排序好的選項放入下拉選單
        for (String value : uniqueValues) {
            comboKeyword.addItem(value);
        }
    }

    /**
     * 根據條件過濾表格資料
     */
    private void filterData() {
        if (sorter == null) return;
        
        Object selectedObj = comboKeyword.getSelectedItem();
        JTextField editor = (JTextField) comboKeyword.getEditor().getEditorComponent();
        String text = (selectedObj != null) ? selectedObj.toString() : editor.getText();
        
        int selectedIndex = comboFilter.getSelectedIndex();
        
        if (text == null || text.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            int colIndex = -1; 
            switch(selectedIndex) {
                case 1: colIndex = 0; break;
                case 2: colIndex = 2; break;
                case 3: colIndex = 3; break;
                case 4: colIndex = 4; break;
                case 5: colIndex = 5; break;
                case 6: colIndex = 1; break;
            }
            
            try {
                // 使用 Pattern.quote 來避免特殊字元造成正則表達式錯誤，(?i)代表忽略大小寫
                if(colIndex == -1) {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text.trim())));
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text.trim()), colIndex));
                }
            } catch (Exception ex) {
                sorter.setRowFilter(null);
            }
        }
        updateSummary(); // 每次篩選完，重新計算總額
    }

    /**
     * 計算當前表格上顯示的總數量與總額
     */
    private void updateSummary() {
        int totalQty = 0;
        double totalAmount = 0.0;
        
        for (int i = 0; i < table.getRowCount(); i++) {
            totalQty += (Integer) table.getValueAt(i, 6); // Qty
            totalAmount += (Double) table.getValueAt(i, 7); // Subtotal
        }
        
        lblTotalQty.setText("Total Quantity: " + totalQty);
        lblTotalAmount.setText(String.format("Total Amount: $%.2f", totalAmount));
    }
}