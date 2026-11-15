
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import connectdb.ConnectDB;
import dao.NhanVien_dao;
import dao.PhieuNhapKho_dao;
import entity.NhanVien;
import entity.PhieuNhapKho;

public class PhieuNhapKho_GUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTable tablePNK;
    private DefaultTableModel modelPNK;

    private JTextField txtMaPNK;
    private JTextField txtNgayLap;
    private JTextField txtTrangThai;
    private JTextField txtTim;

    private JComboBox<String> cboNhanVien;

    private JButton btnTim;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXoaTrang;

    private PhieuNhapKho_dao pnk_dao;
    private NhanVien_dao nv_dao;

    private DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PhieuNhapKho_GUI() {

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pnk_dao = new PhieuNhapKho_dao();
        nv_dao = new NhanVien_dao();

        setTitle("Quản lý phiếu nhập kho");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ PHIẾU NHẬP KHO");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));

        Box b = Box.createVerticalBox();

        Box b1, b2, b3;

        JLabel lblMaPNK, lblNgayLap, lblNhanVien, lblTrangThai;

        // Dòng tiêu đề
        b.add(b1 = Box.createHorizontalBox());
        b1.add(lblTieuDe);

        b.add(Box.createVerticalStrut(15));

        // Dòng mã phiếu
        b.add(b2 = Box.createHorizontalBox());

        b2.add(lblMaPNK = new JLabel("Mã phiếu nhập: "));
        b2.add(txtMaPNK = new JTextField());

        b.add(Box.createVerticalStrut(10));

        // Dòng ngày lập và nhân viên
        b.add(b3 = Box.createHorizontalBox());

        b3.add(lblNgayLap = new JLabel("Ngày lập: "));
        b3.add(txtNgayLap = new JTextField());

        b3.add(lblNhanVien = new JLabel("Nhân viên: "));
        b3.add(cboNhanVien = new JComboBox<String>());

        b.add(Box.createVerticalStrut(10));

        // Dòng trạng thái
        Box b4 = Box.createHorizontalBox();
        b.add(b4);

        b4.add(lblTrangThai = new JLabel("Trạng thái: "));
        b4.add(txtTrangThai = new JTextField());

        // Căn chỉnh kích thước
        lblNgayLap.setPreferredSize(lblMaPNK.getPreferredSize());
        lblNhanVien.setPreferredSize(lblMaPNK.getPreferredSize());
        lblTrangThai.setPreferredSize(lblMaPNK.getPreferredSize());

        // Đổ dữ liệu nhân viên vào ComboBox
        ArrayList<NhanVien> listNV = nv_dao.getAllNhanVien();

        for (NhanVien nv : listNV) {
            cboNhanVien.addItem(nv.getMaNV());
        }

        b.add(Box.createVerticalStrut(15));

        add(b, BorderLayout.NORTH);

        // Tạo bảng
        String[] colHeader = {
            "Mã phiếu nhập",
            "Ngày lập",
            "Mã nhân viên",
            "Trạng thái"
        };

        modelPNK = new DefaultTableModel(colHeader, 0);
        tablePNK = new JTable(modelPNK);

        add(new JScrollPane(tablePNK), BorderLayout.CENTER);

        // Panel chứa các nút
        JPanel p = new JPanel();

        add(p, BorderLayout.SOUTH);

        JPanel pnlLeft = new JPanel();
        JPanel pnlRight = new JPanel();

        p.add(pnlLeft);
        p.add(pnlRight);

        // Tìm kiếm
        pnlLeft.add(new JLabel("Mã phiếu cần tìm: "));
        pnlLeft.add(txtTim = new JTextField(10));
        pnlLeft.add(btnTim = new JButton("Tìm"));

        // Các nút chức năng
        pnlRight.add(btnThem = new JButton("Thêm"));
        pnlRight.add(btnSua = new JButton("Sửa"));
        pnlRight.add(btnXoa = new JButton("Xóa"));
        pnlRight.add(btnXoaTrang = new JButton("Xóa trắng"));

        // Đăng ký sự kiện
        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);

        // Sự kiện chọn dòng trong bảng
        tablePNK.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = tablePNK.getSelectedRow();

                if (row >= 0) {

                    txtMaPNK.setText(
                        modelPNK.getValueAt(row, 0).toString()
                    );

                    txtNgayLap.setText(
                        modelPNK.getValueAt(row, 1).toString()
                    );

                    cboNhanVien.setSelectedItem(
                        modelPNK.getValueAt(row, 2).toString()
                    );

                    txtTrangThai.setText(
                        modelPNK.getValueAt(row, 3).toString()
                    );

                    // Không cho sửa mã phiếu
                    txtMaPNK.setEditable(false);
                }
            }
        });

        // Đọc dữ liệu từ database
        ReadDataFromDB();
    }

    // Đọc dữ liệu từ database
    private void ReadDataFromDB() {

        modelPNK.setRowCount(0);

        ArrayList<PhieuNhapKho> list =
                pnk_dao.getAllPhieuNhapKho();

        for (PhieuNhapKho pnk : list) {

            modelPNK.addRow(new Object[] {
                pnk.getMaPNK(),
                pnk.getNgayLap().format(formatter),
                pnk.getNhanVien().getMaNV(),
                pnk.getTrangThai()
            });
        }
    }

    // Kiểm tra dữ liệu
    private boolean validData() {

        if (txtMaPNK.getText().trim().isEmpty()
                || txtNgayLap.getText().trim().isEmpty()
                || txtTrangThai.getText().trim().isEmpty()
                || cboNhanVien.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!");

            return false;
        }

        try {

            LocalDate.parse(txtNgayLap.getText().trim(), formatter);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Ngày lập không hợp lệ! Định dạng dd/MM/yyyy");

            return false;
        }

        return true;
    }

    // Xóa trắng
    private void xoaTrang() {

        txtMaPNK.setText("");
        txtNgayLap.setText("");
        txtTrangThai.setText("");

        if (cboNhanVien.getItemCount() > 0) {
            cboNhanVien.setSelectedIndex(0);
        }

        txtMaPNK.setEditable(true);
        tablePNK.clearSelection();
        txtMaPNK.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        // Thêm phiếu nhập kho
        if (o.equals(btnThem)) {

            if (!validData()) {
                return;
            }

            try {

                String ma = txtMaPNK.getText().trim();

                LocalDate ngayLap = LocalDate.parse(
                        txtNgayLap.getText().trim(), formatter);

                String maNV = cboNhanVien.getSelectedItem().toString();

                String trangThai = txtTrangThai.getText().trim();

                NhanVien nv = new NhanVien(maNV);

                PhieuNhapKho pnk = new PhieuNhapKho(
                        ma, ngayLap, nv, trangThai);

                if (pnk_dao.themPhieuNhapKho(pnk)) {

                    JOptionPane.showMessageDialog(this,
                            "Thêm phiếu nhập kho thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Thêm phiếu nhập kho thất bại!");
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Dữ liệu không hợp lệ!");
            }
        }

        // Sửa phiếu nhập kho
        else if (o.equals(btnSua)) {

            if (tablePNK.getSelectedRow() < 0) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn phiếu cần sửa!");

                return;
            }

            if (!validData()) {
                return;
            }

            try {

                String ma = txtMaPNK.getText().trim();

                LocalDate ngayLap = LocalDate.parse(
                        txtNgayLap.getText().trim(), formatter);

                String maNV = cboNhanVien.getSelectedItem().toString();

                String trangThai = txtTrangThai.getText().trim();

                NhanVien nv = new NhanVien(maNV);

                PhieuNhapKho pnk = new PhieuNhapKho(
                        ma, ngayLap, nv, trangThai);

                if (pnk_dao.suaPhieuNhapKho(pnk)) {

                    JOptionPane.showMessageDialog(this,
                            "Cập nhật thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Cập nhật thất bại!");
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Dữ liệu không hợp lệ!");
            }
        }

        // Xóa phiếu nhập kho
        else if (o.equals(btnXoa)) {

            int row = tablePNK.getSelectedRow();

            if (row < 0) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn phiếu cần xóa!");

                return;
            }

            String ma = txtMaPNK.getText();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa phiếu " + ma + "?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                if (pnk_dao.xoaPhieuNhapKho(ma)) {

                    JOptionPane.showMessageDialog(this,
                            "Xóa thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Xóa thất bại!");
                }
            }
        }

        // Tìm kiếm
        else if (o.equals(btnTim)) {

            String ma = txtTim.getText().trim();

            if (ma.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập mã phiếu cần tìm!");

                return;
            }

            modelPNK.setRowCount(0);

            ArrayList<PhieuNhapKho> list =
                    pnk_dao.timPhieuNhapKho(ma);

            for (PhieuNhapKho pnk : list) {

                modelPNK.addRow(new Object[] {
                    pnk.getMaPNK(),
                    pnk.getNgayLap().format(formatter),
                    pnk.getNhanVien().getMaNV(),
                    pnk.getTrangThai()
                });
            }

            if (list.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy phiếu nhập kho!");
            }
        }

        // Xóa trắng
        else if (o.equals(btnXoaTrang)) {

            xoaTrang();
        }
    }

    public static void main(String[] args) {

        new PhieuNhapKho_GUI().setVisible(true);
    }
}
