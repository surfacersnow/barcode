package com.ruisoft.core.key.controller;

import com.ruisoft.base.controller.BaseController;
import com.ruisoft.core.key.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: LFC
 * Date: 2015/6/29
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/key/")
public class KeyController  extends BaseController {

    @Autowired
    public KeyGenerator keyGenerator;

    /**
     * 根据ID规则获取系统中的ID取值
     */
    @RequestMapping("getkey")
      public void getkey() {
        try {
            String keyid = request.getReader().readLine();
            System.out.println("取得key值");
            String value= keyGenerator.getKeyByRule(keyid);
            response(value);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 后台获取UUID
     */
    @RequestMapping("getuuid")
    public void getuuid() {
        try {
            String value= keyGenerator.getUUID();
            response(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 后台获取32位UUID
     */
    @RequestMapping("getuu32id")
    public void getuu32id() {
        try {
            String value= keyGenerator.get32UUID();
            response(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取固定规则的主键ID
     */
    @RequestMapping("getPKUUID")
    public void getPKUUID() {
        try {
            String value= keyGenerator.getPKUUID();
            response(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
