package com.xu.timesheet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.timesheet.domain.TimesheetManualWorklog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人工记工 Mapper
 */
@Mapper
public interface TimesheetManualWorklogMapper extends BaseMapper<TimesheetManualWorklog> {
}
