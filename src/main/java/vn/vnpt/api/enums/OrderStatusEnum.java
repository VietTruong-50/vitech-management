package vn.vnpt.api.enums;

public enum OrderStatusEnum {
    // Tất cả đơn hàng
    ALL(0),

    // Đơn hàng đang chờ xác nhận và xử lý
    PENDING(1),

    // Đơn hàng đã xác nhận và chờ xử lý
    CONFIRMED_AND_PROCESSING(2),

//    // Đơn hàng đang xử lý
//    PROCESSING(3),

    // Đơn hàng đang trong quá trình giao hàng
    SHIPPED(4),

    // Đơn hàng đã được giao thành công
    COMPLETED(5),

    // Đơn hàng đã bị hủy
    CANCELLED(6),

    // Đơn hàng đã được hoàn hàng trở lại cửa hàng
    REFUND(7);


    public final int value;

    OrderStatusEnum(int value) {
        this.value = value;
    }
}