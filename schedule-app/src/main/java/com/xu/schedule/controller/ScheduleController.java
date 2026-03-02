package com.xu.schedule.controller;

import com.xu.common.param.IdPO;
import com.xu.common.response.Response;
import com.xu.common.utils.SessionUtil;
import com.xu.schedule.domain.Schedule;
import com.xu.schedule.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 排班管理接口
 */
@RestController
@RequestMapping("/schedule/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 查询日期范围内的排班
     */
    @GetMapping("/list")
    public Response<List<Schedule>> getScheduleList(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Schedule> schedules = scheduleService.getSchedulesByDateRange(startDate, endDate);
        return Response.success(schedules);
    }

    /**
     * 查询当前用户的排班
     */
    @GetMapping("/my")
    public Response<List<Schedule>> getMySchedules(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        String account = SessionUtil.getCurrentAccount();
        List<Schedule> schedules = scheduleService.getSchedulesByAccount(account, startDate, endDate);
        return Response.success(schedules);
    }

    /**
     * 创建排班
     */
    @PostMapping("/create")
    public Response<?> createSchedule(@RequestBody Schedule schedule) {
        schedule.setCreateTime(LocalDateTime.now());
        schedule.setUpdateTime(LocalDateTime.now());
        schedule.setCreateBy(SessionUtil.getCurrentAccount());
        schedule.setIsDelete(0);
        boolean result = scheduleService.save(schedule);
        return Response.checkResult(result);
    }

    /**
     * 批量创建排班
     */
    @PostMapping("/batchCreate")
    public Response<?> batchCreateSchedules(@RequestBody List<Schedule> schedules) {
        String currentAccount = SessionUtil.getCurrentAccount();
        LocalDateTime now = LocalDateTime.now();
        schedules.forEach(s -> {
            s.setCreateTime(now);
            s.setUpdateTime(now);
            s.setCreateBy(currentAccount);
            s.setIsDelete(0);
        });
        boolean result = scheduleService.batchCreateSchedules(schedules);
        return Response.checkResult(result);
    }

    /**
     * 更新排班
     */
    @PostMapping("/update")
    public Response<?> updateSchedule(@RequestBody Schedule schedule) {
        schedule.setUpdateTime(LocalDateTime.now());
        boolean result = scheduleService.updateById(schedule);
        return Response.checkResult(result);
    }

    /**
     * 删除排班（软删除）
     */
    @PostMapping("/delete")
    public Response<?> deleteSchedule(@RequestBody IdPO po) {
        Long id = po.getId();
        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setIsDelete(1);
        schedule.setUpdateTime(LocalDateTime.now());
        boolean result = scheduleService.updateById(schedule);
        return Response.checkResult(result);
    }
}
