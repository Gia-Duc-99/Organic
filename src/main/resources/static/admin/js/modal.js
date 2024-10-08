window.showModal = function (url, title, actionText, onSubmit) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            // Kiểm tra nếu nội dung trả về là trang duyet.html hoặc một thông báo lỗi
            if (html.includes("Nhân viên này đang bị cấm")) {
                // Xử lý nếu là nhân viên bị cấm: hiển thị thông báo lỗi trong modal hoặc chuyển hướng
                document.getElementById('modalContent').innerHTML = `<p class="text-danger">Nhân viên này đang bị cấm và không thể chỉnh sửa.</p>`;
                document.getElementById('modalActionBtn').style.display = 'none'; // Ẩn nút hành động
            } else {
                // Nếu không bị cấm, hiển thị form sửa trong modal
                document.getElementById('modalContent').innerHTML = html;
                document.getElementById('modalActionBtn').innerText = actionText;
                document.getElementById('modalActionBtn').style.display = 'block'; // Hiện nút thực hiện
                document.getElementById('modalActionBtn').onclick = onSubmit;
            }
            $('#showModal').modal('show');
        })
        .catch(error => {
            console.error('Có lỗi xảy ra:', error);
        });
};

document.querySelector('[data-target="#showModal"]').onclick = function () {
    var formContent = document.getElementById('formThemContainer').innerHTML;
    document.getElementById('modalContent').innerHTML = formContent;
    document.getElementById('modalActionBtn').innerText = 'Thêm';
    document.getElementById('modalActionBtn').onclick = function () {
        document.querySelector('#showModal form').submit();
    };
    $('#showModal').modal('show');
};