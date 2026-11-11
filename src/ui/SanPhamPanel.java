package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

public class SanPhamPanel extends JPanel {

    private static final String KHONG_CO = "(Không có)";

    private final SanPhamDAO dao = new SanPhamDAO();
    private final DanhMucDAO danhMucDAO = new DanhMucDAO();
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    private ArrayList<DanhMuc> dsDanhMuc = new ArrayList<>();
    private ArrayList<KhuyenMai> dsKhuyenMai = new ArrayList<>();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Mã SP", "Tên sản phẩm", "Giá nhập", "Tồn kho", "Danh mục", "Khuyến mãi" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField txtMa = new JTextField(10);
    private final JTextField txtTen = new JTextField(20);
    private final JTextField txtGiaNhap = new JTextField(10);
    private final JTextField txtTon = new JTextField(8);
    private final JComboBox<String> cboDanhMuc = new JComboBox<>();
    private final JComboBox<String> cboKhuyenMai = new JComboBox<>();
    private final JTextField txtTim = new JTextField(20);

    public SanPhamPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel dong1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dong1.add(new JLabel("Mã SP:"));
        dong1.add(txtMa);
        dong1.add(new JLabel("Tên SP:"));
        dong1.add(txtTen);
        dong1.add(new JLabel("Giá nhập:"));
        dong1.add(txtGiaNhap);
        dong1.add(new JLabel("Số lượng tồn:"));
        dong1.add(txtTon);

        JPanel dong2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dong2.add(new JLabel("Danh mục:"));
        dong2.add(cboDanhMuc);
        dong2.add(new JLabel("Khuyến mãi:"));
        dong2.add(cboKhuyenMai);

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xóa trắng");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlNut.add(btnThem);
        pnlNut.add(btnSua);
        pnlNut.add(btnXoa);
        pnlNut.add(btnXoaTrang);

        JButton btnTim = new JButton("Tìm");
        JButton btnTatCa = new JButton("Hiện tất cả");
        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTim.add(new JLabel("Từ khóa (mã/tên):"));
        pnlTim.add(txtTim);
        pnlTim.add(btnTim);
        pnlTim.add(btnTatCa);

