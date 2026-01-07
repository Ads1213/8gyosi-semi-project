/* boardDetail.js */
$(document).ready(function () {
    

    // ===================== 댓글 목록 =====================
    function loadComments() {
    $.ajax({
        url: `/editBoard/comment/${boardId}`,
        type: 'GET',
        success: function(comments) {
            // 1. 댓글을 감싸는 전체 컨테이너를 ul로 선택 (없다면 HTML에 <ul id="commentList"></ul> 필요)
            const $list = $('#commentList'); 
            $list.empty();

            comments.forEach(c => {
                // 삭제 권한 확인
                const canDelete = c.memberNo === loginMemberNo || sessionRole === 'ADMIN';
                let deleteBtn = canDelete ? `<button class="deleteCommentBtn" data-id="${c.commentNo}">삭제</button>` : '';
                
                // 2. li 태그를 이용하여 댓글 행 생성
                // 클래스명(comment-row 등)을 부여하면 CSS 잡기가 편합니다.
                const li = `
                    <li class="comment-row" id="comment-${c.commentNo}">
                        <div class="comment-header">
                            <span class="nickname"><strong>${c.memberNickname}</strong></span>
                            <span class="write-date">(${c.commentWriteDate})</span>
                        </div>
                        
                        <div class="comment-content">
                            <p>${c.commentContent}</p>
                        </div>

                        <div class="comment-footer">
                            ${deleteBtn}
                        </div>
                    </li>
                `;
                
                $list.append(li);
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
    // ===================== 댓글 등록 (기본) =====================
$('#addCommentBtn').click(function() {
    const content = $('#commentContent').val().trim();
    if(!content) return alert('댓글 내용을 입력하세요');

    const obj = { 
        "commentContent" : content,
        "boardId" : boardId,
        "parentCommentNo": 0 // 일반 댓글은 부모 0
    };

    $.ajax({
        url: `/editBoard/comment/${boardId}`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(obj),
        success: function(res) {
            $('#commentContent').val('');
            location.reload(); 
        },
        error: function(err) {
            alert('댓글 작성 실패');
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
            if (res.success || res > 0) {
                alert("삭제되었습니다.");
                location.reload();
            } else {
                alert("삭제 실패");
            }
        }
    });
};

// ===================== 답글(대댓글) 작성창 출력 =======================
window.showInsertComment = (parentCommentNo, btn) => {
    // 1. 이미 열려있는 답글창 삭제 로직
    const temp = document.getElementsByClassName("commentInsertContent");
    if(temp.length > 0){
        if(confirm("작성 중인 답글이 있습니다. 이 댓글에 작성하시겠습니까?")){
            temp[0].nextElementSibling.remove(); 
            temp[0].remove(); 
        } else return;
    }
    
    // 2. li 태그(부모 행)를 찾아서 그 아래에 삽입
    const parentLi = btn.closest("li");
    
    const textarea = document.createElement("textarea");
    textarea.classList.add("commentInsertContent");
    textarea.placeholder = "답글을 입력해주세요.";
    
    const commentBtnArea = document.createElement("div");
    commentBtnArea.classList.add("comment-btn-area");

    const insertBtn = document.createElement("button");
    insertBtn.innerText = "등록";
    insertBtn.onclick = () => window.insertChildComment(parentCommentNo, insertBtn);

    const cancelBtn = document.createElement("button");
    cancelBtn.innerText = "취소";
    cancelBtn.onclick = () => window.insertCancel(cancelBtn);

    commentBtnArea.append(insertBtn, cancelBtn);
    
    // 리스트 항목(li) 내부 마지막에 추가
    parentLi.append(textarea, commentBtnArea);
};

// ===================== 답글 등록 실행 =======================
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
        "parentCommentNo": parentCommentNo
    };

    fetch(`/editBoard/comment/${boardId}`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(obj)
    })
    .then(resp => resp.json())
    .then(data => {
        if(data.success || data > 0){
            alert("답글이 등록되었습니다.");
            location.reload(); 
        } else alert("등록 실패");
    });
};

// ===================== 댓글 수정창 출력 =======================
window.showUpdateComment = (commentNo, btn) => {
    const commentLi = btn.closest("li"); // 현재 댓글 li
    const contentArea = commentLi.querySelector(".comment-content"); // 내용 영역
    const oldContent = contentArea.innerText; // 수정 전 텍스트

    // 1. 수정용 textarea 생성
    const textarea = document.createElement("textarea");
    textarea.classList.add("update-textarea");
    textarea.value = oldContent;
    
    // 2. 기존 내용 영역을 textarea로 교체
    contentArea.innerHTML = "";
    contentArea.appendChild(textarea);

    // 3. 버튼 영역 변경 (수정 전용 등록/취소 버튼)
    const buttonArea = commentLi.querySelector(".comment-btn-area");
    buttonArea.innerHTML = `
        <button class="update-submit" onclick="updateComment(${commentNo}, this)">등록</button>
        <button class="update-cancel" onclick="location.reload()">취소</button>
    `;
};

// ===================== 댓글 수정 실행 =======================
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

    fetch("/editBoard/comment", { 
        method : "PUT",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(data)
    })
    .then(resp => resp.json())
    .then(result => {
        if(result.success || result > 0) {
            alert("수정되었습니다.");
            location.reload();
        } else alert("수정 실패");
    });
};

// 답글/수정 취소
window.insertCancel = (btn) => {
    btn.parentElement.previousElementSibling.remove();
    btn.parentElement.remove();
};
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

document.querySelector("#boardLike").addEventListener("click", e => {
   
    const heartIcon = e.target; 
    const currentId = e.currentTarget.dataset.boardId || boardId;
    

  // 로그인 상태가 아닌 경우 동작 X
  if (loginMemberNo == null) {
    alert("로그인 후 이용해주세요.");
    return;
  }

  const obj = {
    "memberNo": loginMemberNo,
    "boardId": currentId,
    "likeCheck": likeCheck
  };

  // 좋아요 INSERT/DELETE 비동기 요청
  fetch("/board/like", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(obj)
  })
    .then(resp => resp.text())
    .then(count => {

        if (count == -1) {
            console.log("좋아요 처리 실패");
            return;
        }
      // 클릭 이벤트 성공(.then) 내부 로직 예시
        
        const heartIcon = document.getElementById("boardLike");
        
         if (likeCheck == 0) { // 비어있던 상태였다면
            heartIcon.classList.replace('fa-regular', 'fa-solid');
            likeCheck = 1; // 상태 변경
        } else { // 채워져있던 상태였다면
            heartIcon.classList.replace('fa-solid', 'fa-regular');
            likeCheck = 0; // 상태 변경
        }
        
        // 좋아요 카운트 숫자 업데이트
        document.getElementById("likeCount").innerText;
    

      // 5. likeCheck 값 0 <-> 1 변환
      // -> 클릭 될 때 마다 INSERT/DELETE 동작을 번갈아 가면서 할 수 있게끔
      likeCheck = likeCheck == 0 ? 1 : 0;

      // 6. 하트를 채우기/비우기 바꾸기
      heartIcon.classList.toggle("fa-regular");
      heartIcon.classList.toggle("fa-solid");

      // 7. 게시글 좋아요 수 수정
      heartIcon.nextElementSibling.innerText = count;

    });

});