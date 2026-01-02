
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.semiproject.board.model.service.MainService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/")
    public String main(Model model) {

        model.addAttribute("freeList",
            mainService.getPopularBoard("FREE", 5));

        model.addAttribute("qnaList",
            mainService.getPopularBoard("QNA", 5));

        model.addAttribute("noticeList",
            mainService.getPopularBoard("NOTICE", 5));

        model.addAttribute("mealList",
            mainService.getPopularBoard("MEAL", 5));

        model.addAttribute("solveList",
            mainService.getPopularBoard("SOLVE", 5));

        model.addAttribute("admissionList",
            mainService.getPopularBoard("ADMISSION", 5));

        return "main";
    }
}