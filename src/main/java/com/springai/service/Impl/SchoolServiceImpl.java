package com.springai.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springai.entity.po.School;
import com.springai.mapper.SchoolMapper;
import com.springai.service.ISchoolService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 校区表 服务实现类
 * </p>
 *
 * @author huge
 * @since 2025-03-08
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {

}
