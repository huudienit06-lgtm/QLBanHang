package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.SanPhamDAO;
import entity.SanPham;

public class ThongKeSanPhamPanel extends JPanel implements ActionListener, ChangeListener {

    private static final String HET_HANG = "HẾT HÀNG";
    private static final String SAP_HET = "SẮP HẾT";
    private static final String CON_HANG = "Còn hàng";

    private SanPhamDAO sanPhamDAO;

    // Phần tồn kho
    private DefaultTableModel modelTonKho;
    private JTable tblTonKho;
    private JSpinner spnNguongSapHet;
    private JLabel lblTongKet;
    private JButton btnCapNhatTon;

    // Phần sản phẩm bán chạy
    private DefaultTableModel modelBanChay;
    private JTable tblBanChay;
    private JComboBox<String> cboKyThongKe;
    private JTextField txtGiaTriKy;
    private JLabel lblDinhDang;
    private JSpinner spnTop;
    private JButton btnXemThongKe;
    private BieuDoCot bieuDo;

    public ThongKeSanPhamPanel() {
        sanPhamDAO = new SanPhamDAO();

        setLayout(new BorderLayout(0, 12));
        setBackground(GiaoDien.MAU_NEN);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        add(GiaoDien.taoTieuDe("THỐNG KÊ SẢN PHẨM"), BorderLayout.NORTH);

        // ----- Khung 1: tồn kho và cảnh báo -----
        JPanel pnlTonKho = new JPanel(new BorderLayout(0, 8));
        pnlTonKho.setOpaque(false);

        JPanel pnlDieuKhienTon = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlDieuKhienTon.setOpaque(false);
        spnNguongSapHet = new JSpinner(new SpinnerNumberModel(10, 0, 100000, 1));
        btnCapNhatTon = GiaoDien.taoNut("Cập nhật", GiaoDien.MAU_CHINH);
        lblTongKet = GiaoDien.taoNhan(" ");
        lblTongKet.setFont(GiaoDien.FONT_DAM);
        lblTongKet.setForeground(GiaoDien.MAU_XOA);
        pnlDieuKhienTon.add(GiaoDien.taoNhan("Tồn kho hiện tại - cảnh báo sắp hết khi số lượng ≤"));
        pnlDieuKhienTon.add(spnNguongSapHet);
        pnlDieuKhienTon.add(btnCapNhatTon);
        pnlDieuKhienTon.add(lblTongKet);
        pnlTonKho.add(pnlDieuKhienTon, BorderLayout.NORTH);

        String[] cotTonKho = { "Mã SP", "Tên sản phẩm", "Danh mục", "Số lượng tồn", "Cảnh báo" };
        modelTonKho = new DefaultTableModel(cotTonKho, 0);
        tblTonKho = new JTable(modelTonKho);
        tblTonKho.setDefaultEditor(Object.class, null);
        GiaoDien.dinhDangBang(tblTonKho);
        tblTonKho.setDefaultRenderer(Object.class, new CanhBaoRenderer());
        JScrollPane scrollTon = new JScrollPane(tblTonKho);
        scrollTon.getViewport().setBackground(Color.WHITE);
        pnlTonKho.add(scrollTon, BorderLayout.CENTER);

        // ----- Khung 2: sản phẩm bán chạy -----
        JPanel pnlBanChay = new JPanel(new BorderLayout(0, 8));
        pnlBanChay.setOpaque(false);

        JPanel pnlDieuKhienBan = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlDieuKhienBan.setOpaque(false);
        cboKyThongKe = GiaoDien.taoComboBox();
        cboKyThongKe.addItem("Theo ngày");
        cboKyThongKe.addItem("Theo tháng");
        cboKyThongKe.addItem("Theo năm");
        txtGiaTriKy = GiaoDien.taoOText(10);
        lblDinhDang = GiaoDien.taoNhan(" ");
        spnTop = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
        btnXemThongKe = GiaoDien.taoNut("Xem thống kê", GiaoDien.MAU_THEM);
        pnlDieuKhienBan.add(GiaoDien.taoNhan("Sản phẩm bán chạy:"));
        pnlDieuKhienBan.add(cboKyThongKe);
        pnlDieuKhienBan.add(txtGiaTriKy);
        pnlDieuKhienBan.add(lblDinhDang);
        pnlDieuKhienBan.add(GiaoDien.taoNhan("Top:"));
        pnlDieuKhienBan.add(spnTop);
        pnlDieuKhienBan.add(btnXemThongKe);
        pnlBanChay.add(pnlDieuKhienBan, BorderLayout.NORTH);

        String[] cotBanChay = { "Hạng", "Mã SP", "Tên sản phẩm", "Số lượng đã bán" };
        modelBanChay = new DefaultTableModel(cotBanChay, 0);
        tblBanChay = new JTable(modelBanChay);
        tblBanChay.setDefaultEditor(Object.class, null);
        GiaoDien.dinhDangBang(tblBanChay);
        JScrollPane scrollBan = new JScrollPane(tblBanChay);
        scrollBan.getViewport().setBackground(Color.WHITE);

        bieuDo = new BieuDoCot();
        JSplitPane splitBanChay = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollBan, bieuDo);
        splitBanChay.setResizeWeight(0.45);
        pnlBanChay.add(splitBanChay, BorderLayout.CENTER);

