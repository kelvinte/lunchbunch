package sg.okayfoods.lunchbunch.infrastracture.adapter.incoming;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LunchPlanController {

    @GetMapping("/test")
    public String test(){
        return "asdf";
    }
}
