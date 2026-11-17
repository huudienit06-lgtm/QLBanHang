package ui;

import dao.HoaDonDAO;
import dao.NhanVien_DAO;
import entity.HoaDon;
import entity.NhanVien;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class FrmThongKeHoaDon extends JPanel implements ActionListener {

    private static final String NGAY = "Ngày";
    private static final String TUAN = "Tuần";
    private static final String THANG = "Tháng";
    private static final String NAM = "Năm";
    private static final String TAT_CA = "Tất cả";
    private static final DateTimeFormatter FMT_NGAY
            = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FMT_THANG
            = DateTimeFormatter.ofPattern("MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final HoaDonDAO hoaDonDao = new HoaDonDAO();
    private final JComboBox<String> cboThoiGian;
    private final JComboBox<NhanVien> cboNhanVien;
    private final JTextField txtThoiGian;
    private final JLabel lblGoiY;
    private final JLabel lblTongSoHoaDon;
    private final JLabel lblTongDoanhThu;
    private final JLabel lblNhanVienLap;
    private final JLabel lblThoiGianThongKe;
    private final JButton btnThongKe;
    private final DefaultTableModel modelHoaDon;

    public FrmThongKeHoaDon() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        Color background = new Color(219, 234, 254);

        JLabel lblTitle = new JLabel("Thống kê doanh thu");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(30, 64, 175));
        JPanel Title = new JPanel(new FlowLayout());
        Title.setBackground(background);
        Title.add(lblTitle);
        add(Title,BorderLayout.NORTH);

        JPanel pnlDieuKhien = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlDieuKhien.setBorder(BorderFactory.createTitledBorder("Điều kiện thống kê"));
        pnlDieuKhien.add(new JLabel("Thời gian:"));
        cboThoiGian = new JComboBox<>(new String[]{NGAY, TUAN, THANG, NAM, TAT_CA});
        pnlDieuKhien.add(cboThoiGian);

        txtThoiGian = new JTextField(LocalDate.now().format(FMT_NGAY), 10);
        pnlDieuKhien.add(txtThoiGian);
        lblGoiY = new JLabel();
        pnlDieuKhien.add(lblGoiY);

        pnlDieuKhien.add(new JLabel("Nhân viên:"));
        cboNhanVien = new JComboBox<>();
        napDanhSachNhanVien();
        pnlDieuKhien.add(cboNhanVien);

        btnThongKe = new JButton("Thống kê");
        pnlDieuKhien.add(btnThongKe);

        JPanel pnlTren = new JPanel(new BorderLayout(5, 5));
        pnlTren.add(Title, BorderLayout.NORTH);
        pnlTren.add(pnlDieuKhien, BorderLayout.CENTER);
        add(pnlTren, BorderLayout.NORTH);

        modelHoaDon = new DefaultTableModel(
                new String[]{"Mã hóa đơn", "Ngày lập", "Mã khách hàng",
                    "Tên khách hàng", "Mã nhân viên", "Nhân viên lập", "Trạng thái",
                    "Giá trị hóa đơn"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(23);
        tblHoaDon.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JPanel pnlKetQua = new JPanel(new GridLayout(4, 2, 10, 8));
        pnlKetQua.setBorder(BorderFactory.createTitledBorder("Kết quả thống kê"));
        pnlKetQua.add(new JLabel("Tổng số hóa đơn:"));
        lblTongSoHoaDon = taoNhanKetQua();
        pnlKetQua.add(lblTongSoHoaDon);
        pnlKetQua.add(new JLabel("Tổng doanh thu (đã thanh toán):"));
        lblTongDoanhThu = taoNhanKetQua();
        pnlKetQua.add(lblTongDoanhThu);
        pnlKetQua.add(new JLabel("Nhân viên lập:"));
        lblNhanVienLap = taoNhanKetQua();
        pnlKetQua.add(lblNhanVienLap);
        pnlKetQua.add(new JLabel("Thời gian thống kê:"));
        lblThoiGianThongKe = taoNhanKetQua();
        pnlKetQua.add(lblThoiGianThongKe);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblHoaDon),
                pnlKetQua);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7);
        splitPane.setContinuousLayout(true);
        add(splitPane, BorderLayout.CENTER);

        cboThoiGian.addActionListener(e -> {
            capNhatGoiY();
            capNhatThoiGianMacDinh();
        });
        btnThongKe.addActionListener(this);
        txtThoiGian.addActionListener(this);
        capNhatGoiY();
    }

    private JLabel taoNhanKetQua() {
        JLabel label = new JLabel("-");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void napDanhSachNhanVien() {
        cboNhanVien.addItem(null);
        ArrayList<NhanVien> dsNhanVien = new NhanVien_DAO().getAllNhanVien();
        for (NhanVien nhanVien : dsNhanVien) {
            cboNhanVien.addItem(nhanVien);
        }

        ListCellRenderer<? super NhanVien> renderer = (list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            if (value == null) {
                label.setText("Tất cả nhân viên");
            } else {
                label.setText(value.getMaNV() + " - " + value.getTenNV());
            }
            if (isSelected) {
                label.setOpaque(true);
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            }
            return label;
        };
        cboNhanVien.setRenderer(renderer);
    }

    private void capNhatGoiY() {
        String loai = (String) cboThoiGian.getSelectedItem();
        if (TAT_CA.equals(loai)) {
            txtThoiGian.setEnabled(false);
            lblGoiY.setText("Không giới hạn ngày");
        } else {
            txtThoiGian.setEnabled(true);
            switch (loai) {
                case NGAY:
                    lblGoiY.setText("(dd/MM/yyyy)");
                    break;
                case TUAN:
                    lblGoiY.setText("(nhập ngày bất kỳ: dd/MM/yyyy)");
                    break;
                case THANG:
                    lblGoiY.setText("(MM/yyyy)");
                    break;
                default:
                    lblGoiY.setText("(yyyy)");
                    break;
            }
        }
    }

    private void capNhatThoiGianMacDinh() {
        LocalDate homNay = LocalDate.now();
        switch ((String) cboThoiGian.getSelectedItem()) {
            case NGAY:
            case TUAN:
                txtThoiGian.setText(homNay.format(FMT_NGAY));
                break;
            case THANG:
                txtThoiGian.setText(homNay.format(FMT_THANG));
                break;
            case NAM:
                txtThoiGian.setText(String.valueOf(homNay.getYear()));
                break;
            default:
                txtThoiGian.setText("");
                break;
        }
    }

    private LocalDate[] tinhKhoangNgay() {
        String loai = (String) cboThoiGian.getSelectedItem();
        if (TAT_CA.equals(loai)) {
            return new LocalDate[]{null, null};
        }

        String thoiGian = txtThoiGian.getText().trim();
        try {
            switch (loai) {
                case NGAY: {
                    LocalDate ngay = LocalDate.parse(thoiGian, FMT_NGAY);
                    return new LocalDate[]{ngay, ngay};
                }
                case TUAN: {
                    LocalDate ngay = LocalDate.parse(thoiGian, FMT_NGAY);
                    LocalDate dauTuan = ngay.with(
                            TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                    return new LocalDate[]{dauTuan, dauTuan.plusDays(6)};
                }
                case THANG: {
                    YearMonth thang = YearMonth.parse(thoiGian, FMT_THANG);
                    return new LocalDate[]{thang.atDay(1), thang.atEndOfMonth()};
                }
                default: {
                    int nam = Integer.parseInt(thoiGian);
                    if (nam < 1900 || nam > 2999) {
                        throw new NumberFormatException();
                    }
                    return new LocalDate[]{
                        LocalDate.of(nam, 1, 1),
                        LocalDate.of(nam, 12, 31)
                    };
                }
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Thời gian không hợp lệ. " + lblGoiY.getText(),
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            txtThoiGian.requestFocus();
            return null;
        }
    }

    private void thongKe() {
        LocalDate[] khoangNgay = tinhKhoangNgay();
        if (khoangNgay == null) {
            return;
        }

        NhanVien nhanVien = (NhanVien) cboNhanVien.getSelectedItem();
        String maNV = nhanVien == null ? null : nhanVien.getMaNV();
        try {
            ArrayList<HoaDon> dsHoaDon = hoaDonDao.getHoaDonTheoDieuKien(
                    khoangNgay[0], khoangNgay[1], maNV);
            modelHoaDon.setRowCount(0);
            double tongDoanhThu = 0;
            NumberFormat dinhDangTien = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            for (HoaDon hoaDon : dsHoaDon) {
                double giaTriHoaDon = hoaDon.tinhThanhTien();
                if ("Đã thanh toán".equalsIgnoreCase(hoaDon.getTrangThai())) {
                    tongDoanhThu += giaTriHoaDon;
                }
                modelHoaDon.addRow(new Object[]{
                    hoaDon.getMaHD(),
                    hoaDon.getNgayLap() == null ? "" : hoaDon.getNgayLap().format(FMT_NGAY),
                    hoaDon.getKhachHang().getMaKH(),
                    hoaDon.getKhachHang().getTenKH(),
                    hoaDon.getNhanVien().getMaNV(),
                    hoaDon.getNhanVien().getTenNV(),
                    hoaDon.getTrangThai(),
                    dinhDangTien.format(giaTriHoaDon)
                });
            }

            lblTongSoHoaDon.setText(String.valueOf(dsHoaDon.size()));
            lblTongDoanhThu.setText(dinhDangTien.format(tongDoanhThu));
            lblNhanVienLap.setText(nhanVien == null
                    ? "Tất cả nhân viên"
                    : nhanVien.getTenNV() + " (" + nhanVien.getMaNV() + ")");
            lblThoiGianThongKe.setText(TAT_CA.equals(cboThoiGian.getSelectedItem())
                    ? "Toàn thời gian"
                    : khoangNgay[0].format(FMT_NGAY) + " - " + khoangNgay[1].format(FMT_NGAY));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể thống kê hóa đơn: " + e.getMessage(),
                    "Lỗi cơ sở dữ liệu",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        thongKe();
    }
}
