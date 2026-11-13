package ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.ScrollPane;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import dao.HoaDonDAO;
import dao.KhachHang_Dao;
import dao.KhuyenMaiDAO;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.TaiKhoan;
import entity.Thue;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class FrmHoaDon extends JPanel {

    private JTextField txtMaHD;
    private JComboBox<KhachHang> cbbKhachHang;
    private final JTextField txtNVLap;
    private final ArrayList<KhuyenMai> listKM;
    private JComboBox<KhuyenMai> cbbKhuyenMai;
    private final JTextArea txtThue;
    // private final JComboBox<Object> cbbTrangThai;
    private JComboBox cbbPTTToan;
    private ArrayList<KhachHang> listKH;
    private final JTextField txtTenKH;
    private final JTextField txtSDT;
    private final JTextField txtNgayLap;
    private final JTextField txtTrangThai;
    private JButton btnTimSanPham;
    private JTable tblSanPham;

    public FrmHoaDon(TaiKhoan taiKhoan) {

        setLayout(new BorderLayout());
        JPanel pnlThongTin = new JPanel(new GridBagLayout());

        JLabel lblTitle = new JLabel("Lập Hóa Đơn");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel Title = new JPanel(new FlowLayout());
        Title.add(lblTitle);
        add(Title, BorderLayout.NORTH);

        JPanel pnlHoaDon = new JPanel();
        Box b, b1, b2, b3, b4, b5;
        b = Box.createVerticalBox();
        b1 = Box.createHorizontalBox();
        b2 = Box.createHorizontalBox();
        b3 = Box.createHorizontalBox();
        b4 = Box.createHorizontalBox();
        b5 = Box.createHorizontalBox();
        b1.add(new JLabel("Mã hóa đơn:"));
        txtMaHD = new JTextField();
        txtMaHD.setEditable(false);
        txtMaHD.setText(sinhMaHD());
        b1.add(txtMaHD);

        b1.add(new JLabel("Chọn khách hàng:"));
        listKH = new KhachHang_Dao().getAllKhachHang();
        cbbKhachHang = new JComboBox<KhachHang>();
        setCBBKhachHang();
        b1.add(cbbKhachHang);
        b.add(b1);

        b2.add(new JLabel("Tên Khách Hàng:"));
        txtTenKH = new JTextField();
        b2.add(txtTenKH);

        b2.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        b2.add(txtSDT);
        b.add(b2);

        b3.add(new JLabel("Nhân viên lập phiếu:"));
        txtNVLap = new JTextField();
        txtNVLap.setEditable(false);
        txtNVLap.setText(taiKhoan.getNhanVien().getTenNV());
        b3.add(txtNVLap);

        b3.add(new JLabel("Ngày lập:"));
        txtNgayLap = new JTextField();
        txtNgayLap.setEditable(false);
        setNgayLap();
        b3.add(txtNgayLap);
        b.add(b3);

        b4.add(new JLabel("Khuyến mãi được áp dụng:"));
        listKM = new KhuyenMaiDAO().getAllKhuyenMai();
        cbbKhuyenMai = new JComboBox<KhuyenMai>();
        setCBBKhuyenMai();
        b4.add(cbbKhuyenMai);

        b4.add(new JLabel("Phương thức thanh toán:"));
        cbbPTTToan = new JComboBox<>();
        setCBBPTTToan();
        b4.add(cbbPTTToan);
        b.add(b4);

        txtTrangThai = new JTextField();
        txtTrangThai.setEditable(false);
        txtTrangThai.setText("Chưa thanh toán");
        b5.add(new JLabel("Trạng thái hóa đơn:"));
        b5.add(txtTrangThai);
        b.add(b5);

        b.add(new JLabel("Thuế:"));
        txtThue = new JTextArea();
        txtThue.setEditable(false);
        txtThue.setText(new Thue().toString());
        b.add(txtThue);

        pnlHoaDon.add(b);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlThongTin.add(pnlHoaDon, gbc);

        JPanel pnlCTHD = new JPanel();
        Box x, x1, x2, x3;
        x = Box.createVerticalBox();
        x1 = Box.createHorizontalBox();
        x2 = Box.createHorizontalBox();
        x3 = Box.createHorizontalBox();

        btnTimSanPham = new JButton("Thêm sản phẩm vào hóa đơn");
        b.add(btnTimSanPham);
        // String[] header = {"Mã ản phẩm", "Tên sản phẩm", "Giá gốc", "Giá sau Khuyến mãi"};
        // DefaultTableModel modelCT = new DefaultTableModel(header, 0);
        // tblSanPham = new JTable(modelCT);
        // x.add(new JScrollPane(tblSanPham));
        // pnlCTHD.add(x);
        // gbc.gridx = 1;
        // gbc.gridy = 0;

        pnlThongTin.add(pnlCTHD, gbc);
        add(pnlThongTin, BorderLayout.CENTER);

    }

    public void setNgayLap() {

        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String ngayHienTai = LocalDate.now().format(formatter);
        txtNgayLap.setText(ngayHienTai);
    }

    public String sinhMaHD() {
        int count = new HoaDonDAO().getSoLuong();
        if (count < 9) {
            return "HD000" + count++;
        }
        if (count < 99) {
            return "HD00" + count++;
        }
        if (count < 999) {
            return "HD0" + count++;
        }
        if (count < 9999) {
            return "HD" + count++;
        }
        if (count == -1) {
            System.out.println("Không lấy được số lượng hóa đơn");
        }
        return null;
    }

    public void setCBBKhachHang() {
        for (KhachHang kh : listKH) {
            cbbKhachHang.addItem(kh);
        }
    }

    public void setCBBKhuyenMai() {
        LocalDate ngayHienTai = LocalDate.now();
        for (KhuyenMai khuyenMai : listKM) {
            if (khuyenMai.getNgayKetThuc().isAfter(ngayHienTai) && khuyenMai.getTrangThai().equalsIgnoreCase("Đang áp dụng"));
            cbbKhuyenMai.addItem(khuyenMai);
        }
    }

    public void setCBBPTTToan() {
        cbbPTTToan.addItem("Tiền mặt");
        cbbPTTToan.addItem("Chuyển khoản");
    }

}
