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

					alert(' thêm sản phẩm vào giỏ hàng thành công được!.');

					$('#cart-total').text(json['total']);
					$('#cart_total_down').text(json['total']);
					$('#cart').load('/giohang/ajax/get-html');

				}
			},
			error: function () {
				alert('Lỗi!-Không thêm sản phẩm vào giỏ hàng được! Kiểm tra đường dẫn ajax và thử lại.');
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
					toastr.success(json['success'], 'Thêm Sản Phẩm vào Giỏ OK')

					$('#cart').load('/giohang/ajax/get-html');
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
					toastr.success(json['success'], 'Thêm Sản Phẩm vào Giỏ OK')

					$('#cart').load('/giohang/ajax/get-html');
				}
			},
			error: function () {
				alert('error!');
			}
		});
	}


}