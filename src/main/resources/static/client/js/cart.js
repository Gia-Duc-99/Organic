var cart = {
	add: function (id_sanpham, soluong, ten) {
		$.ajax({
			url: '/giohang/them/ajax',
			type: 'post',
			data: {
				id_sanpham: id_sanpham,
				soluong: typeof (soluong) !== 'undefined' ? soluong : 1,
				ten: ten
			},
			dataType: 'json',
			success: function (json) {
				if (json.success) {
					// Cập nhật tổng sản phẩm và tổng tiền
					$('#cart-total').text(json.total);
					$('#cart_total_price').text(json.totalCartVi + ' đ');
					// Cập nhật sản phẩm trong danh sách (chỉ thêm mục mới)
					var cartItem = `<div class="cart-item" data-id="${id_sanpham}">
                                        <span class="item-name">${ten}</span>
                                        <span class="item-quantity">${soluong}</span>
                                        <span class="item-price">${json.price}</span>
                                    </div>`;
					$('#cart-items').append(cartItem);

					showModal('Đã thêm thành công sản phẩm vào giỏ hàng');
				}
			},
			error: function () {
				showModal('Lỗi! Không thêm sản phẩm vào giỏ hàng được!');
			}
		});
	},

	update: function (id_sanpham, so_luong, ten) {
		$.ajax({
			url: '/giohang/sua/ajax',
			type: 'post',
			data: {
				id_sanpham: id_sanpham,
				ten: ten,
				so_luong: parseInt(so_luong)
			},
			dataType: 'json',
			success: function (json) {
				if (json.success) {

					var updatedTotalPrice = so_luong * parseFloat(json.donGiaVi);
					$('#total_sp_' + id_sanpham).text(updatedTotalPrice.toLocaleString() + ' đ');
					$('#cart-total').text(json.total_quantity);
					$('#cart_total_price').text(json.totalCartVi + ' đ');
				} else {
					showModal('Lỗi khi cập nhật giỏ hàng!');
				}
			},
			error: function () {
				showModal('Lỗi khi cập nhật giỏ hàng!');
			}
		});
	},

	remove: function (id_sanpham, ten) {
		$.ajax({
			url: '/giohang/xoa/ajax',
			type: 'post',
			data: {
				id_sanpham: id_sanpham,
				ten: ten
			},
			dataType: 'json',
			success: function (json) {
				$('#cart-total').text(json.total);
				$('#cart_total_price').text(json.totalCartVi + ' đ');
				showModal('Sản phẩm đã được xóa khỏi giỏ hàng!');

				$('#success-modal').on('hidden.bs.modal', function () {
					location.reload(); // Tải lại trang khi modal đóng
				});
			},
			error: function () {
				alert('Lỗi khi xóa sản phẩm khỏi giỏ hàng!');
			}
		});
	}
};
//1 số js bổ sung
function showModal(message) {
	$('#modal-message').text(message);
	$('#success-modal').modal('show');
}
function toggleCart() {
	var cartDropdown = document.getElementById('cart-dropdown');
	if (cartDropdown.style.display === 'none' || cartDropdown.style.display === '') {
		cartDropdown.style.display = 'block';
		loadCartContent();
	} else {
		cartDropdown.style.display = 'none';
	}
}

function loadCartContent() {
	var cartContent = document.getElementById('cart-content');
	fetch('/giohang/ajax/get-html')
		.then(response => {
			if (!response.ok) {
				throw new Error('HTTP error ' + response.status);
			}
			return response.text();
		})
		.then(data => {
			cartContent.innerHTML = data;
		})
		.catch(error => {
			console.error('Error:', error);
			cartContent.innerHTML = 'Error loading cart content: ' + error.message;
		});
}
document.addEventListener("DOMContentLoaded", function () {

	var heroItem = document.querySelector(".hero__item.set-bg");

	var heroSection = document.querySelector("section.hero");

	// Kiểm tra URL của trang
	var currentPath = window.location.pathname;

	// Hiển thị hero__item nếu URL là /trangchu
	if (currentPath === "/trangchu") {
		heroItem.style.display = "block";
		heroSection.className = "hero";
	} else {
		// Ẩn hero__item nếu URL không phải là /trangchu
		heroItem.style.display = "none";
		heroSection.className = "hero hero-normal";
	}
});
