package com.xu.timesheet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.timesheet.domain.TimesheetAttendanceRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到记录 Mapper
 */
@Mapper
public interface TimesheetAttendanceRecordMapper extends BaseMapper<TimesheetAttendanceRecord> {
}
