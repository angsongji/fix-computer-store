package gui;

import bus.SanPhamBUS;
import dto.SanPhamDTO;
import gui.model.IconModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SanPhamGUI extends JPanel {
    private int width, height;
    private Color colorBackground = Color.decode("#FFFFFF");
    private Color color1 = Color.decode("#006270");
    private Color color2 = Color.decode("#009394");
    private Color color3 = Color.decode("#00E0C7");

    private JPanel pnInfor, pnFilter, pnTable;
    private ArrayList<JPanel> arrPnInfor;
    private ArrayList<JLabel> arrLbInfor;
    private ArrayList<JTextField> arrTfInfor;
    private JLabel lbImgSanPham;
    private JComboBox cbBrand;
    private BufferedImage bufferImg = null;
    private JButton btnThem, btnSua, btnXoa, btnNhapExcel, btnXuatExcel;

        
    private JTable table;
    private TableRowSorter<TableModel> rowSorter;
    private DefaultTableModel model;
    private SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private String imgSanPham = "null";
    
    private boolean quyenThem, quyenSua, quyenXoa;
    
    private boolean isEditing = false; // true = đang trong chế độ sửa, false = đang trong chế độ thêm hoặc khác
    
    public SanPhamGUI(int width, int height, boolean quyenThem, boolean quyenSua, boolean quyenXoa) {
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
        JPanel result = new JPanel(new FlowLayout(1, 0, 25));
        result.setPreferredSize(new Dimension(this.width, 300));
        
        JPanel pn_infor = new JPanel(new BorderLayout());
        pn_infor.setPreferredSize(new Dimension(this.width - 100, 250));
        pn_infor.setBackground(this.color1);
        pn_infor.setBorder(BorderFactory.createLineBorder(color1, 2));
        
        // create panel avatar
        JPanel pn_avatar = new JPanel(new FlowLayout(1, 0, 25));
        pn_avatar.setPreferredSize(new Dimension(250, 250));
        
        this.lbImgSanPham = new JLabel("Image", JLabel.CENTER);
        this.lbImgSanPham.setPreferredSize(new Dimension(175, 200));
        this.lbImgSanPham.setBorder(BorderFactory.createLineBorder(Color.black));
        
        pn_avatar.add(this.lbImgSanPham);
        
        // create panel description
        JPanel pn_desc = new JPanel(new FlowLayout(1, 15, 10));

        String[] thuoc_tinh = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Giá bán"};
        int len = thuoc_tinh.length;
        this.arrPnInfor = new ArrayList<JPanel>();
        this.arrLbInfor = new ArrayList<JLabel>();
        this.arrTfInfor = new ArrayList<JTextField>();
        
        Dimension d_pn = new Dimension(400, 30);
        Dimension d_lb = new Dimension(130, 30);
        Dimension d_tf = new Dimension(220, 30);
        Color color_font = this.color1;
        Font font_infor = new Font("Segoe UI", Font.PLAIN, 15);
        for (int i = 0; i < len; i++) {
            this.arrPnInfor.add(new JPanel(new FlowLayout(0, 0, 0)));
            this.arrPnInfor.get(i).setPreferredSize(d_pn);
            
            this.arrLbInfor.add(new JLabel(thuoc_tinh[i]));
            this.arrLbInfor.get(i).setPreferredSize(d_lb);
            this.arrTfInfor.add(new JTextField());
            this.arrTfInfor.get(i).setPreferredSize(d_tf);

            this.arrLbInfor.get(i).setForeground(color_font);
            this.arrLbInfor.get(i).setFont(font_infor);
            this.arrTfInfor.get(i).setForeground(color_font);
            this.arrTfInfor.get(i).setFont(font_infor);
            
            this.arrPnInfor.get(i).add(this.arrLbInfor.get(i));
            this.arrPnInfor.get(i).add(this.arrTfInfor.get(i));
            pn_desc.add(this.arrPnInfor.get(i));
            this.arrTfInfor.get(i).setEditable(false);
        }

        JPanel pn_brand = new JPanel(new FlowLayout(0, 0, 0));
        pn_brand.setPreferredSize(d_pn);
        pn_brand.setForeground(color_font);
        pn_brand.setFont(font_infor);
        
        JLabel lb_brand = new JLabel("Hãng");
        lb_brand.setPreferredSize(d_lb);
        lb_brand.setForeground(color_font);
        lb_brand.setFont(font_infor);
        
        String[] brand = {"Acer", "Asus", "Dell", "LG", "Mac", "MSI"};    
        this.cbBrand = new JComboBox(brand);
        this.cbBrand.setPreferredSize(d_tf);
        this.cbBrand.setForeground(color_font);
        this.cbBrand.setFont(font_infor);
        
        pn_brand.add(lb_brand);
        pn_brand.add(this.cbBrand);
        pn_desc.add(pn_brand);
        
        // create panel button
        JPanel pn_btn = new JPanel(new FlowLayout(1, 25, 10));
        pn_btn.setPreferredSize(new Dimension(200, 250));
        
        // các nút chức năng mặc định
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnNhapExcel = new JButton("Nhập Excel");
        btnXuatExcel = new JButton("Xuất Excel");
        
        showCN();
        
        // các nút chức năng phụ
        JButton btn_hoan_thanh = new JButton("Hoàn thành");
        JButton btn_tro_ve = new JButton("Trở về");
        JButton btn_chon_anh = new JButton("Chọn ảnh");
        
        // Thiết kế giao diện nút
        Dimension d_btn = new Dimension(150, 30);
        btnThem.setPreferredSize(d_btn);
        btnSua.setPreferredSize(d_btn);
        btnXoa.setPreferredSize(d_btn);
        btnNhapExcel.setPreferredSize(d_btn);
        btnXuatExcel.setPreferredSize(d_btn);
        
        btn_hoan_thanh.setPreferredSize(d_btn);
        btn_tro_ve.setPreferredSize(d_btn);
        btn_chon_anh.setPreferredSize(d_btn);
        btn_hoan_thanh.setVisible(false);
        btn_tro_ve.setVisible(false);
        btn_chon_anh.setVisible(false);
        
        Color color_button = this.color2;
        btnThem.setBackground(color_button);
        btnSua.setBackground(color_button);
        btnXoa.setBackground(color_button);
        btnNhapExcel.setBackground(color_button);
        btnXuatExcel.setBackground(color_button);

        btn_hoan_thanh.setBackground(color_button);
        btn_tro_ve.setBackground(color_button);
        btn_chon_anh.setBackground(color_button);
        
        Color color_font_btn = this.colorBackground;
        btnThem.setForeground(color_font_btn);
        btnSua.setForeground(color_font_btn);
        btnXoa.setForeground(color_font_btn);
        btnNhapExcel.setForeground(color_font_btn);
        btnXuatExcel.setForeground(color_font_btn);

        btn_hoan_thanh.setForeground(color_font_btn);
        btn_tro_ve.setForeground(color_font_btn);
        btn_chon_anh.setForeground(color_font_btn);
        
        Font font_btn = new Font("Segoe UI", Font.BOLD, 13);
        btnThem.setFont(font_btn);
        btnSua.setFont(font_btn);
        btnXoa.setFont(font_btn);
        btnNhapExcel.setFont(font_btn);
        btnXuatExcel.setFont(font_btn);

        btn_hoan_thanh.setFont(font_btn);
        btn_tro_ve.setFont(font_btn);
        btn_chon_anh.setFont(font_btn);
        
        // thêm các nút
        pn_btn.add(btnThem);
//        pn_btn.add(btnSua);
        pn_btn.add(btnXoa);
        pn_btn.add(btnNhapExcel);
        pn_btn.add(btnXuatExcel);
        pn_btn.add(btn_hoan_thanh);
        pn_btn.add(btn_tro_ve);
        pn_btn.add(btn_chon_anh);
        
        // khi ấn nút thêm
        btnThem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                blankInfor();
                isEditing = false;
                lockInforAdd();
                
                arrTfInfor.get(0).setText(sanPhamBUS.createNewId());
                arrTfInfor.get(2).setText("0");
                arrTfInfor.get(3).setText("0");
                arrTfInfor.get(4).setText("0");
                
                
                btnThem.setVisible(false);
                btnSua.setVisible(false);
                btnXoa.setVisible(false);
                btnNhapExcel.setVisible(false);
                btnXuatExcel.setVisible(false);
                
                btn_hoan_thanh.setVisible(true);
                btn_tro_ve.setVisible(true);
                btn_chon_anh.setVisible(true);
                
                table.clearSelection();
                table.setEnabled(false);
            }
        });
        
        // khi ấn nút sửa
