
package ui;

import dao.TaiKhoan_Dao;
import entity.NhanVien;
import entity.TaiKhoan;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TaiKhoan_gui extends JPanel {

    private final TaiKhoan_Dao dao = new TaiKhoan_Dao();
    private ArrayList<TaiKhoan> ds = new ArrayList<>();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"Mã NV", "Mật khẩu", "Trạng thái"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final JTextField txtMa = new JTextField();
    private final JPasswordField txtMk = new JPasswordField();

    private final JComboBox<String> cboTrangThai =
            new JComboBox<>(new String[]{"Hoạt động", "Khóa"});

    public TaiKhoan_gui() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 244, 248));

        JLabel title = new JLabel("QUẢN LÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(41, 128, 185));
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(0, 50));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createTitledBorder("Thông tin tài khoản")));
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Cột nhãn
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 0;
        form.add(taolabel("Mã Nhân Viên:"), gbc);

        gbc.gridy = 1;
        form.add(taolabel("Mật khẩu:"), gbc);

        gbc.gridy = 2;
        form.add(taolabel("Trạng thái:"), gbc);

        // Cột nhập liệu
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa.setPreferredSize(new Dimension(350, 28));
        txtMk.setPreferredSize(new Dimension(350, 28));
        cboTrangThai.setPreferredSize(new Dimension(650, 38));

        gbc.gridy = 0;
        form.add(txtMa, gbc);

        gbc.gridy = 1;
        form.add(txtMk, gbc);

        gbc.gridy = 2;
        form.add(cboTrangThai, gbc);

        // Định dạng ô nhập liệu
        Color mauVien = new Color(189, 195, 199);

        JTextField[] dsText = {txtMa};

        for (JTextField txt : dsText) {
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txt.setBackground(Color.WHITE);
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(mauVien),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        }

        txtMk.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMk.setBackground(Color.WHITE);
        txtMk.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mauVien),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));

        cboTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel formContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        formContainer.setBackground(new Color(240, 244, 248));
        formContainer.add(form);

        // Các nút chức năng
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btns.setBackground(new Color(240, 244, 248));

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnDoiMk = new JButton("Đổi mật khẩu");
        JButton btnLam = new JButton("Làm mới");

        JButton[] dsNut = {
                btnThem, btnSua, btnXoa, btnDoiMk, btnLam
        };

        Color[] dsMau = {
                new Color(39, 174, 96),
                new Color(243, 156, 18),
                new Color(231, 76, 60),
                new Color(41, 128, 185),
                new Color(127, 140, 141)
        };

        for (int i = 0; i < dsNut.length; i++) {
            dsNut[i].setFont(new Font("Segoe UI", Font.BOLD, 13));
            dsNut[i].setForeground(Color.WHITE);
            dsNut[i].setBackground(dsMau[i]);
            dsNut[i].setFocusPainted(false);
            dsNut[i].setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            dsNut[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            dsNut[i].setOpaque(true);
        }

        btns.add(btnThem);
        btns.add(btnSua);
        btns.add(btnXoa);
        btns.add(btnDoiMk);
        btns.add(btnLam);

        // Khu vực phía trên
        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(new Color(240, 244, 248));

        north.add(title, BorderLayout.NORTH);
        north.add(formContainer, BorderLayout.CENTER);
        north.add(btns, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);

        // Bảng tài khoản
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(new Color(44, 62, 80));
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new Color(174, 214, 241));
        table.setSelectionForeground(Color.BLACK);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 225, 230));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setReorderingAllowed(false);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Chọn dòng trên bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });

        // Sự kiện các nút
        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnDoiMk.addActionListener(e -> doiMatKhau());

        btnLam.addActionListener(e -> {
            clearForm();
            loadData();
        });

        loadData();
    }

    // Tạo nhãn
    private JLabel taolabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(120, 25));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    // Load dữ liệu
    private void loadData() {
        ds = dao.getallTaiKhoan();
        model.setRowCount(0);

        for (TaiKhoan tk : ds) {
            model.addRow(new Object[]{
                    tk.getNhanVien().getMaNV(),
                    "••••••••",
                    tk.getTrangThai()
            });
        }
    }

    // Đưa dữ liệu lên form
    private void fillForm() {
        int r = table.getSelectedRow();

        if (r < 0 || r >= ds.size()) {
            return;
        }

        TaiKhoan tk = ds.get(r);

        txtMa.setText(tk.getNhanVien().getMaNV());
        txtMk.setText(tk.getMatKhau());
        cboTrangThai.setSelectedItem(tk.getTrangThai());

        txtMa.setEditable(false);
    }

    // Xóa dữ liệu trên form
    private void clearForm() {
        txtMa.setText("");
        txtMk.setText("");
        cboTrangThai.setSelectedIndex(0);

        txtMa.setEditable(true);
        table.clearSelection();
    }

    // Kiểm tra dữ liệu
    private boolean validateForm() {

        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Mã nhân viên không được để trống!");
            return false;
        }

        if (new String(txtMk.getPassword()).length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Mật khẩu phải có ít nhất 6 ký tự!");
            return false;
        }

        return true;
    }

    // Đọc dữ liệu từ form
    private TaiKhoan readForm() {

        NhanVien nv = new NhanVien();
        nv.setMaNV(txtMa.getText().trim());

        return new TaiKhoan(
                nv,
                new String(txtMk.getPassword()),
                String.valueOf(cboTrangThai.getSelectedItem())
        );
    }

    // Thêm tài khoản
    private void them() {

        if (!validateForm()) {
            return;
        }

        if (dao.themTaiKhoan(readForm())) {
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm thất bại (nhân viên chưa tồn tại hoặc đã có tài khoản)!");
        }
    }

    // Sửa tài khoản
    private void sua() {

        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this,
                    "Hãy chọn tài khoản cần sửa!");
            return;
        }

        if (!validateForm()) {
            return;
        }

        if (dao.suaTaiKhoan(txtMa.getText().trim(), readForm())) {
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
        }
    }

    // Xóa tài khoản
    private void xoa() {

        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this,
                    "Hãy chọn tài khoản cần xóa!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(
                this,
                "Xóa tài khoản này?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );

        if (chon != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.xoaTaiKhoan(txtMa.getText().trim())) {
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }

    // Đổi mật khẩu
    private void doiMatKhau() {

        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this,
                    "Hãy chọn tài khoản!");
            return;
        }

        if (new String(txtMk.getPassword()).length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Nhập mật khẩu mới (>= 6 ký tự) vào ô Mật khẩu rồi bấm lại!");
            return;
        }

        if (dao.doiMatKhau(
                txtMa.getText().trim(),
                new String(txtMk.getPassword()))) {

            JOptionPane.showMessageDialog(this,
                    "Đổi mật khẩu thành công!");

            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Đổi mật khẩu thất bại!");
        }
    }
}
