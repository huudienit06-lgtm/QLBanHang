package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.SanPhamDAO;
import entity.SanPham;

public class ThongKeSanPhamPanel extends JPanel {

    private static final String HET_HANG = "HẾT HÀNG";
    private static final String SAP_HET = "SẮP HẾT";
    private static final String CON_HANG = "Còn hàng";

    private static final String THEO_NGAY = "Theo ngày";
    private static final String THEO_THANG = "Theo tháng";
    private static final String THEO_NAM = "Theo năm";

    private final SanPhamDAO dao = new SanPhamDAO();

    private final DefaultTableModel modelTon = new DefaultTableModel(
            new String[] { "Mã SP", "Tên sản phẩm", "Danh mục", "Số lượng tồn", "Cảnh báo" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel modelBanChay = new DefaultTableModel(
            new String[] { "Hạng", "Mã SP", "Tên sản phẩm", "Số lượng đã bán" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblTon = new JTable(modelTon);
    private final JTable tblBanChay = new JTable(modelBanChay);

    private final JSpinner spNguong = new JSpinner(new SpinnerNumberModel(10, 0, 100000, 1));
    private final JSpinner spTop = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));
    private final JComboBox<String> cboKy = new JComboBox<>(new String[] { THEO_NGAY, THEO_THANG, THEO_NAM });
    private final JTextField txtKy = new JTextField(10);
    private final JLabel lblGoiY = new JLabel();
    private final JLabel lblTongKet = new JLabel(" ");
    private final BieuDoCot bieuDo = new BieuDoCot();

    public ThongKeSanPhamPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tblTon.setRowHeight(24);
        tblBanChay.setRowHeight(24);
        tblTon.setDefaultRenderer(Object.class, new CanhBaoRenderer());

        // ----- Tồn kho và cảnh báo -----
        JButton btnLamMoi = new JButton("Cập nhật tồn kho");
        btnLamMoi.addActionListener(e -> napTonKho());

        JPanel pnlTon = new JPanel(new BorderLayout(5, 5));
        JPanel pnlTonBac = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlTonBac.add(new JLabel("Tồn kho hiện tại. Cảnh báo sắp hết khi tồn kho ≤"));
        pnlTonBac.add(spNguong);
        pnlTonBac.add(btnLamMoi);
        pnlTonBac.add(lblTongKet);
        pnlTon.add(pnlTonBac, BorderLayout.NORTH);
        pnlTon.add(new JScrollPane(tblTon), BorderLayout.CENTER);

        // ----- Sản phẩm bán chạy theo ngày / tháng / năm -----
        JButton btnXem = new JButton("Xem thống kê");
        btnXem.addActionListener(e -> napBanChay());
        cboKy.addActionListener(e -> datGoiYKy());

        JPanel pnlBanChayBac = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlBanChayBac.add(new JLabel("Sản phẩm bán chạy:"));
        pnlBanChayBac.add(cboKy);
        pnlBanChayBac.add(txtKy);
        pnlBanChayBac.add(lblGoiY);
        pnlBanChayBac.add(new JLabel("Top:"));
        pnlBanChayBac.add(spTop);
        pnlBanChayBac.add(btnXem);

        JSplitPane splitBan = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tblBanChay), bieuDo);
        splitBan.setResizeWeight(0.45);

        JPanel pnlBanChay = new JPanel(new BorderLayout(5, 5));
        pnlBanChay.add(pnlBanChayBac, BorderLayout.NORTH);
        pnlBanChay.add(splitBan, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlTon, pnlBanChay);
        split.setResizeWeight(0.45);
        add(split, BorderLayout.CENTER);

        spNguong.addChangeListener(e -> napTonKho());