        JPanel pnlBac = new JPanel(new GridLayout(0, 1));
        pnlBac.add(dong1);
        pnlBac.add(dong2);
        pnlBac.add(pnlNut);
        pnlBac.add(pnlTim);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(pnlBac, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    docDongVaoForm(r);
                }
            }
        });

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnXoaTrang.addActionListener(e -> xoaTrang());
        btnTim.addActionListener(e -> {
            String tk = txtTim.getText().trim();
            napBang(tk.isEmpty() ? dao.getAllSanPham() : dao.timSanPham(tk));
        });
        btnTatCa.addActionListener(e -> {
            txtTim.setText("");
            lamMoi();
        });

        lamMoi();
    }

    public void lamMoi() {
        napComboBox();
        napBang(dao.getAllSanPham());
        xoaTrang();
    }

    private void napComboBox() {
        dsDanhMuc = danhMucDAO.getAllDanhMuc();
        dsKhuyenMai = khuyenMaiDAO.getAllKhuyenMai();

        cboDanhMuc.removeAllItems();
        cboDanhMuc.addItem(KHONG_CO);
        for (DanhMuc dm : dsDanhMuc) {
            cboDanhMuc.addItem(dm.getMaDM() + " - " + dm.getTenDM());
        }

        cboKhuyenMai.removeAllItems();
        cboKhuyenMai.addItem(KHONG_CO);
        for (KhuyenMai km : dsKhuyenMai) {
            cboKhuyenMai.addItem(km.getMaKM() + " - " + km.getTenKM());
        }
    }

    private void napBang(ArrayList<SanPham> ds) {
        model.setRowCount(0);
        for (SanPham sp : ds) {
            model.addRow(new Object[] {
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getGiaNhap(),
                    sp.getSoLuongTon(),
                    sp.getDanhMuc() == null ? "" : sp.getDanhMuc().getMaDM() + " - " + sp.getDanhMuc().getTenDM(),
                    sp.getKhuyenMai() == null ? "" : sp.getKhuyenMai().getMaKM() + " - " + sp.getKhuyenMai().getTenKM()
            });
        }
    }

    private void docDongVaoForm(int r) {
        txtMa.setText(String.valueOf(model.getValueAt(r, 0)));
        txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
        txtGiaNhap.setText(String.valueOf(model.getValueAt(r, 2)));
        txtTon.setText(String.valueOf(model.getValueAt(r, 3)));
        chonCombo(cboDanhMuc, String.valueOf(model.getValueAt(r, 4)));
        chonCombo(cboKhuyenMai, String.valueOf(model.getValueAt(r, 5)));
        txtMa.setEditable(false);
    }

    private void chonCombo(JComboBox<String> cbo, String giaTri) {
        if (giaTri == null || giaTri.isEmpty()) {
            cbo.setSelectedIndex(0);
            return;
        }
        cbo.setSelectedItem(giaTri);
        if (cbo.getSelectedItem() == null || !giaTri.equals(cbo.getSelectedItem())) {
            cbo.setSelectedIndex(0);
        }
    }

    private void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtGiaNhap.setText("");
        txtTon.setText("");
        if (cboDanhMuc.getItemCount() > 0) {
            cboDanhMuc.setSelectedIndex(0);
        }
        if (cboKhuyenMai.getItemCount() > 0) {
            cboKhuyenMai.setSelectedIndex(0);
        }
        txtMa.setEditable(true);
        table.clearSelection();
        txtMa.requestFocus();
    }

    /** Đọc dữ liệu từ form, trả về null (và báo lỗi) nếu dữ liệu không hợp lệ. */
    private SanPham docForm() {
        String ma = txtMa.getText().trim();
        String ten = txtTen.getText().trim();
        if (ma.isEmpty() || ten.isEmpty() || txtGiaNhap.getText().trim().isEmpty()
                || txtTon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin sản phẩm!");
            return null;
        }
        if (ma.length() > 10 || ten.length() > 20) {
            JOptionPane.showMessageDialog(this, "Mã SP tối đa 10 ký tự, tên SP tối đa 20 ký tự!");
            return null;
        }
        BigDecimal giaNhap;
        int ton;
        try {
            giaNhap = new BigDecimal(txtGiaNhap.getText().trim());
            ton = Integer.parseInt(txtTon.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá nhập và số lượng tồn phải là số!");
            return null;
        }
        if (giaNhap.signum() < 0 || ton < 0) {
            JOptionPane.showMessageDialog(this, "Giá nhập và số lượng tồn không được âm!");
            return null;
        }

        int iDM = cboDanhMuc.getSelectedIndex();
        int iKM = cboKhuyenMai.getSelectedIndex();
        DanhMuc dm = iDM > 0 ? dsDanhMuc.get(iDM - 1) : null;
        KhuyenMai km = iKM > 0 ? dsKhuyenMai.get(iKM - 1) : null;

        return new SanPham(ma, ten, giaNhap, ton, km, dm);
    }

    private void them() {
        SanPham sp = docForm();
        if (sp == null) {
            return;
        }
        if (dao.themSanPham(sp)) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (mã sản phẩm có thể đã tồn tại)!");
        }
    }

    private void sua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần sửa!");
            return;
        }
        SanPham sp = docForm();
        if (sp == null) {
            return;
        }
        if (dao.suaSanPham(sp)) {
            JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
        }
    }

    private void xoa() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn sản phẩm cần xóa!");
            return;
        }
        String ma = String.valueOf(model.getValueAt(r, 0));
        int chon = JOptionPane.showConfirmDialog(this, "Xóa sản phẩm " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        if (dao.xoaSanPham(ma)) {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Không xóa được (sản phẩm đã có trong hóa đơn hoặc phiếu nhập kho)!");
        }
    }
}
