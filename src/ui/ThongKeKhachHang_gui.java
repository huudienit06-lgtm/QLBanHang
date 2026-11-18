package ui;

import dao.HoaDonDAO;
import dao.KhachHang_Dao;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThongKeKhachHang_gui extends JPanel {
    private final KhachHang_Dao khDao = new KhachHang_Dao();
    private final HoaDonDAO hdDao = new HoaDonDAO();
    
    private final DecimalFormat money = new DecimalFormat("#,##0");

    private final JLabel lblSoLuong = new JLabel("0", SwingConstants.CENTER);
    private final JLabel lblTrungBinh = new JLabel("0", SwingConstants.CENTER);
    private final JSpinner spnTop = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Hạng", "Mã KH", "Tên KH", "Số hóa đơn", "Tổng chi tiêu (VNĐ)"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    public ThongKeKhachHang_gui() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Color background = new Color(219, 234, 254);

        JLabel lblTitle = new JLabel("Lập Hóa Đơn");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(30, 64, 175));
        JPanel Title = new JPanel(new FlowLayout());
        Title.setBackground(background);
        Title.add(lblTitle);

        JPanel cards = new JPanel(new GridLayout(1, 2, 20, 0));
        cards.add(card("Số lượng khách hàng", lblSoLuong));
        cards.add(card("Giá trị hóa đơn trung bình (VNĐ)", lblTrungBinh));

        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.add(Title, BorderLayout.NORTH);
        north.add(cards, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTK = new JButton("Thống kê");
        ctrl.add(new JLabel("Top khách hàng chi tiêu nhiều nhất:"));
        ctrl.add(spnTop);
        ctrl.add(btnTK);

        JTable table = new JTable(model);
        table.setRowHeight(24);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(ctrl, BorderLayout.NORTH);
        bottom.add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.CENTER);

        btnTK.addActionListener(e -> thongKe());
        thongKe();
    }

    private JPanel card(String caption, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel cap = new JLabel(caption, SwingConstants.CENTER);
        cap.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        value.setFont(new Font("Segoe UI", Font.BOLD, 32));
        p.add(cap, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    public void thongKe() {
        lblSoLuong.setText(String.valueOf(khDao.getAllKhachHang().size()));
        lblTrungBinh.setText(money.format(hdDao.giaTriHoaDonTrungBinh()));

        model.setRowCount(0);
        List<Object[]> top = hdDao.topKhachHang((Integer) spnTop.getValue());
        for (Object[] r : top) {
            r[4] = money.format((Double) r[4]);
            model.addRow(r);
        }
    }
}