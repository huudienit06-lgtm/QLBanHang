package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
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
    private JTextField txtLoiNhuan;
    private JTextField txtTimKiem;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTim;
    private JButton btnHienTatCa;

    public DanhMucPanel() {
        danhMucDAO = new DanhMucDAO();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----- Phần trên: form nhập, các nút, ô tìm kiếm -----
        txtMaDM = new JTextField(12);
        txtMaDM.setEditable(false);
        txtTenDM = new JTextField(25);
        txtLoiNhuan = new JTextField(10);

        JPanel dongForm1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongForm1.add(new JLabel("Mã danh mục:"));
        dongForm1.add(txtMaDM);
        dongForm1.add(new JLabel("Tên danh mục:"));
        dongForm1.add(txtTenDM);
        dongForm1.add(new JLabel("Lợi nhuận (%):"));
        dongForm1.add(txtLoiNhuan);

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
        pnlBac.add(dongNut);
        pnlBac.add(dongTim);
        Color background = new Color(219, 234, 254);
        JLabel lblTitle = new JLabel("Quản lý danh mục");
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

        String[] tenCot = { "Mã danh mục", "Tên danh mục", "Lợi nhuận" };
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

    // Nạp lại bảng từ database và chuẩn bị form để thêm mới
    public void lamMoi() {
        hienThiLenBang(danhMucDAO.getAllDanhMuc());
        xoaTrang();
    }

    private void hienThiLenBang(ArrayList<DanhMuc> dsDM) {
        tableModel.setRowCount(0);
        for (int i = 0; i < dsDM.size(); i++) {
            DanhMuc dm = dsDM.get(i);
            Object[] dong = { dm.getMaDM(), dm.getTenDM(), dinhDangThapPhan(dm.getLoiNhuan()) };
            tableModel.addRow(dong);
        }
    }

    // Xóa ô tên và tự đặt sẵn mã mới vào form
    private void xoaTrang() {
        txtMaDM.setText(danhMucDAO.phatSinhMa());
        txtTenDM.setText("");
        txtLoiNhuan.setText("");
        table.clearSelection();
        txtTenDM.requestFocus();
    }

    // Hiện số thập phân lên bảng: 0.5 -> "0.5", 1.0 -> "1"
    private String dinhDangThapPhan(double thapPhan) {
        return java.math.BigDecimal.valueOf(thapPhan)
                .stripTrailingZeros()
                .toPlainString();
    }

    // 0.05 -> "5", 0.075 -> "7.5"
    private String thapPhanSangPhanTram(double thapPhan) {
        return java.math.BigDecimal.valueOf(thapPhan)
                .multiply(java.math.BigDecimal.valueOf(100))
                .stripTrailingZeros()
                .toPlainString();
    }

    // "5" -> 0.05 (đã kiểm tra hợp lệ ở kiemTraDuLieu)
    private double phanTramSangThapPhan(String text) {
        return Double.parseDouble(text.trim().replace(',', '.')) / 100;
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
        String loiNhuanText = txtLoiNhuan.getText().trim().replace(',', '.');
        if (loiNhuanText.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lợi nhuận!");
            txtLoiNhuan.requestFocus();
            return false;
        }
        double phanTram = 0;
        try {
            phanTram = Double.parseDouble(loiNhuanText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lợi nhuận phải là số!");
            txtLoiNhuan.requestFocus();
            return false;
        }
        if (phanTram < 0 || phanTram > 100) {
            JOptionPane.showMessageDialog(this, "Lợi nhuận phải từ 0 đến 100 (%)!");
            txtLoiNhuan.requestFocus();
            return false;
        }
        return true;
    }

    private void themDanhMuc() {
        if (!kiemTraDuLieu()) {
            return;
        }
        double loiNhuan = phanTramSangThapPhan(txtLoiNhuan.getText());
        DanhMuc dm = new DanhMuc(txtMaDM.getText().trim(), txtTenDM.getText().trim(), loiNhuan);
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
        double loiNhuan = phanTramSangThapPhan(txtLoiNhuan.getText());
        DanhMuc dm = new DanhMuc(txtMaDM.getText().trim(), txtTenDM.getText().trim(), loiNhuan);
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
            double thapPhan = Double.parseDouble(tableModel.getValueAt(dong, 2).toString());
            txtLoiNhuan.setText(thapPhanSangPhanTram(thapPhan));
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
