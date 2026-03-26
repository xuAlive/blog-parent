package com.xu.blog.controller;

import com.xu.blog.domain.Miniapp;
import com.xu.blog.service.MiniappService;
import com.xu.common.param.IdPO;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序Controller
 */
@RequestMapping("/blog/miniapp")
@RestController
public class MiniappController {

    private final MiniappService miniappService;

    public MiniappController(MiniappService miniappService) {
        this.miniappService = miniappService;
    }

    /**
     * 获取小程序列表
     */
    @GetMapping("/list")
    public Response getList() {
        List<Miniapp> list = miniappService.getValidList();
        return Response.success(list);
    }

    @GetMapping("/manage/list")
    public Response getManageList() {
        return Response.success(miniappService.getManageList());
    }

    @PostMapping("/manage/offline")
    public Response offline(@RequestBody IdPO po) {
        return Response.checkResult(miniappService.offline(po.getId().intValue()));
    }

    @PostMapping("/manage/online")
    public Response online(@RequestBody IdPO po) {
        return Response.checkResult(miniappService.online(po.getId().intValue()));
    }
}
