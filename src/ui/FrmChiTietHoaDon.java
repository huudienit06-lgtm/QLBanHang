package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import dao.SanPhamDAO;
import entity.HoaDon;
import entity.SanPham;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class FrmChiTietHoaDon extends JFrame implements ActionListener {

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
    private final JButton btnTimSanPham;
    private DefaultTableModel modelSP;
    private final JTable tblSP;

    public FrmChiTietHoaDon(HoaDon hd) throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 900);
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

        JSpinner spnSoLuong = new JSpinner(model);
        txtTongTien = new JTextField();
        txtTongTien.setEditable(false);
        pnlThongTinSanPham.add(spnSoLuong);
        pnlThongTinSanPham.add(txtTongTien);

        JPanel pnlTimSanPham = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTimSanPham.add(new JLabel("Mã:"));
        txtTimTheoMa = new JTextField(10);
        pnlTimSanPham.add(txtTimTheoMa);

        pnlTimSanPham.add(new JLabel("Tên:"));
        txtTimTheoTen = new JTextField(10);
        pnlTimSanPham.add(txtTimTheoTen);

        btnTimSanPham = new JButton("Tìm sản phẩm theo thuộc tính");
        pnlTimSanPham.add(btnTimSanPham);

        String[] headerSP = {"Mã Sản Phẩm", "Tên Sản Phẩm", "Danh Mục", "Giá Bán", "Giá Sau Khuyến Mãi"};
        modelSP = new DefaultTableModel(headerSP, 0);
        tblSP = new JTable(modelSP);
        tblSP.setPreferredScrollableViewportSize(
                new Dimension(300, 200)
        );
        tblSP.setRowHeight(10);

        JPanel pnlSanPham = new JPanel(new BorderLayout(5, 10));
        pnlSanPham.setBorder(BorderFactory.createTitledBorder("Tìm Sản Phẩm"));
        pnlSanPham.add(pnlTimSanPham, BorderLayout.NORTH);
        pnlSanPham.add(new JScrollPane(tblSP), BorderLayout.CENTER);

        String[] header = {
            "Mã sản phẩm",
            "Tên sản phẩm",
            "Giá bán",
            "Giá sau khuyến mãi",
            "Số lượng",
            "Tổng tiền"
        };

        modelCTHD = new DefaultTableModel(header, 0);
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

        btnTimSanPham.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnTimSanPham)) {
            modelSP.setRowCount(0);
            String maSP = txtTimTheoMa.getText().trim();
            String tenSP = txtTimTheoTen.getText().trim();
            String thuocTinhTim = tenSP.isEmpty() ? maSP : tenSP;
            ArrayList<SanPham> rs = new SanPhamDAO().timSanPham(thuocTinhTim);
            for (SanPham sp : rs) {
                modelSP.addRow(new Object[]{sp.getMaSP(), sp.getTenSP(), sp.getDanhMuc().getTenDM(), sp.tinhGiaBan(), sp.tinhGiaBanSauKhuyenMai()});
            }
        }
    }

    public static void main(String[] args) {
        new FrmChiTietHoaDon(new HoaDon()).setVisible(true);
    }

}