        datGoiYKy();
        lamMoi();
    }

    public void lamMoi() {
        napTonKho();
        napBanChay();
    }

    private void datGoiYKy() {
        LocalDate homNay = LocalDate.now();
        String ky = (String) cboKy.getSelectedItem();
        if (THEO_NGAY.equals(ky)) {
            txtKy.setText(homNay.toString());
            lblGoiY.setText("(yyyy-MM-dd)");
        } else if (THEO_THANG.equals(ky)) {
            txtKy.setText(YearMonth.from(homNay).toString());
            lblGoiY.setText("(yyyy-MM)");
        } else {
            txtKy.setText(String.valueOf(homNay.getYear()));
            lblGoiY.setText("(yyyy)");
        }
    }

    private void napTonKho() {
        int nguong = (Integer) spNguong.getValue();
        ArrayList<SanPham> ds = dao.getAllSanPham();
        ds.sort(Comparator.comparingInt(sp -> sp.getSoLuongTon() == null ? 0 : sp.getSoLuongTon()));

        modelTon.setRowCount(0);
        int soHetHang = 0;
        int soSapHet = 0;
        for (SanPham sp : ds) {
            int ton = sp.getSoLuongTon() == null ? 0 : sp.getSoLuongTon();
            String canhBao;
            if (ton == 0) {
                canhBao = HET_HANG;
                soHetHang++;
            } else if (ton <= nguong) {
                canhBao = SAP_HET;
                soSapHet++;
            } else {
                canhBao = CON_HANG;
            }
            modelTon.addRow(new Object[] {
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getDanhMuc() == null ? "" : sp.getDanhMuc().getTenDM(),
                    ton,
                    canhBao });
        }
        lblTongKet.setText("Hết hàng: " + soHetHang + "  |  Sắp hết: " + soSapHet + "  |  Tổng: " + ds.size());
    }

    private void napBanChay() {
        LocalDate tuNgay;
        LocalDate denNgay;
        String ky = (String) cboKy.getSelectedItem();
        String giaTri = txtKy.getText().trim();
        try {
            if (THEO_NGAY.equals(ky)) {
                tuNgay = LocalDate.parse(giaTri);
                denNgay = tuNgay;
            } else if (THEO_THANG.equals(ky)) {
                YearMonth ym = YearMonth.parse(giaTri);
                tuNgay = ym.atDay(1);
                denNgay = ym.atEndOfMonth();
            } else {
                int nam = Integer.parseInt(giaTri);
                tuNgay = LocalDate.of(nam, 1, 1);
                denNgay = LocalDate.of(nam, 12, 31);
            }
        } catch (DateTimeParseException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá trị kỳ thống kê không đúng định dạng " + lblGoiY.getText() + "!");
            return;
        }

        int top = (Integer) spTop.getValue();
        modelBanChay.setRowCount(0);
        ArrayList<String> nhan = new ArrayList<>();
        ArrayList<Integer> giaTriCot = new ArrayList<>();
        int hang = 1;
        for (Object[] dong : dao.getSanPhamBanChay(top, tuNgay, denNgay)) {
            modelBanChay.addRow(new Object[] { hang++, dong[0], dong[1], dong[2] });
            nhan.add(String.valueOf(dong[1]));
            giaTriCot.add((Integer) dong[2]);
        }
        bieuDo.setDuLieu(nhan, giaTriCot);
    }

    /** Tô màu dòng theo mức cảnh báo tồn kho. */
    private static class CanhBaoRenderer extends DefaultTableCellRenderer {
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

    /** Biểu đồ cột số lượng bán của các sản phẩm đứng đầu. */
    private static class BieuDoCot extends JPanel {
        private ArrayList<String> nhan = new ArrayList<>();
        private ArrayList<Integer> giaTri = new ArrayList<>();

        BieuDoCot() {
            setBackground(Color.WHITE);
        }

        void setDuLieu(ArrayList<String> nhan, ArrayList<Integer> giaTri) {
            this.nhan = nhan;
            this.giaTri = giaTri;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g2.getFontMetrics();

            int w = getWidth();
            int h = getHeight();
            if (giaTri.isEmpty()) {
                String tb = "Không có dữ liệu bán hàng trong kỳ này";
                g2.setColor(Color.GRAY);
                g2.drawString(tb, (w - fm.stringWidth(tb)) / 2, h / 2);
                g2.dispose();
                return;
            }

            int le = 40;
            int day = h - 45;
            int dinh = 30;
            int max = 1;
            for (int v : giaTri) {
                max = Math.max(max, v);
            }

            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(le, dinh, le, day);
            g2.drawLine(le, day, w - 15, day);
            g2.drawString("Số lượng bán", le, dinh - 10);

            int n = giaTri.size();
            int vung = (w - 15 - le) / n;
            int rongCot = Math.max(10, vung * 2 / 3);
            for (int i = 0; i < n; i++) {
                int cao = (int) ((double) giaTri.get(i) / max * (day - dinh - 15));
                int x = le + i * vung + (vung - rongCot) / 2;
                int y = day - cao;
                g2.setColor(new Color(66, 133, 244));
                g2.fillRect(x, y, rongCot, cao);
                g2.setColor(Color.BLACK);
                String sl = String.valueOf(giaTri.get(i));
                g2.drawString(sl, x + (rongCot - fm.stringWidth(sl)) / 2, y - 4);

                String ten = nhan.get(i);
                while (ten.length() > 1 && fm.stringWidth(ten) > vung - 4) {
                    ten = ten.substring(0, ten.length() - 1);
                }
                g2.drawString(ten, le + i * vung + (vung - fm.stringWidth(ten)) / 2, day + 18);
            }
            g2.dispose();
        }
    }
}
