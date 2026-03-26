package com.xu.timesheet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.timesheet.domain.TimesheetMakeupRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 补签申请 Mapper
 */
@Mapper
public interface TimesheetMakeupRequestMapper extends BaseMapper<TimesheetMakeupRequest> {
}
