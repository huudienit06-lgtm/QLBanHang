package ui;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import dao.KhachHang_Dao;
import dao.KhuyenMaiDAO;
import dao.SanPhamDAO;
import dao.Thue_dao;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.KhuyenMai;
import entity.NhanVien;
import entity.SanPham;
import entity.TaiKhoan;
import entity.Thue;
import util.XuatHoaDon;

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
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class FrmHoaDon extends JPanel implements ActionListener, TableModelListener {

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
    private final ArrayList<Thue> listThue;
    private SanPhamDAO spDao = new SanPhamDAO();
    private final ChiTietHoaDonDAO ctDao = new ChiTietHoaDonDAO();
    private final HoaDonDAO hdDao = new HoaDonDAO();
    private DefaultTableModel modelCT;
    private final JPanel pnlHoaDon;
    private JTextField txtTienNhan;
    private Box b1, b2;
    private JTextField txtTienThua;
    private double tienCanThanhToan;
    boolean laQuanLy;
    private HoaDon hoaDonHienTai = null;
    private ArrayList<ChiTietHoaDon> listChiTietHoaDonHienTai = new ArrayList<>();
    ;
    private final JPanel pnlButton;
    private JButton btnCapNhat;
    private JButton btnLuu;

    public FrmHoaDon(TaiKhoan taiKhoan) {
        setLayout(new BorderLayout());
        Color background = new Color(219, 234, 254);

        JLabel lblTitle = new JLabel("Lập Hóa Đơn");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(30, 64, 175));
        JPanel Title = new JPanel(new FlowLayout());
        Title.setBackground(background);
        Title.add(lblTitle);
        add(Title, BorderLayout.NORTH);

        nvLap = taiKhoan.getNhanVien();
        laQuanLy = nvLap != null
                && "Quản lý".equalsIgnoreCase(nvLap.getChucVu());

        pnlHoaDon = new JPanel(new GridLayout(5, 4, 10, 10));
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

        modelCT = new DefaultTableModel(header, 0);
        tblSanPham = new JTable(modelCT);
        tblSanPham.setRowHeight(25);

        pnlCTHD.add(btnThemSanPham, BorderLayout.NORTH);
        pnlCTHD.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);
