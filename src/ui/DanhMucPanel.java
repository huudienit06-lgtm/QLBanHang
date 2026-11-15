package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.DanhMucDAO;
import entity.DanhMuc;

public class DanhMucPanel extends JPanel implements ActionListener, MouseListener {

    private DanhMucDAO danhMucDAO;

    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField txtMaDM;
    private JTextField txtTenDM;
    private JTextField txtTimKiem;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTim;
    private JButton btnHienTatCa;

    public DanhMucPanel() {
        danhMucDAO = new DanhMucDAO();

        setLayout(new BorderLayout(0, 12));
        setBackground(GiaoDien.MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // ----- Phần trên: tiêu đề, form nhập, các nút -----
        JPanel pnlTren = new JPanel(new BorderLayout(0, 10));
        pnlTren.setOpaque(false);
        pnlTren.add(GiaoDien.taoTieuDe("QUẢN LÝ DANH MỤC"), BorderLayout.NORTH);

        JPanel pnlForm = GiaoDien.taoKhungForm("Thông tin danh mục");
        txtMaDM = GiaoDien.taoOText(15);
        GiaoDien.khoaOText(txtMaDM);
        txtTenDM = GiaoDien.taoOText(30);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Mã danh mục:"), 0, 0, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtMaDM, 1, 0, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Tên danh mục:"), 2, 0, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtTenDM, 3, 0, 1);
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

        String[] tenCot = { "Mã danh mục", "Tên danh mục" };
        tableModel = new DefaultTableModel(tenCot, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GiaoDien.dinhDangBang(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(java.awt.Color.WHITE);
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

    // Nạp lại bảng từ database và chuẩn bị form để thêm mới
    public void lamMoi() {
        hienThiLenBang(danhMucDAO.getAllDanhMuc());
        xoaTrang();
    }

    private void hienThiLenBang(ArrayList<DanhMuc> dsDM) {
        tableModel.setRowCount(0);
        for (int i = 0; i < dsDM.size(); i++) {
            DanhMuc dm = dsDM.get(i);
            Object[] dong = { dm.getMaDM(), dm.getTenDM() };
            tableModel.addRow(dong);
        }
    }

    // Xóa ô tên và tự đặt sẵn mã mới vào form
    private void xoaTrang() {
        txtMaDM.setText(danhMucDAO.phatSinhMa());
        txtTenDM.setText("");
        table.clearSelection();
        txtTenDM.requestFocus();
    }

    private boolean kiemTraDuLieu() {
        String ten = txtTenDM.getText().trim();
        if (ten.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên danh mục!");
            txtTenDM.requestFocus();
            return false;
        }
        if (ten.length() > 30) {
            JOptionPane.showMessageDialog(this, "Tên danh mục tối đa 30 ký tự!");
            txtTenDM.requestFocus();
            return false;
        }
        return true;
    }

    private void themDanhMuc() {
        if (!kiemTraDuLieu()) {
            return;
        }
        DanhMuc dm = new DanhMuc(txtMaDM.getText().trim(), txtTenDM.getText().trim());
        if (danhMucDAO.themDanhMuc(dm)) {
            JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm danh mục thất bại!");
        }
    }

    private void suaDanhMuc() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn danh mục cần sửa!");
            return;
        }
        if (!kiemTraDuLieu()) {
            return;
        }
        DanhMuc dm = new DanhMuc(txtMaDM.getText().trim(), txtTenDM.getText().trim());
        if (danhMucDAO.suaDanhMuc(dm)) {
            JOptionPane.showMessageDialog(this, "Sửa danh mục thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa danh mục thất bại!");
        }
    }

    private void xoaDanhMuc() {
        int dong = table.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn danh mục cần xóa!");
            return;
        }
        String maDM = tableModel.getValueAt(dong, 0).toString();
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa danh mục " + maDM + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        if (danhMucDAO.xoaDanhMuc(maDM)) {
            JOptionPane.showMessageDialog(this, "Xóa danh mục thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Không xóa được vì danh mục đang được sản phẩm sử dụng!");
        }
    }

    private void timDanhMuc() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.equals("")) {
            hienThiLenBang(danhMucDAO.getAllDanhMuc());
        } else {
            hienThiLenBang(danhMucDAO.timDanhMuc(tuKhoa));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon.equals(btnThem)) {
            themDanhMuc();
        } else if (nguon.equals(btnSua)) {
            suaDanhMuc();
        } else if (nguon.equals(btnXoa)) {
            xoaDanhMuc();
        } else if (nguon.equals(btnLamMoi)) {
            txtTimKiem.setText("");
            lamMoi();
        } else if (nguon.equals(btnTim)) {
            timDanhMuc();
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
            txtMaDM.setText(tableModel.getValueAt(dong, 0).toString());
            txtTenDM.setText(tableModel.getValueAt(dong, 1).toString());
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
