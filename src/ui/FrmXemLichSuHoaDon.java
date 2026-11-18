package ui;

import dao.ChiTietHoaDonDAO;
import dao.HoaDonDAO;
import entity.HoaDon;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FrmXemLichSuHoaDon extends JDialog implements DocumentListener, ActionListener {

    private final JTextField txtTimTheoMa;
    private final JTextField txtTimTheoTenKH;
    private final JTextField txtTimTheoNhanVienLap;
    private final DefaultTableModel modelHoaDon;
    private final JTable tblHoaDon;
    private final HoaDonDAO hdDao = new HoaDonDAO();
    private final ChiTietHoaDonDAO ctDao = new ChiTietHoaDonDAO();
    private final JButton btnXemChiTiet;
    private final JButton btnXoaHoaDon;
    private HoaDon selectedHoaDon;
    private final JButton btnXoaTrang;

    public FrmXemLichSuHoaDon(JFrame parent) {
        super(parent, "Lịch sử hóa đơn", true);

        setTitle("Lịch sử hóa đơn");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("HÓA ĐƠN ĐÃ LẬP");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.add(lblTitle);

        JPanel pnlTimHoaDon = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTimHoaDon.setBorder(BorderFactory.createTitledBorder("Nhập để tìm kiếm hóa đơn"));

        pnlTimHoaDon.add(new JLabel("Tìm theo mã:"));
        txtTimTheoMa = new JTextField(10);
        pnlTimHoaDon.add(txtTimTheoMa);

        pnlTimHoaDon.add(new JLabel("Tìm theo tên khách hàng:"));
        txtTimTheoTenKH = new JTextField(10);
        pnlTimHoaDon.add(txtTimTheoTenKH);

        pnlTimHoaDon.add(new JLabel("Tìm theo Nhân viên lập:"));
        txtTimTheoNhanVienLap = new JTextField(10);
        pnlTimHoaDon.add(txtTimTheoNhanVienLap);

        String[] header = {"Mã hóa đơn", "Tên khách hàng", "Nhân viên lập", "Ngày lập", "Tổng tiền", "Phương thức thanh toán", "Trạng thái"};
        modelHoaDon = new DefaultTableModel(header, 0);
        tblHoaDon = new JTable(modelHoaDon);
        tblHoaDon.setRowHeight(20);
        JPanel pnlHienThiHoaDon = new JPanel(new BorderLayout(5, 10));
        pnlHienThiHoaDon.add(new JScrollPane(tblHoaDon));
        showDSHoaDon();

        JPanel pnlCenter = new JPanel(new BorderLayout(10, 10));
        pnlCenter.add(pnlTimHoaDon, BorderLayout.NORTH);
        pnlCenter.add(pnlHienThiHoaDon, BorderLayout.CENTER);

        btnXemChiTiet = new JButton("Xem chi tiết hóa đơn");
        btnXoaHoaDon = new JButton("Xóa hóa đơn");
        btnXoaTrang = new JButton("Xóa trắng");
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        pnlButton.add(btnXemChiTiet);
        pnlButton.add(btnXoaHoaDon);
        pnlButton.add(btnXoaTrang);

        add(pnlTitle, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);

        txtTimTheoMa.getDocument().addDocumentListener(this);
        txtTimTheoNhanVienLap.getDocument().addDocumentListener(this);
        txtTimTheoTenKH.getDocument().addDocumentListener(this);

        btnXemChiTiet.addActionListener(this);
        btnXoaHoaDon.addActionListener(this);
    }

    public void showDSHoaDon() {
        modelHoaDon.setRowCount(0);
        ArrayList<HoaDon> list = hdDao.getDSHoaDon();
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : list) {
            modelHoaDon.addRow(new Object[]{hd.getMaHD(), hd.getKhachHang().getTenKH(), hd.getNhanVien().getTenNV(), hd.getNgayLapFormatted(), hd.getTongTienFormatted(hd.tinhThanhTien()), hd.getPhuongThucThanhToan(), hd.getTrangThai()});
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        timKiemHoaDon();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        timKiemHoaDon();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        timKiemHoaDon();
    }

    private void timKiemHoaDon() {
        String maHD = txtTimTheoMa.getText().trim();
        String tenKH = txtTimTheoTenKH.getText().trim();
        String tenNV = txtTimTheoNhanVienLap.getText().trim();

        ArrayList<HoaDon> ds = hdDao.searchHoaDonByTuKhoa(
                maHD, tenKH, tenNV
        );

        modelHoaDon.setRowCount(0);

        for (HoaDon hd : ds) {
            modelHoaDon.addRow(new Object[]{hd.getMaHD(), hd.getKhachHang().getTenKH(), hd.getNhanVien().getTenNV(), hd.getNgayLapFormatted(), hd.getTongTienFormatted(hd.tinhThanhTien()), hd.getPhuongThucThanhToan(), hd.getTrangThai()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnXemChiTiet)) {
            setSelectedHoadonDon();
            dispose();
        }
        if (o.equals(btnXoaHoaDon)) {
            int row = tblHoaDon.getSelectedRow();
            if (JOptionPane.showConfirmDialog(this, "Bạn muốn xóa hóa đơn này?", "Xác Nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                if (!ctDao.deleteAllByMaHD(modelHoaDon.getValueAt(row, 0).toString())) {
                    JOptionPane.showMessageDialog(null, "Xóa chi tiết hóa đơn không thành công");
                    return;
                }

                if (!hdDao.deleteHoaDon(modelHoaDon.getValueAt(row, 0).toString())) {
                    JOptionPane.showMessageDialog(null, "Xóa chi tiết không thành công");
                    return;
                }
                JOptionPane.showMessageDialog(null, "Xóa thành công hóa đơn");
                showDSHoaDon();
            }
        }
        if (o.equals(btnXoaTrang)) {
            txtTimTheoMa.setText("");
            txtTimTheoNhanVienLap.setText("");
            txtTimTheoTenKH.setText("");
            showDSHoaDon();
        }
    }

    public void setSelectedHoadonDon() {
        int row = tblHoaDon.getSelectedRow();
        if (row >= 0) {
            if (JOptionPane.showConfirmDialog(this, "Bạn muốn xem chi tiết hóa đơn này?", "Xác Nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String maHD = (String) modelHoaDon.getValueAt(row, 0);
                selectedHoaDon = hdDao.getHoaDonById(maHD);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn hóa đơn cần xem");
        }

    }

    public HoaDon getSelectedHoaDon() {
        return selectedHoaDon;
    }

    private static class ChiTietHoaDon {

        public ChiTietHoaDon() {
        }
    }

}
