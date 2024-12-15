package gui;

import bus.CTHoaDonBUS;
import bus.HoaDonBUS;
import com.toedter.calendar.JDateChooser;
import dto.CTHoaDonDTO;
import dto.HoaDonDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class HoaDonGUI extends JPanel {

    private int width, height;
    private Color colorBackground = Color.decode("#FFFFFF");
    private Color color1 = Color.decode("#006270");
    private Color color2 = Color.decode("#009394");
    private Color color3 = Color.decode("#00E0C7");
    private HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private CTHoaDonBUS ctHoaDonBUS = new CTHoaDonBUS();

    private JPanel pnInfor, pnFilter, pnTable;
    private ArrayList<JLabel> arrLbInfor;

    private JTable table, tableCT;
    private TableRowSorter<TableModel> rowSorter;
    private DefaultTableModel model, modelCT;

    public HoaDonGUI(int width, int height) {
        this.width = width;
        this.height = height;
        this.init();
    }

    public void init() {
        this.setSize(this.width, this.height);
        this.setBackground(this.colorBackground);

        this.pnInfor = this.createPnInfor();
        this.pnFilter = this.createPnFilter();
        this.pnTable = this.createPnTable();

        this.setLayout(new BorderLayout());
        this.add(this.pnInfor, BorderLayout.NORTH);
        this.add(this.pnFilter, BorderLayout.CENTER);
        this.add(this.pnTable, BorderLayout.SOUTH);
    }

    public JPanel createPnInfor() {
        JPanel result = new JPanel(new FlowLayout(1, 0, 25));
        result.setPreferredSize(new Dimension(this.width, 300));

        // phần thông tin hóa đơn
        JPanel pn_infor = new JPanel(new FlowLayout(1, 5, 10));
        pn_infor.setPreferredSize(new Dimension(300, 250));
        pn_infor.setBorder(BorderFactory.createLineBorder(color1, 2));

        String[] thuoc_tinh = {
            "Mã hóa đơn: ", "Mã khách hàng: ", "Mã nhân viên: ", "Ngày xuất: ", "Tổng tiền: "
        };
        int len = thuoc_tinh.length;
        this.arrLbInfor = new ArrayList<>();

        Dimension d_tf = new Dimension(290, 30);
        Color color_font = this.color1;
        Font font_infor = new Font("Segoe UI", Font.PLAIN, 13);
        for (int i = 0; i < len; i++) {
            this.arrLbInfor.add(new JLabel(thuoc_tinh[i]));
            this.arrLbInfor.get(i).setPreferredSize(d_tf);
            this.arrLbInfor.get(i).setForeground(color_font);
            this.arrLbInfor.get(i).setFont(font_infor);
            pn_infor.add(this.arrLbInfor.get(i));
        }

        // phần thông tin chi tiết hóa đơn
        JPanel pn_table = new JPanel(new FlowLayout());

        String[] col = {
            "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá"
        };
        modelCT = new DefaultTableModel(col, 0);
        tableCT = new JTable();
        tableCT.setModel(modelCT);
        JScrollPane scroll = new JScrollPane(tableCT);
        scroll.setPreferredSize(new Dimension(600, 250));

        tableCT.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableCT.getColumnModel().getColumn(1).setPreferredWidth(70);
        tableCT.getColumnModel().getColumn(2).setPreferredWidth(10);
        tableCT.getColumnModel().getColumn(3).setPreferredWidth(40);

        pn_table.add(scroll);

        Font font_table = new Font("Segoe UI", Font.BOLD, 13);
        tableCT.getTableHeader().setBackground(color1);
        tableCT.getTableHeader().setFont(font_table);
        tableCT.getTableHeader().setForeground(this.colorBackground);
        tableCT.getTableHeader().setOpaque(false);
        tableCT.getTableHeader().setBorder(BorderFactory.createLineBorder(this.color1));

        // căn giữa các chữ trong ô
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < col.length; i++) {
            tableCT.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        tableCT.setFocusable(false);
        tableCT.setShowVerticalLines(false);
        tableCT.setIntercellSpacing(new Dimension(0, 0));
        tableCT.setFillsViewportHeight(true);
        tableCT.setSelectionBackground(color3);
        tableCT.setRowHeight(30);
        tableCT.setBorder(BorderFactory.createLineBorder(this.color1));

        result.add(pn_infor);
        result.add(pn_table);

        return result;
    }

    public JPanel createPnFilter() {
        JPanel pn_filter = new JPanel(new FlowLayout(1, 10, 20));

        Font font_filter = new Font("Segoe UI", Font.BOLD, 13);
        JLabel lb_tim_kiem = new JLabel("Tìm kiếm");
        lb_tim_kiem.setFont(font_filter);
        lb_tim_kiem.setForeground(color1);

        JPanel pn_tim_kiem = new JPanel(new FlowLayout(1, 0, 0));
        pn_tim_kiem.setPreferredSize(new Dimension(250, 30));
        JComboBox cb_tim_kiem = new JComboBox();
        cb_tim_kiem.setPreferredSize(new Dimension(140, 30));
        cb_tim_kiem.addItem("Mã hóa đơn");
        cb_tim_kiem.addItem("Mã khách");
        cb_tim_kiem.addItem("Mã nhân viên");
        cb_tim_kiem.setForeground(color1);
        cb_tim_kiem.setBackground(colorBackground);
        cb_tim_kiem.setFont(font_filter);

        JTextField tf_tim_kiem = new JTextField();
        tf_tim_kiem.setPreferredSize(new Dimension(100, 30));
        tf_tim_kiem.setFont(font_filter);
        tf_tim_kiem.setForeground(color1);

        tf_tim_kiem.getDocument().addDocumentListener(new DocumentListener() {
            private Timer timer = new Timer(1000, e -> handleSearch(tf_tim_kiem, cb_tim_kiem)); // Thời gian 1 giây

            @Override
            public void insertUpdate(DocumentEvent e) {
                restartTimer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                restartTimer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Không cần xử lý
            }

            private void restartTimer() {
                timer.stop(); // Dừng bộ đếm nếu nó đang chạy
                timer.start(); // Bắt đầu lại bộ đếm
            }
        });

        pn_tim_kiem.add(cb_tim_kiem);
        pn_tim_kiem.add(tf_tim_kiem);

        // lọc theo ngày
        JLabel lb_ngay = new JLabel("Ngày", JLabel.CENTER);
        lb_ngay.setFont(font_filter);
        lb_ngay.setForeground(color1);

        JDateChooser date_from = new JDateChooser();
        JDateChooser date_to = new JDateChooser();

        date_from.setPreferredSize(new Dimension(150, 30));
        date_to.setPreferredSize(new Dimension(150, 30));

        JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
        sep1.setPreferredSize(new Dimension(10, 40));
        JSeparator sep2 = new JSeparator(JSeparator.HORIZONTAL);
        sep2.setPreferredSize(new Dimension(20, 10));

        JButton btn_loc = new JButton("Lọc");
        btn_loc.setPreferredSize(new Dimension(100, 30));
        btn_loc.setBackground(color2);
        btn_loc.setFont(font_filter);
        btn_loc.setForeground(this.colorBackground);

        btn_loc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Date input1 = date_from.getDate();
                Date input2 = date_to.getDate();
                if (input1 == null && input2 == null) {
                    JOptionPane.showMessageDialog(null, "Không được để trống ngày lọc");
                    return;
                } else if (input1 == null) {
                    JOptionPane.showMessageDialog(null, "Không được để trống ngày bắt đầu");
                    return;
                } else if (input2 == null) {
                    JOptionPane.showMessageDialog(null, "Không được để trống ngày kết thúc");
                    return;
                } else if (input1 == null || input2 == null || input1.after(input2)) {
                    JOptionPane.showMessageDialog(null, "Ngày bắt đầu phải bé hơn hoặc bằng ngày kết thúc");
                    return;
                }
                LocalDate date1 = input1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate date2 = input2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                reloadHD(hoaDonBUS.filter(date1, date2));
            }
        });

        pn_filter.add(lb_tim_kiem);
        pn_filter.add(pn_tim_kiem);
        pn_filter.add(sep1);
        pn_filter.add(lb_ngay);
        pn_filter.add(date_from);
        pn_filter.add(sep2);
        pn_filter.add(date_to);
        pn_filter.add(btn_loc);

        return pn_filter;
    }

    private void handleSearch(JTextField tf_tim_kiem, JComboBox cb_tim_kiem) {
        String text = tf_tim_kiem.getText().trim();
        int choice = cb_tim_kiem.getSelectedIndex();

        if (text.length() < 4) {
            System.out.println("Định dạng không hợp lệ. Mã phải có ít nhất 4 ký tự.");
            rowSorter.setRowFilter(null);
            return;
        }

        boolean isValid = false;
        if (choice == 0) {
            isValid = text.toLowerCase().matches("^hd\\d+$"); // Định dạng: hd + số
            if (!isValid) {
                System.out.println("Định dạng không hợp lệ. Mã phải là 'hd' theo sau bởi số.");
            }
        } else if (choice == 1) {
            isValid = text.toLowerCase().matches("^kh\\d+$"); // Định dạng: kh + số
            if (!isValid) {
                System.out.println("Định dạng không hợp lệ. Mã phải là 'kh' theo sau bởi số.");
            }
        } else if (choice == 2) {
            isValid = text.toLowerCase().matches("^us\\d+$"); // Định dạng: us + số
            if (!isValid) {
                System.out.println("Định dạng không hợp lệ. Mã phải là 'us' theo sau bởi số.");
            }
        }

        if (isValid) {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, choice));
        } else {
            rowSorter.setRowFilter(null);
        }
    }

    public JPanel createPnTable() {
        JPanel pn_table = new JPanel();
        pn_table.setPreferredSize(new Dimension(this.width, 300));

        String[] col = {
            "Mã hóa đơn", "Mã khách hàng", "Mã nhân viên", "Ngày xuất", "Tổng tiền"
        };
        this.model = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(model);
        rowSorter = new TableRowSorter<TableModel>(model);
        this.table.setModel(model);
        this.table.setRowSorter(rowSorter);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(900, 250));

        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        this.loadHD();

        pn_table.add(scroll);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (table.getRowSorter() != null) {
                    row = table.getRowSorter().convertRowIndexToModel(row);
                }

                // set thông tin cho sản phẩm
                arrLbInfor.get(0).setText("Mã hóa đơn: " + table.getModel().getValueAt(row, 0).toString());
                arrLbInfor.get(1).setText("Mã khách hàng: " + table.getModel().getValueAt(row, 1).toString());
                arrLbInfor.get(2).setText("Mã nhân viên: " + table.getModel().getValueAt(row, 2).toString());
                arrLbInfor.get(3).setText("Ngày xuất: " + table.getModel().getValueAt(row, 3).toString());
                arrLbInfor.get(4).setText("Tổng tiền: " + table.getModel().getValueAt(row, 4).toString());

                loadCTHD(table.getModel().getValueAt(row, 0).toString());
            }
        });

        // giao diện table
        Font font_table = new Font("Segoe UI", Font.BOLD, 13);
        table.getTableHeader().setBackground(color1);
        table.getTableHeader().setFont(font_table);
        table.getTableHeader().setForeground(this.colorBackground);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(this.color1));

        // căn giữa các chữ trong ô
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < col.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setFocusable(false);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionBackground(color3);
        table.setRowHeight(30);
        table.setBorder(BorderFactory.createLineBorder(this.color1));
        return pn_table;
    }

    public void loadHD() {
        if (hoaDonBUS.getHdList() == null) {
            hoaDonBUS.list();
        }
        ArrayList<HoaDonDTO> hdList = hoaDonBUS.getHdList();
        model.setRowCount(0);
        reloadHD(hdList);
    }

    public void reloadHD(ArrayList<HoaDonDTO> hdList) {
        if (hdList.size() > 0) {
            model.setRowCount(0);
            for (HoaDonDTO hd : hdList) {
                model.addRow(new Object[]{
                    hd.getIdHoaDon(), hd.getIdKhachHang(), hd.getIdUser(), hd.getNgayXuat(), hd.getTongTien()
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không có hóa đơn trong khoảng thời gian này");
            return;
        }
    }

    public void loadCTHD(String id) {
        if (ctHoaDonBUS.getCthdList() == null) {
            ctHoaDonBUS.list();
        }
        ArrayList<CTHoaDonDTO> cthdList = ctHoaDonBUS.listId(id);
        modelCT.setRowCount(0);
        reloadCTHD(cthdList);
    }

    public void reloadCTHD(ArrayList<CTHoaDonDTO> cthdList) {
        modelCT.setRowCount(0);
        for (CTHoaDonDTO cthd : cthdList) {
            modelCT.addRow(new Object[]{
                cthd.getIdSanPham(), cthd.getTenSanPham(), cthd.getSoLuong(), cthd.getDonGia()
            });
        }
    }
}
