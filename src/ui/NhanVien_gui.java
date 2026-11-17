
// package ui;

// import java.awt.*;
// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;

// import dao.NhanVien_DAO;
// import entity.NhanVien;

// public class NhanVien_gui extends JPanel {
//     private final NhanVien_DAO dao = new NhanVien_DAO();

//     private final DefaultTableModel model = new DefaultTableModel(
//             new String[]{"Mã NV", "Tên NV", "SĐT", "Giới tính", "Chức vụ"}, 0) {
//         @Override
//         public boolean isCellEditable(int r, int c) {
//             return false;
//         }
//     };

//     private final JTable table = new JTable(model);

//     private final JTextField txtMa = new JTextField();
//     private final JTextField txtTen = new JTextField();
//     private final JTextField txtSdt = new JTextField();
//     private final JTextField txtTim = new JTextField(15);

//     private final JComboBox<String> cboGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
//     private final JComboBox<String> cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});

//     // Màu giao diện
//     private final Color MAU_NEN = new Color(240, 244, 248);
//     private final Color MAU_CHINH = new Color(41, 128, 185);
//     private final Color MAU_THEM = new Color(39, 174, 96);
//     private final Color MAU_SUA = new Color(243, 156, 18);
//     private final Color MAU_XOA = new Color(231, 76, 60);
//     private final Color MAU_PHU = new Color(127, 140, 141);

//     public NhanVien_gui() {
//         setLayout(new BorderLayout(0, 12));
//         setBackground(MAU_NEN);
//         setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

//         txtMa.setEditable(false);
//         txtMa.setText(dao.taoMaNhanVien());

//         // Tiêu đề
//         JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
//         title.setFont(new Font("Segoe UI", Font.BOLD, 22));
//         title.setForeground(MAU_CHINH);
//         title.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

//         // Form thông tin
//         JPanel form = new JPanel(new GridBagLayout());
//         form.setBackground(Color.WHITE);
//         form.setBorder(BorderFactory.createCompoundBorder(
//                 BorderFactory.createLineBorder(new Color(220, 225, 230)),
//                 BorderFactory.createEmptyBorder(10, 30, 10, 30)
//         ));

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(7, 7, 7, 7);
//         gbc.anchor = GridBagConstraints.WEST;

//         // Cột nhãn thứ nhất
//         gbc.gridx = 0;
//         gbc.weightx = 0;
//         gbc.fill = GridBagConstraints.NONE;

//         gbc.gridy = 0;
//         form.add(taolabel("Mã Nhân Viên:"), gbc);

//         gbc.gridy = 1;
//         form.add(taolabel("Số Điện Thoại:"), gbc);

//         gbc.gridy = 2;
//         form.add(taolabel("Chức vụ:"), gbc);

//         // Cột nhập liệu thứ nhất
//         gbc.gridx = 1;
//         gbc.weightx = 1;
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         txtMa.setPreferredSize(new Dimension(200, 32));
//         txtSdt.setPreferredSize(new Dimension(200, 32));
//         cboChucVu.setPreferredSize(new Dimension(200, 32));

//         gbc.gridy = 0;
//         form.add(txtMa, gbc);

//         gbc.gridy = 1;
//         form.add(txtSdt, gbc);

//         gbc.gridy = 2;
//         form.add(cboChucVu, gbc);

//         // Cột nhãn thứ hai
//         gbc.gridx = 2;
//         gbc.weightx = 0;
//         gbc.fill = GridBagConstraints.NONE;

//         gbc.gridy = 0;
//         form.add(taolabel("Tên Nhân Viên:"), gbc);

//         gbc.gridy = 1;
//         form.add(taolabel("Giới tính:"), gbc);

//         // Cột nhập liệu thứ hai
//         gbc.gridx = 3;
//         gbc.weightx = 1;
//         gbc.fill = GridBagConstraints.HORIZONTAL;

//         txtTen.setPreferredSize(new Dimension(200, 32));
//         cboGT.setPreferredSize(new Dimension(200, 32));

//         gbc.gridy = 0;
//         form.add(txtTen, gbc);

//         gbc.gridy = 1;
//         form.add(cboGT, gbc);

//         // Panel nút chức năng
//         JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
//         btns.setOpaque(false);

