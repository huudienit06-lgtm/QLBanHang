package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DecimalFormat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import connectdb.ConnectDB;
import dao.ChiTietPhieuNhapKho_dao;
import entity.ChiTietPhieuNhapKho;

public class ThongKeNhapKho_GUI extends JPanel{

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FMT_THANG =
            DateTimeFormatter.ofPattern("MM/yyyy");

    private static final DecimalFormat DF =
            new DecimalFormat("#,###");

    private static final String TAT_CA = "Tất cả";

    private JComboBox<String> cboLoai, cboThoiGian, cboSoLuong;
    private JLabel lblKhoang, lblSoLuong, lblTongTien;
    private JTable table;
    private DefaultTableModel model;

    private ChiTietPhieuNhapKho_dao dao = new ChiTietPhieuNhapKho_dao();

    private boolean dangTai = false;

    public JButton btnReload;

    public ThongKeNhapKho_GUI() {

        setLayout(new BorderLayout(10, 10));

        // Tiêu đề
        Color background = new Color(219, 234, 254);
        JLabel lblTitle = new JLabel("THỐNG KÊ HÀNG NHẬP KHO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(30, 64, 175));

        JPanel Title = new JPanel(new FlowLayout());
        Title.setBackground(background);
        Title.add(lblTitle);

        // Bộ lọc
        cboLoai = new JComboBox<>(new String[]{TAT_CA, "Ngày", "Tuần", "Tháng", "Năm"});
        cboThoiGian = new JComboBox<>();
        cboSoLuong = new JComboBox<>(
                new String[]{"5", "10", "15", "20", "25", "50", TAT_CA});
        cboSoLuong.setSelectedItem(TAT_CA);

        JButton btnThongKe = new JButton("Thống kê");

        JPanel pnlLoc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlLoc.add(new JLabel("Thống kê theo:"));
        pnlLoc.add(cboLoai);
        pnlLoc.add(new JLabel("Thời gian:"));
        pnlLoc.add(cboThoiGian);
        pnlLoc.add(new JLabel("Số lượng:"));
        pnlLoc.add(cboSoLuong);
        pnlLoc.add(btnThongKe);
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.add(Title, BorderLayout.NORTH);
        pnlTop.add(pnlLoc, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);

        // Bảng
        model = new DefaultTableModel(new String[]{
            "Mã PNK", "Mã SP", "Tên SP", "Số lượng", "Số lượng tồn",
            "Giá nhập", "Thành tiền", "Ngày nhập"
        }, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Tổng kết
        lblKhoang = new JLabel("Khoảng thời gian: -");
        lblSoLuong = new JLabel("Tổng số lượng nhập: 0");
        lblTongTien = new JLabel("Tổng tiền nhập: 0 VNĐ");
        lblTongTien.setForeground(Color.RED);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        pnlBottom.add(lblKhoang);
        pnlBottom.add(lblSoLuong);
        pnlBottom.add(lblTongTien);

        add(pnlBottom, BorderLayout.SOUTH);

        // Sự kiện
        cboLoai.addActionListener(e -> {
            loadThoiGian();
            if (cboLoai.getSelectedItem().equals(TAT_CA)) {
                thongKe(false);
            } else {
                xoaKetQua();
            }
        });

        cboThoiGian.addActionListener(e -> {
            if (!dangTai && cboThoiGian.getSelectedItem() != null)
                thongKe(false);
        });

        cboSoLuong.addActionListener(e -> thongKe(false));
        btnThongKe.addActionListener(e -> thongKe(true));

        // Mở lên là hiển thị tất cả dữ liệu
        loadThoiGian();
        thongKe(false);
    }

    // Lấy danh sách thời gian từ database
    private void loadThoiGian() {

        dangTai = true;
        cboThoiGian.removeAllItems();

        String loai = cboLoai.getSelectedItem().toString();

        // "Tất cả" không cần chọn thời gian
        if (loai.equals(TAT_CA)) {
            cboThoiGian.setEnabled(false);
            dangTai = false;
            return;
        }
        cboThoiGian.setEnabled(true);

        String sql = "SELECT DISTINCT CAST(ngayLap AS DATE) ngayLap "
                   + "FROM PhieuNhapKho WHERE ngayLap IS NOT NULL";

        try {
            Connection con = ConnectDB.getConnection();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                // Dùng LocalDate để sắp xếp đúng thứ tự thời gian
                TreeSet<LocalDate> dsNgay = new TreeSet<>(
                        Comparator.reverseOrder());

                while (rs.next()) {
                    dsNgay.add(rs.getDate("ngayLap").toLocalDate());
                }

                // LinkedHashSet-like: giữ thứ tự, bỏ trùng
                ArrayList<String> ds = new ArrayList<>();

                for (LocalDate ngay : dsNgay) {
                    String s;

                    if (loai.equals("Ngày") || loai.equals("Tuần"))
                        s = ngay.format(FMT);
                    else if (loai.equals("Tháng"))
                        s = ngay.format(FMT_THANG);
                    else
                        s = String.valueOf(ngay.getYear());

                    if (!ds.contains(s))
                        ds.add(s);
                }

                for (String s : ds)
                    cboThoiGian.addItem(s);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        dangTai = false;
    }

    // Xác định khoảng thời gian
    private LocalDate[] tinhKhoang() {

        String loai = cboLoai.getSelectedItem().toString();

        // Tất cả: lấy khoảng đủ rộng để bao trọn dữ liệu
        if (loai.equals(TAT_CA)) {
            return new LocalDate[]{
                LocalDate.of(1900, 1, 1),
                LocalDate.of(2999, 12, 31)
            };
        }

        if (cboThoiGian.getSelectedItem() == null)
            return null;

        String text = cboThoiGian.getSelectedItem().toString();

        try {
            if (loai.equals("Ngày")) {
                LocalDate ngay = LocalDate.parse(text, FMT);
                return new LocalDate[]{ngay, ngay};
            }

            if (loai.equals("Tuần")) {
                LocalDate ngay = LocalDate.parse(text, FMT);
                LocalDate dau = ngay.with(
                        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                return new LocalDate[]{dau, dau.plusDays(6)};
            }

            if (loai.equals("Tháng")) {
                YearMonth thang = YearMonth.parse(text, FMT_THANG);
                return new LocalDate[]{thang.atDay(1), thang.atEndOfMonth()};
            }

            int nam = Integer.parseInt(text);
            return new LocalDate[]{
                LocalDate.of(nam, 1, 1),
                LocalDate.of(nam, 12, 31)
            };

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Thời gian không hợp lệ!");
            return null;
        }
    }

    // Thống kê
    // baoKhongCoDuLieu = true: hiện hộp thoại khi không có dữ liệu
    private void thongKe(boolean baoKhongCoDuLieu) {

        LocalDate[] k = tinhKhoang();
        if (k == null) return;

        ArrayList<ChiTietPhieuNhapKho> ds =
                dao.getThongKeNhapKho(k[0], k[1]);

        if (ds == null) ds = new ArrayList<>();

        model.setRowCount(0);

        int gioiHan = cboSoLuong.getSelectedItem().equals(TAT_CA)
                ? Integer.MAX_VALUE
                : Integer.parseInt(cboSoLuong.getSelectedItem().toString());

        long tongSL = 0;
        double tongTien = 0;
        int dem = 0;

        for (ChiTietPhieuNhapKho ct : ds) {

            int sl = ct.getSoLuong();
            double gia = ct.getSanPham().getGiaNhap();
            double tien = sl * gia;

            tongSL += sl;
            tongTien += tien;

            if (dem < gioiHan) {
                model.addRow(new Object[]{
                    ct.getPhieuNhapKho().getMaPNK(),
                    ct.getSanPham().getMaSP(),
                    ct.getSanPham().getTenSP(),
                    sl,
                    ct.getSanPham().getSoLuongTon(),
                    DF.format(gia),
                    DF.format(tien),
                    ct.getPhieuNhapKho().getNgayLap().format(FMT)
                });
                dem++;
            }
        }

        if (cboLoai.getSelectedItem().equals(TAT_CA)) {
            lblKhoang.setText("Khoảng thời gian: Tất cả");
        } else {
            lblKhoang.setText("Khoảng thời gian: "
                    + k[0].format(FMT) + " - " + k[1].format(FMT));
        }

        lblSoLuong.setText("Tổng số lượng nhập: " + DF.format(tongSL));
        lblTongTien.setText("Tổng tiền nhập: " + DF.format(tongTien) + " VNĐ");

        if (ds.isEmpty() && baoKhongCoDuLieu)
            JOptionPane.showMessageDialog(this, "Không có dữ liệu nhập kho!");
    }

    private void xoaKetQua() {
        model.setRowCount(0);
        lblKhoang.setText("Khoảng thời gian: -");
        lblSoLuong.setText("Tổng số lượng nhập: 0");
        lblTongTien.setText("Tổng tiền nhập: 0 VNĐ");
    }

}