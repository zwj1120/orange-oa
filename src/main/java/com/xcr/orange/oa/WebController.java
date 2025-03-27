package com.xcr.orange.oa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/welcome")
public class WebController {

    /**
     * 主页
     *
     * @return 路径信息
     */
    @RequestMapping
    public ModelAndView index() {
        return new ModelAndView("main/home");
    }

}