//         JButton btnThem = taoNut("Thêm", MAU_THEM);
//         JButton btnSua = taoNut("Sửa", MAU_SUA);
//         JButton btnXoa = taoNut("Xóa", MAU_XOA);
//         JButton btnLam = taoNut("Làm mới", MAU_PHU);
//         JButton btnTim = taoNut("Tìm theo mã", MAU_CHINH);

//         btns.add(btnThem);
//         btns.add(btnSua);
//         btns.add(btnXoa);
//         btns.add(btnLam);

//         JLabel lblTim = taolabel("Mã Nhân Viên:");
//         btns.add(lblTim);

//         txtTim.setPreferredSize(new Dimension(150, 32));
//         btns.add(txtTim);
//         btns.add(btnTim);

//         // Khu vực phía trên
//         JPanel top = new JPanel(new BorderLayout(0, 10));
//         top.setOpaque(false);
//         top.add(title, BorderLayout.NORTH);
//         top.add(form, BorderLayout.CENTER);
//         top.add(btns, BorderLayout.SOUTH);

//         add(top, BorderLayout.NORTH);

//         // Bảng nhân viên
//         table.setRowHeight(30);
//         table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
//         table.setForeground(Color.BLACK);
//         table.setBackground(Color.WHITE);
//         table.setSelectionBackground(new Color(214, 234, 248));
//         table.setSelectionForeground(Color.BLACK);
//         table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//         table.setGridColor(new Color(230, 230, 230));

//         table.getTableHeader().setBackground(MAU_CHINH);
//         table.getTableHeader().setForeground(Color.WHITE);
//         table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
//         table.getTableHeader().setPreferredSize(new Dimension(0, 35));

//         JScrollPane scroll = new JScrollPane(table);
//         scroll.getViewport().setBackground(Color.WHITE);
//         scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));

//         add(scroll, BorderLayout.CENTER);

//         // Sự kiện chọn dòng
//         table.getSelectionModel().addListSelectionListener(e -> {
//             if (!e.getValueIsAdjusting()) {
//                 fillForm();
//             }
//         });

//         btnThem.addActionListener(e -> them());
//         btnSua.addActionListener(e -> sua());
//         btnXoa.addActionListener(e -> xoa());
//         btnLam.addActionListener(e -> {
//             clearForm();
//             loadData();
//         });
//         btnTim.addActionListener(e -> tim());

//         loadData();
//     }

//     // Tạo nút có màu
//     private JButton taoNut(String text, Color mau) {
//         JButton btn = new JButton(text);
//         btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
//         btn.setBackground(mau);
//         btn.setForeground(Color.WHITE);
//         btn.setFocusPainted(false);
//         btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
//         btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
//         return btn;
//     }

//     // Tạo nhãn
//     private JLabel taolabel(String text) {
//         JLabel label = new JLabel(text);
//         label.setPreferredSize(new Dimension(120, 28));
//         label.setFont(new Font("Segoe UI", Font.BOLD, 13));
//         label.setForeground(new Color(52, 73, 94));
//         return label;
//     }

//     // Nạp dữ liệu
//     private void loadData() {
//         model.setRowCount(0);
//         for (NhanVien nv : dao.getAllNhanVien()) {
//             addRow(nv);
//         }
//     }

//     private void addRow(NhanVien nv) {
//         model.addRow(new Object[]{
//                 nv.getMaNV(),
//                 nv.getTenNV(),
//                 nv.getSdt(),
//                 nv.getGioiTinh(),
//                 nv.getChucVu()
//         });
//     }

//     // Đưa dữ liệu từ bảng lên form
//     private void fillForm() {
//         int r = table.getSelectedRow();
//         if (r < 0) return;

//         txtMa.setText(model.getValueAt(r, 0).toString());
//         txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
//         txtSdt.setText(String.valueOf(model.getValueAt(r, 2)));
//         cboGT.setSelectedItem(String.valueOf(model.getValueAt(r, 3)));
//         cboChucVu.setSelectedItem(String.valueOf(model.getValueAt(r, 4)));

//         txtMa.setEditable(false);
//     }

//     // Làm mới form
//     private void clearForm() {
//         txtMa.setText(dao.taoMaNhanVien());
//         txtTen.setText("");
//         txtSdt.setText("");
//         cboChucVu.setSelectedIndex(0);
//         cboGT.setSelectedIndex(0);
//         txtMa.setEditable(false);
//         table.clearSelection();
//     }

//     // Kiểm tra dữ liệu
//     private boolean validateForm() {
//         if (txtTen.getText().trim().isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
//             txtTen.requestFocus();
//             return false;
//         }

