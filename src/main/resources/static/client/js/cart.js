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
					$('#cart_total_down').text(json.total);

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
				so_luong: parseInt(so_luong) + 1
			},
			dataType: 'json',
			success: function (json) {
				if (json.success) {
					// Cập nhật số lượng mới
					var newQuantity = parseInt(so_luong) + 1;

					// Cập nhật thành tiền mới
					var newTotal = newQuantity * parseFloat(json.donGiaVi); // donGiaVi là đơn giá từ phản hồi

					// Cập nhật giao diện
					var cartItem = $(`.cart-item[data-id="${id_sanpham}"]`);
					cartItem.find('.item-quantity').text(newQuantity);
					cartItem.find('.item-price').text(newTotal.toFixed(0)); // Định dạng số tiền

					// Cập nhật tổng số tiền của giỏ hàng
					$('#cart-total').text(json.total_cart);
					$('#cart_total_down').text(json.total_cart);

					showModal('Cập nhật số lượng sản phẩm thành công!');
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
				// Cập nhật tổng số lượng và tổng tiền
				$('#cart-total').text(json.total);
				$('#cart_total_down').text(json.total);

				// Xóa sản phẩm khỏi danh sách
				$(`.cart-item[data-id="${id_sanpham}"]`).remove();

				showModal('Sản phẩm đã được xóa khỏi giỏ hàng!');
			},
			error: function () {
				alert('Lỗi khi xóa sản phẩm khỏi giỏ hàng!');
			}
		});
	}
}
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