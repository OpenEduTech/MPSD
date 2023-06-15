package com.example.poem.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@MapperScan("com.fii.gxback.spm.mapping.mapper")
//@MapperScan({"com.fii.cec.mpmapping.mapper"})
//@MapperScan({"com.fii.cec.*.mapper"})
public class MyBatisPlusConfig {



    /**
     * 分页插件
     *
     * @return 分页插件的实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//乐观锁插件

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//分页插件

        return interceptor;
    }


    /**
     *  获取SqlParser的表名查找类
     * @return
     */
    @Bean
    public TablesNamesFinder tablesNamesFinder(){
        return new TablesNamesFinder();
    }



}
