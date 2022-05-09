package com.wuyiling.worktest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

//在对应的Mapper上继承基本的类baseMapper
@Mapper
public interface UserMapper extends BaseMapper<User> {
    //所有的CRUD已经编写完成
    //不需要像以前的配置一些xml

}

