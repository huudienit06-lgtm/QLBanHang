
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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
public class ThongKeNhapKho_GUI extends JPanel {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DecimalFormat DF =
            new DecimalFormat("#,###");

    private JComboBox<String> cboLoai, cboThoiGian, cboSoLuong;
    private JLabel lblKhoang, lblSoLuong, lblTongTien;
    private JTable table;
    private DefaultTableModel model;

    private ChiTietPhieuNhapKho_dao dao =
            new ChiTietPhieuNhapKho_dao();

    private boolean dangTai = false;

    public ThongKeNhapKho_GUI() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("THỐNG KÊ HÀNG NHẬP KHO",
                SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.BLUE);

        cboLoai = new JComboBox<>(new String[]{"Tuần", "Tháng", "Năm"});
        cboThoiGian = new JComboBox<>();
        cboSoLuong = new JComboBox<>(
                new String[]{"5", "10", "15", "20", "25", "50", "Tất cả"});

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
        pnlTop.add(title, BorderLayout.NORTH);
        pnlTop.add(pnlLoc, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
            "Mã PNK", "Mã SP", "Tên SP", "Số lượng",
            "Giá nhập", "Thành tiền", "Ngày nhập"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);

        lblKhoang = new JLabel("Khoảng thời gian: -");
        lblSoLuong = new JLabel("Tổng số lượng nhập: 0");
        lblTongTien = new JLabel("Tổng tiền nhập: 0 VNĐ");
        lblTongTien.setForeground(Color.RED);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 5));
        pnlBottom.add(lblKhoang);
        pnlBottom.add(lblSoLuong);
        pnlBottom.add(lblTongTien);

        add(pnlBottom, BorderLayout.SOUTH);

        cboLoai.addActionListener(e -> {
            loadThoiGian();
            xoaKetQua();
        });

        cboThoiGian.addActionListener(e -> {
            if (!dangTai && cboThoiGian.getSelectedItem() != null)
                thongKe();
        });

        cboSoLuong.addActionListener(e -> thongKe());
        btnThongKe.addActionListener(e -> thongKe());

        loadThoiGian();
    }

    // Lấy thời gian từ database
    private void loadThoiGian() {

        dangTai = true;
        cboThoiGian.removeAllItems();

        String loai = cboLoai.getSelectedItem().toString();

        String sql = "SELECT DISTINCT CAST(ngayLap AS DATE) ngayLap "
                   + "FROM PhieuNhapKho WHERE ngayLap IS NOT NULL";

        try {
            Connection con = ConnectDB.getInstance().getConnection();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                TreeSet<String> ds = new TreeSet<>(
                        Comparator.reverseOrder());

                while (rs.next()) {
                    LocalDate ngay = rs.getDate("ngayLap").toLocalDate();

                    if (loai.equals("Tuần"))
                        ds.add(ngay.format(FMT));
                    else if (loai.equals("Tháng"))
                        ds.add(ngay.format(
                                DateTimeFormatter.ofPattern("MM/yyyy")));
                    else
                        ds.add(String.valueOf(ngay.getYear()));
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

        if (cboThoiGian.getSelectedItem() == null)
            return null;

        String loai = cboLoai.getSelectedItem().toString();
        String text = cboThoiGian.getSelectedItem().toString();

        try {
            if (loai.equals("Tuần")) {
                LocalDate ngay = LocalDate.parse(text, FMT);
                LocalDate dau = ngay.with(
                        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                return new LocalDate[]{dau, dau.plusDays(6)};
            }

            if (loai.equals("Tháng")) {
                YearMonth thang = YearMonth.parse(
                        text, DateTimeFormatter.ofPattern("MM/yyyy"));
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
    private void thongKe() {

        LocalDate[] k = tinhKhoang();
        if (k == null) return;

        ArrayList<ChiTietPhieuNhapKho> ds =
                dao.getThongKeNhapKho(k[0], k[1]);

        if (ds == null) ds = new ArrayList<>();

        model.setRowCount(0);

        int gioiHan = cboSoLuong.getSelectedItem().equals("Tất cả")
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
                    DF.format(gia),
                    DF.format(tien),
                    ct.getPhieuNhapKho().getNgayLap().format(FMT)
                });
                dem++;
            }
        }

        lblKhoang.setText("Khoảng thời gian: "
                + k[0].format(FMT) + " - " + k[1].format(FMT));

        lblSoLuong.setText("Tổng số lượng nhập: " + DF.format(tongSL));
        lblTongTien.setText("Tổng tiền nhập: " + DF.format(tongTien) + " VNĐ");

        if (ds.isEmpty())
            JOptionPane.showMessageDialog(this, "Không có dữ liệu nhập kho!");
    }

    private void xoaKetQua() {
        model.setRowCount(0);
        lblKhoang.setText("Khoảng thời gian: -");
        lblSoLuong.setText("Tổng số lượng nhập: 0");
        lblTongTien.setText("Tổng tiền nhập: 0 VNĐ");
    }
//    public static void main(String[] args) {
//     JFrame frame = new JFrame("Thống kê nhập kho");
//     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//     frame.setSize(1000, 600);
//     frame.setLocationRelativeTo(null);
//     frame.add(new ThongKeNhapKho_GUI());
//     frame.setVisible(true);
// }
}
