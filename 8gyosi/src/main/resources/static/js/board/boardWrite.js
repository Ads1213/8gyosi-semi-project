/*$(document).ready(function () {
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
			success: function(res){
			    if(res.error){
			        alert('로그인이 필요합니다');
			        return;
			    }

			    if(isEditorImage || file.type.startsWith("image/")){
			        $('#boardContent').summernote('insertImage', res.url);
			    } else {
			        $('#boardContent').summernote(
			            'pasteHTML',
			            `<a href="${res.url}" target="_blank">${file.name}</a><br>`
			        );
			    }
			},
            error: function(){ 
                alert('파일 업로드 실패'); 
            }
        });
    }

    // 폼 제출 전 유효성 체크
    $('#boardForm').on('submit', function(){
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

    // 이미지 미리보기
    $('#img-input').on('change', function(){
        const files = this.files;
        const previewArea = $('#image-preview-area');
        previewArea.empty(); // 기존 미리보기 초기화

        Array.from(files).forEach((file, idx) => {
            const reader = new FileReader();
            reader.onload = function(e){
                const img = $('<img>').attr('src', e.target.result)
                                       .addClass('preview')
                                       .attr('data-index', idx);
                previewArea.append(img);
            }
            reader.readAsDataURL(file);
        });
    });
});

*/


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
                    uploadEditorFile(file, true);
                }
            }
        }
    });

    // Summernote 이미지/파일 업로드
    function uploadEditorFile(file, isImage=false){
        let formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: uploadUrl,
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function(res){
                if(res.error){
                    alert('로그인이 필요합니다');
                    return;
                }

                if(isImage || file.type.startsWith("image/")){
                    $('#boardContent').summernote('insertImage', res.url);
                } else {
                    $('#boardContent').summernote(
                        'pasteHTML',
                        `<a href="${res.url}" target="_blank">${file.name}</a><br>`
                    );
                }
            },
            error: function(){ 
                alert('파일 업로드 실패'); 
            }
        });
    }

    // 파일 input 선택 시 UI에 파일 이름 표시
    const fileInput = $('#file-input');
    const fileListArea = $('<ul id="file-list"></ul>'); // 파일 이름 리스트
    $('.boardFile').append(fileListArea);

    fileInput.on('change', function(){
        fileListArea.empty(); // 초기화
        const files = this.files;

        Array.from(files).forEach((file, idx)=>{
            const li = $('<li></li>').text(file.name)
                                      .attr('data-index', idx)
                                      .css({marginBottom:'4px'});
            const removeBtn = $('<button type="button">삭제</button>')
                                .css({marginLeft:'8px'})
                                .on('click', function(){
                                    removeFile(idx);
                                });
            li.append(removeBtn);
            fileListArea.append(li);
        });
    });

    // 선택된 파일 삭제 (input.files는 직접 수정 불가하므로 workaround)
    function removeFile(index){
        const dt = new DataTransfer();
        const files = fileInput[0].files;

        Array.from(files).forEach((file, i)=>{
            if(i !== index){
                dt.items.add(file);
            }
        });

        fileInput[0].files = dt.files;

        // UI 업데이트
        fileInput.trigger('change');
    }

    // 폼 제출 전 유효성 체크
    $('#boardForm').on('submit', function(){
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

    // 이미지 미리보기
    $('#img-input').on('change', function(){
        const files = this.files;
        const previewArea = $('#image-preview-area');
        previewArea.empty();

        Array.from(files).forEach((file, idx)=>{
            const reader = new FileReader();
            reader.onload = function(e){
                const img = $('<img>').attr('src', e.target.result)
                                       .addClass('preview')
                                       .attr('data-index', idx)
                                       .css({width:'100px', marginRight:'5px'});
                previewArea.append(img);
            }
            reader.readAsDataURL(file);
        });
    });
});

