package com.ziroom.tech.demeterapi.dao.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
public class DemeterCoreDataExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DemeterCoreDataExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
protected abstract static class GeneratedCriteria {

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCoreNameIsNull() {
            addCriterion("core_name is null");
            return (Criteria) this;
        }

        public Criteria andCoreNameIsNotNull() {
            addCriterion("core_name is not null");
            return (Criteria) this;
        }

        public Criteria andCoreNameEqualTo(String value) {
            addCriterion("core_name =", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameNotEqualTo(String value) {
            addCriterion("core_name <>", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameGreaterThan(String value) {
            addCriterion("core_name >", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameGreaterThanOrEqualTo(String value) {
            addCriterion("core_name >=", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameLessThan(String value) {
            addCriterion("core_name <", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameLessThanOrEqualTo(String value) {
            addCriterion("core_name <=", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameLike(String value) {
            addCriterion("core_name like", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameNotLike(String value) {
            addCriterion("core_name not like", value, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameIn(List<String> values) {
            addCriterion("core_name in", values, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameNotIn(List<String> values) {
            addCriterion("core_name not in", values, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameBetween(String value1, String value2) {
            addCriterion("core_name between", value1, value2, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreNameNotBetween(String value1, String value2) {
            addCriterion("core_name not between", value1, value2, "coreName");
            return (Criteria) this;
        }

        public Criteria andCoreDataIsNull() {
            addCriterion("core_data is null");
            return (Criteria) this;
        }

        public Criteria andCoreDataIsNotNull() {
            addCriterion("core_data is not null");
            return (Criteria) this;
        }

        public Criteria andCoreDataEqualTo(BigDecimal value) {
            addCriterion("core_data =", value, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataNotEqualTo(BigDecimal value) {
            addCriterion("core_data <>", value, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataGreaterThan(BigDecimal value) {
            addCriterion("core_data >", value, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("core_data >=", value, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataLessThan(BigDecimal value) {
            addCriterion("core_data <", value, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataLessThanOrEqualTo(BigDecimal value) {
            addCriterion("core_data <=", value, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataIn(List<BigDecimal> values) {
            addCriterion("core_data in", values, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataNotIn(List<BigDecimal> values) {
            addCriterion("core_data not in", values, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("core_data between", value1, value2, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreDataNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("core_data not between", value1, value2, "coreData");
            return (Criteria) this;
        }

        public Criteria andCoreTypeIsNull() {
            addCriterion("core_type is null");
            return (Criteria) this;
        }

        public Criteria andCoreTypeIsNotNull() {
            addCriterion("core_type is not null");
            return (Criteria) this;
        }

        public Criteria andCoreTypeEqualTo(String value) {
            addCriterion("core_type =", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeNotEqualTo(String value) {
            addCriterion("core_type <>", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeGreaterThan(String value) {
            addCriterion("core_type >", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeGreaterThanOrEqualTo(String value) {
            addCriterion("core_type >=", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeLessThan(String value) {
            addCriterion("core_type <", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeLessThanOrEqualTo(String value) {
            addCriterion("core_type <=", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeLike(String value) {
            addCriterion("core_type like", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeNotLike(String value) {
            addCriterion("core_type not like", value, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeIn(List<String> values) {
            addCriterion("core_type in", values, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeNotIn(List<String> values) {
            addCriterion("core_type not in", values, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeBetween(String value1, String value2) {
            addCriterion("core_type between", value1, value2, "coreType");
            return (Criteria) this;
        }

        public Criteria andCoreTypeNotBetween(String value1, String value2) {
            addCriterion("core_type not between", value1, value2, "coreType");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeIsNull() {
            addCriterion("department_code is null");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeIsNotNull() {
            addCriterion("department_code is not null");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeEqualTo(String value) {
            addCriterion("department_code =", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeNotEqualTo(String value) {
            addCriterion("department_code <>", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeGreaterThan(String value) {
            addCriterion("department_code >", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeGreaterThanOrEqualTo(String value) {
            addCriterion("department_code >=", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeLessThan(String value) {
            addCriterion("department_code <", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeLessThanOrEqualTo(String value) {
            addCriterion("department_code <=", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeLike(String value) {
            addCriterion("department_code like", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeNotLike(String value) {
            addCriterion("department_code not like", value, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeIn(List<String> values) {
            addCriterion("department_code in", values, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeNotIn(List<String> values) {
            addCriterion("department_code not in", values, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeBetween(String value1, String value2) {
            addCriterion("department_code between", value1, value2, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andDepartmentCodeNotBetween(String value1, String value2) {
            addCriterion("department_code not between", value1, value2, "departmentCode");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNull() {
            addCriterion("create_user is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIsNotNull() {
            addCriterion("create_user is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserEqualTo(String value) {
            addCriterion("create_user =", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotEqualTo(String value) {
            addCriterion("create_user <>", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThan(String value) {
            addCriterion("create_user >", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("create_user >=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThan(String value) {
            addCriterion("create_user <", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLessThanOrEqualTo(String value) {
            addCriterion("create_user <=", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserLike(String value) {
            addCriterion("create_user like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotLike(String value) {
            addCriterion("create_user not like", value, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserIn(List<String> values) {
            addCriterion("create_user in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotIn(List<String> values) {
            addCriterion("create_user not in", values, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserBetween(String value1, String value2) {
            addCriterion("create_user between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateUserNotBetween(String value1, String value2) {
            addCriterion("create_user not between", value1, value2, "createUser");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameIsNull() {
            addCriterion("core_sys_name is null");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameIsNotNull() {
            addCriterion("core_sys_name is not null");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameEqualTo(String value) {
            addCriterion("core_sys_name =", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameNotEqualTo(String value) {
            addCriterion("core_sys_name <>", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameGreaterThan(String value) {
            addCriterion("core_sys_name >", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameGreaterThanOrEqualTo(String value) {
            addCriterion("core_sys_name >=", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameLessThan(String value) {
            addCriterion("core_sys_name <", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameLessThanOrEqualTo(String value) {
            addCriterion("core_sys_name <=", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameLike(String value) {
            addCriterion("core_sys_name like", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameNotLike(String value) {
            addCriterion("core_sys_name not like", value, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameIn(List<String> values) {
            addCriterion("core_sys_name in", values, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameNotIn(List<String> values) {
            addCriterion("core_sys_name not in", values, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameBetween(String value1, String value2) {
            addCriterion("core_sys_name between", value1, value2, "coreSysName");
            return (Criteria) this;
        }

        public Criteria andCoreSysNameNotBetween(String value1, String value2) {
            addCriterion("core_sys_name not between", value1, value2, "coreSysName");
            return (Criteria) this;
        }
    }

    /**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
public static class Criteria extends GeneratedCriteria {


        protected Criteria() {
            super();
        }
    }

    /**
 * <pre>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * </pre>
 */
public static class Criterion {

        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}