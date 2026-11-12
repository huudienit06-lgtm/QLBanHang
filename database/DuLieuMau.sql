
USE QLBH;
GO

-- =============================================
-- 1. KHACHHANG
-- =============================================
INSERT INTO KhachHang
    (maKH, tenKH, sdt, gmail, diaChi, loaiKH)
VALUES
('KH001', N'Nguyễn Văn An', '0901234567', N'an@gmail.com', N'Quận 1, TP.HCM', N'Thường'),
('KH002', N'Trần Thị Bình', '0912345678', N'binh@gmail.com', N'Quận 3, TP.HCM', N'VIP'),
('KH003', N'Lê Hoàng Nam', '0923456789', N'nam@gmail.com', N'Thủ Đức, TP.HCM', N'Thân thiết'),
('KH004', N'Phạm Minh Tuấn', '0934567890', N'tuan@gmail.com', N'Quận 7, TP.HCM', N'Thường'),
('KH005', N'Hoàng Thị Mai', '0945678901', N'mai@gmail.com', N'Quận 5, TP.HCM', N'VIP'),
('KH006', N'Đỗ Văn Hùng', '0956789012', N'hung@gmail.com', N'Bình Thạnh, TP.HCM', N'Thân thiết'),
('KH007', N'Vũ Thị Lan', '0967890123', N'lan@gmail.com', N'Gò Vấp, TP.HCM', N'Thường'),
('KH008', N'Ngô Quốc Việt', '0978901234', N'viet@gmail.com', N'Quận 10, TP.HCM', N'Thường');

-- =============================================
-- 2. NHANVIEN
-- =============================================
INSERT INTO NhanVien
    (maNV, tenNV, sdt, gioiTinh, chucVu)
VALUES
('NV001', N'Nguyễn Thành Đạt', '0981234567', N'Nam', N'Quản lý'),
('NV002', N'Trần Thị Hương', '0982345678', N'Nữ', N'Nhân viên'),
('NV003', N'Lê Minh Khang', '0983456789', N'Nam', N'Nhân viên'),
('NV004', N'Phạm Hoàng Long', '0984567890', N'Nam', N'Thu ngân'),
('NV005', N'Đỗ Ngọc Ánh', '0985678901', N'Nữ', N'Nhân viên');

-- =============================================
-- 3. TAIKHOAN
-- maNV vua la PK vua la FK
-- =============================================
INSERT INTO TaiKhoan
    (maNV, matKhau, trangThai)
VALUES
('NV001', '123456', N'Hoạt động'),
('NV002', '123456', N'Hoạt động'),
('NV003', '123456', N'Hoạt động'),
('NV004', '123456', N'Hoạt động'),
('NV005', '123456', N'Bị khóa');

-- =============================================
-- 4. KHUYENMAI
-- tiLeGiamGia tinh theo phan tram
-- =============================================
INSERT INTO KhuyenMai
    (maKM, tenKM, tiLeGiamGia, donViTinh, ngayBatDau, ngayKetThuc, trangThai)
VALUES
('KM001', N'Khuyến mãi khai trương', 10, N'Phần trăm', '20261001', '20261231', N'Đang áp dụng'),
('KM002', N'Ưu đãi khách hàng VIP', 15, N'Phần trăm', '20261101', '20261130', N'Đang áp dụng'),
('KM003', N'Khuyến mãi mùa hè', 20, N'Phần trăm', '20260601', '20260831', N'Đã kết thúc'),
('KM004', N'Ưu đãi cuối năm', 25, N'Phần trăm', '20261101', '20261231', N'Đang áp dụng'),
('KM005', N'Giảm giá đặc biệt', 5, N'Phần trăm', '20260101', '20260331', N'Đã kết thúc');

-- =============================================
-- 5. DANHMUC
-- =============================================
INSERT INTO DanhMuc (maDM, tenDM, loiNhuan)
VALUES
('DM001', N'Đồ uống',0.05),
('DM002', N'Đồ ăn',0.025),
('DM003', N'Gia dụng',0.03),
('DM004', N'Điện tử',0.05),
('DM005', N'Văn phòng phẩm',0.025);

-- =============================================
-- 6. SANPHAM
-- =============================================
INSERT INTO SanPham
    (maSP, tenSP, giaNhap, soLuongTon, maKM, maDM)
