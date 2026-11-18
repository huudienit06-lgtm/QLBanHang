package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import connectdb.ConnectDB;
import dao.ChiTietPhieuNhapKho_dao;
import dao.NhanVien_DAO;
import dao.PhieuNhapKho_dao;
import dao.SanPhamDAO;
import entity.ChiTietPhieuNhapKho;
import entity.NhanVien;
import entity.PhieuNhapKho;
import entity.SanPham;

public class PhieuNhapKho_GUI extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTable tablePNK;
    private DefaultTableModel modelPNK;

    // Bảng tạm: các sản phẩm sẽ nhập trong phiếu mới
    private JTable tableSPDaChon;
    private DefaultTableModel modelSP;

    private JTextField txtMaPNK;
    private JTextField txtNgayLap;
    private JTextField txtTrangThai;
    private JTextField txtTim;

    private JComboBox<String> cboNhanVien;
    private JComboBox<String> cboMaSP;      // hiển thị "maSP - tenSP"
    private JTextField txtSoLuongSP;

    private JButton btnThemSP;
    private JButton btnXoaSP;

    private JButton btnTim;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXoaTrang;
    private JButton btnChiTiet;

    private PhieuNhapKho_dao pnk_dao;
    private NhanVien_DAO nv_dao;
    private SanPhamDAO sp_dao;
    private ChiTietPhieuNhapKho_dao ctPNK_dao;

    private ArrayList<SanPham> listSP;

    private static final String PHAN_CACH = " - ";

    private DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PhieuNhapKho_GUI() {
        setLayout(new BorderLayout(10, 10));

        Color background = new Color(219, 234, 254);
        JLabel lblTitle = new JLabel("QUẢN LÝ PHIẾU NHẬP KHO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitle.setForeground(new Color(30, 64, 175));

        JPanel Title = new JPanel(new FlowLayout());
        Title.setBackground(background);
        Title.add(lblTitle);

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        pnk_dao = new PhieuNhapKho_dao();
        nv_dao = new NhanVien_DAO();
        sp_dao = new SanPhamDAO();
        ctPNK_dao = new ChiTietPhieuNhapKho_dao();

        Box b = Box.createVerticalBox();

        Box b2, b3, b4, b5;

        JLabel lblMaPNK, lblNgayLap, lblNhanVien, lblTrangThai, lblMaSP, lblSoLuong;

        b.add(Box.createVerticalStrut(10));

        // Dòng mã phiếu
        b.add(b2 = Box.createHorizontalBox());
        b2.add(lblMaPNK = new JLabel("Mã phiếu nhập: "));
        b2.add(txtMaPNK = new JTextField());
        txtMaPNK.setEditable(false);

        b.add(Box.createVerticalStrut(10));

        // Dòng ngày lập và nhân viên
        b.add(b3 = Box.createHorizontalBox());
        b3.add(lblNgayLap = new JLabel("Ngày lập: "));
        b3.add(txtNgayLap = new JTextField());
        b3.add(lblNhanVien = new JLabel("Nhân viên: "));
        b3.add(cboNhanVien = new JComboBox<String>());

        b.add(Box.createVerticalStrut(10));

        // Dòng trạng thái
        b.add(b4 = Box.createHorizontalBox());
        b4.add(lblTrangThai = new JLabel("Trạng thái: "));
        b4.add(txtTrangThai = new JTextField());

        b.add(Box.createVerticalStrut(10));

        // Dòng sản phẩm, số lượng và nút Thêm SP / Xóa SP
        b.add(b5 = Box.createHorizontalBox());
        b5.add(lblMaSP = new JLabel("Sản phẩm: "));
        b5.add(cboMaSP = new JComboBox<String>());
        b5.add(lblSoLuong = new JLabel("Số lượng: "));
        b5.add(txtSoLuongSP = new JTextField());
        txtSoLuongSP.setEditable(true);
        b5.add(Box.createHorizontalStrut(5));
        b5.add(btnThemSP = new JButton("Thêm SP"));
        b5.add(Box.createHorizontalStrut(5));
        b5.add(btnXoaSP = new JButton("Xóa SP"));

        // Căn chỉnh kích thước
        lblNgayLap.setPreferredSize(lblMaPNK.getPreferredSize());
        lblNhanVien.setPreferredSize(lblMaPNK.getPreferredSize());
        lblTrangThai.setPreferredSize(lblMaPNK.getPreferredSize());
        lblMaSP.setPreferredSize(lblMaPNK.getPreferredSize());
        lblSoLuong.setPreferredSize(lblMaPNK.getPreferredSize());

        // Đổ dữ liệu nhân viên vào ComboBox
        ArrayList<NhanVien> listNV = nv_dao.getAllNhanVien();
        for (NhanVien nv : listNV) {
            cboNhanVien.addItem(nv.getMaNV());
        }

        // Đổ sản phẩm vào ComboBox dạng "maSP - tenSP"
        listSP = sp_dao.getAllSanPham();
        for (SanPham sp : listSP) {
            cboMaSP.addItem(sp.getMaSP() + " - " + sp.getTenSP());
        }

        b.add(Box.createVerticalStrut(15));

        // Gom tiêu đề + form vào 1 panel ở NORTH
        JPanel pnlNorth = new JPanel(new BorderLayout());
        pnlNorth.add(Title, BorderLayout.NORTH);
        pnlNorth.add(b, BorderLayout.CENTER);
        add(pnlNorth, BorderLayout.NORTH);

        // Bảng tạm sản phẩm đã chọn
        String[] colSP = { "Mã sản phẩm", "Tên sản phẩm", "Số lượng" };
        modelSP = new DefaultTableModel(colSP, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSPDaChon = new JTable(modelSP);

        JScrollPane scrSP = new JScrollPane(tableSPDaChon);
        scrSP.setPreferredSize(new Dimension(0, 130));
        scrSP.setBorder(BorderFactory.createTitledBorder(
                "Sản phẩm trong phiếu nhập mới"));

        // Bảng phiếu nhập kho (thêm cột Mã sản phẩm và Số lượng)
        String[] colHeader = {
            "Mã phiếu nhập",
            "Ngày lập",
            "Mã nhân viên",
            "Trạng thái",
            "Mã sản phẩm",
            "Số lượng"
        };

        modelPNK = new DefaultTableModel(colHeader, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablePNK = new JTable(modelPNK);

        JScrollPane scrPNK = new JScrollPane(tablePNK);
        scrPNK.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu nhập kho"));

        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.add(scrSP, BorderLayout.NORTH);
        pnlCenter.add(scrPNK, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

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
        pnlRight.add(btnChiTiet = new JButton("Xem Chi tiết phiếu nhập kho"));

        // Đăng ký sự kiện
        btnThemSP.addActionListener(this);
        btnXoaSP.addActionListener(this);
        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnChiTiet.addActionListener(this);

        // Sự kiện chọn dòng trong bảng phiếu nhập
        tablePNK.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tablePNK.getSelectedRow();
                if (row >= 0) {
                    txtMaPNK.setText(modelPNK.getValueAt(row, 0).toString());
                    txtNgayLap.setText(modelPNK.getValueAt(row, 1).toString());
                    cboNhanVien.setSelectedItem(modelPNK.getValueAt(row, 2).toString());
                    txtTrangThai.setText(modelPNK.getValueAt(row, 3).toString());
                    txtSoLuongSP.setEditable(false);
                }
            }
        });

        // Đọc dữ liệu từ database
        ReadDataFromDB();

        // Giá trị mặc định cho phiếu mới
        xoaTrang();
    }

    // Đọc dữ liệu từ database
    private void ReadDataFromDB() {

        modelPNK.setRowCount(0);

        ArrayList<PhieuNhapKho> list = pnk_dao.getAllPhieuNhapKho();

        // Gom chi tiết theo mã phiếu để tránh truy vấn nhiều lần
        Map<String, ArrayList<ChiTietPhieuNhapKho>> mapCT = new HashMap<>();
        for (ChiTietPhieuNhapKho ct : ctPNK_dao.getAllChiTietPhieuNhapKho_dao()) {
            String maPNK = ct.getPhieuNhapKho().getMaPNK();
            mapCT.computeIfAbsent(maPNK, k -> new ArrayList<>()).add(ct);
        }

        for (PhieuNhapKho pnk : list) {
            ArrayList<ChiTietPhieuNhapKho> ds = mapCT.get(pnk.getMaPNK());
            modelPNK.addRow(taoDong(pnk, ds));
        }
    }

    // Tạo 1 dòng cho bảng chính: mã SP nối bằng ";" và tổng số lượng
    private Object[] taoDong(PhieuNhapKho pnk, ArrayList<ChiTietPhieuNhapKho> dsCT) {

        StringBuilder sbMaSP = new StringBuilder();
        int tongSoLuong = 0;

        if (dsCT != null) {
            for (ChiTietPhieuNhapKho ct : dsCT) {
                if (sbMaSP.length() > 0) {
                    sbMaSP.append(";");
                }
                sbMaSP.append(ct.getSanPham().getMaSP());
                tongSoLuong += ct.getSoLuong();
            }
        }

        return new Object[] {
            pnk.getMaPNK(),
            pnk.getNgayLap().format(formatter),
            pnk.getNhanVien().getMaNV(),
            pnk.getTrangThai(),
            sbMaSP.toString(),
            tongSoLuong
        };
    }

    // Lấy mã SP từ mục đang chọn trong combobox ("maSP - tenSP")
    private String layMaSPDangChon() {
        Object item = cboMaSP.getSelectedItem();
        if (item == null) {
            return null;
        }
        String s = item.toString();
        int idx = s.indexOf(PHAN_CACH);
        return idx >= 0 ? s.substring(0, idx).trim() : s.trim();
    }

    // Lấy tên sản phẩm theo mã
    private String layTenSP(String maSP) {
        for (SanPham sp : listSP) {
            if (sp.getMaSP().equals(maSP)) {
                return sp.getTenSP();
            }
        }
        return "";
    }

    // Kiểm tra thông tin phiếu nhập
    private boolean validData() {

        if (txtNgayLap.getText().trim().isEmpty()
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

    // Kiểm tra sản phẩm + số lượng trước khi thêm vào bảng tạm
    private boolean validSanPham() {

        if (cboMaSP.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
            return false;
        }

        String sl = txtSoLuongSP.getText().trim();

        if (sl.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
            txtSoLuongSP.requestFocus();
            return false;
        }

        try {
            int soLuong = Integer.parseInt(sl);
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                txtSoLuongSP.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
            txtSoLuongSP.requestFocus();
            return false;
        }

        return true;
    }

    // Thêm sản phẩm vào bảng tạm (trùng mã thì cộng dồn số lượng)
    private void themSPVaoBangTam() {

        if (!validSanPham()) {
            return;
        }

        String maSP = layMaSPDangChon();
        int soLuong = Integer.parseInt(txtSoLuongSP.getText().trim());

        for (int i = 0; i < modelSP.getRowCount(); i++) {
            if (modelSP.getValueAt(i, 0).toString().equals(maSP)) {
                int cu = Integer.parseInt(modelSP.getValueAt(i, 2).toString());
                modelSP.setValueAt(cu + soLuong, i, 2);

                txtSoLuongSP.setText("");
                txtSoLuongSP.requestFocus();
                return;
            }
        }

        modelSP.addRow(new Object[] { maSP, layTenSP(maSP), soLuong });

        txtSoLuongSP.setText("");
        txtSoLuongSP.requestFocus();
    }

    // Xóa sản phẩm đang chọn khỏi bảng tạm
    private void xoaSPKhoiBangTam() {

        int row = tableSPDaChon.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn sản phẩm cần xóa khỏi danh sách!");
            return;
        }

        modelSP.removeRow(row);
    }

    // Xóa trắng
    private void xoaTrang() {

        // Mã phiếu mới luôn lấy từ sinhMaPNK()
        txtMaPNK.setText(pnk_dao.sinhMaPNK());
        txtMaPNK.setEditable(false);

        txtNgayLap.setText(LocalDate.now().format(formatter));
        txtTrangThai.setText("");
        txtSoLuongSP.setText("");
        txtSoLuongSP.setEditable(true);

        if (cboNhanVien.getItemCount() > 0) {
            cboNhanVien.setSelectedIndex(0);
        }
        if (cboMaSP.getItemCount() > 0) {
            cboMaSP.setSelectedIndex(0);
        }

        modelSP.setRowCount(0);

        tablePNK.clearSelection();
        txtTrangThai.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        // Thêm sản phẩm vào bảng tạm
        if (o.equals(btnThemSP)) {

            themSPVaoBangTam();
        }

        // Xóa sản phẩm khỏi bảng tạm
        else if (o.equals(btnXoaSP)) {

            xoaSPKhoiBangTam();
        }

        // Thêm phiếu nhập kho + toàn bộ chi tiết trong bảng tạm
        else if (o.equals(btnThem)) {

            if (!validData()) {
                return;
            }

            if (modelSP.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng thêm ít nhất 1 sản phẩm vào phiếu nhập!");
                return;
            }

            try {

                // Luôn sinh mã mới, tránh trùng khi đang chọn dòng trong bảng
                String ma = pnk_dao.sinhMaPNK();

                LocalDate ngayLap = LocalDate.parse(
                        txtNgayLap.getText().trim(), formatter);

                String maNV = cboNhanVien.getSelectedItem().toString();
                String trangThai = txtTrangThai.getText().trim();

                NhanVien nv = new NhanVien(maNV);
                PhieuNhapKho pnk = new PhieuNhapKho(ma, ngayLap, nv, trangThai);

                // Lấy danh sách chi tiết từ bảng tạm
                ArrayList<ChiTietPhieuNhapKho> dsChiTiet = new ArrayList<>();

                for (int i = 0; i < modelSP.getRowCount(); i++) {
                    String maSP = modelSP.getValueAt(i, 0).toString();
                    int soLuong = Integer.parseInt(
                            modelSP.getValueAt(i, 2).toString());

                    dsChiTiet.add(new ChiTietPhieuNhapKho(
                            new PhieuNhapKho(ma), new SanPham(maSP), soLuong));
                }

                // Thêm phiếu + chi tiết + cập nhật tồn kho trong 1 transaction
                if (ctPNK_dao.themPhieuNhapKhoVaNhieuChiTiet(pnk, dsChiTiet)) {

                    JOptionPane.showMessageDialog(this,
                            "Thêm phiếu nhập kho thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this,
                            "Thêm phiếu nhập kho thất bại!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Dữ liệu không hợp lệ!");
            }
        }

        // Sửa phiếu nhập kho (chỉ sửa thông tin phiếu)
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
                PhieuNhapKho pnk = new PhieuNhapKho(ma, ngayLap, nv, trangThai);

                if (pnk_dao.suaPhieuNhapKho(pnk)) {

                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
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

                    JOptionPane.showMessageDialog(this, "Xóa thành công!");

                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
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

            PhieuNhapKho pnk = pnk_dao.timPhieuNhapKho(ma);

            if (pnk != null) {

                ArrayList<ChiTietPhieuNhapKho> dsCT =
                        ctPNK_dao.timChiTietPhieuNhapKho(pnk.getMaPNK());

                modelPNK.addRow(taoDong(pnk, dsCT));

            } else {

                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy phiếu nhập kho!");
            }
        }

        // Xóa trắng
        else if (o.equals(btnXoaTrang)) {

            xoaTrang();
        }

        // Mở màn hình chi tiết phiếu nhập kho
        else if (o.equals(btnChiTiet)) {

            new ChiTietPhieuNhapKho_GUI().setVisible(true);
        }
    }
}