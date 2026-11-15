package ui;

import dao.DanhMucDAO;
import dao.KhuyenMaiDAO;
import dao.SanPhamDAO;
import entity.DanhMuc;
import entity.KhuyenMai;
import entity.SanPham;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
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

import dao.DanhMucDAO;
import dao.KhuyenMaiDAO;
import dao.SanPhamDAO;
import entity.DanhMuc;
import entity.KhuyenMai;
import entity.SanPham;

public class SanPhamPanel extends JPanel implements ActionListener, MouseListener {

    private SanPhamDAO sanPhamDAO;
    private DanhMucDAO danhMucDAO;
    private KhuyenMaiDAO khuyenMaiDAO;

    // Danh sách danh mục, khuyến mãi đang hiển thị trong 2 ô chọn
    private ArrayList<DanhMuc> dsDanhMuc;
    private ArrayList<KhuyenMai> dsKhuyenMai;

    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtGiaNhap;
    private JTextField txtSoLuongTon;
    private JComboBox<String> cboDanhMuc;
    private JComboBox<String> cboKhuyenMai;
    private JTextField txtTimKiem;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnTim;
    private JButton btnHienTatCa;

    public SanPhamPanel() {
        sanPhamDAO = new SanPhamDAO();
        danhMucDAO = new DanhMucDAO();
        khuyenMaiDAO = new KhuyenMaiDAO();
        dsDanhMuc = new ArrayList<DanhMuc>();
        dsKhuyenMai = new ArrayList<KhuyenMai>();

        setLayout(new BorderLayout(0, 12));
        setBackground(GiaoDien.MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // ----- Phần trên: tiêu đề, form nhập, các nút -----
        JPanel pnlTren = new JPanel(new BorderLayout(0, 10));
        pnlTren.setOpaque(false);
        pnlTren.add(GiaoDien.taoTieuDe("QUẢN LÝ SẢN PHẨM"), BorderLayout.NORTH);

        JPanel pnlForm = GiaoDien.taoKhungForm("Thông tin sản phẩm");
        txtMaSP = GiaoDien.taoOText(15);
        GiaoDien.khoaOText(txtMaSP);
        txtTenSP = GiaoDien.taoOText(20);
        txtGiaNhap = GiaoDien.taoOText(15);
        txtSoLuongTon = GiaoDien.taoOText(15);
        cboDanhMuc = GiaoDien.taoComboBox();
        cboKhuyenMai = GiaoDien.taoComboBox();

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Mã sản phẩm:"), 0, 0, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtMaSP, 1, 0, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Tên sản phẩm:"), 2, 0, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtTenSP, 3, 0, 1);

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Giá nhập:"), 0, 1, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtGiaNhap, 1, 1, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Số lượng tồn:"), 2, 1, 0);
        GiaoDien.themVaoLuoi(pnlForm, txtSoLuongTon, 3, 1, 1);

        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Danh mục:"), 0, 2, 0);
        GiaoDien.themVaoLuoi(pnlForm, cboDanhMuc, 1, 2, 1);
        GiaoDien.themVaoLuoi(pnlForm, GiaoDien.taoNhan("Khuyến mãi:"), 2, 2, 0);
        GiaoDien.themVaoLuoi(pnlForm, cboKhuyenMai, 3, 2, 1);
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

        String[] tenCot = { "Mã SP", "Tên sản phẩm", "Giá nhập", "Tồn kho", "Danh mục", "Khuyến mãi" };
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

    // Nạp lại 2 ô chọn và bảng từ database, rồi chuẩn bị form để thêm mới
    public void lamMoi() {
        napComboBox();
        hienThiLenBang(sanPhamDAO.getAllSanPham());
        xoaTrang();
    }

    private void napComboBox() {
        dsDanhMuc = danhMucDAO.getAllDanhMuc();
        dsKhuyenMai = khuyenMaiDAO.getAllKhuyenMai();

        cboDanhMuc.removeAllItems();
        cboDanhMuc.addItem("(Không có)");
        for (int i = 0; i < dsDanhMuc.size(); i++) {
            DanhMuc dm = dsDanhMuc.get(i);
            cboDanhMuc.addItem(dm.getMaDM() + " - " + dm.getTenDM());
        }

        cboKhuyenMai.removeAllItems();
        cboKhuyenMai.addItem("(Không có)");
        for (int i = 0; i < dsKhuyenMai.size(); i++) {
            KhuyenMai km = dsKhuyenMai.get(i);
            cboKhuyenMai.addItem(km.getMaKM() + " - " + km.getTenKM());
        }
    }

    private void hienThiLenBang(ArrayList<SanPham> dsSP) {
        tableModel.setRowCount(0);
        for (int i = 0; i < dsSP.size(); i++) {
            SanPham sp = dsSP.get(i);
            String tenDM = "";
            if (sp.getDanhMuc() != null) {
                tenDM = sp.getDanhMuc().getMaDM() + " - " + sp.getDanhMuc().getTenDM();
            }
            String tenKM = "";
            if (sp.getKhuyenMai() != null) {
                tenKM = sp.getKhuyenMai().getMaKM() + " - " + sp.getKhuyenMai().getTenKM();
            }
            Object[] dong = { sp.getMaSP(), sp.getTenSP(), sp.getGiaNhap(), sp.getSoLuongTon(), tenDM, tenKM };
            tableModel.addRow(dong);
        }
    }

    // Xóa các ô nhập và tự đặt sẵn mã mới vào form
    private void xoaTrang() {
        txtMaSP.setText(sanPhamDAO.phatSinhMa());
        txtTenSP.setText("");
        txtGiaNhap.setText("");
        txtSoLuongTon.setText("");
        if (cboDanhMuc.getItemCount() > 0) {
            cboDanhMuc.setSelectedIndex(0);
        }
        if (cboKhuyenMai.getItemCount() > 0) {
            cboKhuyenMai.setSelectedIndex(0);
        }
        table.clearSelection();
        txtTenSP.requestFocus();
    }

    // Kiểm tra dữ liệu trên form, hợp lệ thì trả về đối tượng SanPham, sai thì báo lỗi và trả về null
    private SanPham layDuLieuTuForm() {
        String maSP = txtMaSP.getText().trim();
        String tenSP = txtTenSP.getText().trim();
        String giaText = txtGiaNhap.getText().trim();
        String tonText = txtSoLuongTon.getText().trim();

        if (tenSP.equals("") || giaText.equals("") || tonText.equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin sản phẩm!");
            return null;
        }
        if (tenSP.length() > 20) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm tối đa 20 ký tự!");
            txtTenSP.requestFocus();
            return null;
        }

        BigDecimal giaNhap = null;
        int soLuongTon = 0;
        try {
            giaNhap = new BigDecimal(giaText);
            soLuongTon = Integer.parseInt(tonText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá nhập và số lượng tồn phải là số!");
            return null;
        }
        if (giaNhap.signum() < 0 || soLuongTon < 0) {
            JOptionPane.showMessageDialog(this, "Giá nhập và số lượng tồn không được âm!");
            return null;
        }

        // Dòng đầu của ô chọn là "(Không có)" nên danh sách lệch 1 vị trí
        DanhMuc danhMuc = null;
        int viTriDM = cboDanhMuc.getSelectedIndex();
        if (viTriDM > 0) {
            danhMuc = dsDanhMuc.get(viTriDM - 1);
        }
        KhuyenMai khuyenMai = null;
        int viTriKM = cboKhuyenMai.getSelectedIndex();
        if (viTriKM > 0) {
            khuyenMai = dsKhuyenMai.get(viTriKM - 1);
        }

        return new SanPham(maSP, tenSP, giaNhap.doubleValue(), soLuongTon, khuyenMai, danhMuc);
    }

    private void themSanPham() {
        SanPham sp = layDuLieuTuForm();
        if (sp == null) {
            return;
        }
        if (sanPhamDAO.themSanPham(sp)) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại!");
        }
    }

    private void suaSanPham() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần sửa!");
            return;
        }
        SanPham sp = layDuLieuTuForm();
        if (sp == null) {
            return;
        }
        if (sanPhamDAO.suaSanPham(sp)) {
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm thất bại!");
        }
    }

    private void xoaSanPham() {
        int dong = table.getSelectedRow();
        if (dong < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần xóa!");
            return;
        }
        String maSP = tableModel.getValueAt(dong, 0).toString();
        int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm " + maSP + "?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        if (sanPhamDAO.xoaSanPham(maSP)) {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Không xóa được vì sản phẩm đã có trong hóa đơn hoặc phiếu nhập kho!");
        }
    }

    private void timSanPham() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.equals("")) {
            hienThiLenBang(sanPhamDAO.getAllSanPham());
        } else {
            hienThiLenBang(sanPhamDAO.timSanPham(tuKhoa));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon.equals(btnThem)) {
            themSanPham();
        } else if (nguon.equals(btnSua)) {
            suaSanPham();
        } else if (nguon.equals(btnXoa)) {
            xoaSanPham();
        } else if (nguon.equals(btnLamMoi)) {
            txtTimKiem.setText("");
            lamMoi();
        } else if (nguon.equals(btnTim)) {
            timSanPham();
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
            txtMaSP.setText(tableModel.getValueAt(dong, 0).toString());
            txtTenSP.setText(tableModel.getValueAt(dong, 1).toString());
            txtGiaNhap.setText(tableModel.getValueAt(dong, 2).toString());
            txtSoLuongTon.setText(tableModel.getValueAt(dong, 3).toString());
            chonTrongComboBox(cboDanhMuc, tableModel.getValueAt(dong, 4).toString());
            chonTrongComboBox(cboKhuyenMai, tableModel.getValueAt(dong, 5).toString());
        }
    }

    // Chọn đúng mục trong ô chọn theo chữ hiển thị; để trống thì chọn "(Không có)"
    private void chonTrongComboBox(JComboBox<String> cbo, String noiDung) {
        if (noiDung.equals("")) {
            cbo.setSelectedIndex(0);
            return;
        }
        for (int i = 0; i < cbo.getItemCount(); i++) {
            if (cbo.getItemAt(i).equals(noiDung)) {
                cbo.setSelectedIndex(i);
                return;
            }
        }
        cbo.setSelectedIndex(0);
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
