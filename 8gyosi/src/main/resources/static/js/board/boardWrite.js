/* ===================== Summernote ===================== */
$(document).ready(function () {
    $('#boardContent').summernote({
        height: 400,
        lang: 'ko-KR',
        callbacks: {
            onImageUpload: function (files) {
                for (let file of files) {
                    uploadEditorFile(file);
                }
            }
        }
    });
});

/* Summernote 이미지 업로드 */
function uploadEditorFile(file) {
    const formData = new FormData();
    formData.append("file", file);

    $.ajax({
        url: "/editBoard/uploadFile",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function (res) {
            if (res.error) {
                alert("로그인이 필요합니다.");
                return;
            }
            $('#boardContent').summernote('insertImage', res.url);
        },
        error: () => alert("이미지 업로드 실패")
    });
}

/* ===================== 이미지 (멀티 + 누적) ===================== */
const imgInput = document.getElementById("img-input");
const imgPreviewArea = document.getElementById("image-preview-area");
const dtImages = new DataTransfer();

imgInput.addEventListener("change", () => {
    Array.from(imgInput.files).forEach(file => {
        if (!file.type.startsWith("image/")) return;

        const exists = Array.from(dtImages.files)
            .some(f => f.name === file.name && f.size === file.size);

        if (!exists) dtImages.items.add(file);
    });

    renderImagePreview();
});

function renderImagePreview() {
    imgPreviewArea.innerHTML = "";

    Array.from(dtImages.files).forEach((file, idx) => {
        const reader = new FileReader();
        reader.onload = e => {
            imgPreviewArea.insertAdjacentHTML("beforeend", `
                <div class="img-box">
                    <img src="${e.target.result}" class="preview">
                    <button type="button" class="remove-img" data-idx="${idx}">×</button>
                </div>
            `);
        };
        reader.readAsDataURL(file);
    });
}

imgPreviewArea.addEventListener("click", e => {
    if (!e.target.classList.contains("remove-img")) return;

    const removeIdx = Number(e.target.dataset.idx);
    const newDt = new DataTransfer();

    Array.from(dtImages.files).forEach((file, idx) => {
        if (idx !== removeIdx) newDt.items.add(file);
    });

    dtImages.items.clear();
    Array.from(newDt.files).forEach(f => dtImages.items.add(f));

    renderImagePreview();
});

/* ===================== 일반 파일 (멀티 + 누적) ===================== */
const fileInput = document.getElementById("file-input");
const filePreviewArea = document.getElementById("file-preview-area");
const dtFiles = new DataTransfer();

fileInput.addEventListener("change", () => {
    Array.from(fileInput.files).forEach(file => {
        const exists = Array.from(dtFiles.files)
            .some(f => f.name === file.name && f.size === file.size);

        if (!exists) dtFiles.items.add(file);
    });

    renderFilePreview();
});

function renderFilePreview() {
    filePreviewArea.innerHTML = "";

    Array.from(dtFiles.files).forEach((file, idx) => {
        filePreviewArea.insertAdjacentHTML("beforeend", `
            <div class="file-item">
                <span>${file.name}</span>
                <button type="button" class="remove-file" data-idx="${idx}">삭제</button>
            </div>
        `);
    });
}

filePreviewArea.addEventListener("click", e => {
    if (!e.target.classList.contains("remove-file")) return;

    const removeIdx = Number(e.target.dataset.idx);
    const newDt = new DataTransfer();

    Array.from(dtFiles.files).forEach((file, idx) => {
        if (idx !== removeIdx) newDt.items.add(file);
    });

    dtFiles.items.clear();
    Array.from(newDt.files).forEach(f => dtFiles.items.add(f));

    renderFilePreview();
});

/* ===================== ★ 핵심: submit 가로채기 ===================== */
const boardForm = document.getElementById("boardForm");

boardForm.addEventListener("submit", e => {
    e.preventDefault(); // 🔥 브라우저 기본 submit 차단

    const formData = new FormData(boardForm);

    // ⚠️ 기존 input files 제거 (덮어쓰기 방지)
    formData.delete("images");
    formData.delete("files");

    // 🔥 누적된 파일 기준으로 다시 append
    Array.from(dtImages.files).forEach(img => {
        formData.append("images", img);
    });

    Array.from(dtFiles.files).forEach(file => {
        formData.append("files", file);
    });

    fetch(boardForm.action, {
        method: "POST",
        body: formData
    })
    .then(res => {
        if (res.redirected) {
            location.href = res.url;
        } else {
            alert("게시글 등록 실패");
        }
    })
    .catch(err => {
        console.error(err);
        alert("서버 오류");
    });
});
