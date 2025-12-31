$(document).ready(function () {
    const uploadUrl = '/editBoard/uploadFile';

    // Summernote 초기화
    $('#boardContent').summernote({
        placeholder: '내용을 입력하세요',
        height: 400,
        toolbar: [
            ['style', ['bold','italic','underline','clear']],
            ['font',['fontsize','color','fontname']],
            ['para',['ul','ol','paragraph']],
            ['insert',['link','picture','video']],
            ['view',['fullscreen','codeview']]
        ],
        callbacks: {
            onImageUpload: function(files) {
                for(let file of files){
                    uploadFile(file, true);
                }
            }
        }
    });

    function uploadFile(file, isEditorImage=false){
        let formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: uploadUrl,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(url){
                if(isEditorImage || file.type.startsWith("image/")){
                    $('#boardContent').summernote('insertImage', url);
                } else {
                    const linkHtml = `<a href="${url}" target="_blank">${file.name}</a><br>`;
                    $('#boardContent').summernote('pasteHTML', linkHtml);
                }
            },
            error: function(){
                alert('파일 업로드 실패');
            }
        });
    }

    // + 버튼으로 새 파일 추가
    $('#fileContainer').on('click', '.addFileBtn', function(){
        const row = `<div class="file-row">
                        <input type="file" name="files">
                        <button type="button" class="addFileBtn">+</button>
                     </div>`;
        $('#fileContainer').append(row);
    });

    // 기존 이미지 삭제 처리
    $('.delete-image').on('click', function(){
        const order = $(this).data('order');
        let current = $('#deleteImageList').val();
        $('#deleteImageList').val(current ? current + ',' + order : order);
        $(this).closest('.boardImg').remove();
    });

    // 기존 파일 삭제 처리
    $('.delete-file').on('click', function(){
        const order = $(this).data('order');
        let current = $('#deleteFileList').val();
        $('#deleteFileList').val(current ? current + ',' + order : order);
        $(this).closest('.boardFile').remove();
    });

    // 제출 전 필수 체크
    $('form').on('submit', function(){
        const title = $('input[name="boardTitle"]').val().trim();
        const content = $('#boardContent').summernote('code').trim();

        if(!title){
            alert('제목을 입력해주세요');
            return false;
        }
        if(!content || content === '<p><br></p>'){
            alert('내용을 입력해주세요');
            return false;
        }
    });
});

