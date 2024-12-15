package gui;

import bus.KhachHangBUS;
import dto.KhachHangDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ThemKhachHangGUI extends JDialog implements ActionListener {

    private Color colorBackground = Color.decode("#FFFFFF");
    private Color color1 = Color.decode("#006270");
    private Color color2 = Color.decode("#009394");
    private Color color3 = Color.decode("#00E0C7");

    private ArrayList<JPanel> arrPnInfor;
    private ArrayList<JLabel> arrLbInfor;
    private ArrayList<JTextField> arrTfInfor;
    private JButton btnThem, btnQuayVe;

    private KhachHangBUS khachHangBUS = new KhachHangBUS();

    public ThemKhachHangGUI() {
        this.setModal(true);
        this.init();
    }

    public void init() {
        System.out.println("hello themkh");
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setLayout(new BorderLayout());

        // fill thong tin
        JPanel pn_desc = new JPanel(new FlowLayout(1, 20, 30));

        String[] thuoc_tinh = {"Mã khách hàng", "Tên khách hàng", "Địa chỉ", "Số điện thoại"};
        int len = thuoc_tinh.length;
        this.arrPnInfor = new ArrayList<>();
        this.arrLbInfor = new ArrayList<>();
        this.arrTfInfor = new ArrayList<>();

        Dimension d_pn = new Dimension(530, 30);
        Dimension d_lb = new Dimension(130, 30);
        Dimension d_tf = new Dimension(400, 30);
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
        }

        if (khachHangBUS.getKhList() == null) {
            khachHangBUS.list();
        }

        this.arrTfInfor.get(0).setEditable(false);
        this.arrTfInfor.get(0).setText(khachHangBUS.createNewId());

        // btn
        JPanel pn_btn = new JPanel(new FlowLayout(1));
        pn_btn.setPreferredSize(new Dimension(700, 50));

        btnThem = new JButton("Thêm");
        btnQuayVe = new JButton("Quay về");

        btnThem.setPreferredSize(new Dimension(150, 30));
        btnQuayVe.setPreferredSize(new Dimension(150, 30));
        btnThem.setBackground(color2);
        btnQuayVe.setBackground(color2);
        btnThem.setForeground(this.colorBackground);
        btnQuayVe.setForeground(this.colorBackground);

        btnThem.addActionListener(this);
        btnQuayVe.addActionListener(this);

        pn_btn.add(btnThem);
        pn_btn.add(btnQuayVe);

        this.add(pn_desc, BorderLayout.CENTER);
        this.add(pn_btn, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.btnThem)) {
            String idKH = arrTfInfor.get(0).getText();
            String tenKH = arrTfInfor.get(1).getText();
            String diachi = arrTfInfor.get(2).getText();
            String sdt = arrTfInfor.get(3).getText();

            if (tenKH.equals("") || diachi.equals("") || sdt.equals("")) {
                JOptionPane.showMessageDialog(this, "Các thông tin không được để trống!");
                return;
            } else if (!isValidName(tenKH)) { // kiem tra hop le ten          
                return;
            } else if (!isValidAddress(diachi)) {
                return;
            } else if (!isValidPhoneNumber(sdt)) { // kiem tra hop le so dien thoai
                JOptionPane.showMessageDialog(null,
                        "Số điện thoại chỉ được nhập số có 10 chữ số và số bắt đầu phải là số 0",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmed = JOptionPane.showConfirmDialog(null, "Xác nhận thêm khách hàng", "", JOptionPane.YES_NO_OPTION);
            if (confirmed == 0) { // xác nhận thêm

                KhachHangDTO kh = new KhachHangDTO(idKH, tenKH, diachi, sdt, true);
                khachHangBUS.addKhachHang(kh);
                
                JOptionPane.showMessageDialog(null,
                        "Thêm khách hàng thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }

        } else if (e.getSource().equals(this.btnQuayVe)) {
            dispose();
        }
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^0\\d{9}$");
    }

    public boolean isValidName(String name) {
        // Kiểm tra nếu chuỗi rỗng hoặc chỉ chứa khoảng trắng
        if (name.trim().isEmpty() || !name.trim().equals(name)) {
            JOptionPane.showMessageDialog(null, "Tên khách hàng không được để trống và không có khoảng cách đầu cuối", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false; // không hợp lệ
        }

        // Biểu thức chính quy đơn giản hơn: phải có ít nhất 1 khoảng trắng giữa các từ
        String regex = "^[\\p{L}]+(\\s[\\p{L}]+)+$";
        if(!name.matches(regex)){
            JOptionPane.showMessageDialog(null, "Tên khách hàng chỉ được nhập kí tự chữ và có ít nhất 2 chữ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true; // Trả về true nếu tên hợp lệ, ngược lại là false
    }
    
    public boolean isValidAddress(String address){
        //kiểm tra địa chỉ không rỗng và không có khoảng trắng đầu cuối
        if (address.trim().isEmpty() || !address.trim().equals(address)) {
            JOptionPane.showMessageDialog(null, "Địa chỉ không được để trống và không có khoảng cách đầu cuối", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false; // không hợp lệ
        }

        //
       String regex = "^[a-zA-Z0-9,./\\p{L}]+( [a-zA-Z0-9,./\\p{L}]+)+$";
        if(!address.matches(regex)){
            JOptionPane.showMessageDialog(null, "Địa chỉ chỉ được nhập chữ, số, dấu phẩy, dấu chấm, dấu / và có ít nhất 2 chữ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    public static void main(String[] args) {
        ThemKhachHangGUI t = new ThemKhachHangGUI();
    }
}