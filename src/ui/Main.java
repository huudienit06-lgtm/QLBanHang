package ui;

import entity.NhanVien;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main extends JFrame {

    private TaiKhoan taiKhoan = null;
    JPanel pnlContent = new JPanel(new BorderLayout());
    JPanel pnlMenu = new JPanel(new BorderLayout());
    
    public Main() throws HeadlessException {
        
        setTitle("Quản Lý Bán Hàng");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // pnlMenu.add(new MenuQL(), BorderLayout.NORTH);
        add(pnlMenu, BorderLayout.NORTH);

        add(pnlContent, BorderLayout.CENTER);
        setContent(new FrmDangNhap(this));

    }

    private void setContent(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    private void setMenu(JPanel panel) {
        pnlMenu.removeAll();
        if (panel != null) {
            pnlMenu.add(panel, BorderLayout.NORTH);
        }
        pnlMenu.revalidate();
        pnlMenu.repaint();
    }

    public void dangNhapThanhCong(TaiKhoan tk) {
        this.taiKhoan = tk;
        NhanVien nv = taiKhoan.getNhanVien();
        setMenu(new Menu(nv, this));
        setContent(taoTrangChuTheoVaiTro());
    }

    private JPanel taoTrangChuTheoVaiTro() {
        NhanVien nv = taiKhoan.getNhanVien();
        if (nv != null && "Quản lý".equalsIgnoreCase(nv.getChucVu())) {
            return new FrmQuanLi(nv);
        }
        return new FrmNhanVien(nv);
    }

    public void xuLyChucNang(String chucNang) {
        switch (chucNang) {
            case "Trang Chủ":
                setContent(taoTrangChuTheoVaiTro());
                break;
            case "Sản Phẩm":
                setContent(new SanPhamPanel());
                break;
            case "Khách Hàng":
                setContent(new KhachHang_gui());
                break;
            case "Nhân Viên":
                setContent(new NhanVien_gui());
                break;
            case "Danh Mục":
                setContent(new DanhMucPanel());
                break;
            case "Khuyến Mãi":
                setContent(new KhuyenMaiPanel());
                break;
            case "Thuế":
                setContent(new Thue_GUI());
                break;
            case "Tài khoản":
                setContent(new TaiKhoan_gui());
                break;
            case "Thống kê sản phẩm":
                setContent(new ThongKeSanPhamPanel());
                break;
            case "Lập hóa đơn":
                setContent(new FrmHoaDon(taiKhoan));
                break;
            case "Lập phiếu nhập kho":
                setContent(new PhieuNhapKho_GUI());
                break;
            case "Đăng Xuất":
                setMenu(null);
                setContent(new FrmDangNhap(this));
                break;
            case "Thống kê nhập kho":
                setContent(new ThongKeNhapKho_GUI());
                break;
            default:
                JOptionPane.showMessageDialog(this, "Chức năng \"" + chucNang + "\" chưa được xây dựng.");
                break;
        }

    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
