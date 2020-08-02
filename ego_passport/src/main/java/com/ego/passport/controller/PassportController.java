package com.ego.passport.controller;

import com.ego.commons.pojo.EgoResult;
import com.ego.passport.service.PassportService;
import com.ego.pojo.TbUser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class PassportController {

    @Autowired
    private PassportService passportService;

    /**
     * 显示登陆页面
     * @return
     */
    @RequestMapping("/user/showLogin")
    public String showLogin(@RequestHeader(name = "Referer",required = false) String referer, Model model){
        if(Strings.isNotEmpty(referer)){
            if(!referer.endsWith("/user/showRegister")){
                model.addAttribute("redirect",referer);
            }
        }
        return "login";
    }

    /**
     * 显示注册页面
     * @return
     */
    @RequestMapping("/user/showRegister")
    public String showRegister(){
        return "register";
    }

    /**
     * 通过用户名，邮箱，手机号检查用户是否存在
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public EgoResult check(@PathVariable String param,@PathVariable int type){
        TbUser tbUser = new TbUser();

        if(type==1){
            tbUser.setUsername(param);
        }else if (type==2){
            tbUser.setPhone(param);
        }else if(type==3){
            tbUser.setEmail(param);
        }

        return passportService.check(tbUser);
    }

    /**
     * 注册
     * @param tbUser
     * @param pwdRepeat
     * @return
     */
    @RequestMapping("/user/register")
    @ResponseBody
    public EgoResult insert(TbUser tbUser,String pwdRepeat){

        // 用户名只能包含字母和数字，且6-12位
        if (Strings.isEmpty(tbUser.getUsername())||!tbUser.getUsername().matches("^[a-zA-Z0-9]{6,12}$")){
            return EgoResult.error("用户名必须是6-12位，且只能是数字和字母");
        }
        if(Strings.isEmpty(tbUser.getEmail())||!tbUser.getEmail().matches("^[a-z\\d]+(\\.[a-z\\d]+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$")){
            return EgoResult.error("邮箱格式不正确");
        }
        if(Strings.isEmpty(tbUser.getPassword())||!tbUser.getPassword().matches("^[a-zA-Z0-9]{6,12}$")){
            return EgoResult.error("密码必须是6-12位，且只能是数字和字母");
        }
        if(Strings.isEmpty(pwdRepeat)||!pwdRepeat.equals(tbUser.getPassword())){
            return EgoResult.error("密码不一致");
        }
        if(Strings.isEmpty(tbUser.getPhone())||!tbUser.getPhone().matches("^1\\d{10}$")){
            return EgoResult.error("手机号不正确");
        }

        return passportService.insert(tbUser);
    }

    /**
     * 用户登陆
     * @param tbUser
     * @param session
     * @return
     */
    @RequestMapping("/user/login")
    @ResponseBody
    public EgoResult login(TbUser tbUser,HttpSession session){
        EgoResult egoResult = passportService.login(tbUser);
        if (egoResult.getStatus()==200) {
            session.setAttribute("loginUser",egoResult.getData());
            egoResult.setData(null);
        }
        return egoResult;
    }

    /**
     * 在session中获取用户信息
     * @param session
     * @return
     */
    @RequestMapping("/user/token/{token}")
    @ResponseBody
    //异步请求，跨域请求，需要携带cookie时要设置 allowCredentials = "true"，表示允许接收Cookie数据
    @CrossOrigin(allowCredentials = "true")
    public EgoResult token(HttpSession session){
        Object obj = session.getAttribute("loginUser");
        if (obj!=null){
            TbUser tbUser = (TbUser)obj;
            tbUser.setPassword(null);
            return EgoResult.ok(tbUser);
        }
        return EgoResult.error("获取用户信息失败");
    }

    /**
     * 用户退出
     * @param session
     * @return
     */
    @RequestMapping("/user/logout/{token}")
    @ResponseBody
    //异步请求，跨域请求，需要携带cookie时要设置 allowCredentials = "true"，表示允许接收Cookie数据
    @CrossOrigin(allowCredentials = "true")
    public EgoResult logout(HttpSession session){
        session.invalidate();
        return EgoResult.ok();
    }
}
