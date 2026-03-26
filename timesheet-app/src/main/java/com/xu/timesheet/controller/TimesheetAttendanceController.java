package com.xu.timesheet.controller;

import com.xu.common.response.Response;
import com.xu.common.utils.SessionUtil;
import com.xu.timesheet.domain.TimesheetAttendanceRecord;
import com.xu.timesheet.domain.TimesheetMakeupRequest;
import com.xu.timesheet.param.ApproveMakeupRequest;
import com.xu.timesheet.param.AttendanceSignRequest;
import com.xu.timesheet.param.MakeupRequestCreate;
import com.xu.timesheet.service.TimesheetAttendanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 签到记工接口
 */
@RestController
@RequestMapping("/timesheet/attendance")
public class TimesheetAttendanceController {

    private final TimesheetAttendanceService attendanceService;

    public TimesheetAttendanceController(TimesheetAttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * 员工签到或离班签到
     */
    @PostMapping("/sign")
    public Response<TimesheetAttendanceRecord> sign(@RequestBody AttendanceSignRequest request) {
        return Response.success(attendanceService.sign(request, currentAccount()));
    }

    /**
     * 提交补签申请
     */
    @PostMapping("/makeup/create")
    public Response<?> createMakeup(@RequestBody MakeupRequestCreate request) {
        return Response.checkResult(attendanceService.createMakeupRequest(request, currentAccount(), currentUserName()));
    }

    /**
     * 审批补签申请
     */
    @PostMapping("/makeup/approve")
    public Response<?> approveMakeup(@RequestBody ApproveMakeupRequest request) {
        return Response.checkResult(attendanceService.approveMakeup(request, currentAccount(), currentUserName()));
    }

    /**
     * 查询项目签到记录
     */
    @GetMapping("/list")
    public Response<List<TimesheetAttendanceRecord>> list(@RequestParam("projectId") Long projectId) {
        return Response.success(attendanceService.listAttendance(projectId, currentAccount()));
    }

    /**
     * 查询补签申请列表
     */
    @GetMapping("/makeup/list")
    public Response<List<TimesheetMakeupRequest>> makeupList(@RequestParam("projectId") Long projectId) {
        return Response.success(attendanceService.listMakeupRequests(projectId, currentAccount()));
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
