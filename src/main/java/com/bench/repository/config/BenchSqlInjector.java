package com.bench.repository.config;

import java.util.List;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.Insert;

/**
 * @className BenchSqlInjector
 * @autor cold
 * @DATE 2021/6/10 20:30
 **/
public class BenchSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        Insert insertOneMethod = null;
        for (AbstractMethod method : methodList){
            if(method instanceof Insert){
                 insertOneMethod = (Insert)method;
                 break;
            }
        }
        if (insertOneMethod != null) {
            methodList.remove(insertOneMethod);
        }
        BenchMybatisPlusInsert benchMybatisPlusInsert = new BenchMybatisPlusInsert();
        methodList.add(benchMybatisPlusInsert);
        //增加自定义方法

        return methodList;
    }
}
