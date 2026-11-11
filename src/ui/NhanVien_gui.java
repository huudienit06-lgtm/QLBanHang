package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.NhanVien_DAO;
import entity.NhanVien;

public class NhanVien_gui extends JPanel {
    private final NhanVien_DAO dao = new NhanVien_DAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã NV", "Tên NV", "SĐT", "Giới tính", "Chức vụ"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField txtMa = new JTextField(), txtTen = new JTextField(),
            txtSdt = new JTextField(), txtChucVu = new JTextField(), txtTim = new JTextField(15);
    private final JComboBox<String> cboGT = new JComboBox<>(new String[]{"Nam", "Nữ"});

    public NhanVien_gui() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        form.add(new JLabel("Mã NV:"));      form.add(txtMa);
        form.add(new JLabel("Tên NV:"));     form.add(txtTen);
        form.add(new JLabel("SĐT:"));        form.add(txtSdt);
        form.add(new JLabel("Giới tính:"));  form.add(cboGT);
        form.add(new JLabel("Chức vụ:"));    form.add(txtChucVu);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnThem = new JButton("Thêm"), btnSua = new JButton("Sửa"),
                btnXoa = new JButton("Xóa"), btnLam = new JButton("Làm mới"),
                btnTim = new JButton("Tìm theo mã");
        btns.add(btnThem); btns.add(btnSua); btns.add(btnXoa); btns.add(btnLam);
        btns.add(new JLabel("   Mã NV:")); btns.add(txtTim); btns.add(btnTim);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btns, BorderLayout.SOUTH);

        add(top, BorderLayout.PAGE_START);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JPanel north = new JPanel(new BorderLayout());
        north.add(title, BorderLayout.NORTH);
        north.add(top, BorderLayout.CENTER);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> { if (!e.getValueIsAdjusting()) fillForm(); });

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnLam.addActionListener(e -> { clearForm(); loadData(); });
        btnTim.addActionListener(e -> tim());

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        for (NhanVien nv : dao.getAllNhanVien()) addRow(nv);
    }

    private void addRow(NhanVien nv) {
        model.addRow(new Object[]{nv.getMaNV(), nv.getTenNV(), nv.getSdt(), nv.getGioiTinh(), nv.getChucVu()});
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        txtMa.setText(model.getValueAt(r, 0).toString());
        txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
        txtSdt.setText(String.valueOf(model.getValueAt(r, 2)));
        cboGT.setSelectedItem(String.valueOf(model.getValueAt(r, 3)));
        txtChucVu.setText(String.valueOf(model.getValueAt(r, 4)));
        txtMa.setEditable(false);
    }

    private void clearForm() {
        txtMa.setText(""); txtTen.setText(""); txtSdt.setText(""); txtChucVu.setText("");
        cboGT.setSelectedIndex(0); txtMa.setEditable(true); table.clearSelection();
    }

    private boolean validateForm() {
        if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã và tên nhân viên không được để trống!");
            return false;
        }
        if (!txtSdt.getText().trim().matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 chữ số, bắt đầu bằng 0!");
            return false;
        }
        return true;
    }

    private NhanVien readForm() {
        return new NhanVien(txtMa.getText().trim(), txtTen.getText().trim(), txtSdt.getText().trim(),
                cboGT.getSelectedItem().toString(), txtChucVu.getText().trim());
    }

    private void them() {
        if (!validateForm()) return;
        if (dao.themNhanVien(readForm())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Thêm thất bại (có thể trùng mã)!");
    }

    private void sua() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần sửa!"); return; }
        if (!validateForm()) return;
        if (dao.suaNhanVien(txtMa.getText().trim(), readForm())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Sửa thất bại!");
    }

    private void xoa() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần xóa!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        if (dao.xoaNhanVien(txtMa.getText().trim())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Xóa thất bại (có thể đang được tham chiếu bởi dữ liệu khác)!");
    }

    private void tim() {
        String ma = txtTim.getText().trim();
        if (ma.isEmpty()) { loadData(); return; }
        model.setRowCount(0);
        for (NhanVien nv : dao.timNhanVientheoMa(ma)) addRow(nv);
        if (model.getRowCount() == 0) JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
    }
}