VALUES
('SP001', N'Nước suối', 5000, 100, 'KM001', 'DM001'),
('SP002', N'Coca Cola', 8000, 80, 'KM001', 'DM001'),
('SP003', N'Pepsi', 8000, 75, 'KM002', 'DM001'),
('SP004', N'Mì Hảo Hảo', 3500, 200, NULL, 'DM002'),
('SP005', N'Bánh Oreo', 12000, 60, 'KM002', 'DM002'),
('SP006', N'Bánh Chocopie', 25000, 40, NULL, 'DM002'),
('SP007', N'Ấm siêu tốc', 150000, 20, 'KM004', 'DM003'),
('SP008', N'Quạt mini', 90000, 30, NULL, 'DM003'),
('SP009', N'Tai nghe', 120000, 25, 'KM004', 'DM004'),
('SP010', N'Chuột không dây', 180000, 35, 'KM001', 'DM004'),
('SP011', N'Bút bi', 3000, 300, NULL, 'DM005'),
('SP012', N'Vở học sinh', 7000, 150, 'KM002', 'DM005');

-- =============================================
-- 7. THUE
-- tiLeThue tinh theo phan tram
-- =============================================
INSERT INTO Thue (maThue, tenThue, tiLeThue)
VALUES
('TH001', N'Thuế VAT 5%', 5),
('TH002', N'Thuế VAT 8%', 8),
('TH003', N'Thuế VAT 10%', 10);

-- =============================================
-- 8. HOADON
-- =============================================
INSERT INTO HoaDon
    (maHD, ngayLap, maKH, maNV, maKM, maThue, trangThai, phuongThucThanhToan)
VALUES
('HD001', '20261101', 'KH001', 'NV004', 'KM001', 'TH002', N'Đã thanh toán', N'Tiền mặt'),
('HD002', '20261102', 'KH002', 'NV004', 'KM002', 'TH003', N'Đã thanh toán', N'Chuyển khoản'),
('HD003', '20261103', 'KH003', 'NV002', NULL, 'TH002', N'Đã thanh toán', N'Tiền mặt'),
('HD004', '20261104', 'KH004', 'NV003', 'KM001', 'TH001', N'Chưa thanh toán', N'Tiền mặt'),
('HD005', '20261105', 'KH005', 'NV004', NULL, 'TH003', N'Đã thanh toán', N'Chuyển khoản'),
('HD006', '20261106', 'KH006', 'NV002', 'KM004', 'TH002', N'Đã thanh toán', N'Tiền mặt'),
('HD007', '20261107', 'KH007', 'NV003', NULL, 'TH001', N'Đã hủy', N'Tiền mặt'),
('HD008', '20261108', 'KH008', 'NV004', 'KM002', 'TH003', N'Chưa thanh toán', N'Chuyển khoản');

-- =============================================
-- 9. CHITIETHOADON
-- Moi cap maHD + maSP chi xuat hien mot lan
-- =============================================
INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong)
VALUES
('HD001', 'SP001', 5),
('HD001', 'SP002', 3),
('HD002', 'SP005', 2),
('HD002', 'SP006', 1),
('HD003', 'SP004', 10),
('HD003', 'SP011', 5),
('HD004', 'SP007', 1),
('HD004', 'SP008', 2),
('HD005', 'SP009', 1),
('HD005', 'SP010', 1),
('HD006', 'SP003', 4),
('HD006', 'SP012', 10),
('HD007', 'SP001', 2),
('HD008', 'SP005', 3),
('HD008', 'SP011', 10);

-- =============================================
-- 10. PHIEUNHAPKHO
-- =============================================
INSERT INTO PhieuNhapKho
    (maPNK, ngayLap, maNV, trangThai)
VALUES
('PNK001', '20261025', 'NV001', N'Đã nhập'),
('PNK002', '20261027', 'NV002', N'Đã nhập'),
('PNK003', '20261029', 'NV003', N'Đã nhập'),
('PNK004', '20261105', 'NV001', N'Chưa nhập'),
('PNK005', '20261108', 'NV002', N'Đã nhập');

-- =============================================
-- 11. CHITIETPHIEUNHAPKHO
-- Moi cap maPNK + maSP chi xuat hien mot lan
-- =============================================
INSERT INTO ChiTietPhieuNhapKho
    (maPNK, maSP, soLuong)
VALUES
('PNK001', 'SP001', 50),
('PNK001', 'SP002', 30),
('PNK002', 'SP004', 100),
('PNK002', 'SP005', 40),
('PNK003', 'SP007', 10),
('PNK003', 'SP008', 15),
('PNK004', 'SP009', 20),
('PNK004', 'SP010', 25),
('PNK005', 'SP003', 40),
('PNK005', 'SP006', 30),
('PNK005', 'SP011', 100),
('PNK005', 'SP012', 60);

GO
