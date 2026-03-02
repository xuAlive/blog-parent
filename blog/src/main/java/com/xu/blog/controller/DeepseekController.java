package com.xu.blog.controller;

import com.xu.blog.ai.deepseek.DSMamage;
import com.xu.blog.param.po.deepseek.DialogueInfoPO;
import com.xu.blog.service.DeepseekDialogueInfoService;
import com.xu.common.param.IdPO;
import com.xu.common.utils.SessionUtil;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blog/ds")
@RestController
public class DeepseekController {

    private final DSMamage dSMamage;
    private final DeepseekDialogueInfoService deepseekDialogueInfoService;

    public DeepseekController(DSMamage dSMamage, DeepseekDialogueInfoService deepseekDialogueInfoService) {
        this.dSMamage = dSMamage;
        this.deepseekDialogueInfoService = deepseekDialogueInfoService;
    }

    @PostMapping("/sendCompletion")
    public Response sendCompletion(@RequestBody DialogueInfoPO po){
        // 从ThreadLocal获取当前登录用户账号，无需传递HttpServletRequest
        String account = SessionUtil.getCurrentAccount();
        po.setAccount(account);
        return Response.success(dSMamage.sendCompletion(po));
    }

    @GetMapping("/getCompletionList")
    public Response getCompletion(@RequestParam("dialogueId") Long dialogueId){
        // 从ThreadLocal获取当前登录用户账号，无需传递HttpServletRequest
        String account = SessionUtil.getCurrentAccount();
        return Response.success(deepseekDialogueInfoService.getDialogueInfo(dialogueId, account));
    }

    @GetMapping("/getCompletionHistoryList")
    public Response getCompletionHistoryList(){
        // 从ThreadLocal获取当前登录用户账号，无需传递HttpServletRequest
        String account = SessionUtil.getCurrentAccount();
        return Response.success(deepseekDialogueInfoService.getCompletionHistoryList(account));
    }

    @PostMapping("/deleteDialogue")
    public Response deleteDialogue(@RequestBody IdPO po){
        String account = SessionUtil.getCurrentAccount();
        boolean success = deepseekDialogueInfoService.deleteDialogue(po.getId(), account);
        if (success) {
            return Response.success("删除成功");
        } else {
            return Response.error("删除失败");
        }
    }

    @GetMapping("/getDialogueCount")
    public Response getDialogueCount(){
        String account = SessionUtil.getCurrentAccount();
        int count = deepseekDialogueInfoService.countDialogueByAccount(account);
        return Response.success(count);
    }

    @GetMapping("/checkAdmin")
    public Response checkAdmin(){
        String account = SessionUtil.getCurrentAccount();
        boolean isAdmin = deepseekDialogueInfoService.isAdmin(account);
        return Response.success(isAdmin);
    }
}