//         if (!txtSdt.getText().trim().matches("0\\d{9}")) {
//             JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 chữ số, bắt đầu bằng 0!");
//             txtSdt.requestFocus();
//             return false;
//         }

//         return true;
//     }

//     // Đọc dữ liệu từ form
//     private NhanVien readForm() {
//         return new NhanVien(
//                 txtMa.getText().trim(),
//                 txtTen.getText().trim(),
//                 txtSdt.getText().trim(),
//                 cboGT.getSelectedItem().toString(),
//                 cboChucVu.getSelectedItem().toString()
//         );
//     }

//     // Thêm nhân viên
//     private void them() {
//         if (!validateForm()) return;

//         String maMoi = dao.taoMaNhanVien();

//         if (maMoi == null) {
//             JOptionPane.showMessageDialog(this, "Không tạo được mã nhân viên!");
//             return;
//         }

//         txtMa.setText(maMoi);

//         if (dao.themNhanVien(readForm())) {
//             JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
//             loadData();
//             clearForm();
//         } else {
//             JOptionPane.showMessageDialog(this, "Thêm thất bại!");
//         }
//     }

//     // Sửa nhân viên
//     private void sua() {
//         if (table.getSelectedRow() < 0) {
//             JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần sửa!");
//             return;
//         }

//         if (!validateForm()) return;

//         if (dao.suaNhanVien(txtMa.getText().trim(), readForm())) {
//             JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
//             loadData();
//             clearForm();
//         } else {
//             JOptionPane.showMessageDialog(this, "Sửa thất bại!");
//         }
//     }

//     // Xóa nhân viên
//     private void xoa() {
//         if (table.getSelectedRow() < 0) {
//             JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần xóa!");
//             return;
//         }

//         if (JOptionPane.showConfirmDialog(
//                 this, "Xóa nhân viên này?", "Xác nhận",
//                 JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
//             return;
//         }

//         if (dao.xoaNhanVien(txtMa.getText().trim())) {
//             JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
//             loadData();
//             clearForm();
//         } else {
//             JOptionPane.showMessageDialog(this,
//                     "Xóa thất bại (có thể đang được tham chiếu bởi dữ liệu khác)!");
//         }
//     }

//     // Tìm nhân viên theo mã
//     private void tim() {
//         String ma1 = txtTim.getText().trim();

//         if (ma1.isEmpty()) {
//             loadData();
//             return;
//         }

//         model.setRowCount(0);

//         NhanVien nv = dao.timNhanVientheoMa(ma1);

