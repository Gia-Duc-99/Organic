/*  ---------------------------------------------------
    Template Name: Ogani
    Description:  Ogani eCommerce  HTML Template
    Author: Colorlib
    Author URI: https://colorlib.com
    Version: 1.0
    Created: Colorlib
---------------------------------------------------------  */

'use strict';

(function ($) {

    /*------------------
        Preloader
    --------------------*/
    $(window).on('load', function () {
        $(".loader").fadeOut();
        $("#preloder").delay(200).fadeOut("slow");

        /*------------------
            Gallery filter
        --------------------*/
        $('.featured__controls li').on('click', function () {
            $('.featured__controls li').removeClass('active');
            $(this).addClass('active');
        });
        if ($('.featured__filter').length > 0) {
            var containerEl = document.querySelector('.featured__filter');
            var mixer = mixitup(containerEl);
        }
    });

    /*------------------
        Background Set
    --------------------*/
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });

    //Humberger Menu
    $(".humberger__open").on('click', function () {
        $(".humberger__menu__wrapper").addClass("show__humberger__menu__wrapper");
        $(".humberger__menu__overlay").addClass("active");
        $("body").addClass("over_hid");
    });

    $(".humberger__menu__overlay").on('click', function () {
        $(".humberger__menu__wrapper").removeClass("show__humberger__menu__wrapper");
        $(".humberger__menu__overlay").removeClass("active");
        $("body").removeClass("over_hid");
    });

    /*------------------
        Navigation
    --------------------*/
    $(".mobile-menu").slicknav({
        prependTo: '#mobile-menu-wrap',
        allowParentLinks: true
    });

    /*-----------------------
        Categories Slider
    ------------------------*/
    $(".categories__slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 4,
        dots: false,
        nav: true,
        navText: ["<span class='fa fa-angle-left'><span/>", "<span class='fa fa-angle-right'><span/>"],
        animateOut: 'fadeOut',
        animateIn: 'fadeIn',
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true,
        responsive: {

            0: {
                items: 1,
            },

            480: {
                items: 2,
            },

            768: {
                items: 3,
            },

            992: {
                items: 4,
            }
        }
    });


    $('.hero__categories__all').on('click', function () {
        $('.hero__categories ul').slideToggle(400);
    });

    /*--------------------------
        Latest Product Slider
    ----------------------------*/
    $(".latest-product__slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 1,
        dots: false,
        nav: true,
        navText: ["<span class='fa fa-angle-left'><span/>", "<span class='fa fa-angle-right'><span/>"],
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true
    });

    /*-----------------------------
        Product Discount Slider
    -------------------------------*/
    $(".product__discount__slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 3,
        dots: true,
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true,
        responsive: {

            320: {
                items: 1,
            },

            480: {
                items: 2,
            },

            768: {
                items: 2,
            },

            992: {
                items: 3,
            }
        }
    });

    /*---------------------------------
        Product Details Pic Slider
    ----------------------------------*/
    $(".product__details__pic__slider").owlCarousel({
        loop: true,
        margin: 20,
        items: 4,
        dots: true,
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true
    });

    /*-----------------------
        Price Range Slider
    ------------------------ */
    var rangeSlider = $(".price-range"),
        minamount = $("#minamount"),
        maxamount = $("#maxamount"),
        minPrice = rangeSlider.data('min'),
        maxPrice = rangeSlider.data('max');
    rangeSlider.slider({
        range: true,
        min: minPrice,
        max: maxPrice,
        values: [minPrice, maxPrice],
        slide: function (event, ui) {
            minamount.val('$' + ui.values[0]);
            maxamount.val('$' + ui.values[1]);
        }
    });
    minamount.val('$' + rangeSlider.slider("values", 0));
    maxamount.val('$' + rangeSlider.slider("values", 1));

    /*--------------------------
        Select
    ----------------------------*/
    $("select").niceSelect();

    /*------------------
        Single Product
    --------------------*/
    $('.product__details__pic__slider img').on('click', function () {

        var imgurl = $(this).data('imgbigurl');
        var bigImg = $('.product__details__pic__item--large').attr('src');
        if (imgurl != bigImg) {
            $('.product__details__pic__item--large').attr({
                src: imgurl
            });
        }
    });
    /*-------------------
        Quantity change
    --------------------- */
    var proQty = $('.pro-qty');
    proQty.prepend('<span class="dec qtybtn">-</span>');
    proQty.append('<span class="inc qtybtn">+</span>');

    // Sự kiện click cho các nút tăng/giảm
    proQty.on('click', '.qtybtn', function () {
        var $button = $(this);
        var inputField = $button.parent().find('input'); // Trường input chứa số lượng sản phẩm
        var oldValue = parseFloat(inputField.val());
        var id_sanpham = inputField.attr('id').split('_')[2];  // Lấy ID sản phẩm từ input, ví dụ: soluong_sp_1
        var ten = $button.closest('tr').find('h5').text();
        var donGia = parseFloat($button.closest('tr').find('.shoping__cart__price').text().replace(/[^\d]/g, '')); // Lấy đơn giá sản phẩm
        var newVal;

        // Kiểm tra nếu là nút tăng
        if ($button.hasClass('inc')) {
            newVal = oldValue + 1, 99;  // Tăng số lượng, Giới hạn số lượng tối đa là 99
        } else {
            // Kiểm tra để không giảm dưới 1
            newVal = Math.max(oldValue - 1, 1);  // Giảm số lượng nhưng không dưới 1
        }
        // Cập nhật số lượng trong ô input
        inputField.val(newVal);

        // Gửi yêu cầu AJAX để cập nhật số lượng trên server
        $.ajax({
            url: '/giohang/sua/ajax',
            type: 'post',
            data: {
                id_sanpham: id_sanpham,
                ten: ten,
                so_luong: newVal
            },
            dataType: 'json',
            success: function (json) {
                if (json.success) {
                    // Cập nhật lại thành tiền nếu server có giá khác
                    var totalCell = $button.closest('tr').find('.shoping__cart__total');
                    var updatedTotalPrice = newVal * parseFloat(json.donGiaVi); // Sử dụng giá mới từ server
                    totalCell.text(updatedTotalPrice.toLocaleString() + ' đ');

                    $('#cart-total').text(json.total_quantity);  // Số lượng tổng từ server
                    $('#cart_total_price').text(json.totalCartVi + ' đ');
                } else {
                    // Nếu có lỗi xảy ra, hiển thị thông báo
                    showModal('Lỗi khi cập nhật giỏ hàng!');
                }
            },
            error: function () {
                // Nếu có lỗi trong quá trình gửi AJAX, hiển thị thông báo
                showModal('Lỗi khi cập nhật giỏ hàng!');
            }
        });
    });
})(jQuery);