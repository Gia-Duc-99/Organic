window.showModal = function (url, title, actionText, onSubmit) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            document.getElementById('modalContent').innerHTML = html;
            document.getElementById('modalActionBtn').innerText = actionText;
            document.getElementById('modalActionBtn').style.display = 'block'; // Hiện nút thực hiện
            document.getElementById('modalActionBtn').onclick = onSubmit;
            $('#showModal').modal('show');
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