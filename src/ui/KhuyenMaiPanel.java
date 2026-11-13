package ui;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class KhuyenMaiPanel extends JPanel {

    private final KhuyenMaiDAO dao = new KhuyenMaiDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Mã KM", "Tên khuyến mãi", "Tỉ lệ giảm", "Loại KM", "Ngày bắt đầu", "Ngày kết thúc",
                    "Trạng thái" },
            0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField txtMa = new JTextField(10);
    private final JTextField txtTen = new JTextField(20);
    private final JTextField txtTiLe = new JTextField(8);
    private final JTextField txtLoai = new JTextField(14);
    private final JTextField txtNgayBD = new JTextField(10);
    private final JTextField txtNgayKT = new JTextField(10);
    private final JComboBox<String> cboTrangThai = new JComboBox<>(
            new String[] { "Đang áp dụng", "Ngừng áp dụng", "Hết hạn" });
    private final JTextField txtTim = new JTextField(20);

    public KhuyenMaiPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cboTrangThai.setEditable(true);

        JPanel dong1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dong1.add(new JLabel("Mã KM:"));
        dong1.add(txtMa);
        dong1.add(new JLabel("Tên KM:"));
        dong1.add(txtTen);
        dong1.add(new JLabel("Tỉ lệ giảm:"));
        dong1.add(txtTiLe);
        dong1.add(new JLabel("Loại KM:"));
        dong1.add(txtLoai);

        JPanel dong2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dong2.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"));
        dong2.add(txtNgayBD);
        dong2.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"));
        dong2.add(txtNgayKT);
        dong2.add(new JLabel("Trạng thái:"));
        dong2.add(cboTrangThai);

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xóa trắng");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlNut.add(btnThem);
        pnlNut.add(btnSua);
        pnlNut.add(btnXoa);
        pnlNut.add(btnXoaTrang);

        JButton btnTim = new JButton("Tìm");
        JButton btnTatCa = new JButton("Hiện tất cả");
        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTim.add(new JLabel("Từ khóa (mã/tên):"));
        pnlTim.add(txtTim);
        pnlTim.add(btnTim);
        pnlTim.add(btnTatCa);

        JPanel pnlBac = new JPanel(new GridLayout(0, 1));
        pnlBac.add(dong1);
        pnlBac.add(dong2);
        pnlBac.add(pnlNut);
        pnlBac.add(pnlTim);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(pnlBac, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtMa.setText(String.valueOf(model.getValueAt(r, 0)));
                    txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
                    txtTiLe.setText(String.valueOf(model.getValueAt(r, 2)));
                    txtLoai.setText(String.valueOf(model.getValueAt(r, 3)));
                    txtNgayBD.setText(String.valueOf(model.getValueAt(r, 4)));
                    txtNgayKT.setText(String.valueOf(model.getValueAt(r, 5)));
                    cboTrangThai.setSelectedItem(String.valueOf(model.getValueAt(r, 6)));
                    txtMa.setEditable(false);
                }
            }
        });

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnXoaTrang.addActionListener(e -> xoaTrang());
        btnTim.addActionListener(e -> {
            String tk = txtTim.getText().trim();
            napBang(tk.isEmpty() ? dao.getAllKhuyenMai() : dao.timKhuyenMai(tk));
        });
        btnTatCa.addActionListener(e -> {
            txtTim.setText("");
            lamMoi();
        });

        lamMoi();
    }

    public void lamMoi() {
        napBang(dao.getAllKhuyenMai());
        xoaTrang();
    }

    private void napBang(ArrayList<KhuyenMai> ds) {
        model.setRowCount(0);
        for (KhuyenMai km : ds) {
            model.addRow(new Object[] { km.getMaKM(), km.getTenKM(), km.getTiLeGiamGia(), km.getDonViTinh(),
                    km.getNgayBatDau(), km.getNgayKetThuc(), km.getTrangThai() });
        }
    }

    private void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtTiLe.setText("");
        txtLoai.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        cboTrangThai.setSelectedIndex(0);
        txtMa.setEditable(true);
        table.clearSelection();
        txtMa.requestFocus();
    }

    /** Đọc dữ liệu từ form, trả về null (và báo lỗi) nếu dữ liệu không hợp lệ. */
    private KhuyenMai docForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        String loai = txtLoai.getText().trim();
        if (ma.isEmpty() || ten.isEmpty() || txtTiLe.getText().trim().isEmpty() || loai.isEmpty()
                || txtNgayBD.getText().trim().isEmpty() || txtNgayKT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khuyến mãi!");
            return null;
        }
        if (ma.length() > 10 || ten.length() > 30 || loai.length() > 20) {
            JOptionPane.showMessageDialog(this, "Mã tối đa 10 ký tự, tên tối đa 30 ký tự, loại tối đa 20 ký tự!");
            return null;
        }
        double tiLe;
        try {
            tiLe = Double.parseDouble(txtTiLe.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tỉ lệ giảm phải là số!");
            return null;
        }
        if (tiLe < 0) {
            JOptionPane.showMessageDialog(this, "Tỉ lệ giảm không được âm!");
            return null;
        }
        LocalDate bd;
        LocalDate kt;
        try {
            bd = LocalDate.parse(txtNgayBD.getText().trim());
            kt = LocalDate.parse(txtNgayKT.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày phải theo định dạng yyyy-MM-dd (ví dụ 2026-11-30)!");
            return null;
        }
        if (kt.isBefore(bd)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
            return null;
        }
        String trangThai = String.valueOf(cboTrangThai.getSelectedItem()).trim();
        if (trangThai.length() > 20) {
            JOptionPane.showMessageDialog(this, "Trạng thái tối đa 20 ký tự!");
            return null;
        }
        return new KhuyenMai(ma, ten, tiLe, loai, bd, kt, trangThai);
    }

    private void them() {
        KhuyenMai km = docForm();
        if (km == null) {
            return;
        }
        if (dao.themKhuyenMai(km)) {
            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (mã khuyến mãi có thể đã tồn tại)!");
        }
    }

    private void sua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn khuyến mãi cần sửa!");
            return;
        }
        KhuyenMai km = docForm();
        if (km == null) {
            return;
        }
        if (dao.suaKhuyenMai(km)) {
            JOptionPane.showMessageDialog(this, "Sửa khuyến mãi thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
        }
    }

    private void xoa() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn khuyến mãi cần xóa!");
            return;
        }
        String ma = String.valueOf(model.getValueAt(r, 0));
        int chon = JOptionPane.showConfirmDialog(this, "Xóa khuyến mãi " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        if (dao.xoaKhuyenMai(ma)) {
            JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Không xóa được (khuyến mãi đang được sản phẩm hoặc hóa đơn sử dụng)!");
        }
    }
}
