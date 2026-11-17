package ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
// import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

public class KhuyenMaiPanel extends JPanel implements ActionListener, MouseListener {

    private KhuyenMaiDAO khuyenMaiDAO;

    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField txtMaKM;
    private JTextField txtTenKM;
    private JTextField txtTiLeGiam;
    private JTextField txtDonViTinh;
    private JTextField txtNgayBatDau;
    private JTextField txtNgayKetThuc;
    private JComboBox<String> cboTrangThai;
    private JTextField txtTimKiem;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTim;
    private JButton btnHienTatCa;

    public KhuyenMaiPanel() {
        khuyenMaiDAO = new KhuyenMaiDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----- Phần trên: form nhập, các nút, ô tìm kiếm -----
        txtMaKM = new JTextField(10);
        txtMaKM.setEditable(false);
        txtTenKM = new JTextField(20);
        txtTiLeGiam = new JTextField(8);
        txtDonViTinh = new JTextField(14);
        txtNgayBatDau = new JTextField(10);
        txtNgayKetThuc = new JTextField(10);
        cboTrangThai = new JComboBox<String>();
        cboTrangThai.addItem("Đang áp dụng");
        cboTrangThai.addItem("Đã kết thúc");
        cboTrangThai.addItem("Ngừng áp dụng");
        cboTrangThai.setEditable(true);

        JPanel dongForm1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongForm1.add(new JLabel("Mã KM:"));
        dongForm1.add(txtMaKM);
        dongForm1.add(new JLabel("Tên KM:"));
        dongForm1.add(txtTenKM);
        dongForm1.add(new JLabel("Tỉ lệ giảm:"));
        dongForm1.add(txtTiLeGiam);
        dongForm1.add(new JLabel("Đơn vị giảm giá:"));
        dongForm1.add(txtDonViTinh);

        JPanel dongForm2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongForm2.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"));
        dongForm2.add(txtNgayBatDau);
        dongForm2.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"));
        dongForm2.add(txtNgayKetThuc);
        dongForm2.add(new JLabel("Trạng thái:"));
        dongForm2.add(cboTrangThai);

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        JPanel dongNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongNut.add(btnThem);
        dongNut.add(btnSua);
        dongNut.add(btnXoa);
        dongNut.add(btnLamMoi);

        txtTimKiem = new JTextField(20);
        btnTim = new JButton("Tìm");
        btnHienTatCa = new JButton("Hiện tất cả");
        JPanel dongTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongTim.add(new JLabel("Tìm theo mã hoặc tên:"));
        dongTim.add(txtTimKiem);
        dongTim.add(btnTim);
        dongTim.add(btnHienTatCa);

        JPanel pnlBac = new JPanel(new GridLayout(0, 1));
        pnlBac.add(dongForm1);
        pnlBac.add(dongForm2);
        pnlBac.add(dongNut);
        pnlBac.add(dongTim);
        Color background = new Color(219, 234, 254);
        JLabel lblTitle = new JLabel("Quản lý khuyến mãi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(30, 64, 175));
        JPanel Title = new JPanel(new FlowLayout());
        Title.setBackground(background);
        Title.add(lblTitle);

        // Tiêu đề nằm trên cùng, form nhập nằm ngay dưới tiêu đề
        JPanel pnlTren = new JPanel(new BorderLayout(0, 10));
        pnlTren.add(Title, BorderLayout.NORTH);
        pnlTren.add(pnlBac, BorderLayout.CENTER);
        add(pnlTren, BorderLayout.NORTH);

        String[] tenCot = { "Mã KM", "Tên khuyến mãi", "Tỉ lệ giảm", "Đơn vị giảm giá", "Ngày bắt đầu",
                "Ngày kết thúc", "Trạng thái" };
        tableModel = new DefaultTableModel(tenCot, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ----- Gắn sự kiện -----
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnTim.addActionListener(this);
        btnHienTatCa.addActionListener(this);
        table.addMouseListener(this);

        lamMoi();
    }

    public void lamMoi() {
        hienThiLenBang(khuyenMaiDAO.getAllKhuyenMai());
        xoaTrang();
    }

    private void hienThiLenBang(ArrayList<KhuyenMai> dsKM) {
        tableModel.setRowCount(0);
        for (int i = 0; i < dsKM.size(); i++) {
            KhuyenMai km = dsKM.get(i);
            Object[] dong = { km.getMaKM(), km.getTenKM(), km.getTiLeGiamGia(), km.getDonViTinh(),
                    km.getNgayBatDau(), km.getNgayKetThuc(), km.getTrangThai() };
            tableModel.addRow(dong);
        }
    }

    // Xóa các ô nhập và tự đặt sẵn mã mới vào form
    private void xoaTrang() {
        txtMaKM.setText(khuyenMaiDAO.phatSinhMa());
        txtTenKM.setText("");
        txtTiLeGiam.setText("");
        txtDonViTinh.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        cboTrangThai.setSelectedIndex(0);
        table.clearSelection();
        txtTenKM.requestFocus();
    }

    // Kiểm tra dữ liệu trên form, hợp lệ thì trả về đối tượng KhuyenMai, sai thì báo lỗi và trả về null
    private KhuyenMai layDuLieuTuForm() {
        String maKM = txtMaKM.getText().trim();
        String tenKM = txtTenKM.getText().trim();
        String tiLeText = txtTiLeGiam.getText().trim();
        String donViTinh = txtDonViTinh.getText().trim();
        String ngayBDText = txtNgayBatDau.getText().trim();
        String ngayKTText = txtNgayKetThuc.getText().trim();
        String trangThai = cboTrangThai.getSelectedItem().toString().trim();

        if (tenKM.equals("") || tiLeText.equals("") || donViTinh.equals("") || ngayBDText.equals("")
                || ngayKTText.equals("") || trangThai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khuyến mãi!");
            return null;
        }
        if (tenKM.length() > 30 || donViTinh.length() > 20 || trangThai.length() > 20) {
            JOptionPane.showMessageDialog(this, "Tên tối đa 30 ký tự, đơn vị giảm giá và trạng thái tối đa 20 ký tự!");
            return null;
        }

        double tiLe = 0;
        try {
            tiLe = Double.parseDouble(tiLeText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tỉ lệ giảm phải là số!");
            txtTiLeGiam.requestFocus();
            return null;
        }
        if (tiLe < 0) {
            JOptionPane.showMessageDialog(this, "Tỉ lệ giảm không được âm!");
            txtTiLeGiam.requestFocus();
            return null;
        }

        LocalDate ngayBD = null;
        LocalDate ngayKT = null;
        try {
            ngayBD = LocalDate.parse(ngayBDText);
            ngayKT = LocalDate.parse(ngayKTText);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày phải theo định dạng yyyy-MM-dd, ví dụ 2026-11-30!");
            return null;
        }
        if (ngayKT.isBefore(ngayBD)) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!");
            return null;
        }

        KhuyenMai km = new KhuyenMai();
        km.setMaKM(maKM);
        km.setTenKM(tenKM);
        km.setTiLeGiamGia(tiLe);
        km.setDonViTinh(donViTinh);
        km.setNgayBatDau(ngayBD);
        km.setNgayKetThuc(ngayKT);
        km.setTrangThai(trangThai);
        return km;
    }

    private void themKhuyenMai() {
        KhuyenMai km = layDuLieuTuForm();
        if (km == null) {
            return;
        }
        if (khuyenMaiDAO.themKhuyenMai(km)) {
            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thất bại!");
        }
    }

    private void suaKhuyenMai() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn khuyến mãi cần sửa!");
            return;
        }
        KhuyenMai km = layDuLieuTuForm();
        if (km == null) {
            return;
        }
        if (khuyenMaiDAO.suaKhuyenMai(km)) {
            JOptionPane.showMessageDialog(this, "Sửa khuyến mãi thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa khuyến mãi thất bại!");
        }
    }

