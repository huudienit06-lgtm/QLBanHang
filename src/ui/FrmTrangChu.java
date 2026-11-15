package ui;

import entity.NhanVien;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class FrmTrangChu extends JPanel {

    FrmTrangChu(NhanVien nhanVien, String tenTrang, String tenAnh) {
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 20, 20, 20));

        String ten = nhanVien != null && nhanVien.getTenNV() != null
                ? nhanVien.getTenNV()
                : "Nhân viên";
        JLabel lblChaoMung = new JLabel("Chào mừng " + ten + " - " + tenTrang);
        lblChaoMung.setHorizontalAlignment(SwingConstants.CENTER);
        lblChaoMung.setFont(new Font("Arial",Font.BOLD,25));
        add(lblChaoMung, BorderLayout.NORTH);

        File tepAnh = new File("img", tenAnh);
        try {
            BufferedImage anh = ImageIO.read(tepAnh);
            if (anh == null) {
                throw new IOException("Tệp không phải ảnh hợp lệ.");
            }
            add(new KhungHinhAnh(anh), BorderLayout.CENTER);
        } catch (IOException e) {
            throw new IllegalStateException("Không thể tải hình ảnh " + tepAnh.getPath(), e);
        }
    }

    private static class KhungHinhAnh extends JPanel {
        private final BufferedImage anh;

        KhungHinhAnh(BufferedImage anh) {
            this.anh = anh;
            setOpaque(false);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1100, 620);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (getWidth() <= 0 || getHeight() <= 0) {
                return;
            }

            double tiLe = Math.min(
                    (double) getWidth() / anh.getWidth(),
                    (double) getHeight() / anh.getHeight());
            int rong = (int) (anh.getWidth() * tiLe);
            int cao = (int) (anh.getHeight() * tiLe);
            int x = (getWidth() - rong) / 2;
            int y = (getHeight() - cao) / 2;

            Graphics2D g2d = (Graphics2D) graphics.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(anh, x, y, rong, cao, this);
            g2d.dispose();
        }
    }
}
