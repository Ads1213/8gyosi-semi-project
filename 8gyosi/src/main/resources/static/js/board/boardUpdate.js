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

    // 파일 업로드 함수
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
                    $('#boardContent').summernote('pasteHTML', `<a href="${url}" target="_blank">${file.name}</a><br>`);
                }
            },
            error: function(){ alert('파일 업로드 실패'); }
        });
    }

    // 기존 이미지 삭제
    $('.delete-image').on('click', function(){
        const imgNo = $(this).data('img-id'); 
        let current = $('#deleteImageIds').val();
        $('#deleteImageIds').val(current ? current + ',' + imgNo : imgNo);
        $(this).closest('.boardImg').remove();
    });

    // 기존 파일 삭제
    $('.delete-file').on('click', function(){
        const fileNo = $(this).data('file-id'); 
        let current = $('#deleteFileIds').val();
        $('#deleteFileIds').val(current ? current + ',' + fileNo : fileNo);
        $(this).closest('.boardFile').remove();
    });

    // 폼 제출 전 유효성 체크
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

