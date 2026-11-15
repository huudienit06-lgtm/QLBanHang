
package ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import connectdb.ConnectDB;
import dao.ChiTietPhieuNhapKho_dao;
import entity.ChiTietPhieuNhapKho;

public class ThongKeNhapKho_GUI extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FMT_NGAY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FMT_NGAY_NGAN =
            DateTimeFormatter.ofPattern("dd/MM");

    private static final DecimalFormat FMT_TIEN =
            new DecimalFormat("#,###");

    private static final String TUAN = "Tuần";
    private static final String THANG = "Tháng";
    private static final String NAM = "Năm";

    private ChiTietPhieuNhapKho_dao ctPNK_dao =
            new ChiTietPhieuNhapKho_dao();

    private JComboBox<String> cboLoai;
    private JComboBox<String> cboThoiGian;
    private JComboBox<String> cboSoLuong;

    private JButton btnThongKe;

    private JLabel lblGoiY;
    private JLabel lblKhoangThoiGian;
    private JLabel lblTongSoLuong;
    private JLabel lblTongTien;

    private JTable table;
    private DefaultTableModel model;

    private BieuDoCot bieuDo;

    // Tránh thống kê khi đang tải dữ liệu vào combobox
    private boolean dangTaiThoiGian = false;

    public ThongKeNhapKho_GUI() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(taoPanelDieuKhien(), BorderLayout.NORTH);

        bieuDo = new BieuDoCot();

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                taoPanelBang(),
                bieuDo
        );

        split.setResizeWeight(0.5);
        split.setDividerLocation(260);

        add(split, BorderLayout.CENTER);
        add(taoPanelTongKet(), BorderLayout.SOUTH);

        addEvents();

        capNhatGoiY();
        loadThoiGian();
    }

    // ================= PANEL ĐIỀU KHIỂN =================

    private JPanel taoPanelDieuKhien() {

        JPanel pnl = new JPanel(new BorderLayout());

        JLabel title = new JLabel(
                "THỐNG KÊ HÀNG NHẬP KHO",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(new Color(0, 102, 204));
        title.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0)
        );

        pnl.add(title, BorderLayout.NORTH);

        JPanel pnlLoc = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        pnlLoc.add(new JLabel("Thống kê theo:"));

        cboLoai = new JComboBox<>(
                new String[] { TUAN, THANG, NAM }
        );

        pnlLoc.add(cboLoai);

        pnlLoc.add(new JLabel("Thời gian:"));

        cboThoiGian = new JComboBox<>();
        cboThoiGian.setPreferredSize(
                new java.awt.Dimension(120, 25)
        );

        pnlLoc.add(cboThoiGian);

        lblGoiY = new JLabel();
        lblGoiY.setForeground(Color.GRAY);
        lblGoiY.setFont(
                lblGoiY.getFont().deriveFont(Font.ITALIC)
        );

        pnlLoc.add(lblGoiY);

        pnlLoc.add(new JLabel("Số lượng hiển thị:"));

        cboSoLuong = new JComboBox<>(
                new String[] {
                        "5", "10", "15", "20",
                        "25", "50", "Tất cả"
                }
        );

        pnlLoc.add(cboSoLuong);

        btnThongKe = new JButton("Thống kê");
        btnThongKe.setBackground(new Color(0, 102, 204));
        btnThongKe.setForeground(Color.WHITE);
        btnThongKe.setFocusPainted(false);

        pnlLoc.add(btnThongKe);

        pnl.add(pnlLoc, BorderLayout.CENTER);

        return pnl;
    }

    // ================= PANEL BẢNG =================

    private JScrollPane taoPanelBang() {

        String[] cols = {
                "Mã phiếu nhập kho",
                "Mã SP",
                "Tên SP",
                "Số lượng",
                "Giá nhập",
                "Thành tiền",
                "Ngày nhập"
        };

        model = new DefaultTableModel(cols, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        DefaultTableCellRenderer right =
                new DefaultTableCellRenderer();

        right.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(3)
                .setCellRenderer(right);

        table.getColumnModel().getColumn(4)
                .setCellRenderer(right);

        table.getColumnModel().getColumn(5)
                .setCellRenderer(right);

        JScrollPane sc = new JScrollPane(table);

        sc.setBorder(
                BorderFactory.createTitledBorder(
                        "Danh sách sản phẩm nhập"
                )
        );

        return sc;
    }

    // ================= PANEL TỔNG KẾT =================

    private JPanel taoPanelTongKet() {

        JPanel pnl = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 30, 5)
        );

        Font f = new Font("Arial", Font.BOLD, 14);

        lblKhoangThoiGian =
                new JLabel("Khoảng thời gian: -");

        lblTongSoLuong =
                new JLabel("Tổng số lượng nhập: 0");

        lblTongTien =
                new JLabel("Tổng tiền nhập: 0 VNĐ");

        lblTongTien.setForeground(
                new Color(200, 0, 0)
        );

        for (JLabel l : new JLabel[] {
                lblKhoangThoiGian,
                lblTongSoLuong,
                lblTongTien
        }) {

            l.setFont(f);
            pnl.add(l);
        }

        return pnl;
    }

    // ================= SỰ KIỆN =================

    private void addEvents() {

        cboLoai.addActionListener(e -> {

            capNhatGoiY();

            loadThoiGian();

            xoaKetQua();
        });

        cboThoiGian.addActionListener(e -> {

            if (!dangTaiThoiGian
                    && cboThoiGian.getSelectedItem() != null) {

                thongKe();
            }
        });

        btnThongKe.addActionListener(e -> thongKe());

        cboSoLuong.addActionListener(e -> {

            if (cboThoiGian.getSelectedItem() != null) {
                thongKe();
            }
        });
    }

    private void capNhatGoiY() {

        String loai = (String) cboLoai.getSelectedItem();

        switch (loai) {

        case TUAN:
            lblGoiY.setText("(Chọn ngày nhập kho)");
            break;

        case THANG:
            lblGoiY.setText("(Chọn tháng nhập kho)");
            break;

        default:
            lblGoiY.setText("(Chọn năm nhập kho)");
            break;
        }
    }

    // ================= LẤY THỜI GIAN TỪ DATABASE =================

    private void loadThoiGian() {

        dangTaiThoiGian = true;

        cboThoiGian.removeAllItems();

        String loai = (String) cboLoai.getSelectedItem();

        String sql =
                "SELECT DISTINCT CAST(ngayLap AS DATE) AS ngayLap "
                + "FROM PhieuNhapKho "
                + "WHERE ngayLap IS NOT NULL "
                + "ORDER BY ngayLap DESC";

        try {

            Connection con =
                    ConnectDB.getInstance().getConnection();

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                // Lưu ngày theo thứ tự mới nhất đến cũ nhất
                TreeSet<LocalDate> dsNgay =
                        new TreeSet<>(Comparator.reverseOrder());

                while (rs.next()) {

                    java.sql.Date date =
                            rs.getDate("ngayLap");

                    if (date != null) {
                        dsNgay.add(date.toLocalDate());
                    }
                }

                LinkedHashSet<String> dsThoiGian =
                        new LinkedHashSet<>();

                if (TUAN.equals(loai)) {

                    for (LocalDate ngay : dsNgay) {

                        dsThoiGian.add(
                                ngay.format(FMT_NGAY)
                        );
                    }

                } else if (THANG.equals(loai)) {

                    TreeSet<YearMonth> dsThang =
                            new TreeSet<>(Comparator.reverseOrder());

                    for (LocalDate ngay : dsNgay) {
                        dsThang.add(YearMonth.from(ngay));
                    }

                    for (YearMonth thang : dsThang) {

                        dsThoiGian.add(
                                thang.format(
                                        DateTimeFormatter.ofPattern("MM/yyyy")
                                )
                        );
                    }

                } else {

                    TreeSet<Integer> dsNam =
                            new TreeSet<>(Comparator.reverseOrder());

                    for (LocalDate ngay : dsNgay) {
                        dsNam.add(ngay.getYear());
                    }

                    for (Integer nam : dsNam) {
                        dsThoiGian.add(String.valueOf(nam));
                    }
                }

                for (String s : dsThoiGian) {
                    cboThoiGian.addItem(s);
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không thể tải thời gian nhập kho!\n" + e.getMessage(),
                    "Lỗi database",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();

        } finally {

            dangTaiThoiGian = false;
        }
    }

    // ================= XÓA KẾT QUẢ CŨ =================

    private void xoaKetQua() {

        model.setRowCount(0);

        lblKhoangThoiGian.setText("Khoảng thời gian: -");
        lblTongSoLuong.setText("Tổng số lượng nhập: 0");
        lblTongTien.setText("Tổng tiền nhập: 0 VNĐ");

        bieuDo.setDuLieu(
                "Biểu đồ tiền nhập",
                new String[0],
                new double[0]
        );
    }

    // ================= THỐNG KÊ =================

    private void thongKe() {

        LocalDate[] khoang = tinhKhoangThoiGian();

        if (khoang == null) {
            return;
        }

        String loai =
                (String) cboLoai.getSelectedItem();

        ArrayList<ChiTietPhieuNhapKho> ds =
                ctPNK_dao.getThongKeNhapKho(
                        khoang[0],
                        khoang[1]
                );

        if (ds == null) {
            ds = new ArrayList<>();
        }

        int gioiHan = Integer.MAX_VALUE;

        String chon =
                (String) cboSoLuong.getSelectedItem();

        if (chon != null && !"Tất cả".equals(chon)) {
            gioiHan = Integer.parseInt(chon);
        }

        // Chuẩn bị nhãn cho biểu đồ

        String[] nhan;

        if (NAM.equals(loai)) {

            nhan = new String[12];

            for (int i = 0; i < 12; i++) {
                nhan[i] = "T" + (i + 1);
            }

        } else if (THANG.equals(loai)) {

            nhan = new String[
                    khoang[1].getDayOfMonth()
            ];

            for (int i = 0; i < nhan.length; i++) {
                nhan[i] = String.valueOf(i + 1);
            }

        } else {

            nhan = new String[7];

            for (int i = 0; i < 7; i++) {

                nhan[i] = khoang[0]
                        .plusDays(i)
                        .format(FMT_NGAY_NGAN);
            }
        }

        double[] giaTri = new double[nhan.length];

        model.setRowCount(0);

        long tongSoLuong = 0;

        double tongTien = 0;

        int daHienThi = 0;

        for (ChiTietPhieuNhapKho ct : ds) {

            int soLuong = ct.getSoLuong();

            double giaNhap = ct.getSanPham().getGiaNhap();

            double thanhTien = giaNhap * soLuong;

            LocalDate ngay =
                    ct.getPhieuNhapKho().getNgayLap();

            // Tính tổng trên toàn bộ dữ liệu

            tongSoLuong += soLuong;

            tongTien += thanhTien;

            // Xác định vị trí trên biểu đồ

            int viTri;

            if (NAM.equals(loai)) {

                viTri = ngay.getMonthValue() - 1;

            } else if (THANG.equals(loai)) {

                viTri = ngay.getDayOfMonth() - 1;

            } else {

                viTri = (int) ChronoUnit.DAYS.between(
                        khoang[0],
                        ngay
                );
            }

            if (viTri >= 0 && viTri < giaTri.length) {
                giaTri[viTri] += thanhTien;
            }

            // Chỉ hiển thị số dòng được chọn

            if (daHienThi < gioiHan) {

                model.addRow(new Object[] {

                        ct.getPhieuNhapKho().getMaPNK(),

                        ct.getSanPham().getMaSP(),

                        ct.getSanPham().getTenSP(),

                        soLuong,

                        FMT_TIEN.format(giaNhap),

                        FMT_TIEN.format(thanhTien),

                        ngay.format(FMT_NGAY)
                });

                daHienThi++;
            }
        }

        // Tiêu đề biểu đồ

        String tieuDe;

        if (NAM.equals(loai)) {

            tieuDe = "Tiền nhập theo tháng - năm "
                    + khoang[0].getYear();

        } else if (THANG.equals(loai)) {

            tieuDe = "Tiền nhập theo ngày - tháng "
                    + khoang[0].format(
                            DateTimeFormatter.ofPattern("MM/yyyy")
                    );

        } else {

            tieuDe = "Tiền nhập theo ngày - tuần "
                    + khoang[0].format(FMT_NGAY)
                    + " đến "
                    + khoang[1].format(FMT_NGAY);
        }

        bieuDo.setDuLieu(tieuDe, nhan, giaTri);

        // Cập nhật tổng kết

        lblKhoangThoiGian.setText(
                "Khoảng thời gian: "
                + khoang[0].format(FMT_NGAY)
                + " - "
                + khoang[1].format(FMT_NGAY)
        );

        lblTongSoLuong.setText(
                "Tổng số lượng nhập: "
                + FMT_TIEN.format(tongSoLuong)
        );

        lblTongTien.setText(
                "Tổng tiền nhập: "
                + FMT_TIEN.format(tongTien)
                + " VNĐ"
        );

        if (ds.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không có dữ liệu nhập kho trong khoảng thời gian này."
            );
        }
    }

    // ================= XÁC ĐỊNH KHOẢNG THỜI GIAN =================

    private LocalDate[] tinhKhoangThoiGian() {

        String loai =
                (String) cboLoai.getSelectedItem();

        Object selected =
                cboThoiGian.getSelectedItem();

        if (selected == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không có thời gian nhập kho!"
            );

            return null;
        }

        String text = selected.toString();

        try {

            switch (loai) {

            case TUAN: {

                LocalDate ngay =
                        LocalDate.parse(text, FMT_NGAY);

                LocalDate dauTuan =
                        ngay.with(
                                TemporalAdjusters.previousOrSame(
                                        DayOfWeek.MONDAY
                                )
                        );

                return new LocalDate[] {
                        dauTuan,
                        dauTuan.plusDays(6)
                };
            }

            case THANG: {

                YearMonth ym =
                        YearMonth.parse(
                                text,
                                DateTimeFormatter.ofPattern("MM/yyyy")
                        );

                return new LocalDate[] {
                        ym.atDay(1),
                        ym.atEndOfMonth()
                };
            }

            default: {

                int nam = Integer.parseInt(text);

                return new LocalDate[] {
                        LocalDate.of(nam, 1, 1),
                        LocalDate.of(nam, 12, 31)
                };
            }
            }

        } catch (DateTimeParseException
                | NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Thời gian không hợp lệ!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }
    }

    // ================= BIỂU ĐỒ CỘT =================

    private static class BieuDoCot extends JPanel {

        private static final long serialVersionUID = 1L;

        private static final int LE_TRAI = 70;
        private static final int LE_PHAI = 20;
        private static final int LE_TREN = 40;
        private static final int LE_DUOI = 35;

        private String tieuDe = "Biểu đồ tiền nhập";

        private String[] nhan = new String[0];

        private double[] giaTri = new double[0];

        BieuDoCot() {

            setBackground(Color.WHITE);

            setBorder(
                    BorderFactory.createTitledBorder("Biểu đồ")
            );

            setPreferredSize(
                    new java.awt.Dimension(400, 250)
            );

            addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseMoved(MouseEvent e) {

                    int i = viTriCot(e.getX());

                    if (i >= 0) {

                        setToolTipText(
                                nhan[i] + ": "
                                + FMT_TIEN.format(giaTri[i])
                                + " VNĐ"
                        );

                    } else {

                        setToolTipText(null);
                    }
                }
            });
        }

        void setDuLieu(
                String tieuDe,
                String[] nhan,
                double[] giaTri
        ) {

            this.tieuDe = tieuDe;
            this.nhan = nhan;
            this.giaTri = giaTri;

            repaint();
        }

        private int viTriCot(int x) {

            if (nhan.length == 0) {
                return -1;
            }

            double rong =
                    (double) (getWidth() - LE_TRAI - LE_PHAI)
                    / nhan.length;

            if (rong <= 0) {
                return -1;
            }

            int i = (int) ((x - LE_TRAI) / rong);

            return (x >= LE_TRAI
                    && i >= 0
                    && i < nhan.length) ? i : -1;
        }

        private static String rutGon(double v) {

            if (v >= 1e9) {
                return trimSo(v / 1e9) + " tỷ";
            }

            if (v >= 1e6) {
                return trimSo(v / 1e6) + " tr";
            }

            if (v >= 1e3) {
                return trimSo(v / 1e3) + "k";
            }

            return trimSo(v);
        }

        private static String trimSo(double v) {

            return new DecimalFormat("#.#").format(v);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            int vungW = w - LE_TRAI - LE_PHAI;
            int vungH = h - LE_TREN - LE_DUOI;

            // Tiêu đề

            g2.setFont(new Font("Arial", Font.BOLD, 14));

            g2.setColor(new Color(0, 102, 204));

            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(
                    tieuDe,
                    (w - fm.stringWidth(tieuDe)) / 2,
                    22
            );

            if (nhan.length == 0 || vungW <= 0 || vungH <= 0) {

                g2.setColor(Color.GRAY);

                g2.setFont(
                        new Font("Arial", Font.ITALIC, 13)
                );

                String s =
                        "Chưa có dữ liệu. Hãy chọn thời gian và bấm Thống kê.";

                g2.drawString(
                        s,
                        (w - g2.getFontMetrics().stringWidth(s)) / 2,
                        h / 2
                );

                g2.dispose();

                return;
            }

            // Tìm giá trị lớn nhất

            double max = 0;

            for (double v : giaTri) {
                max = Math.max(max, v);
            }

            double maxTruc = lamTronTren(max);

            // Vẽ lưới ngang và trục Y

            g2.setFont(
                    new Font("Arial", Font.PLAIN, 11)
            );

            fm = g2.getFontMetrics();

            int soVach = 5;

            for (int i = 0; i <= soVach; i++) {

                int y = LE_TREN + vungH
                        - (int) (vungH * i / (double) soVach);

                g2.setColor(new Color(225, 225, 225));

                g2.drawLine(
                        LE_TRAI,
                        y,
                        LE_TRAI + vungW,
                        y
                );

                g2.setColor(Color.DARK_GRAY);

                String s = rutGon(
                        maxTruc * i / soVach
                );

                g2.drawString(
                        s,
                        LE_TRAI - 8 - fm.stringWidth(s),
                        y + fm.getAscent() / 2 - 1
                );
            }

            // Vẽ trục

            g2.setColor(Color.DARK_GRAY);

            g2.setStroke(new BasicStroke(1.5f));

            g2.drawLine(
                    LE_TRAI,
                    LE_TREN,
                    LE_TRAI,
                    LE_TREN + vungH
            );

            g2.drawLine(
                    LE_TRAI,
                    LE_TREN + vungH,
                    LE_TRAI + vungW,
                    LE_TREN + vungH
            );

            // Vẽ các cột

            double rongO =
                    (double) vungW / nhan.length;

            int rongCot =
                    (int) Math.max(2, rongO * 0.6);

            boolean duCho = rongO >= 34;

            for (int i = 0; i < nhan.length; i++) {

                int cao = maxTruc == 0
                        ? 0
                        : (int) (vungH * giaTri[i] / maxTruc);

                int x = LE_TRAI + (int) (
                        rongO * i + (rongO - rongCot) / 2
                );

                int y = LE_TREN + vungH - cao;

                if (cao > 0) {

                    g2.setColor(new Color(0, 122, 204));

                    g2.fillRect(x, y, rongCot, cao);

                    if (duCho) {

                        g2.setColor(Color.DARK_GRAY);

                        String s = rutGon(giaTri[i]);

                        g2.drawString(
                                s,
                                x + (rongCot - fm.stringWidth(s)) / 2,
                                y - 4
                        );
                    }
                }

                // Nhãn trục X

                int buoc = (int) Math.max(
                        1,
                        Math.ceil(
                                fm.stringWidth(nhan[i]) * 1.4 / rongO
                        )
                );

                if (i % buoc == 0) {

                    g2.setColor(Color.DARK_GRAY);

                    g2.drawString(
                            nhan[i],
                            x + (rongCot - fm.stringWidth(nhan[i])) / 2,
                            LE_TREN + vungH + 16
                    );
                }
            }

            g2.dispose();
        }

        // Làm tròn trục Y theo mốc 1, 2, 5 x 10^n

        private static double lamTronTren(double max) {

            if (max <= 0) {
                return 1;
            }

            double mu = Math.pow(
                    10,
                    Math.floor(Math.log10(max))
            );

            double chuan = max / mu;

            double dep;

            if (chuan <= 1) {
                dep = 1;
            } else if (chuan <= 2) {
                dep = 2;
            } else if (chuan <= 5) {
                dep = 5;
            } else {
                dep = 10;
            }

            return dep * mu;
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            try {

                ConnectDB.getInstance().connect();

            } catch (Exception e) {

                e.printStackTrace();

                JOptionPane.showMessageDialog(
                        null,
                        "Không thể kết nối database!"
                );

                return;
            }

            JFrame f = new JFrame("Thống kê nhập kho");

            f.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            f.setSize(1000, 700);

            f.setLocationRelativeTo(null);

            f.setContentPane(
                    new ThongKeNhapKho_GUI()
            );

            f.setVisible(true);
        });
    }
}
