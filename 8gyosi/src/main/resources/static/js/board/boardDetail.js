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
            url: `/editBoard/comment/${boardId}`,
            type: 'GET',
            success: function(comments) {
                const $list = $('#commentList');
                $list.empty();
                comments.forEach(c => {
                    const canDelete = c.memberNo === loginMemberNo || sessionRole === 'ADMIN';
                    let deleteBtn = canDelete ? `<button class="deleteCommentBtn" data-id="${c.commentNo}">삭제</button>` : '';
                    $list.append(`
                        <div class="comment" id="comment-${c.commentNo}">

                            // <p><strong>${c.memberName}</strong> (${c.commentWriteDate})</p>

                            <p><strong>${c.memberNickname}</strong> (${c.commentWriteDate})</p>
							
                            <p>${c.commentContent}</p>
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

        let pNo = 0;
            try {
                if (typeof parentCommentNo !== 'undefined') {
                    pNo = parentCommentNo;
                 }
            } catch (e) {
                pNo = 0;
            }

        const obj = { 
            "commentContent" : content,
            "boardId" : boardId,
            "parentCommentNo": pNo
        };

        $.ajax({
            url: `/editBoard/comment/${boardId}`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(obj),
            success: function(res) {
                $('#commentContent').val('');
                 location.reload();   //자동 새로고침      //loadComments(); 비동기식 방법
            },
            error: function(err) {
                alert('댓글 작성 실패');
                console.error(err);
            }
        });
    });

    // ===================== 댓글 삭제 =====================
    window.deleteComment = function(commentNo) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        $.ajax({
            url: `/editBoard/comment/${boardId}/${commentNo}`,
            type: "DELETE",
            success: function(res) {
            if (res.success) {
                alert(res.message);
                location.reload();
            } else {
                alert(res.message);
            }
        },
        error: function(err) {
            alert("댓글 삭제 중 오류가 발생했습니다.");
            console.error(err);
        }
    });
    };
    //===================== 답글 작성 =======================
   window.showInsertComment = (parentCommentNo, btn) => {
    // 1. 이미 열려있는 답글창이 있는지 확인
    const temp = document.getElementsByClassName("commentInsertContent");
    if(temp.length > 0){
        if(confirm("다른 답글을 작성 중입니다. 이 댓글에 작성하시겠습니까?")){
            temp[0].nextElementSibling.remove(); // 버튼 영역 삭제
            temp[0].remove(); // textarea 삭제
        } else {
            return;
        }
    }
    
    // 2. 답글 입력창(textarea) 생성
    const textarea = document.createElement("textarea");
    textarea.classList.add("commentInsertContent");
    textarea.placeholder = "답글을 입력해주세요.";
    
    // 3. 버튼 영역 생성 (등록/취소)
    const commentBtnArea = document.createElement("div");
    commentBtnArea.classList.add("comment-btn-area");

    const insertBtn = document.createElement("button");
    insertBtn.innerText = "등록";

    insertBtn.onclick = function() {
        window.insertChildComment(parentCommentNo, insertBtn);
    };

    const cancelBtn = document.createElement("button");
    cancelBtn.innerText = "취소";
    cancelBtn.onclick = function() {
        window.insertCancel(cancelBtn);
    };

    // 화면 삽입
    commentBtnArea.append(insertBtn, cancelBtn);
    btn.parentElement.after(textarea);         
    textarea.after(commentBtnArea);     
};

    /** * 답글 등록 */
    window.insertChildComment = (parentCommentNo, btn) => {

        const textarea = btn.parentElement.previousElementSibling;
        const content = textarea.value;

        if(content.trim().length == 0){
            alert("답글 내용을 입력해주세요.");
            textarea.focus();
            return;
        }

        const obj = {
            "commentContent": content,
            "boardId": boardId,
            "parentCommentNo": parentCommentNo || 0
        };

       fetch(`/editBoard/comment/${boardId}`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(obj)
        })
        .then(response => response.json())
        .then(data => {
            console.log("data:", data);
            if(data.success === true || data.result > 0){
                alert(data.message || "답글이 등록되었습니다.");
                location.reload(); 
            } else {
                alert(data.message || "답글 등록 실패");
            }
        })
        .catch(err => console.error("답글 등록 에러:", err));
    };

    /** 답글 작성 취소 */
    window.insertCancel = (btn) => {
        btn.parentElement.previousElementSibling.remove(); // textarea 삭제
        btn.parentElement.remove(); // 버튼 영역 삭제
    };


    //===================== 댓글 수정 =======================
   window.showUpdateComment = (commentNo, btn) => {
    // 부모 li 요소 찾기
    const commentLi = btn.closest("li");
    
    // 댓글 내용이 담긴 요소 (클래스명은 실제 HTML에 맞게 조정하세요)
    const contentArea = commentLi.querySelector(".comment-content");
    const oldContent = contentArea.innerHTML; // 취소 시 복구용

    // 1. 기존 내용 보관 및 textarea 생성
    const textarea = document.createElement("textarea");
    textarea.value = oldContent.replaceAll("<br>", "\n"); // 줄바꿈 처리
    
    // 2. 내용 영역 교체
    contentArea.innerHTML = "";
    contentArea.appendChild(textarea);

    // 3. 버튼 영역 변경
    const buttonArea = commentLi.querySelector(".comment-btn-area");
    const originalButtons = buttonArea.innerHTML; // 기존 버튼들 보관

    buttonArea.innerHTML = `
        <button onclick="updateComment(${commentNo}, this)">등록</button>
        <button onclick="updateCancel(this, '${oldContent.replaceAll("'", "\\'").replaceAll("\n", "")}')">취소</button>
        `;
    };

    window.updateComment = (commentNo, btn) => {
    const textarea = btn.closest("li").querySelector("textarea");
    const content = textarea.value;

    if(content.trim().length === 0) {
        alert("내용을 입력해주세요.");
        return;
    }

    const data = {
        "commentNo" : commentNo,
        "commentContent" : content
    };

    fetch("/editBoard/comment", { // 서버 컨트롤러 주소에 맞게 수정
        method : "PUT",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(data)
    })
    .then(resp => resp.json())
    .then(result => {
        if(result.success) {
            alert("댓글이 수정되었습니다.");
            location.reload();
        } else {
            alert("수정 실패");
        }
    })
    .catch(err => console.log(err));
    };

    // ==================좋아요 버튼========================
    $("#likeBtn").off("click").on("click", function(e) {
    const $btn = $(this);
    const $countSpan = $("#likeCount"); 
    
    const boardId = $btn.data("board-id");
    const commentNo = $btn.data("comment-no");
    let likeCheck = $btn.hasClass("fa-solid") ? 1 : 0;

    const obj = {
        "likeCheck": likeCheck,
        "boardId": boardId
    };
    
    fetch(`/board/${boardId}/${commentNo}/like`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(obj)
    })
    .then(resp => resp.text())
    .then(count => {
        if (count == -1) {
            alert("로그인 후 이용 가능합니다.");
            return;
        }

        $btn.toggleClass("fa-regular fa-solid");
        $countSpan.text(count);
    })
    .catch(err => console.error("오류:", err));
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

