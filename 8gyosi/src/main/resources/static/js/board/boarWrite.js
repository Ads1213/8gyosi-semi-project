$(function(){

    const $fileInput = $('<input type="file" accept="image/*" style="display:none">');
    $('body').append($fileInput);

    $('.add-btn.inside').on('click', function(){
        $fileInput.click();
    });

    $fileInput.on('change', function(){
        const file = this.files[0];
        if(!file) return;

        const formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: '/editBoard/uploadImage',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(url){
                $('#boardContent').summernote('insertImage', url);
            },
            error: function(){
                alert('이미지 업로드 실패');
            }
        });

        this.value = '';
    });

    // 이미지 삭제 버튼 클릭
    $('.delete-image').on('click', function(){
        const order = $(this).data('order');
        let current = $('#deleteOrderList').val();
        if(current) current += ',' + order;
        else current = order;
        $('#deleteOrderList').val(current);
        $(this).closest('.boardImg').remove();
    });
});