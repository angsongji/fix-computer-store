package gui;

import bus.CTQuyenChucNangBUS;
import bus.QuyenBUS;
import dto.CTQuyenChucNangDTO;
import dto.QuyenDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class QuyenGUI extends JPanel {
    private int width, height;
    private Color colorBackground = Color.decode("#FFFFFF");
    private Color color1 = Color.decode("#006270");
    private Color color2 = Color.decode("#009394");
    private Color color3 = Color.decode("#00E0C7");

    private JPanel pnInfor, pnFilter, pnTable;
    private ArrayList<JPanel> arrPnCheckBox;
    private ArrayList<JLabel> arrLbCheckBox;
    private ArrayList<JPanel> arrPnInfor;
    private ArrayList<JLabel> arrLbInfor;
    private ArrayList<JTextField> arrTfInfor;
    private ArrayList<JCheckBox> arrCbXem;
    private ArrayList<JCheckBox> arrCbThem;
    private ArrayList<JCheckBox> arrCbSua;
    private ArrayList<JCheckBox> arrCbXoa;
    private JButton btnThem, btnSua, btnXoa;
        
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    private DefaultTableModel model;    
    private boolean isEditing = false;
    private QuyenBUS quyenBUS = new QuyenBUS();
    private CTQuyenChucNangBUS ctBUS = new CTQuyenChucNangBUS();

    
    private boolean quyenThem, quyenSua, quyenXoa;
    
    public QuyenGUI(int width, int height, boolean quyenThem, boolean quyenSua, boolean quyenXoa) {
        this.width = width;
        this.height = height;
        this.quyenThem = quyenThem;
        this.quyenSua = quyenSua;
        this.quyenXoa = quyenXoa;
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
        this.arrPnCheckBox = new ArrayList<>();
        this.arrLbCheckBox = new ArrayList<>();
        this.arrPnInfor = new ArrayList<>();
        this.arrLbInfor = new ArrayList<>();
        this.arrTfInfor = new ArrayList<>();
        this.arrCbXem = new ArrayList<>();
        this.arrCbThem = new ArrayList<>();
        this.arrCbSua = new ArrayList<>();
        this.arrCbXoa = new ArrayList<>();
        
        Dimension d_pn = new Dimension(300, 18);
        Dimension d_lb = new Dimension(100, 15);
        Dimension d_tf = new Dimension(50, 15);
        
        Dimension d_pn1 = new Dimension(330, 30);
        Dimension d_lb1 = new Dimension(100, 30);
        Dimension d_tf1 = new Dimension(200, 30);
        Color color_font = this.color1;
        Font font_infor = new Font("Segoe UI", Font.PLAIN, 14);
        Font font_filter = new Font("Segoe UI", Font.BOLD, 13);
        
        JPanel result = new JPanel(new FlowLayout(1, 0, 25));
        result.setPreferredSize(new Dimension(this.width, 290));
        
        JPanel pn_infor = new JPanel(new BorderLayout());
        pn_infor.setPreferredSize(new Dimension(this.width - 100, 250));
        pn_infor.setBackground(Color.white);
        pn_infor.setBorder(BorderFactory.createLineBorder(color1, 2));
        
        // create panel infor
        JPanel pn_info = new JPanel(new FlowLayout(1, 0, 25));
        pn_info.setPreferredSize(new Dimension(250, 250));
        
        String[] thuoc_tinh = {"Mã quyền", "Tên Quyền"};
        int len = thuoc_tinh.length;
        
        for (int i = 0; i < len; i++) {
            this.arrPnInfor.add(new JPanel(new FlowLayout(0, 0, 0)));
            this.arrPnInfor.get(i).setPreferredSize(d_pn1);
            
            this.arrLbInfor.add(new JLabel(thuoc_tinh[i]));
            this.arrLbInfor.get(i).setPreferredSize(d_lb1);
            this.arrTfInfor.add(new JTextField());
            this.arrTfInfor.get(i).setPreferredSize(d_tf1);

            this.arrLbInfor.get(i).setForeground(color_font);
            this.arrLbInfor.get(i).setFont(font_infor);
            this.arrTfInfor.get(i).setForeground(color_font);
            this.arrTfInfor.get(i).setFont(font_infor);
            
            this.arrPnInfor.get(i).add(this.arrLbInfor.get(i));
            this.arrPnInfor.get(i).add(this.arrTfInfor.get(i));
            pn_info.add(this.arrPnInfor.get(i));
        }
        this.arrTfInfor.get(0).setEditable(false);
        
        // create panel chon quyen
        JPanel pn_desc = new JPanel(new FlowLayout(1, 5, 4));
        pn_desc.setPreferredSize(new Dimension(350, 250));
        
        JPanel pn_quyen = new JPanel(new FlowLayout(0, 0, 0));
        pn_quyen.setPreferredSize(d_pn);
        pn_quyen.setForeground(color_font);
        pn_quyen.setFont(font_filter);
        
        JLabel lb_quyen = new JLabel("Quyền");
        lb_quyen.setPreferredSize(d_lb);
        lb_quyen.setForeground(color_font);
        lb_quyen.setFont(font_filter);
        
        JLabel lb_xem = new JLabel("Xem");
        lb_xem.setPreferredSize(d_tf);
        lb_xem.setForeground(color_font);
        lb_xem.setFont(font_filter);
        
        JLabel lb_them = new JLabel("Thêm");
        lb_them.setPreferredSize(d_tf);
        lb_them.setForeground(color_font);
        lb_them.setFont(font_filter);
        
        JLabel lb_sua = new JLabel("Sửa");
        lb_sua.setPreferredSize(d_tf);
        lb_sua.setForeground(color_font);
        lb_sua.setFont(font_filter);
        
        JLabel lb_xoa = new JLabel("Xóa");
        lb_xoa.setPreferredSize(d_tf);
        lb_xoa.setForeground(color_font);
        lb_xoa.setFont(font_filter);
        
        pn_quyen.add(lb_quyen);
        pn_quyen.add(lb_xem);
        pn_quyen.add(lb_them);
        pn_quyen.add(lb_sua);
        pn_quyen.add(lb_xoa);
                        
        pn_desc.add(pn_quyen);

        String[] quyen = {
            "Nhân viên", "Khách hàng", "Nhà cung cấp", "Sản phẩm", "Quyền", "Bán hàng", "Nhập hàng", "Hóa đơn", "Phiếu nhập", "Bảo hành"
        };
        int len_quyen = quyen.length;
      
        for (int i = 0; i < len_quyen; i++) {
            this.arrPnCheckBox.add(new JPanel(new FlowLayout(0, 0, 0)));
            this.arrPnCheckBox.get(i).setPreferredSize(d_pn);
            
            this.arrLbCheckBox.add(new JLabel(quyen[i]));
            this.arrLbCheckBox.get(i).setPreferredSize(d_lb);
            
            this.arrCbXem.add(new JCheckBox());
            this.arrCbXem.get(i).setPreferredSize(d_tf);
            
            this.arrCbThem.add(new JCheckBox());
            this.arrCbThem.get(i).setPreferredSize(d_tf);
            
            this.arrCbSua.add(new JCheckBox());
            this.arrCbSua.get(i).setPreferredSize(d_tf);
            
            this.arrCbXoa.add(new JCheckBox());
            this.arrCbXoa.get(i).setPreferredSize(d_tf);
            
            this.arrCbXem.get(i).setEnabled(false);
            this.arrCbThem.get(i).setEnabled(false);
            this.arrCbSua.get(i).setEnabled(false);
            this.arrCbXoa.get(i).setEnabled(false);

            this.arrLbCheckBox.get(i).setForeground(color_font);
            this.arrLbCheckBox.get(i).setFont(font_infor);
            
            this.arrPnCheckBox.get(i).add(this.arrLbCheckBox.get(i));
            this.arrPnCheckBox.get(i).add(this.arrCbXem.get(i));
            this.arrPnCheckBox.get(i).add(this.arrCbThem.get(i));
            this.arrPnCheckBox.get(i).add(this.arrCbSua.get(i));
            this.arrPnCheckBox.get(i).add(this.arrCbXoa.get(i));
            
            pn_desc.add(this.arrPnCheckBox.get(i));
        }
        
        // create panel button
        JPanel pn_btn = new JPanel(new FlowLayout(1, 25, 10));
        pn_btn.setPreferredSize(new Dimension(200, 250));
        
        // các nút chức năng mặc định
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        
        showCN();
         
        // các nút chức năng phụ
        JButton btn_hoan_thanh = new JButton("Hoàn thành");
        JButton btn_tro_ve = new JButton("Trở về");
        
        // Thiết kế giao diện nút
        Dimension d_btn = new Dimension(150, 30);
        btnThem.setPreferredSize(d_btn);
        btnSua.setPreferredSize(d_btn);
        btnXoa.setPreferredSize(d_btn);
        
        btn_hoan_thanh.setPreferredSize(d_btn);
        btn_tro_ve.setPreferredSize(d_btn);
        btn_hoan_thanh.setVisible(false);
        btn_tro_ve.setVisible(false);
        
        Color color_button = this.color2;
        btnThem.setBackground(color_button);
        btnSua.setBackground(color_button);
        btnXoa.setBackground(color_button);

        btn_hoan_thanh.setBackground(color_button);
        btn_tro_ve.setBackground(color_button);
        
        Color color_font_btn = this.colorBackground;
        btnThem.setForeground(color_font_btn);
        btnSua.setForeground(color_font_btn);
        btnXoa.setForeground(color_font_btn);
        
        btn_hoan_thanh.setForeground(color_font_btn);
        btn_tro_ve.setForeground(color_font_btn);
        
        Font font_btn = new Font("Segoe UI", Font.BOLD, 13);
        btnThem.setFont(font_btn);
        btnSua.setFont(font_btn);
        btnXoa.setFont(font_btn);
        
        btn_hoan_thanh.setFont(font_btn);
        btn_tro_ve.setFont(font_btn);
        
        // thêm các nút
        pn_btn.add(btnThem);
        pn_btn.add(btnSua);
        pn_btn.add(btnXoa);
        pn_btn.add(btn_hoan_thanh);
        pn_btn.add(btn_tro_ve);
        
        // khi ấn nút thêm
        btnThem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                arrTfInfor.get(0).setText(quyenBUS.createNewId());
                
                isEditing = false;
                lockInfor(false);
                blankInfor();
                                
                btnThem.setVisible(false);
                btnSua.setVisible(false);
                btnXoa.setVisible(false);
                
                btn_hoan_thanh.setVisible(true);
                btn_tro_ve.setVisible(true);
                
                table.clearSelection();
                table.setEnabled(false);
            }
        });
        
        // khi ấn nút sửa
        btnSua.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (arrTfInfor.get(0).getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn quyền cần sửa!");
                    return;
                }
