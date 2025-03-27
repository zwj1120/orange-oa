package com.xcr.orange.oa.controller;

import com.xcr.orange.oa.service.OaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * @Author xiaochaorou7
 * @Description
 * @Date 2024/5/31
 */
@RestController
@RequestMapping(value = "/oa")
public class QaController {


    @Autowired
    private OaService oaService;


    /**
     * 测试
     * @return
     */
    @ResponseBody
    @GetMapping("/test")

    public String test() {
        return oaService.test();
    }


}
