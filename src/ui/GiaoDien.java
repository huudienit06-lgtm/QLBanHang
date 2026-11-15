package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

/**
 * Chứa màu sắc, font chữ và các hàm tạo thành phần giao diện dùng chung
 * để các màn hình có kiểu dáng giống nhau.
 */
public class GiaoDien {

    public static final Color MAU_CHINH = new Color(30, 90, 160);
    public static final Color MAU_NEN = new Color(240, 244, 249);
    public static final Color MAU_VIEN = new Color(190, 200, 212);
    public static final Color MAU_O_KHOA = new Color(228, 233, 240);
    public static final Color MAU_DONG_CHON = new Color(200, 222, 247);

    public static final Color MAU_THEM = new Color(39, 150, 90);
    public static final Color MAU_SUA = new Color(235, 150, 20);
    public static final Color MAU_XOA = new Color(205, 60, 60);
    public static final Color MAU_PHU = new Color(100, 115, 135);

    public static final Font FONT_THUONG = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_DAM = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TIEU_DE = new Font("Segoe UI", Font.BOLD, 22);

    // Tiêu đề lớn ở đầu mỗi màn hình
    public static JLabel taoTieuDe(String noiDung) {
        JLabel lbl = new JLabel(noiDung);
        lbl.setFont(FONT_TIEU_DE);
        lbl.setForeground(MAU_CHINH);
        return lbl;
    }

    public static JLabel taoNhan(String noiDung) {
        JLabel lbl = new JLabel(noiDung);
        lbl.setFont(FONT_THUONG);
        return lbl;
    }

    public static JTextField taoOText(int soCot) {
        JTextField txt = new JTextField(soCot);
        txt.setFont(FONT_THUONG);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_VIEN),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return txt;
    }

    // Ô chỉ để xem (ví dụ ô mã tự sinh), không cho nhập
    public static void khoaOText(JTextField txt) {
        txt.setEditable(false);
        txt.setBackground(MAU_O_KHOA);
        txt.setForeground(MAU_CHINH);
        txt.setFont(FONT_DAM);
    }

    public static JComboBox<String> taoComboBox() {
        JComboBox<String> cbo = new JComboBox<String>();
        cbo.setFont(FONT_THUONG);
        cbo.setBackground(Color.WHITE);
        return cbo;
    }

    public static JButton taoNut(String ten, Color mau) {
        JButton btn = new JButton(ten);
        btn.setFont(FONT_DAM);
        btn.setBackground(mau);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 22, 9, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // Khung chứa các ô nhập, có viền và tiêu đề
    public static JPanel taoKhungForm(String tieuDe) {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(MAU_VIEN), tieuDe,
                        TitledBorder.LEFT, TitledBorder.TOP, FONT_DAM, MAU_CHINH),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return pnl;
    }

    // Thêm một thành phần vào lưới: cot, dong là vị trí; trongSo > 0 thì cột đó giãn theo chiều ngang
    public static void themVaoLuoi(JPanel pnl, Component c, int cot, int dong, double trongSo) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = cot;
        gbc.gridy = dong;
        gbc.weightx = trongSo;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        pnl.add(c, gbc);
    }

    // Định dạng bảng: tiêu đề cột màu xanh, dòng cao hơn, màu khi chọn dòng
    public static void dinhDangBang(JTable bang) {
        bang.setFont(FONT_THUONG);
        bang.setRowHeight(28);
        bang.setGridColor(new Color(225, 230, 237));
        bang.setSelectionBackground(MAU_DONG_CHON);
        bang.setSelectionForeground(Color.BLACK);
        bang.setFillsViewportHeight(true);

        JTableHeader tieuDeCot = bang.getTableHeader();
        tieuDeCot.setFont(FONT_DAM);
        tieuDeCot.setBackground(MAU_CHINH);
        tieuDeCot.setForeground(Color.WHITE);
        tieuDeCot.setPreferredSize(new Dimension(0, 34));
        tieuDeCot.setReorderingAllowed(false);
    }
}