//        btnSua.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (arrTfInfor.get(0).getText().equals("")) {
//                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần sửa!");
//                    return;
//                }
//                isEditing = true;
//                
//                lockInfor(false);
//                
//                btnThem.setVisible(false);
//                btnSua.setVisible(false);
//                btnXoa.setVisible(false);
//                btnNhapExcel.setVisible(false);
//                btnXuatExcel.setVisible(false);
//                
//                btn_hoan_thanh.setVisible(true);
//                btn_tro_ve.setVisible(true);
//                btn_chon_anh.setVisible(true);
//                
//                table.setEnabled(false);
//            }
//        });
        
        // khi ấn nút xóa
        btnXoa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (arrTfInfor.get(0).getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm cần xóa!");
                    return;
                }
                if (!arrTfInfor.get(2).getText().equals("0")) {
                    JOptionPane.showMessageDialog(null, "Không thể xóa sản còn số lượng!");
                    return;
                }
                
                int confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận xóa", "Alert", JOptionPane.YES_NO_OPTION);
                if (confirmed == 0) { // xác nhận xóa
                    sanPhamBUS.deleteSanPham(arrTfInfor.get(0).getText());
                    blankInfor();
                    table.clearSelection();
                    reloadSP(sanPhamBUS.getSpList());
                }
            }
        });
        
        // khi ấn nút nhập excel
        btnNhapExcel.addMouseListener(new MouseAdapter(){ 
            @Override
            public void mouseClicked(MouseEvent e) {
                File excelFile;
                FileInputStream excelFIS = null;
                BufferedInputStream excelBIS = null;
                XSSFWorkbook excelJTableImport = null;
                ArrayList<SanPhamDTO> listAccExcel = new ArrayList<SanPhamDTO>();
                JFileChooser jf = new JFileChooser();
                int result = jf.showOpenDialog(null);
                jf.setDialogTitle("Open file");
                Workbook workbook = null;
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        excelFile = jf.getSelectedFile();
                        excelFIS = new FileInputStream(excelFile);
                        excelBIS = new BufferedInputStream(excelFIS);
                        excelJTableImport = new XSSFWorkbook(excelBIS);
                        XSSFSheet excelSheet = excelJTableImport.getSheetAt(0);
                        for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                            XSSFRow excelRow = excelSheet.getRow(row);
                            String maMay = excelRow.getCell(0).getStringCellValue();
                            String tenMay = excelRow.getCell(1).getStringCellValue();
                            int soLuong = Integer.valueOf(excelRow.getCell(2).getStringCellValue()) ;
                            int giaNhap = Integer.valueOf(excelRow.getCell(3).getStringCellValue()) ;
                            int giaBan = Integer.valueOf(excelRow.getCell(4).getStringCellValue()) ;
                            String hang = excelRow.getCell(5).getStringCellValue();
                            String img = excelRow.getCell(6).getStringCellValue();
                            SanPhamDTO mt = new SanPhamDTO(maMay, tenMay, soLuong, giaNhap, giaBan, hang, img, true);
                            listAccExcel.add(mt);
                            DefaultTableModel table_acc = (DefaultTableModel) table.getModel();
                            table_acc.setRowCount(0);
                            reloadSP(listAccExcel);
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(SanPhamDTO.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(SanPhamDTO.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                for (int i = 0; i < listAccExcel.size(); i++) {
                    SanPhamDTO sp = listAccExcel.get(i);
                    if (sp.getIdSanPham().contains("SP")) {
                        SanPhamDTO sanPham = new SanPhamDTO(
                                sp.getIdSanPham(), sp.getTenSanPham(), sp.getSoLuong(), sp.getGiaNhap(), sp.getGiaBan(), sp.getHang(), sp.getImgSanPham(), true
                        );
                        sanPhamBUS.updateSanPham(sp);
                    } else {
                        JOptionPane.showMessageDialog(null, "Id " + sp.getIdSanPham() + " không phù hợp !", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        
        // khi ấn xuất excel
        btnXuatExcel.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.showSaveDialog(null);
                    File saveFile = jFileChooser.getSelectedFile();
                    if (saveFile != null) {
                        saveFile = new File(saveFile.toString() + ".xlsx");
                        Workbook wb = new XSSFWorkbook();
                        Sheet sheet = wb.createSheet("SP");

                        Row rowCol = sheet.createRow(0);
                        for (int i = 0; i < table.getColumnCount(); i++) {
                            Cell cell = rowCol.createCell(i);
                            cell.setCellValue(table.getColumnName(i));
                        }

                        for (int j = 0; j < table.getRowCount(); j++) {
                            Row row = sheet.createRow(j + 1);
                            for (int k = 0; k < table.getColumnCount(); k++) {
                                Cell cell = row.createCell(k);
                                if (table.getValueAt(j, k) != null) {
                                    cell.setCellValue(table.getValueAt(j, k).toString());
                                }

                            }
                        }
                        FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                        wb.write(out);
                        wb.close();
                        out.close();
                        openFile(saveFile.toString());
                    }
                } catch (HeadlessException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        // khi ấn nút hoàn thành
        btn_hoan_thanh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int confirmed;
                
                //lấy tên hãng
                String brand = cbBrand.getSelectedItem().toString();
        
                // Lấy tên
                String tenSanPham = arrTfInfor.get(1).getText();

                // kiểm tra chứa tên hãng
                if (!brand.isEmpty()) {
                    String validationMessage = sanPhamBUS.validateProductName(tenSanPham, brand);
                    //kiểm tra tên hợp lý chưa
                    if (validationMessage != null) {
                        JOptionPane.showMessageDialog(null, validationMessage);
                        return;
                    }
                }

                if (isEditing) { // đang trong chế độ sửa
                    confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận sửa sản phẩm", "", JOptionPane.YES_NO_OPTION);
                    if (confirmed == 0) { // xác nhận sửa
                        String idSP = arrTfInfor.get(0).getText();
                        String tenSP = arrTfInfor.get(1).getText();
                        int soLuong = Integer.parseInt(arrTfInfor.get(2).getText());
                        int giaNhap = Integer.parseInt(arrTfInfor.get(3).getText());
                        int giaBan = Integer.parseInt(arrTfInfor.get(4).getText());
                        String hang = (String) cbBrand.getItemAt(cbBrand.getSelectedIndex());
                        String newImg = imgSanPham;
                        
                        SanPhamDTO sp = new SanPhamDTO(idSP, tenSP, soLuong, giaNhap, giaBan, hang, newImg, true);
                        sanPhamBUS.updateSanPham(sp);
                        
                        
                        saveImg();
                        reloadSP(sanPhamBUS.getSpList());
                        
                        JOptionPane.showMessageDialog(null, "Sửa thành công", "OK", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else { // đang trong chế độ thêm
                    confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận thêm sản phẩm", "", JOptionPane.YES_NO_OPTION);
                    if (confirmed == 0) { // xác nhận thêm
                        String idSP = arrTfInfor.get(0).getText();
                        String tenSP = arrTfInfor.get(1).getText();
                        int soLuong = Integer.parseInt(arrTfInfor.get(2).getText());
                        int giaNhap = Integer.parseInt(arrTfInfor.get(3).getText());
                        int giaBan = Integer.parseInt(arrTfInfor.get(4).getText());
                        String hang = (String) cbBrand.getItemAt(cbBrand.getSelectedIndex());
                        String newImg = imgSanPham;
                        
                        if (sanPhamBUS.isExisted(idSP)) {
                            JOptionPane.showMessageDialog(null, "Mã sản phẩm đă tồn tại!");
                            return;
                        }
                        
                        SanPhamDTO sp = new SanPhamDTO(idSP, tenSP, soLuong, giaNhap, giaBan, hang, newImg, true);
                        sanPhamBUS.addSanPham(sp);
                        
                        saveImg();
                        
                        reloadSP(sanPhamBUS.getSpList());
                        
                        blankInfor();
                
                        lockInforAll();

                        btnThem.setVisible(true);
                        btnSua.setVisible(true);
                        btnXoa.setVisible(true);
                        btnNhapExcel.setVisible(true);
                        btnXuatExcel.setVisible(true);

                        showCN();

                        btn_hoan_thanh.setVisible(false);
                        btn_tro_ve.setVisible(false);
                        btn_chon_anh.setVisible(false);

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
                lockInforAll();
                
                btnThem.setVisible(true);
                btnSua.setVisible(true);
                btnXoa.setVisible(true);
                btnNhapExcel.setVisible(true);
                btnXuatExcel.setVisible(true);
                
                showCN();
                
                btn_hoan_thanh.setVisible(false);
                btn_tro_ve.setVisible(false);
                btn_chon_anh.setVisible(false);
                
                table.setEnabled(true);
            }
        });
        
        // khi ấn nút chọn ảnh
        btn_chon_anh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG images", "jpg", "png");
                fc.setFileFilter(filter);
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        bufferImg = ImageIO.read(file);
                        // set tên ảnh là tên mã sản phẩm
                        imgSanPham = arrTfInfor.get(0).getText().concat(".png");      
                        
                        lbImgSanPham.setText("");
                        lbImgSanPham.setIcon(new ImageIcon(bufferImg.getScaledInstance(200, 250, Image.SCALE_SMOOTH)));
                    } catch (IOException ex) {
                        Logger.getLogger(SanPhamGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        // add components
        pn_infor.add(pn_avatar, BorderLayout.WEST);
        pn_infor.add(pn_desc, BorderLayout.CENTER);
        pn_infor.add(pn_btn, BorderLayout.EAST);
        
        result.add(pn_infor);
        
        return result;
    }
    
    public JPanel createPnFilter() {
        JPanel pn_filter = new JPanel(new FlowLayout(1, 20, 20)); 
        
        // Thanh tìm kiếm theo tên hoặc id sản phẩm
        JLabel lb_tim_kiem = new JLabel("Tìm kiếm", JLabel.CENTER);
        JTextField tf_tim_kiem = new JTextField();
        tf_tim_kiem.setPreferredSize(new Dimension(200, 30));
        tf_tim_kiem.getDocument().addDocumentListener(new DocumentListener() { 
            @Override
            public void insertUpdate(DocumentEvent e) {
                String txt = tf_tim_kiem.getText();
                if (txt.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                }
                else if (txt.trim().length() >= 2 && txt.trim().substring(0, 2).toUpperCase().equals("SP")) {
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
                else if (txt.trim().length() >= 2 && txt.trim().substring(0, 2).toUpperCase().equals("SP")) {
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
        
        // Khu lọc theo một số thuộc tính (hãng, giá)
        JLabel lb_hang = new JLabel("Hãng", JLabel.CENTER);
        String[] brand = {"", "Acer", "Asus", "Dell", "LG", "Mac", "MSI"};    
        JComboBox cb_hang = new JComboBox(brand);
        cb_hang.setPreferredSize(new Dimension(100, 30));
        
        JLabel lb_gia = new JLabel("Giá", JLabel.CENTER);
        JTextField tf_min_price = new JTextField();
        tf_min_price.setPreferredSize(new Dimension(100, 30));
        tf_min_price.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Nếu ký tự không phải là số hoặc là ký tự điều khiển, thì hủy sự kiện
                if (!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    e.consume(); // Ngăn không cho nhập
                }
            }
        });
        
        JSeparator sep = new JSeparator(JSeparator.HORIZONTAL);
        sep.setPreferredSize(new Dimension(20, 10));
        
        JTextField tf_max_price = new JTextField();
        tf_max_price.setPreferredSize(new Dimension(100, 30));
        tf_max_price.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Nếu ký tự không phải là số hoặc là ký tự điều khiển, thì hủy sự kiện
                if (!Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE) {
                    e.consume(); // Ngăn không cho nhập
                }
            }
        });
        
        JButton btn_loc = new JButton("Lọc");
        btn_loc.setPreferredSize(new Dimension(100, 30));
        
        btn_loc.addMouseListener(new MouseAdapter() { 
            @Override
            public void mouseClicked(MouseEvent e) {
                String hang = (String) cb_hang.getItemAt(cb_hang.getSelectedIndex());
                int min_price;
                if (tf_min_price.getText().equals("")) {
                    min_price = 0;
                }
                else {
                    min_price = Integer.parseInt(tf_min_price.getText());
                }
                
                int max_price;
                if (tf_max_price.getText().equals("")) {
                    max_price = Integer.MAX_VALUE;
                }
                else {
                    max_price = Integer.parseInt(tf_max_price.getText());
                }
                
                if(min_price > max_price){
                    JOptionPane.showMessageDialog(null,
                        "Giá tối thiểu phải nhỏ hơn giá tối đa",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                reloadSP(sanPhamBUS.filter(hang, min_price, max_price));
            }
        });
        
        // giao diện filter
        Font font_filter = new Font("Segoe UI", Font.BOLD, 13);
        
        lb_tim_kiem.setFont(font_filter);
        tf_tim_kiem.setFont(font_filter);
        lb_hang.setFont(font_filter);
        cb_hang.setFont(font_filter);
        lb_gia.setFont(font_filter);
        tf_min_price.setFont(font_filter);
        tf_max_price.setFont(font_filter);
        
        lb_tim_kiem.setForeground(color1);
        tf_tim_kiem.setForeground(color1);
        lb_hang.setForeground(color1);
        cb_hang.setForeground(color1);
        lb_gia.setForeground(color1);
        tf_min_price.setForeground(color1);
        tf_max_price.setForeground(color1);
        
        btn_loc.setBackground(color2);
        btn_loc.setFont(font_filter);
        btn_loc.setForeground(this.colorBackground);
        
        pn_filter.add(lb_tim_kiem);
        pn_filter.add(tf_tim_kiem);
        pn_filter.add(lb_hang);
        pn_filter.add(cb_hang);
        pn_filter.add(lb_gia);
        pn_filter.add(tf_min_price);
        pn_filter.add(sep);
        pn_filter.add(tf_max_price);
        pn_filter.add(btn_loc);
        
        return pn_filter;
    }
    
    public JPanel createPnTable() {
        JPanel pn_table = new JPanel(new FlowLayout(1, 0, 0));
        pn_table.setPreferredSize(new Dimension(this.width, 300));
        
        String[] col = {
            "Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Giá bán", "Hãng", "IMG"
        };
        this.model = new DefaultTableModel(col, 0){
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
        
        
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(10);
        table.getColumnModel().getColumn(3).setPreferredWidth(40);
        table.getColumnModel().getColumn(4).setPreferredWidth(40);
        table.getColumnModel().getColumn(5).setPreferredWidth(20);
        table.getColumnModel().getColumn(6).setPreferredWidth(20);
        
        this.loadSP();
        
        pn_table.add(scroll);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (table.getRowSorter() != null) {
                    row = table.getRowSorter().convertRowIndexToModel(row);
                }
                imgSanPham = table.getModel().getValueAt(row, 6).toString();
                IconModel icon_sp = new IconModel(175, 200, "SanPham/" + imgSanPham);
                
                // set thông tin cho sản phẩm
                arrTfInfor.get(0).setText(table.getModel().getValueAt(row, 0).toString());
                arrTfInfor.get(1).setText(table.getModel().getValueAt(row, 1).toString());
                arrTfInfor.get(2).setText(table.getModel().getValueAt(row, 2).toString());
                arrTfInfor.get(3).setText(table.getModel().getValueAt(row, 3).toString());
                arrTfInfor.get(4).setText(table.getModel().getValueAt(row, 4).toString());
                cbBrand.setSelectedItem(table.getModel().getValueAt(row, 5).toString());
                lbImgSanPham.setText("");
                lbImgSanPham.setIcon(icon_sp.createIcon());
                
                lockInforAll();
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
    
    public void loadSP() {
        if (sanPhamBUS.getSpList() == null) {
            sanPhamBUS.list();
        }
        ArrayList<SanPhamDTO> spList = sanPhamBUS.getSpList();
        model.setRowCount(0);
        reloadSP(spList);
    }
    
    public void reloadSP(ArrayList<SanPhamDTO> spList) {
        model.setRowCount(0);
        for (SanPhamDTO sp : spList) {
            if (sp.isEnable()) {
                model.addRow(new Object[]{
                    sp.getIdSanPham(), sp.getTenSanPham(), sp.getSoLuong(), sp.getGiaNhap(), sp.getGiaBan(), sp.getHang(), sp.getImgSanPham()
                });
            }
        }
    }
    
    public void saveImg() {
        try {
            if (bufferImg != null) {
                File save = new File("src/img/SanPham/" + imgSanPham);
                ImageIO.write(bufferImg, "png", save);
                bufferImg = null;
            }
        } catch (IOException ex) {
            Logger.getLogger(SanPhamGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    // khóa khả năng thao tác với thông tin
    public void lockInforAdd() {
        arrTfInfor.get(1).setEditable(true);
        cbBrand.setEnabled(true);
    }
    public void lockInforAll() {
        for(int i=0; i<=4; i++){
            arrTfInfor.get(1).setEditable(false);
        }
        cbBrand.setEnabled(false);
    }
    
    public void blankInfor() {
        arrTfInfor.get(0).setText("");
        arrTfInfor.get(1).setText("");
        arrTfInfor.get(2).setText("");
        arrTfInfor.get(3).setText("");
        arrTfInfor.get(4).setText("");
        lbImgSanPham.setIcon(null);
        lbImgSanPham.setText("Image");
        imgSanPham = "null";
    }
    
    public void showCN() {
        this.btnThem.setVisible(quyenThem);
        this.btnSua.setVisible(quyenSua);
        this.btnXoa.setVisible(quyenXoa);
        this.btnNhapExcel.setVisible(quyenSua);
        this.btnXuatExcel.setVisible(quyenSua);
    }
    
    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    
}