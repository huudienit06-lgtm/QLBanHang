package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import dao.TaiKhoan_Dao;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_gui extends JPanel {
    private final TaiKhoan_Dao dao = new TaiKhoan_Dao();
    private ArrayList<TaiKhoan> ds = new ArrayList<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã NV", "Mật khẩu", "Trạng thái"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField txtMa = new JTextField();
    private final JPasswordField txtMk = new JPasswordField();
    private final JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Khóa"});

    public TaiKhoan_gui() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        cboTrangThai.setEditable(true); // cho phép nhập đúng giá trị trạng thái trong CSDL của bạn

        JPanel form = new JPanel(new GridLayout(2, 4, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Thông tin tài khoản"));
        form.add(new JLabel("Mã NV (tên đăng nhập):")); 
        form.add(txtMa);
        form.add(new JLabel("Mật khẩu:"));              
        form.add(txtMk);
        form.add(new JLabel("Trạng thái:"));            
        form.add(cboTrangThai);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnDoiMk = new JButton("Đổi mật khẩu");
        JButton btnLam = new JButton("Làm mới");
        btns.add(btnThem); 
        btns.add(btnSua); 
        btns.add(btnXoa); 
        btns.add(btnDoiMk); 
        btns.add(btnLam);
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
        btnDoiMk.addActionListener(e -> doiMatKhau());
        btnLam.addActionListener(e -> { clearForm(); loadData(); });

        loadData();
    }

    private void loadData() {
        ds = dao.getallTaiKhoan();
        model.setRowCount(0);
        for (TaiKhoan tk : ds) {
            model.addRow(new Object[]{tk.getNhanVien().getMaNV(), "••••••••", tk.getTrangThai()});
        }
    }

    private void fillForm() {
        int r = table.getSelectedRow();
        if (r < 0 || r >= ds.size()) return;
        TaiKhoan tk = ds.get(r);
        txtMa.setText(tk.getNhanVien().getMaNV());
        txtMk.setText(tk.getMatKhau());
        cboTrangThai.setSelectedItem(tk.getTrangThai());
        txtMa.setEditable(false);
    }

    private void clearForm() {
        txtMa.setText(""); txtMk.setText(""); cboTrangThai.setSelectedIndex(0);
        txtMa.setEditable(true); table.clearSelection();
    }

    private boolean validateForm() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống!");
            return false;
        }
        if (new String(txtMk.getPassword()).length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }
        return true;
    }

    private TaiKhoan readForm() {
        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMa.getText().trim());
        return new TaiKhoan(nv, new String(txtMk.getPassword()), String.valueOf(cboTrangThai.getSelectedItem()));
    }

    private void them() {
        if (!validateForm()) return;
        if (dao.themTaiKhoan(readForm())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Thêm thất bại (nhân viên chưa tồn tại hoặc đã có tài khoản)!");
    }

    private void sua() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn tài khoản cần sửa!"); return; }
        if (!validateForm()) return;
        if (dao.suaTaiKhoan(txtMa.getText().trim(), readForm())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Sửa thất bại!");
    }

    private void xoa() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn tài khoản cần xóa!"); return; }
        if (JOptionPane.showConfirmDialog(this, "Xóa tài khoản này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        if (dao.xoaTaiKhoan(txtMa.getText().trim())) { loadData(); clearForm(); }
        else JOptionPane.showMessageDialog(this, "Xóa thất bại!");
    }

    private void doiMatKhau() {
        if (table.getSelectedRow() < 0) { JOptionPane.showMessageDialog(this, "Hãy chọn tài khoản!"); return; }
        if (new String(txtMk.getPassword()).length() < 6) {
            JOptionPane.showMessageDialog(this, "Nhập mật khẩu mới (>= 6 ký tự) vào ô Mật khẩu rồi bấm lại!");
            return;
        }
        if (dao.doiMatKhau(txtMa.getText().trim(), new String(txtMk.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            loadData();
        } else JOptionPane.showMessageDialog(this, "Đổi mật khẩu thất bại!");
    }
}