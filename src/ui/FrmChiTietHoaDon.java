package ui;

import dao.ChiTietHoaDonDAO;
import dao.SanPhamDAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.SanPham;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class FrmChiTietHoaDon extends JDialog implements ActionListener, DocumentListener, MouseListener, ChangeListener {

    private final ChiTietHoaDonDAO dao = new ChiTietHoaDonDAO();
    private final SanPhamDAO spDao = new SanPhamDAO();
    private final JTextField txtMaSP;
    private final JTextField txtTenSP;
    // private final JTextField txtSoLuong;
    private final JTextField txtGiaBan;
    private JTextField txtGiaSauKhuyenMai;
    private JTextField txtTongTien;
    private final JTable tblCTHD;
    private final DefaultTableModel modelCTHD;
    private final JButton btnThem;
    private final JButton btnXoa;
    private final JButton btnSua;
    private final JButton btnXoaTrang;
    private final JButton btnHoanTat;
    private final JTextField txtTimTheoTen;
    private final JTextField txtTimTheoMa;

    private DefaultTableModel modelSP;
    private final JTable tblSP;
    private JSpinner spnSoLuong;
    private final HoaDon hoaDon;

    public FrmChiTietHoaDon(JFrame parent, HoaDon hd , DefaultTableModel modelCT) {
        super(parent, "Chi tiết hóa đơn", true);
        hoaDon = hd;
        modelCTHD=modelCT;
        // setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Chi tiết hóa đơn");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel pnlTitle = new JPanel(new FlowLayout());
        pnlTitle.add(lblTitle);
        add(pnlTitle, BorderLayout.NORTH);

        JPanel pnlThongTinSanPham = new JPanel(new GridLayout(6, 2, 10, 5));
        pnlThongTinSanPham.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        pnlThongTinSanPham.add(new JLabel("Mã sản phẩm:"));
        pnlThongTinSanPham.add(new JLabel("Tên sản phẩm:"));
        txtMaSP = new JTextField();
        txtMaSP.setEditable(false);
        txtTenSP = new JTextField();
        txtTenSP.setEditable(false);
        pnlThongTinSanPham.add(txtMaSP);
        pnlThongTinSanPham.add(txtTenSP);

        pnlThongTinSanPham.add(new JLabel("Giá bán:"));
        pnlThongTinSanPham.add(new JLabel("Giá sau Khuyến mãi(nếu có):"));
        txtGiaBan = new JTextField();
        txtGiaBan.setEditable(false);
        txtGiaSauKhuyenMai = new JTextField();
        txtGiaSauKhuyenMai.setEditable(false);
        pnlThongTinSanPham.add(txtGiaBan);
        pnlThongTinSanPham.add(txtGiaSauKhuyenMai);

        pnlThongTinSanPham.add(new JLabel("Số lượng:"));
        pnlThongTinSanPham.add(new JLabel("Tổng tiền:"));
        SpinnerNumberModel model = new SpinnerNumberModel(
                1, // Giá trị mặc định
                1, // Giá trị nhỏ nhất
                100, // Giá trị lớn nhất
                1 // Bước tăng giảm
        );

        spnSoLuong = new JSpinner(model);
        txtTongTien = new JTextField();
        txtTongTien.setEditable(false);
        pnlThongTinSanPham.add(spnSoLuong);
        pnlThongTinSanPham.add(txtTongTien);

        JPanel pnlTimSanPham = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTimSanPham.add(new JLabel("Tìm Theo Mã:"));
        txtTimTheoMa = new JTextField(10);
        pnlTimSanPham.add(txtTimTheoMa);

        pnlTimSanPham.add(new JLabel("Tìm Theo Tên:"));
        txtTimTheoTen = new JTextField(10);
        pnlTimSanPham.add(txtTimTheoTen);

        String[] headerSP = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Số Lượng Tồn", "Giá Bán", "Giá Sau Khuyến Mãi"};
        modelSP = new DefaultTableModel(headerSP, 0);

        tblSP = new JTable(modelSP);
        tblSP.setPreferredScrollableViewportSize(
                new Dimension(300, 200)
        );
        tblSP.setRowHeight(20);

        JPanel pnlSanPham = new JPanel(new BorderLayout(5, 10));
        pnlSanPham.setBorder(BorderFactory.createTitledBorder("Tìm Sản Phẩm"));
        pnlSanPham.add(pnlTimSanPham, BorderLayout.NORTH);
        pnlSanPham.add(new JScrollPane(tblSP), BorderLayout.CENTER);

        // String[] header = {
        //     "Mã sản phẩm",
        //     "Tên sản phẩm",
        //     "Giá bán",
        //     "Giá sau khuyến mãi",
        //     "Số lượng",
        //     "Tổng tiền"
        // };

        // modelCTHD = new DefaultTableModel(header, 0);
        tblCTHD = new JTable(modelCTHD);
        tblCTHD.setRowHeight(25);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnXoaTrang = new JButton("Xóa Trắng");
        btnHoanTat = new JButton("Hoàn tất");
        pnlButton.add(btnThem);
        pnlButton.add(btnXoa);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoaTrang);
        pnlButton.add(btnHoanTat);

        JPanel pnlCTHD = new JPanel(new BorderLayout(5, 10));
        pnlCTHD.add(new JScrollPane(tblCTHD), BorderLayout.CENTER);
        pnlCTHD.add(pnlButton, BorderLayout.SOUTH);

        // pnlCTHD.add(pnlThongTinSanPham, BorderLayout.NORTH);
        JPanel pnlThemSanPham = new JPanel(new GridLayout(1, 2, 5, 10));
        pnlThemSanPham.add(pnlThongTinSanPham);
        pnlThemSanPham.add(pnlSanPham);

        JPanel pnlTop = new JPanel(new BorderLayout(5, 10));
        pnlTop.add(pnlTitle, BorderLayout.NORTH);
        pnlTop.add(pnlThemSanPham, BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCTHD, BorderLayout.CENTER);
        // add(pnlButton,BorderLayout.SOUTH);

        tblSP.addMouseListener(this);
        tblCTHD.addMouseListener(this);
        txtTimTheoMa.getDocument().addDocumentListener(this);
        txtTimTheoTen.getDocument().addDocumentListener(this);
        spnSoLuong.addChangeListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnHoanTat.addActionListener(this);
    }

    public ChiTietHoaDon getCTHoaDon() {
        SanPham sp = new SanPhamDAO().findByID(txtMaSP.getText());
        if (sp == null) {
            return null;
        }
        int soLuong = (int) spnSoLuong.getValue();
        ChiTietHoaDon ct = new ChiTietHoaDon(hoaDon, sp, soLuong);
        return ct;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnThem)) {

            if (hoaDon == null || hoaDon.getMaHD() == null || hoaDon.getMaHD().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có mã hóa đơn hợp lệ. Hãy mở chi tiết từ màn hình lập hóa đơn.");
                return;
            }
            if (txtMaSP.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần thêm.");
                return;
            }
            ChiTietHoaDon ct = getCTHoaDon();
            if (ct == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm đã chọn.");
                return;
            }
            spnSoLuong.setValue(1);
            int existingRow = -1;
            for (int i = 0; i < modelCTHD.getRowCount(); i++) {
                if (ct.getSanPham().getMaSP().equals(modelCTHD.getValueAt(i, 0))) {
                    existingRow = i;
                    break;
                }
            }
            if (existingRow >= 0) {
                int newQuantity = Integer.parseInt(modelCTHD.getValueAt(existingRow, 4).toString())
                        + ct.getSoLuong();
                modelCTHD.setValueAt(newQuantity, existingRow, 4);
                modelCTHD.setValueAt(ct.getSanPham().tinhGiaBanSauKhuyenMai() * newQuantity,
                        existingRow, 5);
            } else {
                modelCTHD.addRow(new Object[]{ct.getSanPham().getMaSP(), ct.getSanPham().getTenSP(),
                    ct.getSanPham().tinhGiaBan(), ct.getSanPham().tinhGiaBanSauKhuyenMai(),
                    ct.getSoLuong(), ct.tinhThanhTien()});
            }
            JOptionPane.showMessageDialog(null, "Thêm thành công");
        }
        if (o.equals(btnXoa)) {
            if (tblCTHD.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn hàng cần xóa");
            } else {
                int row = tblCTHD.getSelectedRow();
                if (JOptionPane.showConfirmDialog(this, "Bạn có xác nhận xóa", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    // if (dao.deleteCTHD(hoaDon.getMaHD(), modelCTHD.getValueAt(row, 0).toString())) {
                    JOptionPane.showMessageDialog(null, "Xóa thành công");
                    modelCTHD.removeRow(row);
                    // capNhatModelCTHH();
                    // }
                }

            }
        }
        if (o.equals(btnSua)) {
            int row = tblCTHD.getSelectedRow();
            if ( row >= 0) {
                modelCTHD.setValueAt(txtMaSP.getText(),row, 0);
                modelCTHD.setValueAt(txtTenSP.getText(), row, 1);
                modelCTHD.setValueAt(txtGiaBan.getText(),row, 2);
                modelCTHD.setValueAt(txtGiaSauKhuyenMai.getText(),row, 3);
                modelCTHD.setValueAt(spnSoLuong.getValue(), row, 4);
                modelCTHD.setValueAt(txtTongTien.getText(), row, 5);
            } else {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần sửa");
            }
        }
        if (o.equals(btnXoaTrang)) {
            txtMaSP.setText("");
            txtTenSP.setText("");
            txtGiaBan.setText("");
            txtGiaSauKhuyenMai.setText("");
            txtTongTien.setText("");
            spnSoLuong.setValue(0);
            txtTimTheoMa.setText("");
            txtTimTheoTen.setText("");
            modelSP.setRowCount(0);
        }
        if (o.equals(btnHoanTat)) {
            if (JOptionPane.showConfirmDialog(this, "Bạn muốn hoàn tất thêm sản phẩm", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
            }
        }
    }

    // public void capNhatModelCTHH() {
    //     modelCTHD.setRowCount(0);
    //     for (int i = 0; i<modelCTHD.getRowCount();i++) {
    //         modelCTHD.addRow(new Object[]{modelCTHD.getValueAt(i, 0), modelCTHD.getValueAt(i, 1),modelCTHD.getValueAt(i, 2), modelCTHD.getValueAt(i, 3), modelCTHD.getValueAt(i, 4),modelCTHD.getValueAt(i, 5)});
    //     }
    // }
    public DefaultTableModel getModelCTHD() {
        return modelCTHD;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        capNhatBangSanPham();

    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        capNhatBangSanPham();

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        capNhatBangSanPham();
    }

    public void capNhatBangSanPham() {
        modelSP.setRowCount(0);
        String maSP = txtTimTheoMa.getText().trim();
        String tenSP = txtTimTheoTen.getText().trim();
        String thuocTinhTim = tenSP.isEmpty() ? maSP : tenSP;
        ArrayList<SanPham> rs = new SanPhamDAO().timSanPham(thuocTinhTim);
        for (SanPham sp : rs) {
            modelSP.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getSoLuongTon(), sp.tinhGiaBan(), sp.tinhGiaBanSauKhuyenMai()});
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == tblSP) {
            int row = tblSP.getSelectedRow();
            if (row != -1) {
                txtMaSP.setText(modelSP.getValueAt(row, 0).toString());
                txtTenSP.setText(modelSP.getValueAt(row, 1).toString());
                txtGiaBan.setText(modelSP.getValueAt(row, 3).toString());
                txtGiaSauKhuyenMai.setText(modelSP.getValueAt(row, 4).toString());
            }
            tinhTongTien();
        } else if (e.getSource() == tblCTHD) {
            setCTHD();
        }
    }

    public void setCTHD() {
        int row = tblCTHD.getSelectedRow();
        if (row < 0) {
            return;
        }

        txtMaSP.setText(String.valueOf(modelCTHD.getValueAt(row, 0)));
        txtTenSP.setText(String.valueOf(modelCTHD.getValueAt(row, 1)));
        txtGiaBan.setText(String.valueOf(modelCTHD.getValueAt(row, 2)));
        txtGiaSauKhuyenMai.setText(String.valueOf(modelCTHD.getValueAt(row, 3)));
        spnSoLuong.setValue(Integer.parseInt(String.valueOf(modelCTHD.getValueAt(row, 4))));
        txtTongTien.setText(String.valueOf(modelCTHD.getValueAt(row, 5)));
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

    @Override
    public void stateChanged(ChangeEvent e) {
        tinhTongTien();
    }

    private void tinhTongTien() {
        if (txtGiaSauKhuyenMai.getText().isEmpty()) {
            return;
        }

        double gia = Double.parseDouble(
                txtGiaSauKhuyenMai.getText()
        );

        int soLuong = (int) spnSoLuong.getValue();

        double tongTien = gia * soLuong;

        txtTongTien.setText(String.valueOf(tongTien));
    }

}
