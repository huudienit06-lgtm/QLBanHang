package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_Dao;
import entity.KhachHang;

public class KhachHang_gui extends JPanel {
    private final KhachHang_Dao dao = new KhachHang_Dao();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã KH", "Tên KH", "SĐT", "Gmail", "Địa chỉ", "Loại KH"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField txtMa = new JTextField(), txtTen = new JTextField(), txtSdt = new JTextField(),
            txtGmail = new JTextField(), txtDiaChi = new JTextField(), txtTim = new JTextField(15);
    private final JComboBox<String> cboLoai = new JComboBox<>(new String[]{"Thường", "VIP"});

    public KhachHang_gui() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        form.add(new JLabel("Mã KH:"));    
        form.add(txtMa);
        form.add(new JLabel("Tên KH:"));   
        form.add(txtTen);
        form.add(new JLabel("SĐT:"));      
        form.add(txtSdt);
        form.add(new JLabel("Gmail:"));    
        form.add(txtGmail);
        form.add(new JLabel("Địa chỉ:"));  
        form.add(txtDiaChi);
        form.add(new JLabel("Loại KH:"));  
        form.add(cboLoai);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnLam = new JButton("Làm mới");
        JButton btnTim = new JButton("Tìm theo mã");
        btns.add(btnThem); 
        btns.add(btnSua); 
        btns.add(btnXoa); 
        btns.add(btnLam);
        btns.add(new JLabel("   Mã KH:")); 
        btns.add(txtTim); 
        btns.add(btnTim);

        JPanel north = new JPanel(new BorderLayout());
        north.add(title, BorderLayout.NORTH);
        north.add(form, BorderLayout.CENTER);
        north.add(btns, BorderLayout.SOUTH);
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
        for (KhachHang kh : dao.getAllKhachHang()) addRow(kh);
    }

    private void addRow(KhachHang kh) {
        model.addRow(new Object[]{kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getGmail(), kh.getDiaChi(), kh.getLoaiKH()});
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        txtMa.setText(String.valueOf(model.getValueAt(r, 0)));
        txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
        txtSdt.setText(String.valueOf(model.getValueAt(r, 2)));
        txtGmail.setText(String.valueOf(model.getValueAt(r, 3)));
        txtDiaChi.setText(String.valueOf(model.getValueAt(r, 4)));
        cboLoai.setSelectedItem(String.valueOf(model.getValueAt(r, 5)));
        txtMa.setEditable(false);
    }

    private void clearForm() {
        txtMa.setText(""); txtTen.setText(""); txtSdt.setText(""); txtGmail.setText(""); txtDiaChi.setText("");
        cboLoai.setSelectedIndex(0); txtMa.setEditable(true); table.clearSelection();
    }

    private boolean validateForm() {
        if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã và tên khách hàng không được để trống!");
            return false;
        }
        if (!txtSdt.getText().trim().matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 chữ số, bắt đầu bằng 0!");
            return false;
        }
        String gm = txtGmail.getText().trim();
        if (!gm.isEmpty() && !gm.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            JOptionPane.showMessageDialog(this, "Gmail không hợp lệ!");
            return false;
        }
        return true;
    }

    private KhachHang readForm() {
        return new KhachHang(txtMa.getText().trim(), txtTen.getText().trim(), txtSdt.getText().trim(),
                txtGmail.getText().trim(), txtDiaChi.getText().trim(), cboLoai.getSelectedItem().toString());
    }

    private void them() {
        if (!validateForm()) return;
        if (dao.themKhachHang(readForm())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Thêm thất bại (có thể trùng mã)!");
    }

    private void sua() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn khách hàng cần sửa!"); return; }
        if (!validateForm()) return;
        if (dao.suaKhachHang(readForm())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Sửa thất bại!");
    }

    private void xoa() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn khách hàng cần xóa!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        if (dao.xoaKhachHang(txtMa.getText().trim())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Xóa thất bại (có thể khách hàng đã có hóa đơn)!");
    }

    private void tim() {
        String ma = txtTim.getText().trim();
        if (ma.isEmpty()) { loadData(); return; }
        model.setRowCount(0);
        for (KhachHang kh : dao.timkhachHang(ma)) addRow(kh);
        if (model.getRowCount() == 0) JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
    }
}