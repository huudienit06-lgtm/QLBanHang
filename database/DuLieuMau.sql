-- =============================================
--  THEM DU LIEU KHACH HANG
-- =============================================
INSERT INTO KhachHang
    (maKH, tenKH, sdt, gmail, diaChi, loaiKH)
VALUES
('KH001', N'Nguyễn Văn An', '0901234567', 'an@gmail.com', N'Quận 1, TP.HCM', N'Thường'),
('KH002', N'Trần Thị Bình', '0912345678', 'binh@gmail.com', N'Quận 3, TP.HCM', N'Thân thiết'),
('KH003', N'Lê Văn Cường', '0923456789', 'cuong@gmail.com', N'Thủ Đức, TP.HCM', N'Thường'),
('KH004', N'Phạm Thị Dung', '0934567890', 'dung@gmail.com', N'Quận 7, TP.HCM', N'VIP'),
('KH005', N'Hoàng Văn Em', '0945678901', 'em@gmail.com', N'Gò Vấp, TP.HCM', N'Thường');

-- =============================================
--  THEM DU LIEU NHAN VIEN
-- =============================================

INSERT INTO NhanVien
    (maNV, tenNV, sdt, chucVu)
VALUES
('NV001', N'Nguyễn Hoàng Nam', '0901111111', N'Quản lý'),
('NV002', N'Trần Minh Tuấn', '0902222222', N'Nhân viên'),
('NV003', N'Lê Thị Hồng', '0903333333', N'Thu ngân'),
('NV004', N'Phạm Văn Bình', '0904444444', N'Nhân viên');

-- =============================================
--  THEM DU LIEU TAI KHOAN
-- =============================================

INSERT INTO TaiKhoan (maNV, matKhau)
VALUES
('NV001', 'admin123'),
('NV002', 'nv002123'),
('NV003', 'nv003123'),
('NV004', 'nv004123');

-- =============================================
--  THEM DU LIEU KHUYEN MAI
-- =============================================

INSERT INTO KhuyenMai
    (maKM, tenKM, tiLeGiamGia, loaiKM,
     ngayBatDau, ngayKetThuc, trangThai)
VALUES
('KM001', N'Giảm giá mùa hè', 10, N'Phần trăm', '2026-01-01', '2026-12-31', N'Đang áp dụng'),
('KM002', N'Khuyến mãi thành viên', 15, N'Phần trăm', '2026-01-01', '2026-12-31', N'Đang áp dụng'),
('KM003', N'Khuyến mãi cuối năm', 20, N'Phần trăm', '2026-11-01', '2026-12-31', N'Đang áp dụng');

-- =============================================
--  THEM DU LIEU DANH MUC
-- =============================================

INSERT INTO DanhMuc (maDM, tenDM)
VALUES
('DM001', N'Đồ uống'),
('DM002', N'Thực phẩm'),
('DM003', N'Đồ gia dụng'),
('DM004', N'Văn phòng phẩm');

-- =============================================
--  THEM DU LIEU SAN PHAM
-- =============================================

INSERT INTO SanPham
    (maSP, tenSP, giaNhap, soLuongTon, maKM, maDM)
VALUES
('SP001', N'Coca Cola', 8000, 100, 'KM001', 'DM001'),
('SP002', N'Pepsi', 7500, 120, 'KM001', 'DM001'),
('SP003', N'Sữa tươi', 9000, 80, 'KM002', 'DM001'),
('SP004', N'Mì Hảo Hảo', 3500, 200, NULL, 'DM002'),
('SP005', N'Bánh Oreo', 12000, 90, 'KM003', 'DM002'),
('SP006', N'Nồi cơm điện', 350000, 20, NULL, 'DM003'),
('SP007', N'Bút bi Thiên Long', 4000, 150, NULL, 'DM004'),
('SP008', N'Vở học sinh', 8000, 100, 'KM002', 'DM004');

-- =============================================
--  THEM DU LIEU THUE
-- =============================================

INSERT INTO Thue (maThue, tenThue, tiLeThue)
VALUES
('TH001', N'Thuế VAT 5%', 5),
('TH002', N'Thuế VAT 10%', 10);

-- =============================================
--  THEM DU LIEU HOA DON
-- =============================================

INSERT INTO HoaDon
    (maHD, ngayLap, maNV, maKH, maKM,
     maThue, trangThai, phuongThucThanhToan)
VALUES
('HD001', '2026-11-01', 'NV001', 'KH001', NULL, 'TH002', N'Đã thanh toán', N'Tiền mặt'),
('HD002', '2026-11-02', 'NV002', 'KH002', 'KM001', 'TH002', N'Đã thanh toán', N'Chuyển khoản'),
('HD003', '2026-11-03', 'NV003', 'KH003', NULL, 'TH001', N'Đã thanh toán', N'Tiền mặt'),
('HD004', '2026-11-04', 'NV002', 'KH004', 'KM002', 'TH002', N'Chưa thanh toán', N'Tiền mặt'),
('HD005', '2026-11-05', 'NV004', 'KH005', 'KM003', 'TH001', N'Đã thanh toán', N'Chuyển khoản');

-- =============================================
-- THEM DU LIEU CHI TIET HOA DON
-- =============================================

INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong)
VALUES
('HD001', 'SP001', 2),
('HD001', 'SP004', 5),
('HD002', 'SP002', 3),
('HD002', 'SP005', 2),
('HD003', 'SP003', 4),
('HD003', 'SP007', 10),
('HD004', 'SP006', 1),
('HD004', 'SP008', 5),
('HD005', 'SP001', 5),
('HD005', 'SP005', 3);

-- =============================================
--  THEM DU LIEU PHIEU NHAP KHO
-- =============================================

INSERT INTO PhieuNhapKho
    (maPNK, ngayLap, maNV, trangThai)
VALUES
('PNK001', '2026-10-25', 'NV001', N'Đã nhập'),
('PNK002', '2026-10-28', 'NV002', N'Đã nhập'),
('PNK003', '2026-11-05', 'NV004', N'Chờ nhập');

-- =============================================
--  THEM DU LIEU CHI TIET PHIEU NHAP KHO
-- =============================================

INSERT INTO ChiTietPhieuNhapKho
    (maPNK, maSP, soLuong)
VALUES
('PNK001', 'SP001', 50),
('PNK001', 'SP002', 60),
('PNK001', 'SP003', 40),
('PNK002', 'SP004', 100),
('PNK002', 'SP005', 50),
('PNK003', 'SP006', 10),
('PNK003', 'SP007', 100),
('PNK003', 'SP008', 80);