package zinjvi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zinchenko on 30.11.14.
 */
@Controller
@RequestMapping("/simple")
public class SimpleController {

    public SimpleController() {
        System.out.println("c");
    }

    @RequestMapping("/show")
    public String show() {
        return "show";
    }

}
