package com.xu.blog.controller.sys;

import com.xu.common.annotation.RequireRole;
import com.xu.blog.dao.SysUserDao;
import com.xu.blog.param.po.sys.LoginUserPo;
import com.xu.blog.param.po.sys.UserInfoPo;
import com.xu.blog.param.vo.sys.LoginLocationStatsVO;
import com.xu.blog.param.vo.sys.UserLoginVO;
import com.xu.blog.service.SysUserInfoService;
import com.xu.blog.service.SysUserService;
import com.xu.common.utils.SessionUtil;
import com.xu.common.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RequestMapping("/blog/sys")
@RestController
public class SysController {

    private final SysUserService sysUserService;
    private final SysUserInfoService sysUserInfoService;
    private final SysUserDao sysUserDao;

    public SysController(SysUserService sysUserService, SysUserInfoService sysUserInfoService, SysUserDao sysUserDao) {
        this.sysUserService = sysUserService;
        this.sysUserInfoService = sysUserInfoService;
        this.sysUserDao = sysUserDao;
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginUserPo po, HttpServletRequest servletRequest){
        return sysUserService.login(po,servletRequest);
    }

    @PostMapping("/register")
    public Response register(@RequestBody LoginUserPo po){
        return sysUserService.register(po);
    }

    @GetMapping("/getUserInfoByAccount")
    public Response getUserInfoByAccount(@RequestParam(value = "account",required = false) String account){
        if (StringUtils.isBlank( account)){
            account = SessionUtil.getCurrentAccount();
        }
        return sysUserInfoService.getUserInfoByAccount(account);
    }

    @PostMapping("/updateUserInfo")
    public Response updateUserInfo(@RequestBody UserInfoPo po){
        return sysUserInfoService.updateUserInfo(po);
    }

    /**
     * 获取用户列表
     */
    @GetMapping("/getUserList")
    @RequireRole("ADMIN")
    public Response getUserList(){
        return sysUserInfoService.getUserList();
    }

    /**
     * 查询用户登录记录（按账号和IP分组，分页）
     * @param account 账号（可选，不传则查询所有账号）
     * @param page 页码，默认1
     * @param size 每页大小，默认5
     * @return 分页登录记录
     */
    @GetMapping("/getLoginRecords")
    @RequireRole("ADMIN")
    public Response getLoginRecords(@RequestParam(value = "account", required = false) String account,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "5") int size){
         Map<String, Object> result = sysUserDao.getLoginRecords(account, page, size);
         return Response.success(result);
    }

    /**
     * 获取登录地点统计信息（地图标点 + 省份饼形图）
     */
    @GetMapping("/getLoginLocationStats")
    @RequireRole("ADMIN")
    public Response getLoginLocationStats(@RequestParam(value = "account", required = false) String account){
         LoginLocationStatsVO stats = sysUserDao.getLoginLocationStats(account);
         return Response.success(stats);
    }
}
