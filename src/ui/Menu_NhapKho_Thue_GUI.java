package ui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import connectdb.ConnectDB;

public class Menu_NhapKho_Thue_GUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;

    public Menu_NhapKho_Thue_GUI() {
        setTitle("Quản lý nhập kho - Thuế - Thống kê");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        themTab("Phiếu nhập kho", new PhieuNhapKho_GUI());
        themTab("Chi tiết phiếu nhập kho", new ChiTietPhieuNhapKho_GUI());
        themTab("Thuế", new Thue_GUI());
        themTab("Thống kê nhập kho", new ThongKeNhapKho_GUI());

        add(tabbedPane);
    }
    private void themTab(String tieuDe, Object gui) {
        Component noiDung;
        if (gui instanceof JFrame) {
            noiDung = ((JFrame) gui).getContentPane();
        } else {
            noiDung = (Component) gui;
        }
        tabbedPane.addTab(tieuDe, noiDung);
    }

    public static void main(String[] args) {
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không kết nối được CSDL: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
        SwingUtilities.invokeLater(() -> new Menu_NhapKho_Thue_GUI().setVisible(true));
    }
}
