package com.bench.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @className SystemMapper
 * @autor cold
 * @DATE 2021-12-02 16:22
 **/
@Mapper
public interface SystemMapper {
    @Select("select now() from dual")
    Date getCurrentDate();
}
