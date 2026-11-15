package ui;

import dao.HoaDonDAO;
import dao.KhachHang_Dao;
import dao.KhuyenMaiDAO;
import dao.Thue_dao;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.TaiKhoan;
import entity.Thue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class FrmHoaDon extends JPanel implements ActionListener {

    private JTextField txtMaHD;
    private JComboBox<KhachHang> cbbKhachHang;
    private final JTextField txtNVLap;
    private final ArrayList<KhuyenMai> listKM;
    private JComboBox<KhuyenMai> cbbKhuyenMai;
    private JComboBox cbbPTTToan;
    private ArrayList<KhachHang> listKH;
    private final JTextField txtTenKH;
    private final JTextField txtSDT;
    private JTextField txtNgayLap;
    private final JTextField txtTrangThai;
    private JButton btnThemSanPham;
    private JTable tblSanPham;
    private JTable tblTinhTien;
    private JLabel lblTongTien;
    JLabel lblTongTienHang;
    JLabel lblTongKhuyenMai;
    JLabel lblTongSauUuDai;
    JLabel lblTienCanThanhToan;
    private final JButton btnThanhToan;
    private final JButton btnInHoaDon;
    private final JButton btnXoaTrang;
    private final JButton btnXemLichSu;
    private final NhanVien nvLap;
    private final JButton btnTimKhachHang;
    private JLabel lblThue;

    public FrmHoaDon(TaiKhoan taiKhoan) {
        nvLap = taiKhoan.getNhanVien();
        setLayout(new BorderLayout());
        // JPanel pnlThongTin = new JPanel();

        JLabel lblTitle = new JLabel("Lập Hóa Đơn");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel Title = new JPanel(new FlowLayout());
        Title.add(lblTitle);
        add(Title, BorderLayout.NORTH);

        JPanel pnlHoaDon = new JPanel(new GridLayout(5, 4, 10, 10));
        pnlHoaDon.setBorder(
                BorderFactory.createTitledBorder("Thông tin hóa đơn")
        );

// Hàng 1: Mã hóa đơn - Khách hàng
        pnlHoaDon.add(new JLabel("Mã hóa đơn:"));
        txtMaHD = new JTextField();
        txtMaHD.setEditable(false);
        txtMaHD.setText(sinhMaHD());
        pnlHoaDon.add(txtMaHD);

        pnlHoaDon.add(new JLabel("Chọn khách hàng:"));
        listKH = new KhachHang_Dao().getAllKhachHang();
        Box b = Box.createHorizontalBox();
        cbbKhachHang = new JComboBox<KhachHang>();
        setCBBKhachHang();
        b.add(cbbKhachHang);
        btnTimKhachHang = new JButton("Tìm Khách Hàng");
        b.add(btnTimKhachHang);
        pnlHoaDon.add(b);

// Hàng 2: Tên khách hàng - Số điện thoại
        pnlHoaDon.add(new JLabel("Tên khách hàng:"));
        txtTenKH = new JTextField();
        txtTenKH.setEditable(false);
        pnlHoaDon.add(txtTenKH);

        pnlHoaDon.add(new JLabel("Số điện thoại:"));
        txtSDT = new JTextField();
        txtSDT.setEditable(false);
        pnlHoaDon.add(txtSDT);
        setThongTinKH();

// Hàng 3: Nhân viên - Ngày lập
        pnlHoaDon.add(new JLabel("Nhân viên lập phiếu:"));
        txtNVLap = new JTextField();
        txtNVLap.setEditable(false);
        txtNVLap.setText(taiKhoan.getNhanVien().getTenNV());
        pnlHoaDon.add(txtNVLap);

        pnlHoaDon.add(new JLabel("Ngày lập:"));
        txtNgayLap = new JTextField();
        txtNgayLap.setEditable(false);
        setNgayLap();
        pnlHoaDon.add(txtNgayLap);

// Hàng 4: Khuyến mãi - Phương thức thanh toán
        pnlHoaDon.add(new JLabel("Khuyến mãi được áp dụng:"));
        listKM = new KhuyenMaiDAO().getAllKhuyenMai();
        cbbKhuyenMai = new JComboBox<KhuyenMai>();
        setCBBKhuyenMai();
        pnlHoaDon.add(cbbKhuyenMai);

        pnlHoaDon.add(new JLabel("Phương thức thanh toán:"));
        cbbPTTToan = new JComboBox<>();
        setCBBPTTToan();
        pnlHoaDon.add(cbbPTTToan);

// Hàng 5: Trạng thái
        pnlHoaDon.add(new JLabel("Trạng thái hóa đơn:"));
        txtTrangThai = new JTextField();
        txtTrangThai.setEditable(false);
        txtTrangThai.setText("Chưa thanh toán");
        pnlHoaDon.add(txtTrangThai);

// ================= CHI TIẾT HÓA ĐƠN =================
        btnThemSanPham = new JButton("Thêm sản phẩm vào hóa đơn");
        JPanel pnlCTHD = new JPanel(new BorderLayout(5, 10));
        pnlCTHD.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

        String[] header = {
            "Mã sản phẩm",
            "Tên sản phẩm",
            "Giá bán",
            "Giá sau khuyến mãi",
            "Số lượng",
            "Tổng Tiền"
        };

        DefaultTableModel modelCT = new DefaultTableModel(header, 0);
        tblSanPham = new JTable(modelCT);
        tblSanPham.setRowHeight(25);

        pnlCTHD.add(btnThemSanPham, BorderLayout.NORTH);
        pnlCTHD.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);
// =================Panel Tính Tiền===============
        JPanel pnlTongKet = new JPanel(new GridLayout(6, 2, 10, 10));

        pnlTongKet.setBorder(
                BorderFactory.createTitledBorder("Tổng kết thanh toán")
        );

        Font font = new Font("Arial", Font.PLAIN, 15);

        lblTongTienHang = new JLabel("0 VNĐ");
        lblTongKhuyenMai = new JLabel("0 VNĐ");
        lblThue = new JLabel("Thuế");
        lblTongSauUuDai = new JLabel("0 VNĐ");
        lblTienCanThanhToan = new JLabel("0 VNĐ");

        pnlTongKet.add(new JLabel("Tổng tiền hàng:"));
        pnlTongKet.add(lblTongTienHang);

        pnlTongKet.add(new JLabel("Tổng khuyến mãi:"));
        pnlTongKet.add(lblTongKhuyenMai);

        String dsThue = "<html>";
        String tienThue = "<html>";
        ArrayList<Thue> listThue = new Thue_dao().getAllThue();
        for (Thue thue : listThue) {
            dsThue+=thue.getTenThue()+"<br>";
            tienThue+="0 VNĐ<br>";
        }
        dsThue+="</html>";
        tienThue+="</html>";
        pnlTongKet.add(new JLabel(dsThue));
        lblThue=new JLabel(tienThue);
        pnlTongKet.add(lblThue);

        pnlTongKet.add(new JLabel("Tổng tiền sau ưu đãi:"));
        pnlTongKet.add(lblTongSauUuDai);

        JLabel lblThanhToan = new JLabel("SỐ TIỀN CẦN THANH TOÁN:");
        lblThanhToan.setFont(new Font("Arial", Font.BOLD, 15));

        lblTienCanThanhToan.setFont(
                new Font("Arial", Font.BOLD, 18)
        );
        lblTienCanThanhToan.setForeground(Color.RED);

        pnlTongKet.add(lblThanhToan);
        pnlTongKet.add(lblTienCanThanhToan);

        lblTongTienHang.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTongKhuyenMai.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTongSauUuDai.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTienCanThanhToan.setHorizontalAlignment(SwingConstants.RIGHT);
        lblThue.setHorizontalAlignment(SwingConstants.RIGHT);
        //==========Panel button==============
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThanhToan = new JButton("Thanh toán");
        btnInHoaDon = new JButton("In hóa đơn");
        btnXoaTrang = new JButton("Xóa Trắng");
        btnXemLichSu = new JButton("Xem lịch sử");
        pnlButton.add(btnThanhToan);
        pnlButton.add(btnInHoaDon);
        pnlButton.add(btnXoaTrang);
        pnlButton.add(btnXemLichSu);
        // ================= PANEL CHÍNH =================
        JPanel pnlThongTin = new JPanel(new BorderLayout(10, 10));

        pnlThongTin.add(pnlHoaDon, BorderLayout.NORTH);
        pnlThongTin.add(pnlCTHD, BorderLayout.CENTER);
        pnlCTHD.add(pnlTongKet, BorderLayout.SOUTH);
        add(pnlThongTin, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);

        btnThemSanPham.addActionListener(this);
        btnThanhToan.addActionListener(this);
        btnInHoaDon.addActionListener(this);
        btnXemLichSu.addActionListener(this);
        btnXoaTrang.addActionListener(this);

    }


    public HoaDon getHD() {
        String maHD = txtMaHD.getText();
        LocalDate ngayLap = LocalDate.now();
        KhachHang kh = (KhachHang) cbbKhachHang.getSelectedItem();
        NhanVien nv = nvLap;
        KhuyenMai km = (KhuyenMai) cbbKhuyenMai.getSelectedItem();
        String trangThai = txtTrangThai.getText();
        String pTTT = cbbPTTToan.getSelectedItem().toString();
        HoaDon hd = new HoaDon(maHD, ngayLap, kh, nv, km, trangThai, pTTT);
        return hd;
    }

    public void setHD(HoaDon hd){
        txtMaHD.setText(hd.getMaHD());
        txtNgayLap.setText(hd.getNgayLapFormatted());
        KhachHang kh = (KhachHang) cbbKhachHang.getSelectedItem();
        txtTenKH.setText(kh.getTenKH());
        txtSDT.setText(kh.getSdt());
        txtNVLap.setText(nvLap.getTenNV());
        txtTrangThai.setText(hd.getTrangThai());
        setSoTien(hd);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnThemSanPham)) {
            new FrmChiTietHoaDon(new HoaDon(txtMaHD.getText())).setVisible(true);

        }
    }

    public void setSoTien(HoaDon hd) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        lblTongTienHang.setText(formatter.format(hd.tinhTongTien()) + " VNĐ");
        lblTongKhuyenMai.setText(formatter.format(hd.tinhKhuyenMai()) + " VNĐ");
        lblTongSauUuDai.setText(formatter.format(hd.tinhThanhTien()) + " VNĐ");
        lblTienCanThanhToan.setText(formatter.format(hd.tinhThanhTien()) + " VNĐ");
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
        listKH.sort(
                Comparator.comparing(KhachHang::getMaKH).reversed()
        );
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

    public void setThongTinKH(){
        KhachHang kh = (KhachHang) cbbKhachHang.getSelectedItem();
        txtTenKH.setText(kh.getTenKH());
        txtSDT.setText(kh.getSdt());
    }

}