    private void xoaKhuyenMai() {
        int dong = table.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn khuyến mãi cần xóa!");
            return;
        }
        String maKM = tableModel.getValueAt(dong, 0).toString();
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa khuyến mãi " + maKM + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        if (khuyenMaiDAO.xoaKhuyenMai(maKM)) {
            JOptionPane.showMessageDialog(this, "Xóa khuyến mãi thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Không xóa được vì khuyến mãi đang được sản phẩm hoặc hóa đơn sử dụng!");
        }
    }

    private void timKhuyenMai() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.equals("")) {
            hienThiLenBang(khuyenMaiDAO.getAllKhuyenMai());
        } else {
            hienThiLenBang(khuyenMaiDAO.timKhuyenMai(tuKhoa));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon.equals(btnThem)) {
            themKhuyenMai();
        } else if (nguon.equals(btnSua)) {
            suaKhuyenMai();
        } else if (nguon.equals(btnXoa)) {
            xoaKhuyenMai();
        } else if (nguon.equals(btnLamMoi)) {
            txtTimKiem.setText("");
            lamMoi();
        } else if (nguon.equals(btnTim)) {
            timKhuyenMai();
        } else if (nguon.equals(btnHienTatCa)) {
            txtTimKiem.setText("");
            lamMoi();
        }
    }

    // Bấm vào một dòng trong bảng thì đưa dữ liệu lên form
    @Override
    public void mouseClicked(MouseEvent e) {
        int dong = table.getSelectedRow();
        if (dong >= 0) {
            txtMaKM.setText(tableModel.getValueAt(dong, 0).toString());
            txtTenKM.setText(tableModel.getValueAt(dong, 1).toString());
            txtTiLeGiam.setText(tableModel.getValueAt(dong, 2).toString());
            txtDonViTinh.setText(tableModel.getValueAt(dong, 3).toString());
            txtNgayBatDau.setText(tableModel.getValueAt(dong, 4).toString());
            txtNgayKetThuc.setText(tableModel.getValueAt(dong, 5).toString());
            cboTrangThai.setSelectedItem(tableModel.getValueAt(dong, 6).toString());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
