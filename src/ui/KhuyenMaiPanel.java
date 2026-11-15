package ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
    private JTextField txtLoaiKM;
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

        setLayout(new BorderLayout(0, 12));
        setBackground(GiaoDien.MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // ----- Phần trên: tiêu đề, form nhập, các nút -----
        JPanel pnlTren = new JPanel(new BorderLayout(0, 10));
        pnlTren.setOpaque(false);
        pnlTren.add(GiaoDien.taoTieuDe("QUẢN LÝ KHUYẾN MÃI"), BorderLayout.NORTH);

        JPanel pnlForm = GiaoDien.taoKhungForm("Thông tin khuyến mãi");
        txtMaKM = GiaoDien.taoOText(15);
        GiaoDien.khoaOText(txtMaKM);
        txtTenKM = GiaoDien.taoOText(20);
        txtTiLeGiam = GiaoDien.taoOText(15);
        txtLoaiKM = GiaoDien.taoOText(20);
        txtNgayBatDau = GiaoDien.taoOText(15);
        txtNgayKetThuc = GiaoDien.taoOText(15);
        cboTrangThai = GiaoDien.taoComboBox();
        cboTrangThai.addItem("Đang áp dụng");
        cboTrangThai.addItem("Ngừng áp dụng");
        cboTrangThai.addItem("Hết hạn");
        cboTrangThai.setEditable(true);

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Mã khuyến mãi:"), 0, 0, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtMaKM, 1, 0, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Tên khuyến mãi:"), 2, 0, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtTenKM, 3, 0, 1);

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Tỉ lệ giảm:"), 0, 1, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtTiLeGiam, 1, 1, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Loại khuyến mãi:"), 2, 1, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtLoaiKM, 3, 1, 1);

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Ngày bắt đầu (yyyy-MM-dd):"), 0, 2, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtNgayBatDau, 1, 2, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Ngày kết thúc (yyyy-MM-dd):"), 2, 2, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtNgayKetThuc, 3, 2, 1);

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Trạng thái:"), 0, 3, 0);
        GiaoDien.themVaoLuoi(pnlForm, cboTrangThai, 1, 3, 1);
        pnlTren.add(pnlForm, BorderLayout.CENTER);

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlNut.setOpaque(false);
        btnThem = GiaoDien.taoNut("Thêm", GiaoDien.MAU_THEM);
        btnSua = GiaoDien.taoNut("Sửa", GiaoDien.MAU_SUA);
        btnXoa = GiaoDien.taoNut("Xóa", GiaoDien.MAU_XOA);
        btnLamMoi = GiaoDien.taoNut("Làm mới", GiaoDien.MAU_PHU);
        pnlNut.add(btnThem);
        pnlNut.add(btnSua);
        pnlNut.add(btnXoa);
        pnlNut.add(btnLamMoi);
        pnlTren.add(pnlNut, BorderLayout.SOUTH);

        add(pnlTren, BorderLayout.NORTH);

        // ----- Phần giữa: ô tìm kiếm và bảng -----
        JPanel pnlBang = new JPanel(new BorderLayout(0, 8));
        pnlBang.setOpaque(false);

        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlTim.setOpaque(false);
        txtTimKiem = GiaoDien.taoOText(22);
        btnTim = GiaoDien.taoNut("Tìm", GiaoDien.MAU_CHINH);
        btnHienTatCa = GiaoDien.taoNut("Hiện tất cả", GiaoDien.MAU_PHU);
        pnlTim.add(GiaoDien.taoNhan("Tìm theo mã hoặc tên:"));
        pnlTim.add(txtTimKiem);
        pnlTim.add(btnTim);
        pnlTim.add(btnHienTatCa);
        pnlBang.add(pnlTim, BorderLayout.NORTH);

        String[] tenCot = { "Mã KM", "Tên khuyến mãi", "Tỉ lệ giảm", "Loại KM", "Ngày bắt đầu", "Ngày kết thúc",
                "Trạng thái" };
        tableModel = new DefaultTableModel(tenCot, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GiaoDien.dinhDangBang(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        pnlBang.add(scroll, BorderLayout.CENTER);

        add(pnlBang, BorderLayout.CENTER);

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
            Object[] dong = { km.getMaKM(), km.getTenKM(), km.getTiLeGiamGia(), km.getLoaiKM(),
                    km.getNgayBatDau(), km.getNgayKetThuc(), km.getTrangThai() };
            tableModel.addRow(dong);
        }
    }

    // Xóa các ô nhập và tự đặt sẵn mã mới vào form
    private void xoaTrang() {
        txtMaKM.setText(khuyenMaiDAO.phatSinhMa());
        txtTenKM.setText("");
        txtTiLeGiam.setText("");
        txtLoaiKM.setText("");
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
        String loaiKM = txtLoaiKM.getText().trim();
        String ngayBDText = txtNgayBatDau.getText().trim();
        String ngayKTText = txtNgayKetThuc.getText().trim();
        String trangThai = cboTrangThai.getSelectedItem().toString().trim();

        if (tenKM.equals("") || tiLeText.equals("") || loaiKM.equals("") || ngayBDText.equals("")
                || ngayKTText.equals("") || trangThai.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin khuyến mãi!");
            return null;
        }
        if (tenKM.length() > 30 || loaiKM.length() > 20 || trangThai.length() > 20) {
            JOptionPane.showMessageDialog(this, "Tên tối đa 30 ký tự, loại và trạng thái tối đa 20 ký tự!");
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

        return new KhuyenMai(maKM, tenKM, tiLe, loaiKM, ngayBD, ngayKT, trangThai);
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
            txtLoaiKM.setText(tableModel.getValueAt(dong, 3).toString());
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
