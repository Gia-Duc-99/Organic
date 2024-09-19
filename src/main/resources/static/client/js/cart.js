var cart = {
	add: function (id_sanpham, soluong, ten) {
		$.ajax({

			url: '/giohang/them/ajax',
			type: 'post',
			data: 'id_sanpham=' + id_sanpham + '&soluong=' + (typeof (soluong) != 'undefined' ? soluong : 1) + '&ten=' + ten,
			dataType: 'json',
			beforeSend: function () {
			},
			success: function (json) {
				if (json['success']) {
					toastr.success('Thêm sản phẩm vào giỏ hàng thành công!'); // Sử dụng toastr thay vì alert
					$('#cart-total').text(json['total']);
					$('#cart_total_down').text(json['total']);
					$('#cart').load('/giohang/ajax/get-html');
					// Cập nhật số lượng sản phẩm trong giỏ hàng
					$('#cart-icon span').text(json['totalItems']);
					// Refresh lại trang
					location.reload();
				}
			},
			error: function () {
				toastr.error('Lỗi! Không thêm sản phẩm vào giỏ hàng được! Kiểm tra đường dẫn ajax và thử lại.');
			}
		});
	},
	update: function (id_sanpham, so_luong, ten) {
		$.ajax({
			url: '/giohang/sua/ajax',
			type: 'post',
			data: 'id_sanpham=' + id_sanpham + '&ten=' + ten + '&so_luong=' + (typeof (so_luong) != 'undefined' ? so_luong : 1),
			dataType: 'json',

			beforeSend: function () {
				$('#cart > button').button('loading');
			},
			success: function (json) {

				$('#cart-total').text(json['total']);
				$('#cart_total_down').text(json['total']);
				if (window.location.pathname == '/trangchu/giohang') {

					location = '/trangchu/giohang';
				}
				else {
					toastr.success(json['success'], 'Cập nhật giỏ hàng thành công!')

					$('#cart').load('/giohang/ajax/get-html');
					// Cập nhật số lượng sản phẩm trong giỏ hàng
					$('#cart-icon span').text(json['totalItems']);
				}
			}
		});

	},
	remove: function (id_sanpham, ten) {

		$.ajax({
			url: '/giohang/xoa/ajax',
			type: 'post',
			data: 'id_sanpham=' + id_sanpham + '&ten=' + ten,
			dataType: 'json',
			beforeSend: function () {
				$('#cart > button').button('loading');
			},
			success: function (json) {
				$('#cart-total').text(json['total']);
				$('#cart_total_down').text(json['total']);
				if (window.location.pathname == '/trangchu/giohang') {
					location = '/trangchu/giohang';
				}
				else {
					toastr.success(json['success'], 'Xóa sản phẩm khỏi giỏ hàng thành công!')

					$('#cart').load('/giohang/ajax/get-html');
					// Cập nhật số lượng sản phẩm trong giỏ hàng
					$('#cart-icon span').text(json['totalItems']);
				}
			},
			error: function () {
				toastr.error('Lỗi! Không xóa sản phẩm khỏi giỏ hàng được! Kiểm tra đường dẫn ajax và thử lại.');
			}
		});
	}


}