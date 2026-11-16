package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FrmThanhToan extends JDialog implements ActionListener, DocumentListener {

    private JLabel lblTitle;
    private double tongTien;
    private JPanel pnlForm;
    private JTextField txtTienNhan;
    private JTextField txtTongTien;
    private JTextField txtTienThua;
    private final JPanel pnlButton;
    private final JButton btnHuy;
    private JButton btnXacNhan;
    private boolean ketQua;

    public FrmThanhToan(JFrame parent, String pThucThanhToan, double tongtienCanThanhToan) {
        super(parent, "Thanh Toán", true);
        tongTien = tongtienCanThanhToan;
        setSize(500, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setTitle("Thanh Toán");
        if (pThucThanhToan.equals("Chuyển khoản")) {
            lblTitle = new JLabel("THANH TOÁN CHUYỂN KHOẢN");
            ImageIcon icon = new ImageIcon("D:\\HuongSuKien_java\\BTL_Java\\img\\prthanhtoan.jpg");
            Image pr = icon.getImage();
            Image scaledImg = pr.getScaledInstance(
                    300,
                    300,
                    Image.SCALE_SMOOTH
            );
            JLabel lblPR = new JLabel(new ImageIcon(scaledImg));
            pnlForm = new JPanel(new BorderLayout());
            pnlForm.add(lblPR);
        } else {
            lblTitle = new JLabel("THANH TOÁN BẰNG TIỀN MẶT");
            pnlForm = new JPanel(new GridLayout(6, 1, 10, 10));
            pnlForm.add(new JLabel("Tiền nhận từ khách hàng:"));
            txtTienNhan = new JTextField();
            pnlForm.add(txtTienNhan);

            pnlForm.add(new JLabel("Tổng tiền cần thanh toán:"));
            txtTongTien = new JTextField();
            txtTongTien.setEditable(false);
            txtTongTien.setText(tongTien + "");
            pnlForm.add(txtTongTien);

            pnlForm.add(new JLabel("Tiền thừa:"));
            txtTienThua = new JTextField();
            txtTienThua.setEditable(false);
            pnlForm.add(txtTienThua);

        }

        lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlTitle.add(lblTitle);

        pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnHuy = new JButton("Hủy");
        btnXacNhan = new JButton("Xác nhận thanh toán");
        pnlButton.add(btnHuy);
        pnlButton.add(btnXacNhan);

        add(pnlTitle, BorderLayout.NORTH);

        add(pnlForm, BorderLayout.CENTER);

        add(pnlButton, BorderLayout.SOUTH);

        btnHuy.addActionListener(this);
        btnXacNhan.addActionListener(this);
        if (txtTienNhan != null) {
            txtTienNhan.getDocument().addDocumentListener(this);
        }
    }

    private void setTienThua() {
        double tienNhan = 0;
        try {
            tienNhan = Double.parseDouble(txtTienNhan.getText());
            if (tienNhan < tongTien) {
                txtTienThua.setText("Tiền nhận chưa đủ");
                txtTienThua.setForeground(Color.RED);
            } else {
                txtTienThua.setText((tienNhan - tongTien) + "");
                txtTienThua.setForeground(Color.black);
            }

        } catch (NumberFormatException e) {
            txtTienThua.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnHuy)) {
            ketQua = false;
            dispose();
        }
        if (e.getSource().equals(btnXacNhan)) {
            if (JOptionPane.showConfirmDialog(this, "Xác nhận thanh toán", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ketQua = true;
                dispose();
            }
        }
    }

    public boolean traKetQua() {
        return ketQua;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        setTienThua();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setTienThua();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setTienThua();
    }
}
