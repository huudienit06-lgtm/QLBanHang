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
    private final JTextField txtMa = new JTextField(), txtTen = new JTextField(), txtSdt = new JTextField(), txtTim = new JTextField(15);

    private final JComboBox<String> cboGT = new JComboBox<>(new String[]{"Nam", "Nữ"});
    private final JComboBox<String> cboChucVu = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});
    public NhanVien_gui() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtMa.setEditable(false);
        txtMa.setText(dao.taoMaNhanVien());

        JLabel title = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // JPanel form = new JPanel(new GridLayout(3, 4, 10, 10));
        // form.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        // form.setBorder(BorderFactory.createEmptyBorder(10,100,10,100));
        // form.add(new JLabel("Mã Nhân Viên:"));      
        // form.add(txtMa);
        // form.add(new JLabel("Tên Nhân Viên:"));     
        // form.add(txtTen);
        // form.add(new JLabel("Số Điện Thoại:"));        
        // form.add(txtSdt);
        // form.add(new JLabel("Giới tính:"));  
        // form.add(cboGT);
        // form.add(new JLabel("Chức vụ:"));    
        // form.add(cboChucVu);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
        form.setBorder(BorderFactory.createEmptyBorder(10,100,10,100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 0;
        form.add(taolabel("Mã Nhân Viên:"), gbc);

        gbc.gridy = 1;
        form.add(taolabel("Số Điện Thoại:"), gbc);

        gbc.gridy = 2;
        form.add(taolabel("Chức vụ:"), gbc);

        // Cột nhập liệu
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa.setPreferredSize(new Dimension(200, 28));
        txtSdt.setPreferredSize(new Dimension(200, 28));
        cboChucVu.setPreferredSize(new Dimension(200, 28));

        gbc.gridy = 0;
        form.add(txtMa,gbc);

        gbc.gridy = 1;
        form.add(txtSdt,gbc);

        gbc.gridy = 2;
        form.add(cboChucVu,gbc);

        //Cột nhãn
        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy=0;
        form.add(taolabel("Tên Nhân Viên"),gbc);

        gbc.gridy=1;
        form.add(taolabel("Giới tính"),gbc);

        //Cột nhập liệu
        gbc.gridx = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTen.setPreferredSize(new Dimension(200, 28));
        cboGT.setPreferredSize(new Dimension(200, 28));

        gbc.gridy=0;
        form.add(txtTen,gbc);

        gbc.gridy=1;
        form.add(cboGT,gbc);


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
        btns.add(new JLabel("   Mã Nhân Viên:")); 
        btns.add(txtTim); 
        btns.add(btnTim);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btns, BorderLayout.SOUTH);

        add(top, BorderLayout.PAGE_START);
        title.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
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

     // Tạo nhãn
    private JLabel taolabel(String text) {
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(120, 25));
        return label;
    }

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

    private void clearForm() {
        txtMa.setText(dao.taoMaNhanVien());
        txtTen.setText(""); 
        txtSdt.setText(""); 
        cboChucVu.setSelectedIndex(0);
        cboGT.setSelectedIndex(0); 
        txtMa.setEditable(false); 
        table.clearSelection();
    }

    private boolean validateForm() {
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống!");
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
                cboGT.getSelectedItem().toString(), cboChucVu.getSelectedItem().toString());
    }

    private void them() {
         if (!validateForm()) 
            return;

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

    private void sua() {
        if (table.getSelectedRow() < 0) { 
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần sửa!"); 
            return; 
        }
        if (!validateForm()) return;
        if (dao.suaNhanVien(txtMa.getText().trim(), readForm())) { 
            loadData(); 
            clearForm(); }
        else 
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
    }

    private void xoa() {
        if (table.getSelectedRow() < 0) { 
            JOptionPane.showMessageDialog(this, "Hãy chọn nhân viên cần xóa!"); 
            return; 
        }
        if (
            JOptionPane.showConfirmDialog(this, "Xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) 
            return;
        if (dao.xoaNhanVien(txtMa.getText().trim())) { 
            loadData(); 
            clearForm(); }
        else 
            JOptionPane.showMessageDialog(this, "Xóa thất bại (có thể đang được tham chiếu bởi dữ liệu khác)!");
    }

    private void tim() {
        String ma = txtTim.getText().trim();
        if (ma.isEmpty()) { loadData(); return; }
        model.setRowCount(0);
        for (NhanVien nv : dao.timNhanVientheoMa(ma)) 
            addRow(nv);
        if (model.getRowCount() == 0) 
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên!");
    }
}