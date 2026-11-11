package ui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {

    JPanel pnlContent = new JPanel(new BorderLayout());
    JPanel pnlMenu = new JPanel(new BorderLayout());

    public Main() throws HeadlessException {
        setTitle("Quản Lý Bán Hàng");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pnlMenu.add(new MenuQL(), BorderLayout.NORTH);
        add(pnlMenu, BorderLayout.NORTH);
        add(pnlContent, BorderLayout.CENTER);
        setContent(new FrmDangNhap());

    }

    private void setContent(JPanel panel) {
        pnlContent.removeAll();
        pnlContent.add(panel, BorderLayout.CENTER);
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}
