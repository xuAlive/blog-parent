package com.xu.schedule.service;

public interface ScheduleAccessService {

    boolean isAdmin(String account);

    void requireAdmin(String account);
}
