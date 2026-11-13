package ui;

import dao.TaiKhoan_Dao;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class FrmDangNhap extends JPanel implements ActionListener {

    private Main main;
    private final TaiKhoan_Dao taiKhoanDao = new TaiKhoan_Dao();
    private final JTextField txtTenDN;
    private final JPasswordField txtMatKhau;
    private final JCheckBox chkHienMK;
    private final JButton btnDangNhap;
    private final JButton btnQuenMK;

    public FrmDangNhap(Main main) {
        this.main = main;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        // setSize(200, 100);
        // setPreferredSize(new Dimension(400, 300));
        JPanel pnlDangNhap = new JPanel(new BorderLayout());
        Box b, b1, b2;
        b = Box.createVerticalBox();
        b1 = Box.createVerticalBox();
        JPanel pnlButton = new JPanel(new GridLayout(1, 2));

        JLabel lblTitle = new JLabel("Đăng Nhập Hệ Thống");
        lblTitle.setFont((new Font("Arial", Font.BOLD, 26)));
        lblTitle.setForeground(Color.blue);
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        JPanel pnlTitle = new JPanel(new BorderLayout());
        pnlTitle.add(lblTitle, BorderLayout.CENTER);
        b.add(pnlTitle);

        JLabel lblUser = new JLabel("Tên Đăng Nhập: ");
        txtTenDN = new JTextField();
        JLabel lblPass = new JLabel("Mật Khẩu: ");
        txtMatKhau = new JPasswordField();
        chkHienMK = new JCheckBox("Hiển thị mật khẩu");
        // txtTenDN.setMaximumSize(new Dimension(500, 35));
        // txtMatKhau.setMaximumSize(new Dimension(500, 35));
        b1.add(lblUser);
        b1.add(txtTenDN);
        b1.add(lblPass);
        b1.add(txtMatKhau);
        b1.add(chkHienMK);

        btnDangNhap = new JButton("Đăng Nhập");
        btnQuenMK = new JButton("Quên Mật Khẩu");
        pnlButton.add(btnQuenMK);
        pnlButton.add(btnDangNhap);
        b.add(b1);
        b.add(pnlButton);
        pnlDangNhap.add(b, BorderLayout.CENTER);
        add(pnlDangNhap, gbc);

        btnDangNhap.addActionListener(this);
        btnQuenMK.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnDangNhap)) {
            TaiKhoan tk = kiemTraTaiKhoan();
            if (tk != null) {
                main.dangNhapThanhCong(tk);
            }

        } else if (o.equals(btnQuenMK)) {
            // TODO: xử lý quên mật khẩu
        }
    }

    public TaiKhoan kiemTraTaiKhoan() {
        String tenDN = txtTenDN.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        if (tenDN.isEmpty() || matKhau.isEmpty()) {
            return null;
        } else {
            TaiKhoan tk = taiKhoanDao.dangNhap(tenDN, matKhau);
            if (tk == null) {
                return null;
            }
            if (!"Hoạt động".equalsIgnoreCase(tk.getTrangThai().trim())) {
                // Hiển thị thông báo tài khoản bị khóa
                return null;
            }
            return tk;
        }
    }

}
