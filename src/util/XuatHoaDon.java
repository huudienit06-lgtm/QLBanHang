package util;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class XuatHoaDon {

    public static void xuat(Component parent,
            String maHD,
            String noiDungHTML) {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Xuất hóa đơn HTML");

        chooser.setFileFilter(
                new FileNameExtensionFilter(
                        "File HTML (*.html)", "html"
                )
        );

        chooser.setSelectedFile(
                new File("HoaDon_" + maHD + ".html")
        );

        int result = chooser.showSaveDialog(parent);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        String path = file.getAbsolutePath();

        if (!path.toLowerCase().endsWith(".html")) {
            path += ".html";
        }

        file = new File(path);

        // Kiểm tra nếu file đã tồn tại
        if (file.exists()) {
            int confirm = JOptionPane.showConfirmDialog(
                    parent,
                    "File đã tồn tại. Bạn có muốn ghi đè không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            Files.writeString(
                    file.toPath(),
                    noiDungHTML,
                    StandardCharsets.UTF_8
            );

            JOptionPane.showMessageDialog(
                    parent,
                    "Xuất hóa đơn thành công!\n" + path
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Lỗi khi xuất hóa đơn: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
