package com.springai.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springai.entity.po.Course;
import com.springai.mapper.CourseMapper;
import com.springai.service.ICourseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学科表 服务实现类
 * </p>
 *
 * @author huge
 * @since 2025-03-08
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

}
