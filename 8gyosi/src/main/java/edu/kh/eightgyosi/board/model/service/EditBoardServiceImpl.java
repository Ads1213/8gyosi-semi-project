package edu.kh.eightgyosi.board.model.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.semiproject.board.model.dto.*;
import edu.kh.semiproject.board.model.mapper.EditBoardMapper;
import edu.kh.semiproject.common.config.FileConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EditBoardServiceImpl implements EditBoardService {

    private final EditBoardMapper mapper;
    private final FileConfig fileConfig;

    /** 게시글 작성 */
    @Override
    public int boardInsert(Board board, List<MultipartFile> images, List<MultipartFile> files) throws Exception {
        mapper.insertBoard(board);
        int boardNo = board.getBoardNo();

        if(images != null && !images.isEmpty()) saveBoardImages(boardNo, images);
        if(files != null && !files.isEmpty()) saveBoardFiles(boardNo, files);

        return boardNo;
    }

    /** 게시글 수정 */
    @Override
    public int boardUpdate(Board board, List<MultipartFile> images, List<MultipartFile> files,
                           List<Integer> deleteImageList, List<Integer> deleteFileList) throws Exception {

        String beforeContent = mapper.selectBoardContent(board.getBoardNo());
        int result = mapper.updateBoard(board);
        if(result == 0) return 0;
        String afterContent = board.getBoardContent();

        deleteRemovedImages(beforeContent, afterContent);

        if(deleteImageList != null){
            for(int imgNo : deleteImageList){
                BoardImage img = mapper.selectImageByOrder(Map.of("imgNo", imgNo));
                if(img != null){
                    deletePhysicalFile(fileConfig.getUploadImagePath() + img.getRenameName());
                    mapper.deleteBoardImage(img.getImgNo());
                }
            }
        }

        if(images != null && !images.isEmpty()) saveBoardImages(board.getBoardNo(), images);

        if(deleteFileList != null){
            for(int fileNo : deleteFileList){
                BoardFile bf = mapper.selectBoardFile(fileNo);
                if(bf != null){
                    deletePhysicalFile(fileConfig.getUploadFilePath() + bf.getUploadFileStrg());
                    mapper.deleteBoardFile(fileNo);
                }
            }
        }

        if(files != null && !files.isEmpty()) saveBoardFiles(board.getBoardNo(), files);

        return result;
    }

    /** 게시글 삭제 */
    @Override
    public int boardDelete(int boardNo, Member loginMember) throws Exception {
        Board board = mapper.selectOneBoard(boardNo);
        if(board == null) return 0;

        if(board.getMemberNo() != loginMember.getMemberNo() && !"ADMIN".equals(loginMember.getRole())){
            return 0;
        }

        deleteAllImages(board.getBoardContent());

        List<BoardImage> imgList = mapper.selectBoardImages(boardNo);
        for(BoardImage img : imgList){
            deletePhysicalFile(fileConfig.getUploadImagePath() + img.getRenameName());
            mapper.deleteBoardImage(img.getImgNo());
        }

        List<BoardFile> fileList = mapper.selectBoardFiles(boardNo);
        for(BoardFile f : fileList){
            deletePhysicalFile(fileConfig.getUploadFilePath() + f.getUploadFileStrg());
            mapper.deleteBoardFile(f.getFileNo());
        }

        mapper.deleteBoardComments(boardNo);

        return mapper.deleteBoard(Map.of("boardNo", boardNo));
    }

    /** 게시글 조회 */
    @Override
    public Board selectBoard(int boardNo){
        return mapper.selectOneBoard(boardNo);
    }

    /** 이미지 저장 */
    private void saveBoardImages(int boardNo, List<MultipartFile> images) throws Exception {
        int order = mapper.selectMaxImgOrder(boardNo) + 1;
        for(MultipartFile file : images){
            if(file.isEmpty()) continue;
            String origin = file.getOriginalFilename();
            String ext = origin.substring(origin.lastIndexOf("."));
            String rename = UUID.randomUUID().toString() + ext;

            File dir = new File(fileConfig.getUploadImagePath());
            if(!dir.exists()) dir.mkdirs();
            file.transferTo(new File(fileConfig.getUploadImagePath() + rename));

            BoardImage img = BoardImage.builder()
                    .boardNo(boardNo)
                    .imgOrder(order++)
                    .imgPath("/editor/" + rename)
                    .imgOriginName(origin)
                    .imgStoredName(rename)
                    .build();
            mapper.insertBoardImage(img);
        }
    }

    /** 파일 저장 */
    private void saveBoardFiles(int boardNo, List<MultipartFile> files) throws Exception {
        for(MultipartFile f : files){
            if(f.isEmpty()) continue;
            String origin = f.getOriginalFilename();
            String rename = UUID.randomUUID().toString() + origin.substring(origin.lastIndexOf("."));
            File dir = new File(fileConfig.getUploadFilePath());
            if(!dir.exists()) dir.mkdirs();
            f.transferTo(new File(fileConfig.getUploadFilePath() + rename));

            BoardFile bf = BoardFile.builder()
                    .boardNo(boardNo)
                    .fileOriginName(origin)
                    .fileStoredName(rename)
                    .fileSize(f.getSize())
                    .uploadDate(LocalDateTime.now())
                    .build();
            mapper.insertBoardFile(bf);
        }
    }

    /** 삭제된 이미지 추출 */
    private void deleteRemovedImages(String before, String after){
        List<String> beforeImgs = extractImgSrc(before);
        List<String> afterImgs = extractImgSrc(after);
        beforeImgs.removeAll(afterImgs);
        for(String src : beforeImgs) deletePhysicalFile(fileConfig.getUploadImagePath() + src);
    }

    /** 게시글 전체 이미지 삭제 */
    private void deleteAllImages(String content){
        List<String> imgs = extractImgSrc(content);
        for(String src : imgs) deletePhysicalFile(fileConfig.getUploadImagePath() + src);
    }

    /** HTML img src 추출 */
    private List<String> extractImgSrc(String content){
        List<String> list = new ArrayList<>();
        if(content == null) return list;
        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"']");
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) list.add(matcher.group(1));
        return list;
    }

    /** 실제 파일 삭제 */
    private void deletePhysicalFile(String fullPath){
        File file = new File(fullPath);
        if(file.exists()){
            boolean deleted = file.delete();
            log.info("파일 삭제: {} / {}", file.getPath(), deleted);
        }
    }

    /** 좋아요/싫어요 처리 */
    @Override
    public int toggleBoardLike(int boardNo, int memberNo, boolean isLike){
        BoardLike bl = mapper.selectBoardLike(boardNo, memberNo);
        if(bl != null){
            if((isLike && bl.isLikeFlg()) || (!isLike && !bl.isLikeFlg())){
                mapper.deleteBoardLike(bl.getLikeNo());
                return -1;
            } else {
                mapper.updateBoardLike(bl.getLikeNo(), isLike);
                return 1;
            }
        } else {
            mapper.insertBoardLike(BoardLike.builder()
                    .boardNo(boardNo)
                    .memberNo(memberNo)
                    .likeFlg(isLike)
                    .build());
            return 1;
        }
    }

    /** 댓글/대댓글 작성 */
    @Override
    public int insertComment(BoardComment comment){
        return mapper.insertBoardComment(comment);
    }

    /** 댓글/대댓글 삭제 */
    @Override
    public int deleteComment(int commentId, Member loginMember){
        BoardComment comment = mapper.selectBoardComment(commentId);
        if(comment == null) return 0;
        if(comment.getMemberNo() != loginMember.getMemberNo() && !"ADMIN".equals(loginMember.getRole())){
            return 0;
        }
        return mapper.deleteBoardComment(commentId);
    }

    /** Summernote 이미지/파일 업로드 */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String dirPath = file.getContentType().startsWith("image")? fileConfig.getUploadImagePath()
                                                                 : fileConfig.getUploadFilePath();
        File dir = new File(dirPath);
        if(!dir.exists()) dir.mkdirs();

        String origin = file.getOriginalFilename();
        String rename = UUID.randomUUID().toString() + origin.substring(origin.lastIndexOf("."));
        file.transferTo(new File(dirPath + rename));

        return (dirPath.equals(fileConfig.getUploadImagePath())? "/editor/" : "/files/") + rename;
    }
}
