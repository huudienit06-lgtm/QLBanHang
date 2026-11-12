CREATE DATABASE QLBH;
GO 

USE QLBH ;
GO 

create table KhachHang (
	maKH varchar(10) primary key,
	tenKH nvarchar(50),
	sdt varchar(10),
	gmail nvarchar(50),
	diaChi nVarchar(100),
	loaiKH nvarchar(20)
);

create table NhanVien (
	maNV varchar(10) primary key,
	tenNV nvarchar(50),
	sdt varchar(10),
	gioiTinh nvarchar(5),
	chucVu nvarchar(20)
)

create table TaiKhoan(
	maNV varchar(10) primary key,
	matKhau varchar(10),
	trangThai nvarchar(20),
	constraint fk_TaiKhoan_NhanVien foreign key (maNV) references NhanVien(maNV)
)


create table KhuyenMai(
	maKM varchar(10) primary key,
	tenKM nvarchar(30),
	tiLeGiamGia float,
	loaiKM nvarchar(20),
	ngayBatDau date,
	ngayKetThuc date,
	trangThai nvarchar(20)
)


create table DanhMuc (
	maDM varchar(10) primary key,
	tenDM nvarchar(30),
	loiNhuan float
)


create table SanPham (
	maSP varchar(10) primary key ,
	tenSP nvarchar(20),
	giaNhap decimal(10,2),
	soLuongTon int ,
	maKM varchar(10),
	maDM varchar(10),
	constraint fk_SanPham_KhuyenMai foreign key (maKM) references KhuyenMai(maKM),
	constraint fk_SanPham_DanhMuc foreign key (maDM) references DanhMuc(maDM)
)



create table Thue (
	maThue varchar(10) primary key,
	tenThue nvarchar(30),
	tiLeThue float

)

create table HoaDon(
	maHD varchar(10) primary key,
	ngayLap date,
	maKH varchar(10),
	maNV varchar(10),
	maKM varchar(10),
	maThue varchar(10),
	trangThai nvarchar(20),
	phuongThucThanhToan nvarchar(20),

	constraint fk_HoaDon_KhachHang foreign key (maKH) references KhachHang(maKH),
	constraint fk_HoaDon_NhanVien foreign key (maNV) references NhanVien(maNV),
	constraint fk_HoaDon_KhuyenMai foreign key (maKM) references KhuyenMai(maKM),
	constraint fk_HoaDon_Thue foreign key (maThue) references Thue(maThue)
)

create table ChiTietHoaDon (
	maHD varchar(10),
	maSP varchar(10),
	soLuong int,

	constraint pk_ChiTietHoaDon primary key (maHD,maSP),
	constraint fk_ChiTietHoaDon_HoaDon foreign key (maHD) references HoaDon(maHD),
	constraint fk_ChiTietHoaDon_SanPham foreign key (maSP) references SanPham(maSP),
)

create table PhieuNhapKho (
	maPNK varchar(10) primary key ,
	ngayLap date ,
	maNV varchar(10),
	trangThai nvarchar(20),

	constraint fk_PhieuNhapKho_NhanVien foreign key (maNV) references NhanVien(maNV)
)

create table ChiTietPhieuNhapKho(
	maPNK varchar(10),
	maSP varchar(10),
	soLuong int,

	constraint pf_PhieuNhapKho primary key (maPNK,maSP),
	constraint fk_ChiTietPhieuNhapKHo_PhieuNhapKho foreign key (maPNK) references PhieuNhapKho(maPNK),
	constraint fk_ChiTietPhieuNhapKho_SanPham foreign key (maSP) references SanPham(maSP)
)


