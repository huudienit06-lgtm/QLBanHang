package ui;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.ThongKeKhachHang_Dao;

public class ThongKeKhachHang_gui extends JPanel {
    private final ThongKeKhachHang_Dao dao = new ThongKeKhachHang_Dao();
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

        JLabel title = new JLabel("THỐNG KÊ KHÁCH HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel cards = new JPanel(new GridLayout(1, 2, 20, 0));
        cards.add(card("Số lượng khách hàng", lblSoLuong, new Color(52, 152, 219)));
        cards.add(card("Giá trị hóa đơn trung bình (VNĐ)", lblTrungBinh, new Color(46, 204, 113)));

        JPanel north = new JPanel(new BorderLayout(0, 10));
        north.add(title, BorderLayout.NORTH);
        north.add(cards, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTK = new JButton("Thống kê");
        ctrl.add(new JLabel("Top khách hàng mua nhiều nhất:"));
        ctrl.add(spnTop);
        ctrl.add(btnTK);
        bottom.add(ctrl, BorderLayout.NORTH);
        JTable table = new JTable(model);
        table.setRowHeight(24);
        bottom.add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.CENTER);

        btnTK.addActionListener(e -> thongKe());
        thongKe();
    }

    private JPanel card(String caption, JLabel value, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(color);
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel cap = new JLabel(caption, SwingConstants.CENTER);
        cap.setForeground(Color.WHITE);
        cap.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        value.setForeground(Color.WHITE);
        value.setFont(new Font("Segoe UI", Font.BOLD, 32));
        p.add(cap, BorderLayout.NORTH);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    public void thongKe() {
        lblSoLuong.setText(String.valueOf(dao.demKhachHang()));
        lblTrungBinh.setText(money.format(dao.giaTriHoaDonTrungBinh()));

        model.setRowCount(0);
        List<Object[]> top = dao.topKhachHang((Integer) spnTop.getValue());
        for (Object[] r : top) {
            r[4] = money.format((Double) r[4]);
            model.addRow(r);
        }
    }
}