        // Ghép hai khung lại
        JSplitPane splitChinh = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTonKho, pnlBanChay);
        splitChinh.setResizeWeight(0.45);
        splitChinh.setBorder(null);
        splitChinh.setOpaque(false);
        add(splitChinh, BorderLayout.CENTER);

        // ----- Gắn sự kiện -----
        btnCapNhatTon.addActionListener(this);
        btnXemThongKe.addActionListener(this);
        cboKyThongKe.addActionListener(this);
        spnNguongSapHet.addChangeListener(this);

        datGoiYKy();
        lamMoi();
    }

    public void lamMoi() {
        hienThiTonKho();
        hienThiBanChay();
    }

    // Điền sẵn giá trị mặc định (hôm nay) và gợi ý định dạng theo kỳ đang chọn
    private void datGoiYKy() {
        LocalDate homNay = LocalDate.now();
        int viTri = cboKyThongKe.getSelectedIndex();
        if (viTri == 0) {
            txtGiaTriKy.setText(homNay.toString());
            lblDinhDang.setText("(yyyy-MM-dd)");
        } else if (viTri == 1) {
            txtGiaTriKy.setText(YearMonth.from(homNay).toString());
            lblDinhDang.setText("(yyyy-MM)");
        } else {
            txtGiaTriKy.setText(String.valueOf(homNay.getYear()));
            lblDinhDang.setText("(yyyy)");
        }
    }

    private void hienThiTonKho() {
        int nguong = Integer.parseInt(spnNguongSapHet.getValue().toString());
        ArrayList<SanPham> dsSP = sanPhamDAO.getAllSanPhamTheoTonKho();

        modelTonKho.setRowCount(0);
        int soHetHang = 0;
        int soSapHet = 0;
        for (int i = 0; i < dsSP.size(); i++) {
            SanPham sp = dsSP.get(i);
            int ton = sp.getSoLuongTon();

            String canhBao = CON_HANG;
            if (ton == 0) {
                canhBao = HET_HANG;
                soHetHang++;
            } else if (ton <= nguong) {
                canhBao = SAP_HET;
                soSapHet++;
            }

            String tenDM = "";
            if (sp.getDanhMuc() != null) {
                tenDM = sp.getDanhMuc().getTenDM();
            }
            Object[] dong = { sp.getMaSP(), sp.getTenSP(), tenDM, ton, canhBao };
            modelTonKho.addRow(dong);
        }
        lblTongKet.setText("Hết hàng: " + soHetHang + "   |   Sắp hết: " + soSapHet + "   |   Tổng: " + dsSP.size());
    }

    private void hienThiBanChay() {
        // Đổi giá trị người dùng nhập thành khoảng ngày cần thống kê
        LocalDate tuNgay = null;
        LocalDate denNgay = null;
        int viTri = cboKyThongKe.getSelectedIndex();
        String giaTri = txtGiaTriKy.getText().trim();
        try {
            if (viTri == 0) {
                tuNgay = LocalDate.parse(giaTri);
                denNgay = tuNgay;
            } else if (viTri == 1) {
                YearMonth thang = YearMonth.parse(giaTri);
                tuNgay = thang.atDay(1);
                denNgay = thang.atEndOfMonth();
            } else {
                int nam = Integer.parseInt(giaTri);
                tuNgay = LocalDate.of(nam, 1, 1);
                denNgay = LocalDate.of(nam, 12, 31);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Giá trị không đúng định dạng " + lblDinhDang.getText() + "!");
            return;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá trị không đúng định dạng " + lblDinhDang.getText() + "!");
            return;
        }

        int top = Integer.parseInt(spnTop.getValue().toString());
        ArrayList<Object[]> dsBanChay = sanPhamDAO.getSanPhamBanChay(top, tuNgay, denNgay);

        modelBanChay.setRowCount(0);
        ArrayList<String> dsTen = new ArrayList<String>();
        ArrayList<Integer> dsSoLuong = new ArrayList<Integer>();
        for (int i = 0; i < dsBanChay.size(); i++) {
            Object[] dong = dsBanChay.get(i);
            Object[] dongBang = { i + 1, dong[0], dong[1], dong[2] };
            modelBanChay.addRow(dongBang);
            dsTen.add(dong[1].toString());
            dsSoLuong.add(Integer.parseInt(dong[2].toString()));
        }
        bieuDo.setDuLieu(dsTen, dsSoLuong);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon.equals(btnCapNhatTon)) {
            hienThiTonKho();
        } else if (nguon.equals(btnXemThongKe)) {
            hienThiBanChay();
        } else if (nguon.equals(cboKyThongKe)) {
            datGoiYKy();
        }
    }

    // Đổi ngưỡng cảnh báo thì tính lại ngay
    @Override
    public void stateChanged(ChangeEvent e) {
        hienThiTonKho();
    }

    // Tô màu dòng trong bảng tồn kho theo mức cảnh báo
    private class CanhBaoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                Object canhBao = table.getValueAt(row, 4);
                if (HET_HANG.equals(canhBao)) {
                    c.setBackground(new Color(255, 205, 210));
                } else if (SAP_HET.equals(canhBao)) {
                    c.setBackground(new Color(255, 236, 179));
                } else {
                    c.setBackground(Color.WHITE);
                }
                c.setForeground(Color.BLACK);
            }
            return c;
        }
    }

    // Biểu đồ cột: mỗi cột là một sản phẩm, chiều cao theo số lượng bán
    private class BieuDoCot extends JPanel {
        private ArrayList<String> dsTen = new ArrayList<String>();
        private ArrayList<Integer> dsSoLuong = new ArrayList<Integer>();

        public BieuDoCot() {
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(GiaoDien.MAU_VIEN));
        }

        public void setDuLieu(ArrayList<String> dsTen, ArrayList<Integer> dsSoLuong) {
            this.dsTen = dsTen;
            this.dsSoLuong = dsSoLuong;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(GiaoDien.FONT_THUONG);
            FontMetrics fm = g2.getFontMetrics();

            int rong = getWidth();
            int cao = getHeight();

            if (dsSoLuong.size() == 0) {
                String thongBao = "Không có dữ liệu bán hàng trong kỳ này";
                g2.setColor(Color.GRAY);
                g2.drawString(thongBao, (rong - fm.stringWidth(thongBao)) / 2, cao / 2);
                return;
            }

            int leTrai = 45;
            int leDuoi = cao - 45;
            int leTren = 35;

            // Tìm số lượng lớn nhất để quy đổi chiều cao cột
            int lonNhat = 1;
            for (int i = 0; i < dsSoLuong.size(); i++) {
                if (dsSoLuong.get(i) > lonNhat) {
                    lonNhat = dsSoLuong.get(i);
                }
            }

            // Vẽ hai trục
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(leTrai, leTren, leTrai, leDuoi);
            g2.drawLine(leTrai, leDuoi, rong - 15, leDuoi);
            g2.drawString("Số lượng bán", leTrai, leTren - 12);

            int soCot = dsSoLuong.size();
            int beRongMoiCot = (rong - 15 - leTrai) / soCot;
            int beRongCot = beRongMoiCot * 2 / 3;
            if (beRongCot < 10) {
                beRongCot = 10;
            }

            for (int i = 0; i < soCot; i++) {
                int chieuCaoCot = dsSoLuong.get(i) * (leDuoi - leTren - 20) / lonNhat;
                int x = leTrai + i * beRongMoiCot + (beRongMoiCot - beRongCot) / 2;
                int y = leDuoi - chieuCaoCot;

                g2.setColor(GiaoDien.MAU_CHINH);
                g2.fillRect(x, y, beRongCot, chieuCaoCot);

                // Số lượng ghi phía trên cột
                g2.setColor(Color.BLACK);
                String soLuong = dsSoLuong.get(i).toString();
                g2.drawString(soLuong, x + (beRongCot - fm.stringWidth(soLuong)) / 2, y - 5);

                // Tên sản phẩm ghi phía dưới, cắt bớt nếu dài quá bề rộng
                String ten = dsTen.get(i);
                while (ten.length() > 1 && fm.stringWidth(ten) > beRongMoiCot - 4) {
                    ten = ten.substring(0, ten.length() - 1);
                }
                int xTen = leTrai + i * beRongMoiCot + (beRongMoiCot - fm.stringWidth(ten)) / 2;
                g2.drawString(ten, xTen, leDuoi + 20);
            }
        }
    }
}
