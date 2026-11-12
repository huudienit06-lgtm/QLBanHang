package ui;

import connectdb.ConnectDB;
import dao.ChiTietPhieuNhapKho_dao;
import dao.PhieuNhapKho_dao;
import dao.SanPhamDAO;
import entity.ChiTietPhieuNhapKho;
import entity.PhieuNhapKho;
import entity.SanPham;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class ChiTietPhieuNhapKho_GUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTable tableCTPNK;
    private DefaultTableModel modelCTPNK;

    private JComboBox<String> cboMaPNK;
    private JComboBox<String> cboMaSP;

    private JTextField txtSoLuong;
    private JTextField txtTim;

    private JButton btnTim;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXoaTrang;

    private ChiTietPhieuNhapKho_dao ctpnk_dao;
    private PhieuNhapKho_dao pnk_dao;
    private SanPhamDAO sp_dao;

    public ChiTietPhieuNhapKho_GUI() {

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ctpnk_dao = new ChiTietPhieuNhapKho_dao();
        pnk_dao = new PhieuNhapKho_dao();
        sp_dao = new SanPhamDAO();

        setTitle("Quản lý chi tiết phiếu nhập kho");
        setSize(950, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ CHI TIẾT PHIẾU NHẬP KHO");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 22));
        lblTieuDe.setForeground(Color.BLUE);

        Box b = Box.createVerticalBox();

        Box b1, b2, b3;

        JLabel lblMaPNK, lblMaSP, lblSoLuong;

        // Dòng tiêu đề
        b.add(b1 = Box.createHorizontalBox());
        b1.add(lblTieuDe);

        b.add(Box.createVerticalStrut(15));

        // Dòng mã phiếu nhập
        b.add(b2 = Box.createHorizontalBox());

        b2.add(lblMaPNK = new JLabel("Mã phiếu nhập: "));
        b2.add(cboMaPNK = new JComboBox<String>());

        b.add(Box.createVerticalStrut(10));

        // Dòng mã sản phẩm và số lượng
        b.add(b3 = Box.createHorizontalBox());

        b3.add(lblMaSP = new JLabel("Mã sản phẩm: "));
        b3.add(cboMaSP = new JComboBox<String>());

        b3.add(lblSoLuong = new JLabel("Số lượng: "));
        b3.add(txtSoLuong = new JTextField());

        lblMaSP.setPreferredSize(lblMaPNK.getPreferredSize());
        lblSoLuong.setPreferredSize(lblMaPNK.getPreferredSize());

        b.add(Box.createVerticalStrut(15));

        add(b, BorderLayout.NORTH);

        // Đổ mã phiếu nhập vào ComboBox
        ArrayList<PhieuNhapKho> listPNK = pnk_dao.getAllPhieuNhapKho();

        for (PhieuNhapKho pnk : listPNK) {
            cboMaPNK.addItem(pnk.getMaPNK());
        }

        // Đổ mã sản phẩm vào ComboBox
        ArrayList<SanPham> listSP = sp_dao.getAllSanPham();

        for (SanPham sp : listSP) {
            cboMaSP.addItem(sp.getMaSP());
        }

        // Tạo bảng
        String[] colHeader = {
            "Mã phiếu nhập",
            "Mã sản phẩm",
            "Tên sản phẩm",
            "Số lượng"
        };

        modelCTPNK = new DefaultTableModel(colHeader, 0);
        tableCTPNK = new JTable(modelCTPNK);

        add(new JScrollPane(tableCTPNK), BorderLayout.CENTER);

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
        tableCTPNK.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = tableCTPNK.getSelectedRow();

                if (row >= 0) {

                    cboMaPNK.setSelectedItem(
                        modelCTPNK.getValueAt(row, 0).toString()
                    );

                    cboMaSP.setSelectedItem(
                        modelCTPNK.getValueAt(row, 1).toString()
                    );

                    txtSoLuong.setText(
                        modelCTPNK.getValueAt(row, 3).toString()
                    );

                    // Không cho sửa mã phiếu và mã sản phẩm
                    cboMaPNK.setEnabled(false);
                    cboMaSP.setEnabled(false);
                }
            }
        });

        // Đọc dữ liệu từ database
        ReadDataFromDB();
    }

    // Đọc dữ liệu từ database
    private void ReadDataFromDB() {

        modelCTPNK.setRowCount(0);

        ArrayList<ChiTietPhieuNhapKho> list =
                ctpnk_dao.getAllChiTietPhieuNhapKho_dao();
        ArrayList<SanPham> listSP = sp_dao.getAllSanPham();
        for (ChiTietPhieuNhapKho ct : list) {

            String maPNK = ct.getPhieuNhapKho().getMaPNK();
            String maSP = ct.getSanPham().getMaSP();

            // Lấy tên sản phẩm để hiển thị lên bảng
            String tenSP = "";

            for (SanPham sp : listSP) {
                if (sp.getMaSP().equals(maSP)) {
                    tenSP = sp.getTenSP();
                    break;
                }
            }

            modelCTPNK.addRow(new Object[] {
                maPNK,
                maSP,
                tenSP,
                ct.getSoLuong()
            });
        }
    }

    // Kiểm tra dữ liệu
    private boolean validData() {

        if (cboMaPNK.getSelectedItem() == null
                || cboMaSP.getSelectedItem() == null
                || txtSoLuong.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!");

            return false;
        }

        try {

            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());

            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Số lượng phải lớn hơn 0!");
                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Số lượng phải là số nguyên!");

            return false;
        }

        return true;
    }

    // Xóa trắng
    private void xoaTrang() {

        txtSoLuong.setText("");
        txtTim.setText("");

        if (cboMaPNK.getItemCount() > 0) {
            cboMaPNK.setSelectedIndex(0);
        }

        if (cboMaSP.getItemCount() > 0) {
            cboMaSP.setSelectedIndex(0);
        }

        cboMaPNK.setEnabled(true);
        cboMaSP.setEnabled(true);

        tableCTPNK.clearSelection();
        txtSoLuong.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        // Thêm chi tiết phiếu nhập kho
        if (o.equals(btnThem)) {

            if (!validData()) {
                return;
            }

            try {

                String maPNK = cboMaPNK.getSelectedItem().toString();
                String maSP = cboMaSP.getSelectedItem().toString();
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());

                PhieuNhapKho pnk = new PhieuNhapKho(maPNK);
                SanPham sp = new SanPham(maSP);

                ChiTietPhieuNhapKho ct = new ChiTietPhieuNhapKho(
                        pnk, sp, soLuong
                );

                if (ctpnk_dao.themChiTietPhieuNhapKho(ct)) {

                    JOptionPane.showMessageDialog(this,
                            "Thêm chi tiết phiếu nhập kho thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Thêm thất bại! Có thể chi tiết này đã tồn tại.");
                }

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Dữ liệu không hợp lệ!");
            }
        }

        // Sửa số lượng chi tiết phiếu nhập kho
        else if (o.equals(btnSua)) {

            if (tableCTPNK.getSelectedRow() < 0) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn chi tiết cần sửa!");

                return;
            }

            if (!validData()) {
                return;
            }

            try {

                String maPNK = cboMaPNK.getSelectedItem().toString();
                String maSP = cboMaSP.getSelectedItem().toString();
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());

                if (ctpnk_dao.suaChiTietPhieuNhapKho(maPNK, maSP, soLuong)) {

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

        // Xóa chi tiết phiếu nhập kho
        else if (o.equals(btnXoa)) {

            int row = tableCTPNK.getSelectedRow();

            if (row < 0) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn chi tiết cần xóa!");

                return;
            }

            String maPNK = modelCTPNK.getValueAt(row, 0).toString();
            String maSP = modelCTPNK.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc muốn xóa sản phẩm " + maSP
                            + " trong phiếu " + maPNK + "?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                if (ctpnk_dao.xoaChiTietPhieuNhapKho(maPNK, maSP)) {

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

        // Tìm kiếm theo mã phiếu nhập
        else if (o.equals(btnTim)) {

            String maPNK = txtTim.getText().trim();

            if (maPNK.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập mã phiếu cần tìm!");

                return;
            }

            modelCTPNK.setRowCount(0);

            ArrayList<ChiTietPhieuNhapKho> list =
                    ctpnk_dao.timChiTietPhieuNhapKho(maPNK);

            for (ChiTietPhieuNhapKho ct : list) {

                String maSP = ct.getSanPham().getMaSP();
                String tenSP = "";

                for (SanPham sp : sp_dao.getAllSanPham()) {
                    if (sp.getMaSP().equals(maSP)) {
                        tenSP = sp.getTenSP();
                        break;
                    }
                }

                modelCTPNK.addRow(new Object[] {
                    ct.getPhieuNhapKho().getMaPNK(),
                    maSP,
                    tenSP,
                    ct.getSoLuong()
                });
            }

            if (list.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy chi tiết phiếu nhập kho!");
            }
        }

        // Xóa trắng
        else if (o.equals(btnXoaTrang)) {

            xoaTrang();
        }
    }

    public static void main(String[] args) {

        new ChiTietPhieuNhapKho_GUI().setVisible(true);
    }
}