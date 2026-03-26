package com.xu.timesheet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.timesheet.domain.TimesheetSettlementItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 结算明细 Mapper
 */
@Mapper
public interface TimesheetSettlementItemMapper extends BaseMapper<TimesheetSettlementItem> {
}
