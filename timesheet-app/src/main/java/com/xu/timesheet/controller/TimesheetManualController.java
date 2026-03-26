package com.xu.timesheet.controller;

import com.xu.common.response.Response;
import com.xu.common.utils.SessionUtil;
import com.xu.timesheet.domain.TimesheetManualWorklog;
import com.xu.timesheet.param.ManualWorklogSaveRequest;
import com.xu.timesheet.service.TimesheetManualService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 人工记工接口
 */
@RestController
@RequestMapping("/timesheet/manual")
public class TimesheetManualController {

    private final TimesheetManualService manualService;

    public TimesheetManualController(TimesheetManualService manualService) {
        this.manualService = manualService;
    }

    /**
     * 保存人工记工明细
     */
    @PostMapping("/save")
    public Response<?> save(@RequestBody ManualWorklogSaveRequest request) {
        return Response.checkResult(manualService.saveWorklog(request, currentAccount(), currentUserName()));
    }

    /**
     * 查询人工记工列表
     */
    @GetMapping("/list")
    public Response<List<TimesheetManualWorklog>> list(@RequestParam("projectId") Long projectId) {
        return Response.success(manualService.listWorklog(projectId, currentAccount()));
    }

    /**
     * 获取当前登录账号
     */
    private String currentAccount() {
        String account = SessionUtil.getCurrentAccount();
        if (account == null || account.isBlank()) {
            throw new IllegalArgumentException("未登录");
        }
        return account;
    }

    /**
     * 获取当前登录用户名，缺省时回退到账号
     */
    private String currentUserName() {
        String userName = SessionUtil.getCurrentUserName();
        return userName == null || userName.isBlank() ? currentAccount() : userName;
    }
}
