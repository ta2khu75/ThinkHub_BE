package com.ta2khu75.thinkhub.report.entity;

public enum ReportStatus {
	PENDING, // Đang chờ xử lý
	IN_REVIEW, // Admin/mod đang xem xét
	REJECTED, // Bị từ chối (không vi phạm)
	APPROVED, // Đã duyệt – báo cáo hợp lệ
	RESOLVED, // Đã xử lý hậu quả (ẩn/xoá đối tượng)
	IGNORED; // Bỏ qua không hành động (nhẹ/không đủ cơ sở)	
}
