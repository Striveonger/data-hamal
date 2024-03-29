package com.striveonger.study.auth.web.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Dict;
import com.mybatisflex.core.query.QueryWrapper;
import com.striveonger.study.api.leaf.IDGenRemoteService;
import com.striveonger.study.api.leaf.core.ID;
import com.striveonger.study.api.leaf.core.Status;
import com.striveonger.study.auth.entity.Users;
import com.striveonger.study.auth.service.IUsersService;
import com.striveonger.study.auth.web.vo.UserRegisterVo;
import com.striveonger.study.core.constant.ResultStatus;
import com.striveonger.study.core.exception.CustomException;
import com.striveonger.study.core.result.Result;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.striveonger.study.api.leaf.constant.Keys.AUTH_USER;

/**
 * @author Mr.Lee
 * @description:
 * @date 2022-11-03 23:16
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUsersService usersService;

    @Resource
    private PasswordEncoder encoder;

    @DubboReference //(version = "1.0.0")
    private IDGenRemoteService service;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(UserRegisterVo vo) {
        synchronized (vo.toString().intern()) {
            // 1. 检查用户名和邮箱是否已占用
            QueryWrapper wrapper = QueryWrapper.create().where(Users::getUsername).eq(vo.getUsername())
                                                        .or(Users::getEmail).eq(vo.getEmail());
            long count = usersService.count(wrapper);
            if (count > 0) {
                throw new CustomException(ResultStatus.FAIL, "注册用户失败");
            }
            // 2. 落库
            ID id; int retry = 3;
            do {
                id = service.acquireSerial(AUTH_USER.getKey());
            } while (retry-- > 0 && Status.exception(id));
            if (Status.exception(id)) {
                return Result.fail().message("User ID create failure");
            }

            Users user = new Users();
            user.setId(id.toString());
            user.setUsername(vo.getUsername());
            // 3. 密码加密存储
            user.setPassword(encoder.encode(vo.getPassword()));
            user.setEmail(vo.getEmail());
            user.setStatus(0);
            usersService.save(user);
        }
        return Result.success().message("用户注册成功");
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result login(String username, String password) {
        Users hold = usersService.getOne(QueryWrapper.create().where(Users::getUsername).eq(username));
        if (hold == null) {
            return Result.status(ResultStatus.NOT_FOUND).message("用户名错误");
        }
        String encode = encoder.encode(password);
        if (encode.equals(hold.getPassword())) {
            StpUtil.login(hold.getId());
            // 向Redis写入用户信息 TODO: 存入必要信息(JSON串)
            SaTokenInfo token = StpUtil.getTokenInfo();
            return Result.success().message("登录成功").data(Dict.create().set("token", token.getTokenValue()));
        }
        return Result.fail().message("密码错误");
    }


    /**
     * 用户登出
     * @return 登出结果
     */
    @GetMapping("/logout")
    public Result logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            // 删除Redis中的用户信息 TODO: remove key -> UserID
            return Result.success().message("登出成功");
        }
        return Result.fail().message("无效Token");
    }

}