package com.bench.repository.config;

import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;

/**
 * @className BenchMybatisPlusInsert
 * @autor cold
 * @DATE 2021/6/10 20:45
 **/
public class BenchMybatisPlusInsert extends Insert {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String valuesScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /** 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(getMethod(sqlMethod), tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        boolean ifTag;
        for (TableFieldInfo tableFieldInfo:  tableInfo.getFieldList()) {
            if("gmt_create".equals(tableFieldInfo.getColumn()) || "gmt_modified".equals(tableFieldInfo.getColumn())){
                /**
                 * <trim prefix="(" suffix=")" suffixOverrides=",">
                 * <if test="userId != null">user_id,</if>
                 * <if test="loginName != null">login_name,</if>
                 * <if test="loginPassword != null">login_password,</if>
                 * <if test="realName != null">real_name,</if>
                 * <if test="status != null">status,</if>
                 * <if test="email != null">email,</if>
                 * <if test="qq != null">qq,</if>
                 * can_login,
                 * <if test="certType != null">cert_type,</if>
                 * <if test="certNo != null">cert_no,</if>
                 * <if test="gmtCreate != null">gmt_create,</if>
                 * <if test="gmtModified != null">gmt_modified,</if>
                 * email_validate,
                 * qq_validate,
                 * <if test="cell != null">cell,</if>
                 * cell_validate,
                 * <if test="accountPassword != null">account_password,</if>
                 * <if test="nickName != null">nick_name,</if>
                 * <if test="userTypeName != null">user_type_name,</if>
                 * <if test="domain != null">domain,</if>
                 * <if test="subDomain != null">sub_domain,</if>
                 * <if test="gmtChangeIdentity != null">gmt_change_identity,</if>
                 * <if test="sex != null">sex,</if>
                 * <if test="birthday != null">birthday,</if>
                 * <if test="oneAuthId != null">one_auth_id,</if>
                 *
                 * add gmt_create
                 * </trim>
                 */
                StringBuilder columnsSb=new StringBuilder(columnScript);
                int insertGmtCreateColumnIndex =  columnsSb.lastIndexOf("</trim>");;
                columnsSb.insert(insertGmtCreateColumnIndex,""+tableFieldInfo.getColumn()+",");
                columnScript = columnsSb.toString();

                /**
                 * <trim prefix="(" suffix=")" suffixOverrides=",">
                 * <if test="userId != null">#{userId},</if>
                 * <if test="loginName != null">#{loginName},</if>
                 * <if test="loginPassword != null">#{loginPassword},</if>
                 * <if test="realName != null">#{realName},</if>
                 * <if test="status != null">#{status},</if>
                 * <if test="email != null">#{email},</if>
                 * <if test="qq != null">#{qq},</if>
                 * #{canLogin},
                 * <if test="certType != null">#{certType},</if>
                 * <if test="certNo != null">#{certNo},</if>
                 * <if test="gmtCreate != null">#{gmtCreate},</if>
                 * <if test="gmtModified != null">#{gmtModified},</if>
                 * #{emailValidate},
                 * #{qqValidate},
                 * <if test="cell != null">#{cell},</if>
                 * #{cellValidate},
                 * <if test="accountPassword != null">#{accountPassword},</if>
                 * <if test="nickName != null">#{nickName},</if>
                 * <if test="userTypeName != null">#{userTypeName},</if>
                 * <if test="domain != null">#{domain},</if>
                 * <if test="subDomain != null">#{subDomain},</if>
                 * <if test="gmtChangeIdentity != null">#{gmtChangeIdentity},</if>
                 * <if test="sex != null">#{sex},</if>
                 * <if test="birthday != null">#{birthday},</if>
                 * <if test="oneAuthId != null">#{oneAuthId},</if>
                 * </trim>
                 */
                StringBuilder valuesSb=new StringBuilder(valuesScript);

                int insertGmtValuesIndex =  valuesSb.lastIndexOf("</trim>");;
                valuesSb.insert(insertGmtValuesIndex,"now(),");
                valuesScript = valuesSb.toString();
            }
        }
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, getMethod(sqlMethod), sqlSource, keyGenerator, keyProperty, keyColumn);
    }
}
