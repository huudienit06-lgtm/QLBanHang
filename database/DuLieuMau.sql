
USE QLBH;
GO

-- =============================================
-- 1. KHACHHANG
-- =============================================
INSERT INTO KhachHang
    (maKH, tenKH, sdt, gmail, diaChi, loaiKH)
VALUES
('KH0001', N'Nguyễn Văn An', '0901234567', N'an@gmail.com', N'Quận 1, TP.HCM', N'Thường'),
('KH0002', N'Trần Thị Bình', '0912345678', N'binh@gmail.com', N'Quận 3, TP.HCM', N'VIP'),
('KH0003', N'Lê Hoàng Nam', '0923456789', N'nam@gmail.com', N'Thủ Đức, TP.HCM', N'Thân thiết'),
('KH0004', N'Phạm Minh Tuấn', '0934567890', N'tuan@gmail.com', N'Quận 7, TP.HCM', N'Thường'),
('KH0005', N'Hoàng Thị Mai', '0945678901', N'mai@gmail.com', N'Quận 5, TP.HCM', N'VIP'),
('KH0006', N'Đỗ Văn Hùng', '0956789012', N'hung@gmail.com', N'Bình Thạnh, TP.HCM', N'Thân thiết'),
('KH0007', N'Vũ Thị Lan', '0967890123', N'lan@gmail.com', N'Gò Vấp, TP.HCM', N'Thường'),
('KH0008', N'Ngô Quốc Việt', '0978901234', N'viet@gmail.com', N'Quận 10, TP.HCM', N'Thường');

-- =============================================
-- 2. NHANVIEN
-- =============================================
INSERT INTO NhanVien
    (maNV, tenNV, sdt, gioiTinh, chucVu)
VALUES
('NV0001', N'Nguyễn Thành Đạt', '0981234567', N'Nam', N'Quản lý'),
('NV0002', N'Trần Thị Hương', '0982345678', N'Nữ', N'Nhân viên'),
('NV0003', N'Lê Minh Khang', '0983456789', N'Nam', N'Nhân viên'),
('NV0004', N'Phạm Hoàng Long', '0984567890', N'Nam', N'Thu ngân'),
('NV0005', N'Đỗ Ngọc Ánh', '0985678901', N'Nữ', N'Nhân viên');

-- =============================================
-- 3. TAIKHOAN
-- maNV vua la PK vua la FK
-- =============================================
INSERT INTO TaiKhoan
    (maNV, matKhau, trangThai)
VALUES
('NV0001', '123456', N'Hoạt động'),
('NV0002', '123456', N'Hoạt động'),
('NV0003', '123456', N'Hoạt động'),
('NV0004', '123456', N'Hoạt động'),
('NV0005', '123456', N'Bị khóa');

-- =============================================
-- 4. KHUYENMAI
-- tiLeGiamGia tinh theo phan tram
-- =============================================
INSERT INTO KhuyenMai
    (maKM, tenKM, tiLeGiamGia, donViTinh, ngayBatDau, ngayKetThuc, trangThai)
VALUES
('KM0001', N'Khuyến mãi khai trương', 10, N'Phần trăm', '20261001', '20261231', N'Đang áp dụng'),
('KM0002', N'Ưu đãi khách hàng VIP', 15, N'Phần trăm', '20261101', '20261130', N'Đang áp dụng'),
('KM0003', N'Khuyến mãi mùa hè', 20, N'Phần trăm', '20260601', '20260831', N'Đã kết thúc'),
('KM0004', N'Ưu đãi cuối năm', 25, N'Phần trăm', '20261101', '20261231', N'Đang áp dụng'),
('KM0005', N'Giảm giá đặc biệt', 5, N'Phần trăm', '20260101', '20260331', N'Đã kết thúc');

-- =============================================
-- 5. DANHMUC
-- =============================================
INSERT INTO DanhMuc (maDM, tenDM,loiNhuan)
VALUES
('DM0001', N'Đồ uống',0.05),
('DM0002', N'Đồ ăn',0.025),
('DM0003', N'Gia dụng',0.03),
('DM0004', N'Điện tử',0.05),
('DM0005', N'Văn phòng phẩm',0.025);

-- =============================================
-- 6. SANPHAM
-- =============================================
INSERT INTO SanPham
    (maSP, tenSP, giaNhap, soLuongTon, maKM, maDM)
