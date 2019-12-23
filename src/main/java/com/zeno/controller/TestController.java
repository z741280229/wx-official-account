package com.zeno.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: wx-official-account
 * @description:
 * @author: Zeno
 * @create: 2019-12-23 10:08
 **/
@RestController
public class TestController {
    @GetMapping("test")
    public String test(String parameter){
        return parameter;
    }
}
