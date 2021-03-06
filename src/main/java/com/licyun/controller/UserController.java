package com.licyun.controller;

import com.licyun.model.User;
import com.licyun.service.UserService;
import com.licyun.util.UploadImg;
import com.licyun.util.Validate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by 李呈云
 * Description:
 * 2016/10/14.
 */
@Controller
public class UserController {

    //用户头像图片路径
    private final String IMGURL = "/WEB-INF/img/user";

    @Autowired
    private UploadImg uploadImg;

    @Autowired
    private Validate validate;

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param model
     * @return
     */
    @RequestMapping(value = {"/user/register"}, method = {RequestMethod.GET})
    public String register(Model model){
        model.addAttribute("user", new User());
        return "user/register";
    }
    @RequestMapping(value = {"/user/register"}, method = {RequestMethod.POST})
    public String register(@Valid User user, BindingResult result){
        //验证表单输入是否如何规则
        validate.registValidate(user, result);
        if(result.hasErrors()){
            return "user/register";
        }
        //添加用户
        userService.insertUser(user);
        return "redirect:/user/login";
    }

    /**
     * 用户登录
     * @param model
     * @param session   当session存在时跳转到首页
     * @return
     */
    @RequestMapping(value = {"/user/login"}, method = {RequestMethod.GET})
    public String login(Model model, HttpSession session){
        User sessionUser = (User)session.getAttribute("user");
        if(sessionUser != null){
            return "redirect:/user/index";
        }
        model.addAttribute("user", new User());
        return "user/login";
    }
    @RequestMapping(value = {"/user/login"}, method = {RequestMethod.POST})
    public String login(@Valid User user, BindingResult result,
                        Model model, HttpSession session){
        //验证输入格式
        validate.commonValidate(user, result);
        if(result.hasErrors()){
            return "user/login";
        }
        //shiro验证
        Subject subject = SecurityUtils.getSubject();
        //设置email 和password token
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPassword());
        try{
            //使用Shiro验证token
            subject.login(token);
            session.setAttribute("user", userService.findByEmail(user.getEmail()));
            model.addAttribute("user", userService.findByEmail(user.getEmail()));
            return "user/index";
        }catch (Exception e){
            model.addAttribute("error", "邮箱或密码错误");
            model.addAttribute("user", new User());
            return "admin/login";
        }
    }

    /**
     * 用户中心首页
     * @return
     */
    @RequestMapping(value = {"/user", "/user/index"}, method = {RequestMethod.GET})
    public String index(){
        return "user/index";
    }

    /**
     * 编辑用户
     * @param uid 用户id
     * @param model
     * @return
     */
    @RequestMapping(value = {"/user/edituser-{uid}"}, method = {RequestMethod.GET})
    public String editUser(@PathVariable int uid, Model model){
        model.addAttribute("user", userService.findByUserId(uid));
        return "user/edituser";
    }
    @RequestMapping(value = {"/user/edituser-{uid}"}, method = {RequestMethod.POST})
    public String editUser(@Valid User user, BindingResult result,
                           @PathVariable int uid){
        //判断表单数据是否正确
        validate.updateValidate(user, uid, result);
        if(result.hasErrors()){
            return "user/edituser";
        }
        //修改用户
        userService.updateUserById(user, uid);
        return "redirect:/user/index";
    }

    /**
     * 修改图片 get
     * @param uid
     * @param model
     * @return
     */
    @RequestMapping(value = {"/user/editimg-{uid}"}, method = {RequestMethod.GET})
    public String editImg(@PathVariable int uid, Model model){
        model.addAttribute("user", userService.findByUserId(uid));
        return "user/editimg";
    }
    /**
     * 修改图片 post
     * @param request
     * @param session
     * @param file    图片文件
     * @param model
     * @return
     */
    @RequestMapping(value = "/user/editimg-{uid}", method = RequestMethod.POST)
    public String uploadFileHandler(HttpServletRequest request, HttpSession session,
                                    @RequestParam("file") MultipartFile file, Model model) {
        User user = (User) session.getAttribute("user");
        // 上传目录
        String rootPath = request.getServletContext().getRealPath(IMGURL);
        // 上传文件并赋值给user
        user.setImgUrl( uploadImg.uploadUserImg(file, user, rootPath) );
        // 更新user的imgURl
        userService.updateUser(user);
        model.addAttribute("user", user);
        return "user/index";
    }
}