VALUES
('SP0001', N'Nước suối', 5000, 100, 'KM0001', 'DM0001'),
('SP0002', N'Coca Cola', 8000, 80, 'KM0001', 'DM0001'),
('SP0003', N'Pepsi', 8000, 75, 'KM0002', 'DM0001'),
('SP0004', N'Mì Hảo Hảo', 3500, 200, NULL, 'DM0002'),
('SP0005', N'Bánh Oreo', 12000, 60, 'KM0002', 'DM0002'),
('SP0006', N'Bánh Chocopie', 25000, 40, NULL, 'DM0002'),
('SP0007', N'Ấm siêu tốc', 150000, 20, 'KM0004', 'DM0003'),
('SP0008', N'Quạt mini', 90000, 30, NULL, 'DM0003'),
('SP0009', N'Tai nghe', 120000, 25, 'KM0004', 'DM0004'),
('SP0010', N'Chuột không dây', 180000, 35, 'KM0001', 'DM0004'),
('SP0011', N'Bút bi', 3000, 300, NULL, 'DM0005'),
('SP0012', N'Vở học sinh', 7000, 150, 'KM0002', 'DM0005');

-- =============================================
-- 7. THUE
-- tiLeThue tinh theo phan tram
-- =============================================
INSERT INTO Thue (maThue, tenThue, tiLeThue)
VALUES
('TH0001', N'Thuế VAT 5%', 5),

-- =============================================
-- 8. HOADON
-- =============================================
INSERT INTO HoaDon
    (maHD, ngayLap, maKH, maNV, maKM, trangThai, phuongThucThanhToan)
VALUES
('HD0001', '20261101', 'KH0001', 'NV0004', 'KM0001', N'Đã thanh toán', N'Tiền mặt'),
('HD0002', '20261102', 'KH0002', 'NV0004', 'KM0002', N'Đã thanh toán', N'Chuyển khoản'),
('HD0003', '20261103', 'KH0003', 'NV0002', NULL, N'Đã thanh toán', N'Tiền mặt'),
('HD0004', '20261104', 'KH0004', 'NV0003', 'KM0001', N'Chưa thanh toán', N'Tiền mặt'),
('HD0005', '20261105', 'KH0005', 'NV0004', NULL, N'Đã thanh toán', N'Chuyển khoản'),
('HD0006', '20261106', 'KH0006', 'NV0002', 'KM0004', N'Đã thanh toán', N'Tiền mặt'),
('HD0007', '20261107', 'KH0007', 'NV0003', NULL, N'Đã hủy', N'Tiền mặt'),
('HD0008', '20261108', 'KH0008', 'NV0004', 'KM0002', N'Chưa thanh toán', N'Chuyển khoản');

-- =============================================
-- 9. CHITIETHOADON
-- Moi cap maHD + maSP chi xuat hien mot lan
-- =============================================
INSERT INTO ChiTietHoaDon (maHD, maSP, soLuong)
VALUES
('HD0001', 'SP0001', 5),
('HD0001', 'SP0002', 3),
('HD0002', 'SP0005', 2),
('HD0002', 'SP0006', 1),
('HD0003', 'SP0004', 10),
('HD0003', 'SP0011', 5),
('HD0004', 'SP0007', 1),
('HD0004', 'SP0008', 2),
('HD0005', 'SP0009', 1),
('HD0005', 'SP0010', 1),
('HD0006', 'SP0003', 4),
('HD0006', 'SP0012', 10),
('HD0007', 'SP0001', 2),
('HD0008', 'SP0005', 3),
('HD0008', 'SP0011', 10);

-- =============================================
-- 10. PHIEUNHAPKHO
-- =============================================
INSERT INTO PhieuNhapKho
    (maPNK, ngayLap, maNV, trangThai)
VALUES
('PNK0001', '20261025', 'NV0001', N'Đã nhập'),
('PNK0002', '20261027', 'NV0002', N'Đã nhập'),
('PNK0003', '20261029', 'NV0003', N'Đã nhập'),
('PNK0004', '20261105', 'NV0001', N'Chưa nhập'),
('PNK0005', '20261108', 'NV0002', N'Đã nhập');

-- =============================================
-- 11. CHITIETPHIEUNHAPKHO
-- Moi cap maPNK + maSP chi xuat hien mot lan
-- =============================================
INSERT INTO ChiTietPhieuNhapKho
    (maPNK, maSP, soLuong)
VALUES
('PNK0001', 'SP0001', 50),
('PNK0001', 'SP0002', 30),
('PNK0002', 'SP0004', 100),
('PNK0002', 'SP0005', 40),
('PNK0003', 'SP0007', 10),
('PNK0003', 'SP0008', 15),
('PNK0004', 'SP0009', 20),
('PNK0004', 'SP0010', 25),
('PNK0005', 'SP0003', 40),
('PNK0005', 'SP0006', 30),
('PNK0005', 'SP0011', 100),
('PNK0005', 'SP0012', 60);

GO
