/* boardDetail.js */

$(function() {
    const boardId = $('#likeBtn').data('board-id'); // HTML에서 data-board-id 가져오기
    const boardTypeNo = /*[[${board.boardTypeNo}]]*/ 1; // Thymeleaf 바인딩

    // ===================== 좋아요 토글 =====================
    function updateLikeUI(liked, likeCount) {
        $('#likeBtn').text(liked ? '👍 좋아요 취소' : '👍 좋아요');
        $('#likeCount').text(likeCount);
    }

    $('#likeBtn').click(function() {
        $.ajax({
            url: `/board/${boardTypeNo}/${boardId}/like`,
            type: 'POST',
            success: function(res) {
                updateLikeUI(res.liked, res.likeCount);
            },
            error: function(err) {
                alert('좋아요 처리 중 오류 발생');
                console.error(err);
            }
        });
    });

    // ===================== 댓글 목록 =====================
    function loadComments() {
        $.ajax({
            url: `/board/${boardTypeNo}/${boardId}/comments`,
            type: 'GET',
            success: function(comments) {
                const $list = $('#commentList');
                $list.empty();
                comments.forEach(c => {
                    const canDelete = c.memberNo === sessionMemberNo || sessionRole === 'ADMIN';
                    let deleteBtn = canDelete ? `<button class="deleteCommentBtn" data-id="${c.commentId}">삭제</button>` : '';
                    $list.append(`
                        <div class="comment" id="comment-${c.commentId}">
                            <p><strong>${c.memberName}</strong> (${c.createDate})</p>
                            <p>${c.content}</p>
                            ${deleteBtn}
                        </div>
                    `);
                });
            },
            error: function(err) {
                console.error('댓글 로딩 실패', err);
            }
        });
    }

    // 페이지 로딩 시 댓글 불러오기
    loadComments();

    // ===================== 댓글 작성 =====================
    $('#addCommentBtn').click(function() {
        const content = $('#commentContent').val().trim();
        if(!content) return alert('댓글 내용을 입력하세요');

        $.ajax({
            url: `/board/${boardTypeNo}/${boardId}/comments`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ content }),
            success: function(res) {
                $('#commentContent').val('');
                loadComments();
            },
            error: function(err) {
                alert('댓글 작성 실패');
                console.error(err);
            }
        });
    });

    // ===================== 댓글 삭제 =====================
    $('#commentList').on('click', '.deleteCommentBtn', function() {
        const commentId = $(this).data('id');
        if(!confirm('정말 삭제하시겠습니까?')) return;

        $.ajax({
            url: `/board/${boardTypeNo}/${boardId}/comments/${commentId}`,
            type: 'DELETE',
            success: function(res) {
                $(`#comment-${commentId}`).remove();
            },
            error: function(err) {
                alert('댓글 삭제 실패');
                console.error(err);
            }
        });
    });

    // ===================== 게시글 삭제 =====================
    $('#deleteBoardBtn').click(function() {
        if(!confirm('정말 삭제하시겠습니까?')) return;

        $.ajax({
            url: `/board/${boardTypeNo}/${boardId}/delete`,
            type: 'POST',
            success: function(res) {
                alert('게시글이 삭제되었습니다');
                window.location.href = `/board/${boardTypeNo}`;
            },
            error: function(err) {
                alert('게시글 삭제 실패');
                console.error(err);
            }
        });
    });

});