// =================Panel Tính Tiền===============
        JPanel pnlTongKet = new JPanel(new GridLayout(5, 2, 10, 10));

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
        listThue = new Thue_dao().getAllThue();
        for (Thue thue : listThue) {
            dsThue += thue.getTenThue() + "<br>";
            tienThue += "0 VNĐ<br>";
        }
        dsThue += "</html>";
        tienThue += "</html>";
        pnlTongKet.add(new JLabel(dsThue));
        lblThue = new JLabel(tienThue);
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
        pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
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
        btnTimKhachHang.addActionListener(this);
        modelCT.addTableModelListener(this);
        cbbKhachHang.addActionListener(this);

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

    public void setHD(HoaDon hd) {
        txtMaHD.setText(hd.getMaHD());
        txtNgayLap.setText(hd.getNgayLapFormatted());
        for (int i = 0; i < cbbKhachHang.getItemCount(); i++) {
            KhachHang item = cbbKhachHang.getItemAt(i);
            if (item != null && item.getMaKH().equals(hd.getKhachHang().getMaKH())) {
                cbbKhachHang.setSelectedIndex(i);
                break;
            }
        }
        KhuyenMai khuyenMaiHoaDon = hd.getKhuyenMai();
        if (khuyenMaiHoaDon == null) {
            cbbKhuyenMai.setSelectedItem(null);
        } else {
            for (int i = 0; i < cbbKhuyenMai.getItemCount(); i++) {
                KhuyenMai item = cbbKhuyenMai.getItemAt(i);
                if (item != null && item.getMaKM().equals(khuyenMaiHoaDon.getMaKM())) {
                    cbbKhuyenMai.setSelectedIndex(i);
                    break;
                }
            }
        }
        txtTenKH.setText(hd.getKhachHang().getTenKH());
        txtSDT.setText(hd.getKhachHang().getSdt());
        txtNVLap.setText(nvLap.getTenNV());
        txtTrangThai.setText(hd.getTrangThai() == "" ? "Chưa thanh toán" : hd.getTrangThai());
        setSoTien(hd);
    }

    public void setCTHD(HoaDon hd) {
        modelCT.setRowCount(0);
        ArrayList<ChiTietHoaDon> list = ctDao.getDSCTHDByID(hd.getMaHD());
        for (ChiTietHoaDon ct : list) {
            modelCT.addRow(new Object[]{ct.getSanPham().getMaSP(), ct.getSanPham().getTenSP(),
                ct.getSanPham().getGiaBanFormatted(),
                ct.getSanPham().getGiaBanSauKhuyenMaiFormatted(),
                ct.getSoLuong(), ct.getThanhTienFormatted()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (e.getSource().equals(cbbKhachHang)) {
            setThongTinKH();
        }
        if (o.equals(btnTimKhachHang)) {
            FrmTimKhachHang frm = new FrmTimKhachHang((JFrame) SwingUtilities.getWindowAncestor(this));
            frm.setVisible(true);

            KhachHang kh = frm.getSelectedKhachHang();
            if (kh != null) {
                txtTenKH.setText(kh.getTenKH());
                txtSDT.setText(kh.getSdt());
                for (int i = 0; i < cbbKhachHang.getItemCount(); i++) {
                    KhachHang khachHangTrongCombo = cbbKhachHang.getItemAt(i);
                    if (khachHangTrongCombo != null
                            && khachHangTrongCombo.getMaKH().equals(kh.getMaKH())) {
                        cbbKhachHang.setSelectedIndex(i);
                        break;
                    }
                }

            }
        }
        if (o.equals(btnThemSanPham)) {
            if (cbbKhachHang.getSelectedItem() == null || txtTenKH.getText().isEmpty() || txtSDT.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng");
            } else {
                hoaDonHienTai = getHD();
                FrmChiTietHoaDon frm = new FrmChiTietHoaDon(
                        (JFrame) SwingUtilities.getWindowAncestor(this),
                        hoaDonHienTai, modelCT);
                frm.setVisible(true);
                modelCT.removeTableModelListener(this);
                modelCT = frm.getModelCTHD();
                modelCT.addTableModelListener(this);
                tblSanPham.setModel(modelCT);
                listChiTietHoaDonHienTai.clear();
                for (int i = 0; i < modelCT.getRowCount(); i++) {
                    SanPham sp = spDao.findByID((String) modelCT.getValueAt(i, 0));
                    int soLuong = Integer.parseInt(modelCT.getValueAt(i, 4).toString());
                    listChiTietHoaDonHienTai.add(new ChiTietHoaDon(hoaDonHienTai, sp, soLuong));
                }
                setSoTien(hoaDonHienTai);
            }

        }
        if (o.equals(btnThanhToan)) {
            if (listChiTietHoaDonHienTai == null
                    || listChiTietHoaDonHienTai.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Chưa thêm sản phẩm vào hóa đơn");
                return;
            }
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            FrmThanhToan frm = new FrmThanhToan(parent, cbbPTTToan.getSelectedItem().toString(), tienCanThanhToan);
            frm.setVisible(true);
            if (frm.traKetQua()) {
                String trangThaiTruoc = hoaDonHienTai.getTrangThai();
                txtTrangThai.setText("Đã thanh toán");
                hoaDonHienTai.setTrangThai("Đã thanh toán");
                if (luuHoaDon()) {
                    listChiTietHoaDonHienTai.clear();
                    xoaTrang();
                } else {
                    hoaDonHienTai.setTrangThai(trangThaiTruoc);
                    txtTrangThai.setText(trangThaiTruoc);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thanh toán bị hủy. Thông tin hóa đơn được giữ lại để tiếp tục chỉnh sửa.");
            }
        }
        if (o.equals(btnInHoaDon)) {
            HoaDon hd = hoaDonHienTai;

            List<ChiTietHoaDon> dsCTHD = listChiTietHoaDonHienTai;

            String html = taoNoiDungHTML(hd, dsCTHD);

            XuatHoaDon.xuat(
                    this,
                    hd.getMaHD(),
                    html
            );
        }
        if (o.equals(btnXemLichSu)) {

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            FrmXemLichSuHoaDon frm = new FrmXemLichSuHoaDon(parent);
            frm.setVisible(true);
            hoaDonHienTai = frm.getSelectedHoaDon();
            if (hoaDonHienTai != null) {
                setHD(hoaDonHienTai);
                setCTHD(hoaDonHienTai);
                listChiTietHoaDonHienTai = ctDao.getDSCTHDByID(hoaDonHienTai.getMaHD());
                setSoTien(hoaDonHienTai);
            }
        }
        if (o.equals(btnXoaTrang)) {
            xoaTrang();
        }
    }

    public void xoaTrang() {
        txtMaHD.setText(sinhMaHD());
        cbbKhachHang.setSelectedItem(null);
        cbbKhuyenMai.setSelectedItem(null);
        setThongTinKH();
        txtTenKH.setText("");
        txtSDT.setText("");
        txtTrangThai.setText("Chưa thanh toán");
        modelCT.setRowCount(0);
        setSoTien(new HoaDon());
    }

    public void setSoTien(HoaDon hd) {
        if (hd == null || listChiTietHoaDonHienTai == null
                || listChiTietHoaDonHienTai.isEmpty()) {
            lblTongTienHang.setText("0 VNĐ");
            lblTongKhuyenMai.setText("0 VNĐ");
            lblTongSauUuDai.setText("0 VNĐ");
            lblTienCanThanhToan.setText("0 VNĐ");
            lblThue.setText("0 VNĐ");

            tienCanThanhToan = 0;
            return;
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        double tongTienHang = hd.tinhTongTienLocal(listChiTietHoaDonHienTai);
        double tongKhuyenMai = hd.tinhKhuyenMai();
        double tongSauUuDai = tongTienHang - tongKhuyenMai;
        double tongThue = 0;
        StringBuilder chiTietThue = new StringBuilder("<html>");

        for (Thue thue : listThue) {
            double tienThue = thue.tinhThue(tongTienHang);
            tongThue += tienThue;
            chiTietThue.append(formatter.format(tienThue)).append(" VNĐ<br>");
        }
        chiTietThue.append("</html>");

        lblTongTienHang.setText(formatter.format(tongTienHang) + " VNĐ");
        lblTongKhuyenMai.setText(formatter.format(tongKhuyenMai) + " VNĐ");
        lblThue.setText(chiTietThue.toString());
        lblTongSauUuDai.setText(formatter.format(tongSauUuDai) + " VNĐ");
        tienCanThanhToan = tongSauUuDai + tongThue;
        lblTienCanThanhToan.setText(formatter.format(tongSauUuDai + tongThue) + " VNĐ");
    }

    public void setNgayLap() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String ngayHienTai = LocalDate.now().format(formatter);
        txtNgayLap.setText(ngayHienTai);
    }

    public String sinhMaHD() {
        return hdDao.getMaHDTiepTheo();
    }

    public void setCBBKhachHang() {
        cbbKhachHang.addItem(null);
        listKH.sort(
                Comparator.comparing(KhachHang::getMaKH).reversed()
        );
        for (KhachHang kh : listKH) {
            cbbKhachHang.addItem(kh);
        }
    }

    public void setCBBKhuyenMai() {
        LocalDate ngayHienTai = LocalDate.now();
        cbbKhuyenMai.addItem(null);
        for (KhuyenMai khuyenMai : listKM) {
            if (khuyenMai.getNgayKetThuc().isAfter(ngayHienTai) && khuyenMai.getTrangThai().equalsIgnoreCase("Đang áp dụng")) {
                cbbKhuyenMai.addItem(khuyenMai);
            }
        }
    }

    public void setCBBPTTToan() {
        cbbPTTToan.addItem("Tiền mặt");
        cbbPTTToan.addItem("Chuyển khoản");
    }

    public void setThongTinKH() {
        KhachHang kh = (KhachHang) cbbKhachHang.getSelectedItem();
        if (kh == null) {
            txtTenKH.setText("");
            txtSDT.setText("");
        } else {
            txtTenKH.setText(kh.getTenKH());
            txtSDT.setText(kh.getSdt());
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
            return;
        }
        setSoTien(hoaDonHienTai);
    }

    private boolean luuHoaDon() {
        HoaDon hd = hoaDonHienTai;
        if (hdDao.getHoaDonById(hoaDonHienTai.getMaHD()) == null) {
            // Kiểm tra danh sách chi tiết
            if (listChiTietHoaDonHienTai.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Hóa đơn chưa có sản phẩm!");
                return false;
            }

            // Thêm hóa đơn
            if (!hdDao.addHoaDon(hd)) {
                JOptionPane.showMessageDialog(this,
                        "Thêm hóa đơn thất bại!");
                return false;
            }

            // Thêm chi tiết hóa đơn và cập nhật tồn kho
            for (ChiTietHoaDon ct : listChiTietHoaDonHienTai) {

                // Thêm chi tiết hóa đơn
                if (!ctDao.insertCTHH(ct)) {
                    JOptionPane.showMessageDialog(this,
                            "Thêm chi tiết hóa đơn thất bại: "
                            + ct.getSanPham().getMaSP());
                    return false;
                }

                // Cập nhật số lượng tồn
                boolean capNhat = spDao.capNhatSoLuongTon(
                        ct.getSanPham().getMaSP(),
                        ct.getSoLuong()
                );

                if (!capNhat) {
                    JOptionPane.showMessageDialog(this,
                            "Không đủ số lượng tồn cho sản phẩm: "
                            + ct.getSanPham().getTenSP());
                    return false;
                }
            }

        } else {
            if (hdDao.updateHoaDonWithDetails(hoaDonHienTai, listChiTietHoaDonHienTai)) {
                JOptionPane.showMessageDialog(null, "Đã cập nhật hóa đơn");
                return true;
            } else {
                JOptionPane.showMessageDialog(this,
                        "Cập nhật hóa đơn thất bại. Vui lòng kiểm tra tồn kho và thử lại.");
                return false;
            }
        }
        return true;
    }

    private String escapeHTML(String value) {
        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String taoNoiDungHTML(HoaDon hd, List<ChiTietHoaDon> dsCTHD) {

        StringBuilder sb = new StringBuilder();

        // Phần đầu HTML và CSS
        sb.append("""
        <!DOCTYPE html>
        <html lang="vi">
        <head>
        <meta charset="UTF-8">
        <title>Hóa đơn</title>
        </head>
        <body>
        <h1>HÓA ĐƠN BÁN HÀNG</h1>
        """);

        // Thông tin hóa đơn
        sb.append("<p>Mã hóa đơn: ")
                .append(escapeHTML(hd.getMaHD()))
                .append("</p>");

        sb.append("<p>Ngày lập: ")
                .append(escapeHTML(hd.getNgayLapFormatted()))
                .append("</p>");

        sb.append("<p>Khách hàng: ")
                .append(escapeHTML(hd.getKhachHang().getTenKH()))
                .append("</p>");

        sb.append("<table border='1' cellspacing='0' cellpadding='8'>");

        sb.append("""
        <tr>
            <th>STT</th>
            <th>Sản phẩm</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>Thành tiền</th>
        </tr>
        """);

        // Duyệt danh sách chi tiết hóa đơn
        int stt = 1;

        for (ChiTietHoaDon ct : dsCTHD) {

            sb.append("<tr>");

            sb.append("<td>").append(stt++).append("</td>");

            sb.append("<td>")
                    .append(escapeHTML(ct.getSanPham().getTenSP()))
                    .append("</td>");

            sb.append("<td>")
                    .append(ct.getSoLuong())
                    .append("</td>");

            sb.append("<td>")
                    .append(ct.getSanPham().getGiaBanSauKhuyenMaiFormatted())
                    .append("</td>");

            sb.append("<td>")
                    .append(ct.getThanhTienFormatted())
                    .append("</td>");

            sb.append("</tr>");
        }
        if (hd.getKhuyenMai() != null) {
            sb.append("<tr>");
            sb.append("<td>").append("Khuyến mãi:" + hd.getKhuyenMai().getTenKM()).append("</td>");
            sb.append("<td>").append("</td>");
            sb.append("<td>").append("</td>");
            sb.append("<td>").append("</td>");
            sb.append("<td>")
                    .append(hd.getTongTienFormatted(hd.tinhKhuyenMai()))
                    .append("</td>");
            sb.append("</tr>");
        }

        ArrayList<Thue> listThue = new Thue_dao().getAllThue();
        for (Thue t : listThue) {
            sb.append("<tr>");
            sb.append("<td>").append(t.getTenThue()).append("</td>");
            sb.append("<td>").append("</td>");
            sb.append("<td>").append("</td>");
            sb.append("<td>").append("</td>");
            sb.append("<td>")
                    .append(t.getTienThueFormatted(tienCanThanhToan))
                    .append("</td>");
            sb.append("</tr>");
        }

        sb.append("<tr>");
        sb.append("<td>").append("Tổng tiền:").append("</td>");
        sb.append("<td>").append("</td>");
        sb.append("<td>").append("</td>");
        sb.append("<td>").append("</td>");
        sb.append("<td>")
                .append(hd.getTongTienFormatted(tienCanThanhToan))
                .append("</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td>").append("Trạng thái:").append("</td>");
        sb.append("<td>").append("</td>");
        sb.append("<td>").append("</td>");
        sb.append("<td>").append("</td>");
        sb.append("<td>").append(hd.getTrangThai()).append("</td>");
        sb.append("</tr>");
        sb.append("</table>");

        sb.append("""
        <h3>CẢM ƠN QUÝ KHÁCH!</h3>
        </body>
        </html>
        """);

        return sb.toString();
    }

}
