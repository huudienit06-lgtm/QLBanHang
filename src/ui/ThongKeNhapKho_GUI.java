package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import connectdb.ConnectDB;
import dao.ChiTietPhieuNhapKho_dao;
import entity.ChiTietPhieuNhapKho;

public class ThongKeNhapKho_GUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FMT_NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DecimalFormat FMT_TIEN = new DecimalFormat("#,###");

    private static final String TUAN = "Tuần";
    private static final String THANG = "Tháng";
    private static final String NAM = "Năm";
    private static final String TAT_CA = "Tất cả";

    private ChiTietPhieuNhapKho_dao ctPNK_dao = new ChiTietPhieuNhapKho_dao();

    private JComboBox<String> cboLoai;
    private JTextField txtThoiGian;
    private JLabel lblGoiY;
    private JComboBox<String> cboSoLuong;
    private JButton btnThongKe;

    private JLabel lblKhoangThoiGian;
    private JLabel lblTongSoLuong;
    private JLabel lblTongTien;

    private JTable table;
    private DefaultTableModel model;

    public ThongKeNhapKho_GUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(taoPanelDieuKhien(), BorderLayout.NORTH);
        add(taoPanelBang(), BorderLayout.CENTER);
        add(taoPanelTongKet(), BorderLayout.SOUTH);

        addEvents();
        capNhatGoiY();
    }

    private JPanel taoPanelDieuKhien() {
        JPanel pnl = new JPanel(new BorderLayout());

        JLabel title = new JLabel("THỐNG KÊ HÀNG NHẬP KHO", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        pnl.add(title, BorderLayout.NORTH);

        JPanel pnlLoc = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        pnlLoc.add(new JLabel("Thống kê theo:"));
        cboLoai = new JComboBox<>(new String[] { TUAN, THANG, NAM });
        pnlLoc.add(cboLoai);

        pnlLoc.add(new JLabel("Thời gian:"));
        txtThoiGian = new JTextField(10);
        pnlLoc.add(txtThoiGian);

        lblGoiY = new JLabel();
        lblGoiY.setForeground(Color.GRAY);
        lblGoiY.setFont(lblGoiY.getFont().deriveFont(Font.ITALIC));
        pnlLoc.add(lblGoiY);

        pnlLoc.add(new JLabel("Số lượng hiển thị:"));
        cboSoLuong = new JComboBox<>(new String[] { "5", "10", "15", "20", "25", "50", TAT_CA });
        pnlLoc.add(cboSoLuong);

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setBackground(new Color(0, 102, 204));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);
        pnlLoc.add(btnThongKe);

        pnl.add(pnlLoc, BorderLayout.CENTER);
        return pnl;
    }

    private JScrollPane taoPanelBang() {
        String[] cols = { "Mã phiếu nhập kho", "Mã SP", "Tên SP", "Số lượng", "Giá nhập", "Ngày nhập" };
        model = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(3).setCellRenderer(right);
        table.getColumnModel().getColumn(4).setCellRenderer(right);

        JScrollPane sc = new JScrollPane(table);
        sc.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm nhập"));
        return sc;
    }

    private JPanel taoPanelTongKet() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 5));
        Font f = new Font("Arial", Font.BOLD, 14);

        lblKhoangThoiGian = new JLabel("Khoảng thời gian: -");
        lblTongSoLuong = new JLabel("Tổng số lượng nhập: 0");
        lblTongTien = new JLabel("Tổng tiền nhập: 0 VNĐ");
        lblTongTien.setForeground(new Color(200, 0, 0));

        for (JLabel l : new JLabel[] { lblKhoangThoiGian, lblTongSoLuong, lblTongTien }) {
            l.setFont(f);
            pnl.add(l);
        }
        return pnl;
    }

    private void addEvents() {
        cboLoai.addActionListener(e -> {
            capNhatGoiY();
            txtThoiGian.setText("");
        });
        btnThongKe.addActionListener(e -> thongKe());
        txtThoiGian.addActionListener(e -> thongKe());
        cboSoLuong.addActionListener(e -> {
            if (!txtThoiGian.getText().trim().isEmpty()) {
                thongKe();
            }
        });
    }

    private void capNhatGoiY() {
        String loai = (String) cboLoai.getSelectedItem();
        switch (loai) {
        case TUAN:
            lblGoiY.setText("(nhập 1 ngày bất kỳ trong tuần: dd/MM/yyyy)");
            break;
        case THANG:
            lblGoiY.setText("(nhập tháng: MM/yyyy)");
            break;
        default:
            lblGoiY.setText("(nhập năm: yyyy)");
        }
    }

    private void thongKe() {
        // khoang[0] = từ ngày, khoang[1] = đến ngày (đều tính cả 2 đầu)
        LocalDate[] khoang = tinhKhoangThoiGian();
        if (khoang == null) {
            return;
        }

        ArrayList<ChiTietPhieuNhapKho> ds = ctPNK_dao.getThongKeNhapKho(khoang[0], khoang[1]);

        int gioiHan = Integer.MAX_VALUE;
        String chon = (String) cboSoLuong.getSelectedItem();
        if (!TAT_CA.equals(chon)) {
            gioiHan = Integer.parseInt(chon);
        }

        model.setRowCount(0);
        long tongSoLuong = 0;
        BigDecimal tongTien = BigDecimal.ZERO;
        int daHienThi = 0;

        for (ChiTietPhieuNhapKho ct : ds) {
            int soLuong = ct.getSoLuong();
            BigDecimal giaNhap = ct.getSanPham().getGiaNhap();

            // Tổng tính trên toàn bộ dữ liệu trong khoảng thời gian
            tongSoLuong += soLuong;
            tongTien = tongTien.add(giaNhap.multiply(BigDecimal.valueOf(soLuong)));

            // Bảng chỉ hiển thị N dòng đầu
            if (daHienThi < gioiHan) {
                model.addRow(new Object[] {
                        ct.getPhieuNhapKho().getMaPNK(),
                        ct.getSanPham().getMaSP(),
                        ct.getSanPham().getTenSP(),
                        soLuong,
                        FMT_TIEN.format(giaNhap),
                        ct.getPhieuNhapKho().getNgayLap().format(FMT_NGAY) });
                daHienThi++;
            }
        }

        lblKhoangThoiGian.setText("Khoảng thời gian: " + khoang[0].format(FMT_NGAY) + " - " + khoang[1].format(FMT_NGAY));
        lblTongSoLuong.setText("Tổng số lượng nhập: " + FMT_TIEN.format(tongSoLuong));
        lblTongTien.setText("Tổng tiền nhập: " + FMT_TIEN.format(tongTien) + " VNĐ");

        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu nhập kho trong khoảng thời gian này.");
        }
    }

    private LocalDate[] tinhKhoangThoiGian() {
        String loai = (String) cboLoai.getSelectedItem();
        String text = txtThoiGian.getText().trim();

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thời gian cần thống kê!");
            txtThoiGian.requestFocus();
            return null;
        }

        try {
            switch (loai) {
            case TUAN: {
                LocalDate ngay = LocalDate.parse(text, FMT_NGAY);
                LocalDate dauTuan = ngay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                return new LocalDate[] { dauTuan, dauTuan.plusDays(6) };
            }
            case THANG: {
                YearMonth ym = YearMonth.parse(text, DateTimeFormatter.ofPattern("MM/yyyy"));
                return new LocalDate[] { ym.atDay(1), ym.atEndOfMonth() };
            }
            default: {
                int nam = Integer.parseInt(text);
                if (nam < 1900 || nam > 2999) {
                    throw new NumberFormatException();
                }
                return new LocalDate[] { LocalDate.of(nam, 1, 1), LocalDate.of(nam, 12, 31) };
            }
            }
        } catch (DateTimeParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Thời gian không hợp lệ!\n" + lblGoiY.getText(), "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            txtThoiGian.requestFocus();
            return null;
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                ConnectDB.getInstance().connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JFrame f = new JFrame("Thống kê nhập kho");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(950, 550);
            f.setLocationRelativeTo(null);
            f.setContentPane(new ThongKeNhapKho_GUI());
            f.setVisible(true);
        });
    }
}
