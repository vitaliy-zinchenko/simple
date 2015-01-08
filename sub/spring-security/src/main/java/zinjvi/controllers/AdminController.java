package zinjvi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zinchenko on 30.11.14.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/main")
    public String main() {
        return "admin/main";
    }

    @ResponseBody
    @RequestMapping(value = "/resource", method = RequestMethod.GET)
    public String resourceGet() {
        return "get resource";
    }

    @ResponseBody
    @RequestMapping(value = "/resource", method = RequestMethod.POST)
    public String resourcePost() {
        return "post resource";
    }

}