//         if (nv != null) {
//             addRow(nv);
//         } else {
//             JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
//         }
//     }
// }


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
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable table = new JTable(model);

    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtTim = new JTextField(15);

    private final JComboBox<String> cboGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
    private final JComboBox<String> cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});

    public NhanVien_gui() {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        txtMa.setEditable(false);
        txtMa.setText(dao.taoMaNhanVien());

        // Tiêu đề
        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // Form thông tin
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.anchor = GridBagConstraints.WEST;

        // Cột nhãn thứ nhất
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 0;
        form.add(taolabel("Mã Nhân Viên:"), gbc);

        gbc.gridy = 1;
        form.add(taolabel("Số Điện Thoại:"), gbc);

        gbc.gridy = 2;
        form.add(taolabel("Chức vụ:"), gbc);

        // Cột nhập liệu thứ nhất
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa.setPreferredSize(new Dimension(200, 32));
        txtSdt.setPreferredSize(new Dimension(200, 32));
        cboChucVu.setPreferredSize(new Dimension(200, 32));

        gbc.gridy = 0;
        form.add(txtMa, gbc);

        gbc.gridy = 1;
        form.add(txtSdt, gbc);

        gbc.gridy = 2;
        form.add(cboChucVu, gbc);

        // Cột nhãn thứ hai
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 0;
        form.add(taolabel("Tên Nhân Viên:"), gbc);

        gbc.gridy = 1;
        form.add(taolabel("Giới tính:"), gbc);

        // Cột nhập liệu thứ hai
        gbc.gridx = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTen.setPreferredSize(new Dimension(200, 32));
        cboGT.setPreferredSize(new Dimension(200, 32));

        gbc.gridy = 0;
        form.add(txtTen, gbc);

        gbc.gridy = 1;
        form.add(cboGT, gbc);

        // Panel nút chức năng
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btns.setOpaque(false);

        JButton btnThem = taoNut("Thêm");
        JButton btnSua = taoNut("Sửa");
        JButton btnXoa = taoNut("Xóa");
        JButton btnLam = taoNut("Làm mới");
        JButton btnTim = taoNut("Tìm theo mã");

        btns.add(btnThem);
        btns.add(btnSua);
        btns.add(btnXoa);
        btns.add(btnLam);

        JLabel lblTim = taolabel("Mã Nhân Viên:");
        btns.add(lblTim);

        txtTim.setPreferredSize(new Dimension(150, 32));
        btns.add(txtTim);
        btns.add(btnTim);

        // Khu vực phía trên
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);
        top.add(title, BorderLayout.NORTH);
        top.add(form, BorderLayout.CENTER);
        top.add(btns, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);

        // Bảng nhân viên
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        // Sự kiện chọn dòng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillForm();
            }
        });

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnLam.addActionListener(e -> {
            clearForm();
            loadData();
        });
        btnTim.addActionListener(e -> tim());

        loadData();
    }

    // Tạo nút
    private JButton taoNut(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Tạo nhãn
    private JLabel taolabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(120, 28));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    // Nạp dữ liệu
    private void loadData() {
        model.setRowCount(0);
        for (NhanVien nv : dao.getAllNhanVien()) {
            addRow(nv);
        }
    }

    private void addRow(NhanVien nv) {
        model.addRow(new Object[]{
                nv.getMaNV(),
                nv.getTenNV(),
                nv.getSdt(),
                nv.getGioiTinh(),
                nv.getChucVu()
        });
    }

    // Đưa dữ liệu từ bảng lên form
    private void fillForm() {
        int r = table.getSelectedRow();
        if (r < 0) return;

        txtMa.setText(model.getValueAt(r, 0).toString());
        txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
        txtSdt.setText(String.valueOf(model.getValueAt(r, 2)));
        cboGT.setSelectedItem(String.valueOf(model.getValueAt(r, 3)));
        cboChucVu.setSelectedItem(String.valueOf(model.getValueAt(r, 4)));

        txtMa.setEditable(false);
    }

    // Làm mới form
    private void clearForm() {
        txtMa.setText(dao.taoMaNhanVien());
        txtTen.setText("");
        txtSdt.setText("");
        cboChucVu.setSelectedIndex(0);
        cboGT.setSelectedIndex(0);
        txtMa.setEditable(false);
        table.clearSelection();
    }

    // Kiểm tra dữ liệu
    private boolean validateForm() {
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
            txtTen.requestFocus();
            return false;
        }

        if (!txtSdt.getText().trim().matches("0\\d{9}")) {
            JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 chữ số, bắt đầu bằng 0!");
            txtSdt.requestFocus();
            return false;
        }

        return true;
    }

    // Đọc dữ liệu từ form
    private NhanVien readForm() {
        return new NhanVien(
                txtMa.getText().trim(),
                txtTen.getText().trim(),
                txtSdt.getText().trim(),
                cboGT.getSelectedItem().toString(),
                cboChucVu.getSelectedItem().toString()
        );
    }

    // Thêm nhân viên
    private void them() {
        if (!validateForm()) return;

        String maMoi = dao.taoMaNhanVien();

        if (maMoi == null) {
            JOptionPane.showMessageDialog(this, "Không tạo được mã nhân viên!");
            return;
        }

        txtMa.setText(maMoi);

        if (dao.themNhanVien(readForm())) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    // Sửa nhân viên
    private void sua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần sửa!");
            return;
        }

        if (!validateForm()) return;

        if (dao.suaNhanVien(txtMa.getText().trim(), readForm())) {
            JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
        }
    }

    // Xóa nhân viên
    private void xoa() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần xóa!");
            return;
        }

        if (JOptionPane.showConfirmDialog(
                this, "Xóa nhân viên này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.xoaNhanVien(txtMa.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xóa thất bại (có thể đang được tham chiếu bởi dữ liệu khác)!");
        }
    }

    // Tìm nhân viên theo mã
    private void tim() {
        String ma1 = txtTim.getText().trim();

        if (ma1.isEmpty()) {
            loadData();
            return;
        }

        model.setRowCount(0);

        NhanVien nv = dao.timNhanVientheoMa(ma1);

        if (nv != null) {
            addRow(nv);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
        }
    }
}