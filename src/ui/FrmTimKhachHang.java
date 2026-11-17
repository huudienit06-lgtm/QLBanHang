package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import dao.KhachHang_Dao;
import entity.KhachHang;
import java.util.ArrayList;

public class FrmTimKhachHang extends JDialog implements ActionListener, DocumentListener {

    private final JTextField txtTimTheoMa;
    private final JTextField txtTimTheoTen;
    private final DefaultTableModel modelKH;
    private final JTable tblKH;
    private final JButton btnChon;
    private final JButton btnLamMoi;
    private final JButton btnThoat;
    private KhachHang kh;
    private KhachHang_Dao khDao = new KhachHang_Dao();

    public FrmTimKhachHang(JFrame parent) {
        super(parent, "Tìm khách hàng", true);
        setLayout(new BorderLayout(5, 10));
        setSize(1000, 600);
        setLocationRelativeTo(null);
        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblTitle = new JLabel("Tìm khách hàng");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        pnlTitle.add(lblTitle);

        txtTimTheoTen = new JTextField(15);
        txtTimTheoMa = new JTextField(15);

        JPanel pnlTimKhachHang = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        pnlTimKhachHang.add(new JLabel("Tìm theo mã:"));
        pnlTimKhachHang.add(txtTimTheoMa);

        pnlTimKhachHang.add(new JLabel("Tìm theo tên:"));
        pnlTimKhachHang.add(txtTimTheoTen);

        String[] header = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Địa chỉ"};
        modelKH = new DefaultTableModel(header, 0);
        tblKH = new JTable(modelKH);
        tblKH.setRowHeight(25);

        JPanel pnlCenter = new JPanel(new BorderLayout(5, 10));
        pnlCenter.add(pnlTimKhachHang, BorderLayout.NORTH);
        pnlCenter.add(new JScrollPane(tblKH));
        hienThiModel();

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnChon = new JButton("Chọn khách hàng");
        btnLamMoi = new JButton("Làm mới");
        btnThoat = new JButton("Thoát");

        pnlButton.add(btnChon);
        pnlButton.add(btnLamMoi);
        pnlButton.add(btnThoat);

        add(pnlTitle, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);

        btnChon.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnThoat.addActionListener(this);

        txtTimTheoMa.getDocument().addDocumentListener(this);
        txtTimTheoTen.getDocument().addDocumentListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btnChon)) {
            int row = tblKH.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng");
            } else {
                kh = khDao.timkhachHang(modelKH.getValueAt(row, 0).toString());
            }
            dispose();
        }
        if (e.getSource().equals(btnLamMoi)) {
            lamMoi();
            hienThiModel();

        }
        if (e.getSource().equals(btnThoat)) {
            dispose();
        }
    }

    public KhachHang getSelectedKhachHang() {
        return kh;
    }

    public void lamMoi() {
        txtTimTheoMa.setText("");
        txtTimTheoTen.setText("");
        modelKH.setRowCount(0);
    }

    public void timTheoThuocTinh() {
        String tuKhoa = txtTimTheoMa.getText().trim().isEmpty() ? txtTimTheoTen.getText() : txtTimTheoMa.getText();
        ArrayList<KhachHang> list = khDao.timKhachHangTheoTuKhoa(tuKhoa);
        modelKH.setRowCount(0);
        for (KhachHang khachHang : list) {
            modelKH.addRow(new Object[]{khachHang.getMaKH(), khachHang.getTenKH(), khachHang.getSdt(), khachHang.getDiaChi()});
        }
    }

    public void hienThiModel() {
        modelKH.setRowCount(0);
        ArrayList<KhachHang> listAll = khDao.getAllKhachHang();
        for (KhachHang khachHang : listAll) {
            modelKH.addRow(new Object[]{khachHang.getMaKH(), khachHang.getTenKH(), khachHang.getSdt(), khachHang.getDiaChi()});
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        timTheoThuocTinh();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        timTheoThuocTinh();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        timTheoThuocTinh();
    }

}
