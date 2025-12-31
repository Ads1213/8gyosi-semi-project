$(document).ready(function () {
    const boardNo = $('#likeBtn').data('board-no');

    // 좋아요/싫어요
    $('#likeBtn').on('click', function(){ toggleLike(true); });
    $('#hateBtn').on('click', function(){ toggleLike(false); });

    function toggleLike(isLike){
        $.ajax({
            url: '/editBoard/'+boardNo+'/like',
            method: 'POST',
            data: { isLike: isLike },
            success: function(res){
                if(res === 1){
                    alert('좋아요/싫어요 처리 완료');
                    location.reload();
                } else if(res === -1){
                    alert('좋아요/싫어요 취소');
                    location.reload();
                }
            }
        });
    }

    // 댓글 작성
    $('#addCommentBtn').on('click', function(){
        const content = $('#newCommentContent').val().trim();
        if(!content){ alert('댓글 내용을 입력하세요'); return; }

        $.ajax({
            url: '/editBoard/'+boardNo+'/comment',
            method: 'POST',
            data: { commentContent: content },
            success: function(){ location.reload(); }
        });
    });

    // 대댓글 작성
    $('.add-reply-btn').on('click', function(){
        const parentId = $(this).data('parent-id');
        const content = $(this).siblings('.reply-content').val().trim();
        if(!content){ alert('대댓글 내용을 입력하세요'); return; }

        $.ajax({
            url: '/editBoard/'+boardNo+'/comment',
            method: 'POST',
            data: { commentContent: content, parentCommentId: parentId },
            success: function(){ location.reload(); }
        });
    });

    // 댓글 삭제
    $('.delete-comment-btn').on('click', function(){
        const commentId = $(this).data('comment-id');
        if(!confirm('댓글을 삭제하시겠습니까?')) return;

        $.ajax({
            url: '/editBoard/comment/'+commentId+'/delete',
            method: 'POST',
            success: function(){ location.reload(); }
        });
    });

    // 댓글 수정
    $('.edit-comment-btn').on('click', function(){
        const commentId = $(this).data('comment-id');
        const commentDiv = $('#comment-'+commentId);
        const content = commentDiv.find('p:nth-child(2)').text();

        const textarea = `<textarea class="edit-comment-content">${content}</textarea>
                          <button class="save-comment-btn" data-comment-id="${commentId}">저장</button>`;
        commentDiv.find('p:nth-child(2)').replaceWith(textarea);

        // 저장 버튼 클릭
        $('.save-comment-btn').on('click', function(){
            const updatedContent = $(this).siblings('.edit-comment-content').val().trim();
            if(!updatedContent){ alert('내용을 입력하세요'); return; }

            $.ajax({
                url: '/editBoard/comment/'+commentId+'/update',
                method: 'POST',
                data: { commentContent: updatedContent },
                success: function(){ location.reload(); }
            });
        });
    });
});
