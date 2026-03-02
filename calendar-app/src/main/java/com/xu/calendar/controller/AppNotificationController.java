package com.xu.calendar.controller;

import com.xu.calendar.domain.AppNotification;
import com.xu.calendar.service.AppNotificationService;
import com.xu.common.param.IdPO;
import com.xu.common.response.Response;
import com.xu.common.utils.SessionUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用内通知接口
 */
@RestController
@RequestMapping("/calendar/app-notification")
public class AppNotificationController {

    private final AppNotificationService appNotificationService;

    public AppNotificationController(AppNotificationService appNotificationService) {
        this.appNotificationService = appNotificationService;
    }

    /**
     * 获取待显示的通知
     */
    @GetMapping("/pending")
    public Response<List<AppNotification>> getPendingNotifications() {
        String account = SessionUtil.getCurrentAccount();
        List<AppNotification> notifications = appNotificationService.getPendingNotifications(account);
        return Response.success(notifications);
    }

    /**
     * 标记为已读
     */
    @PostMapping("/read")
    public Response<?> markAsRead(@RequestBody IdPO po) {
        boolean result = appNotificationService.markAsRead(po.getId());
        return Response.checkResult(result);
    }

    /**
     * 标记全部已读
     */
    @PostMapping("/readAll")
    public Response<?> markAllAsRead() {
        String account = SessionUtil.getCurrentAccount();
        boolean result = appNotificationService.markAllAsRead(account);
        return Response.checkResult(result);
    }
}
