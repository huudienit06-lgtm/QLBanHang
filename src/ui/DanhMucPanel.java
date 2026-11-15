package ui;

import dao.DanhMucDAO;
import entity.DanhMuc;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class DanhMucPanel extends JPanel {

    private final DanhMucDAO dao = new DanhMucDAO();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Mã danh mục", "Tên danh mục","Lợi nhuận"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    private final JTextField txtMa = new JTextField(12);
    private final JTextField txtTen = new JTextField(25);
    private final JTextField txtTim = new JTextField(20);
    private final JTextField txtLoiNhuan = new JTextField();

    public DanhMucPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlForm.add(new JLabel("Mã danh mục:"));
        pnlForm.add(txtMa);
        pnlForm.add(new JLabel("Tên danh mục:"));
        pnlForm.add(txtTen);
        pnlForm.add(new JLabel("Lợi nhuận:"));
        pnlForm.add(txtLoiNhuan);

        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnXoaTrang = new JButton("Xóa trắng");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlNut.add(btnThem);
        pnlNut.add(btnSua);
        pnlNut.add(btnXoa);
        pnlNut.add(btnXoaTrang);

        JButton btnTim = new JButton("Tìm");
        JButton btnTatCa = new JButton("Hiện tất cả");
        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTim.add(new JLabel("Từ khóa (mã/tên):"));
        pnlTim.add(txtTim);
        pnlTim.add(btnTim);
        pnlTim.add(btnTatCa);

        JPanel pnlBac = new JPanel(new GridLayout(0, 1));
        pnlBac.add(pnlForm);
        pnlBac.add(pnlNut);
        pnlBac.add(pnlTim);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        add(pnlBac, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    txtMa.setText(String.valueOf(model.getValueAt(r, 0)));
                    txtTen.setText(String.valueOf(model.getValueAt(r, 1)));
                    txtMa.setEditable(false);
                }
            }
        });

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnXoaTrang.addActionListener(e -> xoaTrang());
        btnTim.addActionListener(e -> {
            String tk = txtTim.getText().trim();
            napBang(tk.isEmpty() ? dao.getAllDanhMuc() : dao.timDanhMuc(tk));
        });
        btnTatCa.addActionListener(e -> {
            txtTim.setText("");
            lamMoi();
        });

        lamMoi();
    }

    public void lamMoi() {
        napBang(dao.getAllDanhMuc());
        xoaTrang();
    }

    private void napBang(ArrayList<DanhMuc> ds) {
        model.setRowCount(0);
        for (DanhMuc dm : ds) {
            model.addRow(new Object[] { dm.getMaDM(), dm.getTenDM() });
        }
    }

    private void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtMa.setEditable(true);
        table.clearSelection();
        txtMa.requestFocus();
    }

    private boolean kiemTra() {
        if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên danh mục!");
            return false;
        }
        if (txtMa.getText().trim().length() > 10) {
            JOptionPane.showMessageDialog(this, "Mã danh mục tối đa 10 ký tự!");
            return false;
        }
        if (txtTen.getText().trim().length() > 30) {
            JOptionPane.showMessageDialog(this, "Tên danh mục tối đa 30 ký tự!");
            return false;
        }
        return true;
    }

    private void them() {
        if (!kiemTra()) {
            return;
        }
        DanhMuc dm = new DanhMuc(txtMa.getText().trim(), txtTen.getText().trim(), Float.valueOf(txtLoiNhuan.getText()));
        if (dao.themDanhMuc(dm)) {
            JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại (mã danh mục có thể đã tồn tại)!");
        }
    }

    private void sua() {
        if (table.getSelectedRow() < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn danh mục cần sửa!");
            return;
        }
        if (!kiemTra()) {
            return;
        }
        DanhMuc dm = new DanhMuc(txtMa.getText().trim(), txtTen.getText().trim(),Float.valueOf(txtLoiNhuan.getText()));
        if (dao.suaDanhMuc(dm)) {
            JOptionPane.showMessageDialog(this, "Sửa danh mục thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
        }
    }

    private void xoa() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn danh mục cần xóa!");
            return;
        }
        String ma = String.valueOf(model.getValueAt(r, 0));
        int chon = JOptionPane.showConfirmDialog(this, "Xóa danh mục " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        if (dao.xoaDanhMuc(ma)) {
            JOptionPane.showMessageDialog(this, "Xóa danh mục thành công!");
            lamMoi();
        } else {
            JOptionPane.showMessageDialog(this, "Không xóa được (danh mục đang được sản phẩm sử dụng)!");
        }
    }
}
