$(document).ready(function () {
    const uploadUrl = '/editBoard/uploadFile'; // 파일 업로드 요청 URL

    // ===================== Summernote 초기화 =====================
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
            // 이미지 업로드 시 호출
            onImageUpload: function(files) {
                for(let file of files){
                    uploadFile(file, true);
                }
            }
        }
    });

    // ===================== 파일 업로드 AJAX =====================
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
                // 에디터 이미지이면 Summernote에 바로 삽입
                if(isEditorImage || file.type.startsWith("image/")){
                    $('#boardContent').summernote('insertImage', url);
                } else {
                    // 일반 파일은 링크로 삽입
                    const linkHtml = `<a href="${url}" target="_blank">${file.name}</a><br>`;
                    $('#boardContent').summernote('pasteHTML', linkHtml);
                }
            },
            error: function(){
                alert('파일 업로드 실패');
            }
        });
    }

    // ===================== 폼 제출 전 체크 =====================
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

