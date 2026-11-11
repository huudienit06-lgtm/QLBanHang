package ui;

import javax.swing.*;

import connectdb.ConnectDB;

public class mainGD extends JFrame {
    public mainGD() {
        setTitle("Quản lý bán hàng");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Nhân viên", new NhanVien_gui());
        tabs.addTab("Tài khoản", new TaiKhoan_gui());
        tabs.addTab("Khách hàng", new KhachHang_gui());
        ThongKeKhachHang_gui tk = new ThongKeKhachHang_gui();
        tabs.addTab("Thống kê khách hàng", tk);
        tabs.addChangeListener(e -> { if (tabs.getSelectedComponent() == tk) tk.thongKe(); });
        add(tabs);
    }

    public static void main(String[] args) {
        try {
            ConnectDB.getInstance().connect(); // sửa lại nếu ConnectDB của bạn dùng tên phương thức khác
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new mainGD().setVisible(true));
    }
}