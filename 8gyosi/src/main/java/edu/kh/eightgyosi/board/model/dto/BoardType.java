import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시판 타입 DTO
 * - board_type 테이블과 매핑
 * - 게시판 분류(자유, 공지 등) 관리용
 */
@Data
@NoArgsConstructor   // MyBatis 기본 생성자
@AllArgsConstructor
public class BoardType {

    /** 게시판 타입 번호 (PK) */
    private int boardTypeNo;

    /** 게시판 타입 이름 */
    private String boardTypeName;

    /** 게시글 번호 (FK) */
    private int boardId;
}
