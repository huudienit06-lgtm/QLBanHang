
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import connectdb.ConnectDB;
import dao.Thue_dao;
import entity.Thue;

public class Thue_GUI extends JFrame implements ActionListener, MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtMaThue;
    private JTextField txtTenThue;
    private JTextField txtTiLeThue;
    private JTextField txtTim;

    private JButton btnTim;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnXoaTrang;

    private JTable table;
    private DefaultTableModel model;

    private Thue_dao thue_dao;

    public Thue_GUI() {

        ConnectDB.getInstance().connect();

        thue_dao = new Thue_dao();

        setTitle("Quản lý thuế");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("QUẢN LÝ THUẾ");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 25));
        lblTieuDe.setForeground(Color.BLUE);

        JPanel pnlTieuDe = new JPanel();
        pnlTieuDe.add(lblTieuDe);

        // Thông tin thuế
        JPanel pnlThongTin = new JPanel();

        pnlThongTin.add(new JLabel("Mã thuế:"));
        txtMaThue = new JTextField(15);
        pnlThongTin.add(txtMaThue);

        pnlThongTin.add(new JLabel("Tên thuế:"));
        txtTenThue = new JTextField(15);
        pnlThongTin.add(txtTenThue);

        pnlThongTin.add(new JLabel("Tỉ lệ thuế (%):"));
        txtTiLeThue = new JTextField(10);
        pnlThongTin.add(txtTiLeThue);

        // Tìm kiếm
        JPanel pnlTim = new JPanel();

        pnlTim.add(new JLabel("Mã thuế cần tìm:"));
        txtTim = new JTextField(15);
        pnlTim.add(txtTim);

        btnTim = new JButton("Tìm");
        pnlTim.add(btnTim);

        // Bảng dữ liệu
        String[] headers = {
            "Mã thuế", "Tên thuế", "Tỉ lệ thuế (%)"
        };

        model = new DefaultTableModel(headers, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        // Các nút chức năng
        JPanel pnlButton = new JPanel();

        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnXoaTrang = new JButton("Xóa trắng");

        pnlButton.add(btnThem);
        pnlButton.add(btnXoa);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoaTrang);

        // Sắp xếp giao diện
        Box box = Box.createVerticalBox();

        box.add(Box.createVerticalStrut(10));
        box.add(pnlTieuDe);
        box.add(Box.createVerticalStrut(15));
        box.add(pnlThongTin);
        box.add(Box.createVerticalStrut(10));
        box.add(pnlTim);
        box.add(Box.createVerticalStrut(10));
        box.add(scroll);
        box.add(Box.createVerticalStrut(10));
        box.add(pnlButton);

        add(box, BorderLayout.CENTER);

        // Đăng ký sự kiện
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoaTrang.addActionListener(this);
        btnTim.addActionListener(this);

        table.addMouseListener(this);

        // Đọc dữ liệu
        ReadDataFromDB();

    }

    // Đọc dữ liệu từ database lên bảng
    public void ReadDataFromDB() {

        model.setRowCount(0);

        ArrayList<Thue> dsThue = thue_dao.getAllThue();

        for (Thue thue : dsThue) {

            Object[] row = {
                thue.getMaThue(),
                thue.getTenThue(),
                thue.getTiLeThue()
            };

            model.addRow(row);
        }
    }

    // Kiểm tra dữ liệu
    public boolean validData() {

        String maThue = txtMaThue.getText().trim();
        String tenThue = txtTenThue.getText().trim();
        String tiLe = txtTiLeThue.getText().trim();

        if (maThue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã thuế không được để trống!");
            txtMaThue.requestFocus();
            return false;
        }

        if (tenThue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên thuế không được để trống!");
            txtTenThue.requestFocus();
            return false;
        }

        if (tiLe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tỉ lệ thuế không được để trống!");
            txtTiLeThue.requestFocus();
            return false;
        }

        try {

            double value = Double.parseDouble(tiLe);

            if (value < 0 || value > 100) {
                JOptionPane.showMessageDialog(this,
                    "Tỉ lệ thuế phải nằm trong khoảng từ 0 đến 100!");
                txtTiLeThue.requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                "Tỉ lệ thuế phải là số hợp lệ!");

            txtTiLeThue.requestFocus();
            return false;
        }

        return true;
    }

    // Xóa trắng các ô nhập liệu
    public void xoaTrang() {

        txtMaThue.setText("");
        txtTenThue.setText("");
        txtTiLeThue.setText("");
        txtTim.setText("");

        txtMaThue.setEditable(true);

        table.clearSelection();

        txtMaThue.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        // Thêm thuế
        if (o.equals(btnThem)) {

            if (!validData())
                return;

            String maThue = txtMaThue.getText().trim();
            String tenThue = txtTenThue.getText().trim();
            Double tiLeThue = Double.parseDouble(txtTiLeThue.getText().trim());

            if (thue_dao.themThue(new Thue(maThue, tenThue, tiLeThue))) {

                JOptionPane.showMessageDialog(this, "Thêm thuế thành công!");
                ReadDataFromDB();
                xoaTrang();

            } else {

                JOptionPane.showMessageDialog(this,
                    "Thêm thuế thất bại! Kiểm tra mã thuế có bị trùng không.");
            }
        }

        // Xóa thuế
        else if (o.equals(btnXoa)) {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thuế cần xóa!");
                return;
            }

            String maThue = model.getValueAt(row, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa thuế này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                if (thue_dao.xoaThue(maThue)) {

                    JOptionPane.showMessageDialog(this, "Xóa thuế thành công!");
                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this,
                        "Xóa thuế thất bại! Có thể thuế đang được sử dụng.");
                }
            }
        }

        // Sửa thuế
        else if (o.equals(btnSua)) {

            if (!validData())
                return;

            String maThue = txtMaThue.getText().trim();
            String tenThue = txtTenThue.getText().trim();
            Double tiLeThue = Double.parseDouble(txtTiLeThue.getText().trim());

            int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn cập nhật thuế này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {

                if (thue_dao.suaThue(new Thue(maThue, tenThue, tiLeThue))) {

                    JOptionPane.showMessageDialog(this, "Cập nhật thuế thành công!");
                    ReadDataFromDB();
                    xoaTrang();

                } else {

                    JOptionPane.showMessageDialog(this, "Cập nhật thuế thất bại!");
                }
            }
        }

        // Xóa trắng
        else if (o.equals(btnXoaTrang)) {

            xoaTrang();
        }

        // Tìm kiếm
        else if (o.equals(btnTim)) {

            String maThue = txtTim.getText().trim();

            if (maThue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã thuế cần tìm!");
                txtTim.requestFocus();
                return;
            }

            ArrayList<Thue> dsThue = thue_dao.timThue(maThue);

            model.setRowCount(0);

            for (Thue thue : dsThue) {

                Object[] row = {
                    thue.getMaThue(),
                    thue.getTenThue(),
                    thue.getTiLeThue()
                };

                model.addRow(row);
            }

            if (dsThue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy mã thuế!");
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int row = table.getSelectedRow();

        if (row != -1) {

            txtMaThue.setText(model.getValueAt(row, 0).toString());
            txtTenThue.setText(model.getValueAt(row, 1).toString());
            txtTiLeThue.setText(model.getValueAt(row, 2).toString());

            // Không cho sửa mã thuế
            txtMaThue.setEditable(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {

        new Thue_GUI().setVisible(true);
    }
}
