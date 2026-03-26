package com.xu.schedule.controller;

import com.xu.common.param.IdPO;
import com.xu.common.response.Response;
import com.xu.common.utils.SessionUtil;
import com.xu.schedule.domain.Shift;
import com.xu.schedule.service.ScheduleAccessService;
import com.xu.schedule.service.ShiftService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 班次管理接口
 */
@RestController
@RequestMapping("/schedule/shift")
public class ShiftController {

    private final ShiftService shiftService;
    private final ScheduleAccessService scheduleAccessService;

    public ShiftController(ShiftService shiftService, ScheduleAccessService scheduleAccessService) {
        this.shiftService = shiftService;
        this.scheduleAccessService = scheduleAccessService;
    }

    /**
     * 获取所有启用的班次
     */
    @GetMapping("/list")
    public Response<List<Shift>> getShiftList() {
        List<Shift> shifts = shiftService.getActiveShifts();
        return Response.success(shifts);
    }

    /**
     * 获取所有班次（包括禁用）
     */
    @GetMapping("/listAll")
    public Response<List<Shift>> getAllShifts() {
        scheduleAccessService.requireAdmin(SessionUtil.getCurrentAccount());
        List<Shift> shifts = shiftService.list();
        return Response.success(shifts);
    }

    /**
     * 创建班次
     */
    @PostMapping("/create")
    public Response<?> createShift(@RequestBody Shift shift) {
        scheduleAccessService.requireAdmin(SessionUtil.getCurrentAccount());
        shift.setCreateTime(LocalDateTime.now());
        shift.setUpdateTime(LocalDateTime.now());
        shift.setIsDelete(0);
        if (shift.getStatus() == null) {
            shift.setStatus(1);
        }
        boolean result = shiftService.save(shift);
        return Response.checkResult(result);
    }

    /**
     * 更新班次
     */
    @PostMapping("/update")
    public Response<?> updateShift(@RequestBody Shift shift) {
        scheduleAccessService.requireAdmin(SessionUtil.getCurrentAccount());
        shift.setUpdateTime(LocalDateTime.now());
        boolean result = shiftService.updateById(shift);
        return Response.checkResult(result);
    }

    /**
     * 删除班次（软删除）
     */
    @PostMapping("/delete")
    public Response<?> deleteShift(@RequestBody IdPO po) {
        scheduleAccessService.requireAdmin(SessionUtil.getCurrentAccount());
        Long id = po.getId();
        Shift shift = new Shift();
        shift.setId(id);
        shift.setIsDelete(1);
        shift.setUpdateTime(LocalDateTime.now());
        boolean result = shiftService.updateById(shift);
        return Response.checkResult(result);
    }
}
