package com.xu.schedule.service.impl;

import com.xu.schedule.mapper.ScheduleRoleMapper;
import com.xu.schedule.service.ScheduleAccessService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleAccessServiceImpl implements ScheduleAccessService {

    private final ScheduleRoleMapper scheduleRoleMapper;

    public ScheduleAccessServiceImpl(ScheduleRoleMapper scheduleRoleMapper) {
        this.scheduleRoleMapper = scheduleRoleMapper;
    }

    @Override
    public boolean isAdmin(String account) {
        return account != null && scheduleRoleMapper.countAdminRole(account) > 0;
    }

    @Override
    public void requireAdmin(String account) {
        if (!isAdmin(account)) {
            throw new RuntimeException("无权限执行该操作");
        }
    }
}
