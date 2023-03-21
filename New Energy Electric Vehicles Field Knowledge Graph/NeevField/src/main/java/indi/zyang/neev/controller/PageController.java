package indi.zyang.neev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tcm")
public class PageController {

    @RequestMapping("/home")
    public String toTcm(){
        return "tcm";
    }

    @RequestMapping("/info")
    public String toString() {
        return "info";
    }

    @RequestMapping("/us")
    public String toUs(){
        return "us";
    }

    @RequestMapping("/search")
    public String toSearch(){
        return "search";
    }

    @RequestMapping("/correction")
    public String toCorrection(){
        return "correction";
    }
}
