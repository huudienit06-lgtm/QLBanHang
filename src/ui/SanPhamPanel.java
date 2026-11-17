package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
// import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
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

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ----- Phần trên: form nhập, các nút, ô tìm kiếm -----
        txtMaSP = new JTextField(10);
        txtMaSP.setEditable(false);
        txtTenSP = new JTextField(20);
        txtGiaNhap = new JTextField(10);
        txtSoLuongTon = new JTextField(8);
        cboDanhMuc = new JComboBox<String>();
        cboKhuyenMai = new JComboBox<String>();

        JPanel dongForm1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongForm1.add(new JLabel("Mã SP:"));
        dongForm1.add(txtMaSP);
        dongForm1.add(new JLabel("Tên SP:"));
        dongForm1.add(txtTenSP);
        dongForm1.add(new JLabel("Giá nhập:"));
        dongForm1.add(txtGiaNhap);
        dongForm1.add(new JLabel("Số lượng tồn:"));
        dongForm1.add(txtSoLuongTon);

        JPanel dongForm2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dongForm2.add(new JLabel("Danh mục:"));
        dongForm2.add(cboDanhMuc);
        dongForm2.add(new JLabel("Khuyến mãi:"));
        dongForm2.add(cboKhuyenMai);

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
        add(pnlBac, BorderLayout.NORTH);

        String[] tenCot = { "Mã SP", "Tên sản phẩm", "Giá nhập", "Tồn kho", "Danh mục", "Khuyến mãi" };
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
            String giaNhap = BigDecimal.valueOf(sp.getGiaNhap()).stripTrailingZeros().toPlainString();
            Object[] dong = { sp.getMaSP(), sp.getTenSP(), giaNhap, sp.getSoLuongTon(), tenDM, tenKM };
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

        double giaNhap = 0;
        int soLuongTon = 0;
        try {
            giaNhap = Double.parseDouble(giaText);
            soLuongTon = Integer.parseInt(tonText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá nhập và số lượng tồn phải là số!");
            return null;
        }
        if (giaNhap < 0 || soLuongTon < 0) {
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

        SanPham sp = new SanPham();
        sp.setMaSP(maSP);
        sp.setTenSP(tenSP);
        sp.setGiaNhap(giaNhap);
        sp.setSoLuongTon(soLuongTon);
        sp.setKhuyenMai(khuyenMai);
        sp.setDanhMuc(danhMuc);
        return sp;
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