//                else if (arrTfInfor.get(0).getText().equals("QU001")) {
//                    JOptionPane.showMessageDialog(null, "Không được phép sửa quyền admin!");
//                    return;
//                }
                isEditing = true;
                
                lockInfor(false);
                
                btnThem.setVisible(false);
                btnSua.setVisible(false);
                btnXoa.setVisible(false);
                
                btn_hoan_thanh.setVisible(true);
                btn_tro_ve.setVisible(true);
                
                table.setEnabled(false);
            }
        });
        
        // khi ấn nút xóa
        btnXoa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (arrTfInfor.get(0).getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn quyền cần xóa!");
                    return;
                }
                
                int confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận xóa", "Alert", JOptionPane.YES_NO_OPTION);
                if (confirmed == 0) { // xác nhận xóa
                    quyenBUS.deleteQuyen(arrTfInfor.get(0).getText());
                    table.clearSelection();
                    reloadQuyen(quyenBUS.getQuyenList());
                    blankInfor();
                }
            }
        });
        
        // khi ấn nút hoàn thành
        btn_hoan_thanh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isEditing) { // đang trong chế độ sửa
                    String id = arrTfInfor.get(0).getText();
                    String ten = arrTfInfor.get(1).getText();
                    
                    // Validate tên quyền
                    String validateResult = validateTenQuyen(ten, true);
                    if (!validateResult.equals("done")) {
                        JOptionPane.showMessageDialog(null, validateResult, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        arrTfInfor.get(1).requestFocus();
                        return;
                    }
                    
                    int confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận sửa quyền", "", JOptionPane.YES_NO_OPTION);
                    if (confirmed == 0) { // xác nhận sửa
                        QuyenDTO quyen = new QuyenDTO(id, ten, true);
                        quyenBUS.updateQuyen(quyen);
                        reloadQuyen(quyenBUS.getQuyenList());
                        
                        ArrayList<String> arr_cn = getArrIdCN();
                        updateCTQCN(arr_cn);
                        
                        JOptionPane.showMessageDialog(null, "Sửa thành công", "OK", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else { // đang trong chế độ thêm
                    String id = arrTfInfor.get(0).getText();
                    String ten = arrTfInfor.get(1).getText();
                    
                    if (ten.trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "Tên không được để trống!");
                        return;
                    }

                    String validateResult = validateTenQuyen(ten, false);
                    if (!validateResult.equals("done")) {
                        JOptionPane.showMessageDialog(null, validateResult, "Lỗi", JOptionPane.ERROR_MESSAGE);
                        arrTfInfor.get(1).requestFocus();
                        return;
                    }
                    
                    int confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận thêm quyền", "", JOptionPane.YES_NO_OPTION);
                    if (confirmed == 0) { // xác nhận thêm
                        QuyenDTO quyen = new QuyenDTO(id, ten, true);
                        quyenBUS.addQuyen(quyen);
                        
                        ArrayList<String> arr_cn = getArrIdCN();
                        updateCTQCN(arr_cn);
                        reloadCTChucNang(arr_cn);
                  
                        reloadQuyen(quyenBUS.getQuyenList());
                        blankInfor();
                        lockInfor(true);
                        btnThem.setVisible(true);
                        btnSua.setVisible(true);
                        btnXoa.setVisible(true);

                        btn_hoan_thanh.setVisible(false);
                        btn_tro_ve.setVisible(false);
                        
                        table.setEnabled(true);
                    }
                }
            }
        });
        
        // khi ấn nút trở về
        btn_tro_ve.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                blankInfor();
                
                // nếu đang trong chế độ sửa khi thoát ra chỉnh isEditing = false
                if (isEditing) isEditing = false;
                
                btnThem.setVisible(true);
                btnSua.setVisible(true);
                btnXoa.setVisible(true);
                
                showCN();
                
                btn_hoan_thanh.setVisible(false);
                btn_tro_ve.setVisible(false);
                
                table.setEnabled(true);
            }
        });
        
        // add components
        pn_infor.add(pn_info, BorderLayout.CENTER);
        pn_infor.add(pn_desc, BorderLayout.WEST);
        pn_infor.add(pn_btn, BorderLayout.EAST);
        
        result.add(pn_infor);
        
        return result;
    }
    
    public JPanel createPnFilter() {
        JPanel pn_filter = new JPanel(new FlowLayout(1, 20, 20)); 
        
        // Thanh tìm kiếm theo tên hoặc id nhân viên
        JLabel lb_tim_kiem = new JLabel("Tìm kiếm", JLabel.CENTER);
        JTextField tf_tim_kiem = new JTextField();
        tf_tim_kiem.setPreferredSize(new Dimension(250, 30));
        tf_tim_kiem.getDocument().addDocumentListener(new DocumentListener() { 
            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = tf_tim_kiem.getText();
                if (txt.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                }
                else if (txt.trim().length() >= 2 && txt.trim().substring(0, 2).toUpperCase().equals("QU")) {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^" + txt, 0));                    
                } 
                else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^"+ txt +".*", 1));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String txt = tf_tim_kiem.getText();
                if (txt.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                }
                else if (txt.trim().length() >= 2 && txt.trim().substring(0, 2).toUpperCase().equals("NV")) {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^" + txt, 0));                    
                } 
                else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^"+ txt +".*", 1));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               
            }
        });
        
        Font font_filter = new Font("Segoe UI", Font.BOLD, 15);
        
        lb_tim_kiem.setFont(font_filter);
        tf_tim_kiem.setFont(font_filter);
        
        lb_tim_kiem.setForeground(color1);
        tf_tim_kiem.setForeground(color1);
        
        pn_filter.add(lb_tim_kiem);
        pn_filter.add(tf_tim_kiem);
        
        return pn_filter;
    }
    
    public JPanel createPnTable() {
        JPanel pn_table = new JPanel(new FlowLayout(1, 0, 0));
        pn_table.setPreferredSize(new Dimension(this.width, 290));
        
        String[] col = {
            "Mã quyền", "Tên quyền"
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
        
        table.getColumnModel().getColumn(0).setPreferredWidth(450);
        table.getColumnModel().getColumn(1).setPreferredWidth(450);
        
        this.loadQuyen();
        
        pn_table.add(scroll);
        
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (table.getRowSorter() != null) {
                    row = table.getRowSorter().convertRowIndexToModel(row);
                }
                
                // set thông tin cho sản phẩm
                arrTfInfor.get(0).setText(table.getModel().getValueAt(row, 0).toString());
                arrTfInfor.get(1).setText(table.getModel().getValueAt(row, 1).toString());
                
                loadCTChucNang(table.getModel().getValueAt(row, 0).toString());
                
                if (isEditing) {
                    lockInfor(false);
                }
                else lockInfor(true);
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
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
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
    
    public void loadQuyen() {
        if (quyenBUS.getQuyenList() == null) {
            quyenBUS.list();
        }
        ArrayList<QuyenDTO> quyenList = quyenBUS.getQuyenList();
        model.setRowCount(0);
        reloadQuyen(quyenList);
    }
    
    public void reloadQuyen(ArrayList<QuyenDTO> quyenList) {
        model.setRowCount(0);
        for (QuyenDTO quyen : quyenList) {
            if (quyen.isEnable()) {
                model.addRow(new Object[]{
                    quyen.getIdQuyen(), quyen.getTenQuyen()
                });
            }
        }
    }
    
    public void loadCTChucNang(String id) {
        if (ctBUS.getCtList() == null) {
            ctBUS.list();
        }
        ArrayList<String> arr_cn = ctBUS.listId(id);
        reloadCTChucNang(arr_cn);
    }
    
    public void reloadCTChucNang(ArrayList<String> arr_cn) {
        Map<Integer, ArrayList<Integer>> map_cn = new HashMap<>();
        ArrayList<Integer> arr_key = new ArrayList<>();
        
        for (int i = 0; i < this.arrCbXem.size(); i++) {
            this.arrCbXem.get(i).setSelected(false);
            this.arrCbThem.get(i).setSelected(false);
            this.arrCbSua.get(i).setSelected(false);
            this.arrCbXoa.get(i).setSelected(false);
        }
        
        for (int i = 0; i < arr_cn.size(); i++) {
            int id_cn = Integer.parseInt(arr_cn.get(i).charAt(0)+"");
            arr_key.add(id_cn);
        }
        
        
        for (Integer key : arr_key) {
            ArrayList<Integer> arr_value = new ArrayList<>();
            for (int i = 0; i < arr_cn.size(); i++) {
                int id_cn = Integer.parseInt(arr_cn.get(i).charAt(0)+"");
                int id_loai = Integer.parseInt(arr_cn.get(i).charAt(1)+"");
                if (key.equals(id_cn)) {
                    arr_value.add(id_loai);
                }
            }
            map_cn.put(key, arr_value);
        }
        
        
        for (Map.Entry<Integer, ArrayList<Integer>> me : map_cn.entrySet()) {
            switch (me.getKey()) {
                case 0:
                    for (int i = 0; i < me.getValue().size(); i++) {
                        this.arrCbXem.get(me.getValue().get(i)).setSelected(true);
                    }
                    break;
                case 1:
                    for (int i = 0; i < me.getValue().size(); i++) {
                        this.arrCbThem.get(me.getValue().get(i)).setSelected(true);
                    }
                    break;
                case 2:
                    for (int i = 0; i < me.getValue().size(); i++) {
                        this.arrCbSua.get(me.getValue().get(i)).setSelected(true);
                    }
                    break;
                case 3:
                    for (int i = 0; i < me.getValue().size(); i++) {
                        this.arrCbXoa.get(me.getValue().get(i)).setSelected(true);
                    }
                    break;
            }
        }
    }
    
    public ArrayList<String> getArrIdCN() {
        ArrayList<String> arr_cn = new ArrayList<>();
        String id_cn = "";
        
        for (int i = 0; i < this.arrCbXem.size(); i++) {
            if (this.arrCbXem.get(i).isSelected()) {
                id_cn = "0" + i;
                arr_cn.add(id_cn);
            }
        }
        for (int i = 0; i < this.arrCbThem.size(); i++) {
            if (this.arrCbThem.get(i).isSelected()) {
                id_cn = "1" + i;
                arr_cn.add(id_cn);
            }
        }
        for (int i = 0; i < this.arrCbSua.size(); i++) {
            if (this.arrCbSua.get(i).isSelected()) {
                id_cn = "2" + i;
                arr_cn.add(id_cn);
            }
        }
        for (int i = 0; i < this.arrCbXoa.size(); i++) {
            if (this.arrCbXoa.get(i).isSelected()) {
                id_cn = "3" + i;
                arr_cn.add(id_cn);
            }
        }
        
        return arr_cn;
    }
    
    public void updateCTQCN(ArrayList<String> arr_cn) {
        String id_quyen = this.arrTfInfor.get(0).getText();
        ArrayList<String> all_cn = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 9; j++) {
                all_cn.add(i+""+j);
            }
        }
        for (String cn : all_cn) {
            ctBUS.deleteCTQCN(id_quyen, cn);
        }
        for (String cn : arr_cn) {
            CTQuyenChucNangDTO ct = new CTQuyenChucNangDTO(id_quyen, cn);
            ctBUS.addCTQCN(ct);
        }
    }
    
    // khóa khả năng thao tác với thông tin
    public void lockInfor(boolean lock) {
        arrTfInfor.get(1).setEditable(!lock);
        for (int i = 0; i < this.arrCbXem.size(); i++) {
            this.arrCbXem.get(i).setEnabled(!lock);
            this.arrCbThem.get(i).setEnabled(!lock);
            this.arrCbSua.get(i).setEnabled(!lock);
            this.arrCbXoa.get(i).setEnabled(!lock);
             
        }
        
        this.arrCbThem.get(5).setEnabled(false);
        this.arrCbThem.get(6).setEnabled(false);
        this.arrCbThem.get(7).setEnabled(false);
        this.arrCbThem.get(8).setEnabled(false);
        this.arrCbThem.get(9).setEnabled(false);
        
        this.arrCbSua.get(3).setEnabled(false);
        this.arrCbSua.get(5).setEnabled(false);
        this.arrCbSua.get(6).setEnabled(false);
        this.arrCbSua.get(7).setEnabled(false);
        this.arrCbSua.get(8).setEnabled(false);
        this.arrCbSua.get(9).setEnabled(false);

        this.arrCbXoa.get(1).setEnabled(false);
        this.arrCbXoa.get(5).setEnabled(false);
        this.arrCbXoa.get(6).setEnabled(false);
        this.arrCbXoa.get(7).setEnabled(false);
        this.arrCbXoa.get(8).setEnabled(false);
        this.arrCbXoa.get(9).setEnabled(false);

    }
    
    public void blankInfor() {
        arrTfInfor.get(1).setText("");
        for (int i = 0; i < this.arrCbXem.size(); i++) {
            this.arrCbXem.get(i).setSelected(false);
            this.arrCbThem.get(i).setSelected(false);
            this.arrCbSua.get(i).setSelected(false);
            this.arrCbXoa.get(i).setSelected(false);
        }
    }
    
    public void showCN() {
        this.btnThem.setVisible(quyenThem);
        this.btnSua.setVisible(quyenSua);
        this.btnXoa.setVisible(quyenXoa);
    }

    private String validateTenQuyen(String tenQuyen, boolean isEditing) {
        // Kiểm tra độ dài
        if (tenQuyen.length() < 5) {
            return "Tên quyền phải có ít nhất 5 ký tự!";
        }
        if (tenQuyen.length() > 50) {
            return "Tên quyền không được vượt quá 50 ký tự!";
        }

        // Kiểm tra ký tự đặc biệt và số (chỉ cho phép chữ cái và khoảng trắng)
        if (!tenQuyen.matches("^[a-zA-Z\\p{L}\\s]+$")) {
            return "Tên quyền không được chứa ký tự đặc biệt hoặc số!";
        }

        // Kiểm tra khoảng trắng
        if (tenQuyen.startsWith(" ") || tenQuyen.endsWith(" ")) {
            return "Tên quyền không được bắt đầu hoặc kết thúc bằng khoảng trắng!";
        }
        if (tenQuyen.contains("  ")) {
            return "Tên quyền chỉ chứa một khoảng trắng giữa hai kí tự!";
        }

        // Kiểm tra trùng tên
        for (QuyenDTO quyen : quyenBUS.getQuyenList()) {
            if (quyen.isEnable() && quyen.getTenQuyen().equalsIgnoreCase(tenQuyen)) {
                if (isEditing && quyen.getIdQuyen().equals(arrTfInfor.get(0).getText())) {
                    continue;
                }
                return "Tên quyền đã tồn tại trong hệ thống!";
            }
        }

        return "done";
    }
}
