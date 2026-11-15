package ui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class QuanLySanPhamFrame extends JFrame implements ChangeListener {

    private JTabbedPane tabs;
    private SanPhamPanel pnlSanPham;
    private DanhMucPanel pnlDanhMuc;
    private KhuyenMaiPanel pnlKhuyenMai;
    private ThongKeSanPhamPanel pnlThongKe;

    public QuanLySanPhamFrame() {
        setTitle("Quản lý Sản phẩm - Danh mục - Khuyến mãi");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pnlSanPham = new SanPhamPanel();
        pnlDanhMuc = new DanhMucPanel();
        pnlKhuyenMai = new KhuyenMaiPanel();
        pnlThongKe = new ThongKeSanPhamPanel();

        tabs = new JTabbedPane();
        tabs.setFont(GiaoDien.FONT_DAM);
        tabs.addTab("Sản phẩm", pnlSanPham);
        tabs.addTab("Danh mục", pnlDanhMuc);
        tabs.addTab("Khuyến mãi", pnlKhuyenMai);
        tabs.addTab("Thống kê sản phẩm", pnlThongKe);
        tabs.addChangeListener(this);

        add(tabs);
    }

    // Mỗi lần chuyển tab thì nạp lại dữ liệu mới nhất cho tab đó
    @Override
    public void stateChanged(ChangeEvent e) {
        int viTri = tabs.getSelectedIndex();
        if (viTri == 0) {
            pnlSanPham.lamMoi();
        } else if (viTri == 1) {
            pnlDanhMuc.lamMoi();
        } else if (viTri == 2) {
            pnlKhuyenMai.lamMoi();
        } else if (viTri == 3) {
            pnlThongKe.lamMoi();
        }
    }

    public static void main(String[] args) {
        new QuanLySanPhamFrame().setVisible(true);
    }
}
