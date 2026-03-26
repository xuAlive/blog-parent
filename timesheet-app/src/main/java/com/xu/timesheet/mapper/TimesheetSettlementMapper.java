package com.xu.timesheet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.timesheet.domain.TimesheetSettlement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 结算单 Mapper
 */
@Mapper
public interface TimesheetSettlementMapper extends BaseMapper<TimesheetSettlement> {
}
