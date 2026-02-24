package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import model.Product;
import model.Sales;
import service.order.OrderServiceImpl;
import service.product.ProductServiceImpl;

public class ManagerPerformanceUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel model;
    
    private Sales currentManager;
    private OrderServiceImpl orderService = new OrderServiceImpl();
    private ProductServiceImpl productService = new ProductServiceImpl(); 

    // L'ÉCLAT 奢華配色
    private final Color GOLD = new Color(212, 175, 55);
    private final Color DARK_GREY = new Color(50, 50, 50);

    private JTextField txtStartDate, txtEndDate;
    private JComboBox<String> comboReportType;
    private JComboBox<String> comboSpecificItem; 
    private JScrollPane scrollPane;
    private JLabel lblStoreTotal;

    public ManagerPerformanceUI(Sales manager) {
        this.currentManager = manager;
        
        setTitle("L'ÉCLAT - Executive Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 680);
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // --- 1. Header ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(GOLD);
        headerPanel.setBounds(0, 0, 950, 60);
        contentPane.add(headerPanel);
        headerPanel.setLayout(null);

        JLabel lblTitle = new JLabel("COMPANY LEADERBOARD (EXECUTIVE VIEW)");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Didot", Font.BOLD, 22));
        lblTitle.setBounds(20, 15, 550, 30);
        headerPanel.add(lblTitle);

        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> {
            new EmployeeMainUI(currentManager).setVisible(true);
            dispose();
        });
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(DARK_GREY);
        btnBack.setBounds(760, 15, 150, 30);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnBack);

        // --- 2. 控制面板 ---
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(250, 250, 250));
        controlPanel.setBorder(new LineBorder(new Color(230, 230, 230)));
        controlPanel.setBounds(20, 75, 895, 90);
        controlPanel.setLayout(null);
        contentPane.add(controlPanel);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endD = sdf.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String startD = sdf.format(cal.getTime());

        JLabel lblStart = new JLabel("From Date:");
        lblStart.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblStart.setBounds(15, 15, 80, 20);
        controlPanel.add(lblStart);

        txtStartDate = new JTextField(startD);
        txtStartDate.setEditable(false);
        txtStartDate.setBackground(Color.WHITE);
        txtStartDate.setBounds(85, 10, 90, 30);
        controlPanel.add(txtStartDate);

        JButton btnCalStart = new JButton("📅");
        btnCalStart.setBounds(180, 10, 45, 30);
        btnCalStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalStart.addActionListener(e -> {
            String date = new DatePicker(this).setPickedDate();
            if (!date.equals("")) txtStartDate.setText(date);
        });
        controlPanel.add(btnCalStart);

        JLabel lblEnd = new JLabel("To Date:");
        lblEnd.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblEnd.setBounds(240, 15, 60, 20);
        controlPanel.add(lblEnd);

        txtEndDate = new JTextField(endD);
        txtEndDate.setEditable(false);
        txtEndDate.setBackground(Color.WHITE);
        txtEndDate.setBounds(295, 10, 90, 30);
        controlPanel.add(txtEndDate);

        JButton btnCalEnd = new JButton("📅");
        btnCalEnd.setBounds(390, 10, 45, 30);
        btnCalEnd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCalEnd.addActionListener(e -> {
            String date = new DatePicker(this).setPickedDate();
            if (!date.equals("")) txtEndDate.setText(date);
        });
        controlPanel.add(btnCalEnd);

        JLabel lblType = new JLabel("Dimension:");
        lblType.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblType.setBounds(15, 55, 80, 20);
        controlPanel.add(lblType);

        String[] types = {"Overall Performance", "By Category Breakdown", "By Product Breakdown"};
        comboReportType = new JComboBox<>(types);
        comboReportType.setBounds(85, 50, 180, 30);
        controlPanel.add(comboReportType);

        JLabel lblFilter = new JLabel("Filter:");
        lblFilter.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblFilter.setBounds(280, 55, 40, 20);
        controlPanel.add(lblFilter);

        comboSpecificItem = new JComboBox<>(new String[]{"--- N/A ---"});
        comboSpecificItem.setBounds(325, 50, 250, 30);
        comboSpecificItem.setEnabled(false);
        controlPanel.add(comboSpecificItem);

        comboReportType.addActionListener(e -> updateSpecificItemDropdown());

        JButton btnGenerate = new JButton("G E N E R A T E   R E P O R T");
        btnGenerate.setBackground(DARK_GREY);
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnGenerate.setBounds(660, 50, 210, 30);
        btnGenerate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerate.addActionListener(e -> generateReport());
        controlPanel.add(btnGenerate);

        // --- 3. 報表顯示區 ---
        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 180, 895, 390);
        scrollPane.setBorder(new LineBorder(new Color(230,230,230)));
        contentPane.add(scrollPane);
        
        // --- 4. 底部全店總計 ---
        lblStoreTotal = new JLabel("Filtered Data Revenue: $0.00");
        lblStoreTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblStoreTotal.setForeground(DARK_GREY);
        lblStoreTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblStoreTotal.setBounds(465, 580, 450, 30);
        contentPane.add(lblStoreTotal);
        
        generateReport(); 
    }

    private void updateSpecificItemDropdown() {
        comboSpecificItem.removeAllItems();
        int type = comboReportType.getSelectedIndex();
        
        if (type == 0) {
            comboSpecificItem.addItem("--- N/A ---");
            comboSpecificItem.setEnabled(false);
            return;
        }
        
        comboSpecificItem.setEnabled(true);
        List<Product> products = productService.getAllProducts();
        Set<String> items = new TreeSet<>(); 
        
        if (type == 1) { 
            comboSpecificItem.addItem("--- All Categories ---");
            for (Product p : products) items.add(p.getCategoryName());
        } else if (type == 2) { 
            comboSpecificItem.addItem("--- All Products ---");
            for (Product p : products) items.add(p.getProductName());
        }
        
        for (String item : items) {
            if (item != null && !item.isEmpty()) comboSpecificItem.addItem(item);
        }
    }

    private void generateReport() {
        String start = txtStartDate.getText();
        String end = txtEndDate.getText();
        int type = comboReportType.getSelectedIndex();
        
        String specificItem = null;
        if (comboSpecificItem.getSelectedItem() != null) {
            specificItem = comboSpecificItem.getSelectedItem().toString();
        }
        
        List<Object[]> data = orderService.getAdvancedManagerReport(start, end, type, specificItem);
        
        String[] columns;
        if (type == 0) {
            columns = new String[]{"Sales Name", "Total Orders", "Total Revenue", "Avg Order Value (AOV)"};
        } else {
            // 【關鍵修改 1】把產品/分類名稱放在第一位，業務員名稱放第二位！
            columns = new String[]{(type == 1 ? "Category Name" : "Product Name"), "Sales Name", "Quantity Sold", "Revenue Generated"};
        }
        
        model = new DefaultTableModel(null, columns) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
            public Class<?> getColumnClass(int columnIndex) {
                if (type == 0) {
                    if (columnIndex == 1) return Integer.class; 
                    if (columnIndex == 2 || columnIndex == 3) return Double.class;  
                } else {
                    // 第 0 跟第 1 欄都是文字，所以第 2 跟第 3 欄的數字型態設定不用變
                    if (columnIndex == 2) return Integer.class; 
                    if (columnIndex == 3) return Double.class;  
                }
                return String.class;
            }
        };
        
        table = new JTable(model);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.addRowSorterListener(e -> table.getTableHeader().repaint());
        
        styleTable(table);
        
        CurrencyRenderer currencyRenderer = new CurrencyRenderer();
        if (type == 0) {
            table.getColumnModel().getColumn(2).setCellRenderer(currencyRenderer);
            table.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer);
        } else {
            table.getColumnModel().getColumn(3).setCellRenderer(currencyRenderer);
        }
        
        double storeTotal = 0;
        int storeOrders = 0;

        for(Object[] row : data) {
            if (type == 0) {
                model.addRow(new Object[]{ row[0], row[1], row[2], row[3] });
                storeTotal += (Double) row[2];
                storeOrders += (Integer) row[1];
            } else {
                // 【關鍵修改 2】將資料庫撈出來的 row[1](產品名) 放第一欄，row[0](業務名) 放第二欄！
                model.addRow(new Object[]{ row[1], row[0], row[2], row[3] });
                storeTotal += (Double) row[3];
            }
        }
        
        scrollPane.setViewportView(table);
        
        if (type == 0) {
            lblStoreTotal.setText(String.format("Filtered Data Revenue: $%,.2f   |   Total Orders: %d", storeTotal, storeOrders));
        } else {
            lblStoreTotal.setText(String.format("Filtered Data Revenue: $%,.2f", storeTotal));
        }
    }

    // ==========================================
    // 霸氣大尺寸精品動態表頭
    // ==========================================
    private void styleTable(JTable table) {
        table.setRowHeight(45); 
        table.setFont(new Font("SansSerif", Font.PLAIN, 15));
        table.setGridColor(new Color(240, 240, 240));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(DARK_GREY);
        header.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        header.setPreferredSize(new Dimension(0, 60)); 
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, GOLD));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(DARK_GREY);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("SansSerif", Font.BOLD, 16)); 
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(70, 70, 70))); 
                
                if (table.getRowSorter() != null) {
                    List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
                    if (!sortKeys.isEmpty() && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                        SortOrder order = sortKeys.get(0).getSortOrder();
                        String arrow = (order == SortOrder.ASCENDING) ? "▲" : "▼";
                        label.setText("<html><font color='#D4AF37'>" + value.toString() + " &nbsp;<font size='+2'>" + arrow + "</font></font></html>");
                    } else {
                        label.setText("<html><font color='#FFFFFF'>" + value.toString() + " &nbsp;<font color='#888888' size='+1'>↕</font></font></html>"); 
                    }
                } else {
                    label.setText(value.toString());
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        };
        
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    private class CurrencyRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public CurrencyRenderer() { setHorizontalAlignment(SwingConstants.RIGHT); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Double) setText(String.format("$%,.2f", (Double) value));
            return this;
        }
    }

    // ==========================================
    // 輕量級月曆元件
    // ==========================================
    class DatePicker {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        JLabel l = new JLabel("", JLabel.CENTER);
        String day = "";
        JDialog d;
        JButton[] button = new JButton[49];

        public DatePicker(JFrame parent) {
            d = new JDialog(parent, "Select Date", true);
            d.setModal(true);
            String[] header = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
            JPanel p1 = new JPanel(new GridLayout(7, 7));
            p1.setPreferredSize(new Dimension(430, 200));

            for (int i = 0; i < button.length; i++) {
                final int selection = i;
                button[i] = new JButton();
                button[i].setFocusPainted(false);
                button[i].setBackground(Color.WHITE);
                if (i > 6) {
                    button[i].addActionListener(e -> {
                        day = button[selection].getActionCommand();
                        d.dispose();
                    });
                }
                if (i < 7) {
                    button[i].setText(header[i]);
                    button[i].setForeground(GOLD);
                    button[i].setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                p1.add(button[i]);
            }
            JPanel p2 = new JPanel(new GridLayout(1, 3));
            JButton previous = new JButton("<< Prev");
            previous.addActionListener(e -> { month--; displayDate(); });
            p2.add(previous);
            l.setFont(new Font("SansSerif", Font.BOLD, 14));
            p2.add(l);
            JButton next = new JButton("Next >>");
            next.addActionListener(e -> { month++; displayDate(); });
            p2.add(next);
            d.add(p1, BorderLayout.CENTER);
            d.add(p2, BorderLayout.SOUTH);
            d.pack();
            d.setLocationRelativeTo(parent);
            displayDate();
            d.setVisible(true);
        }
        public void displayDate() {
            for (int x = 7; x < button.length; x++) button[x].setText("");
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, 1);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 6 + dayOfWeek, day = 1; day <= daysInMonth; i++, day++) button[i].setText("" + day);
            l.setText(sdf.format(cal.getTime()));
            d.setTitle("L'ÉCLAT Calendar");
        }
        public String setPickedDate() {
            if (day.equals("")) return day;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, Integer.parseInt(day));
            return sdf.format(cal.getTime());
        }
    }
}