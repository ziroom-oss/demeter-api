package com.ziroom.tech.demeterapi.dao.entity;

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
public class DemeterAuthHistoryExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DemeterAuthHistoryExample() {
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

        public Criteria andUserTaskIdIsNull() {
            addCriterion("user_task_id is null");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdIsNotNull() {
            addCriterion("user_task_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdEqualTo(Long value) {
            addCriterion("user_task_id =", value, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdNotEqualTo(Long value) {
            addCriterion("user_task_id <>", value, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdGreaterThan(Long value) {
            addCriterion("user_task_id >", value, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_task_id >=", value, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdLessThan(Long value) {
            addCriterion("user_task_id <", value, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdLessThanOrEqualTo(Long value) {
            addCriterion("user_task_id <=", value, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdIn(List<Long> values) {
            addCriterion("user_task_id in", values, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdNotIn(List<Long> values) {
            addCriterion("user_task_id not in", values, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdBetween(Long value1, Long value2) {
            addCriterion("user_task_id between", value1, value2, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andUserTaskIdNotBetween(Long value1, Long value2) {
            addCriterion("user_task_id not between", value1, value2, "userTaskId");
            return (Criteria) this;
        }

        public Criteria andAuthUserIsNull() {
            addCriterion("auth_user is null");
            return (Criteria) this;
        }

        public Criteria andAuthUserIsNotNull() {
            addCriterion("auth_user is not null");
            return (Criteria) this;
        }

        public Criteria andAuthUserEqualTo(String value) {
            addCriterion("auth_user =", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserNotEqualTo(String value) {
            addCriterion("auth_user <>", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserGreaterThan(String value) {
            addCriterion("auth_user >", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserGreaterThanOrEqualTo(String value) {
            addCriterion("auth_user >=", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserLessThan(String value) {
            addCriterion("auth_user <", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserLessThanOrEqualTo(String value) {
            addCriterion("auth_user <=", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserLike(String value) {
            addCriterion("auth_user like", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserNotLike(String value) {
            addCriterion("auth_user not like", value, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserIn(List<String> values) {
            addCriterion("auth_user in", values, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserNotIn(List<String> values) {
            addCriterion("auth_user not in", values, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserBetween(String value1, String value2) {
            addCriterion("auth_user between", value1, value2, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthUserNotBetween(String value1, String value2) {
            addCriterion("auth_user not between", value1, value2, "authUser");
            return (Criteria) this;
        }

        public Criteria andAuthResultIsNull() {
            addCriterion("auth_result is null");
            return (Criteria) this;
        }

        public Criteria andAuthResultIsNotNull() {
            addCriterion("auth_result is not null");
            return (Criteria) this;
        }

        public Criteria andAuthResultEqualTo(Integer value) {
            addCriterion("auth_result =", value, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultNotEqualTo(Integer value) {
            addCriterion("auth_result <>", value, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultGreaterThan(Integer value) {
            addCriterion("auth_result >", value, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("auth_result >=", value, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultLessThan(Integer value) {
            addCriterion("auth_result <", value, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultLessThanOrEqualTo(Integer value) {
            addCriterion("auth_result <=", value, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultIn(List<Integer> values) {
            addCriterion("auth_result in", values, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultNotIn(List<Integer> values) {
            addCriterion("auth_result not in", values, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultBetween(Integer value1, Integer value2) {
            addCriterion("auth_result between", value1, value2, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthResultNotBetween(Integer value1, Integer value2) {
            addCriterion("auth_result not between", value1, value2, "authResult");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionIsNull() {
            addCriterion("auth_opinion is null");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionIsNotNull() {
            addCriterion("auth_opinion is not null");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionEqualTo(String value) {
            addCriterion("auth_opinion =", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionNotEqualTo(String value) {
            addCriterion("auth_opinion <>", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionGreaterThan(String value) {
            addCriterion("auth_opinion >", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionGreaterThanOrEqualTo(String value) {
            addCriterion("auth_opinion >=", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionLessThan(String value) {
            addCriterion("auth_opinion <", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionLessThanOrEqualTo(String value) {
            addCriterion("auth_opinion <=", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionLike(String value) {
            addCriterion("auth_opinion like", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionNotLike(String value) {
            addCriterion("auth_opinion not like", value, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionIn(List<String> values) {
            addCriterion("auth_opinion in", values, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionNotIn(List<String> values) {
            addCriterion("auth_opinion not in", values, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionBetween(String value1, String value2) {
            addCriterion("auth_opinion between", value1, value2, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andAuthOpinionNotBetween(String value1, String value2) {
            addCriterion("auth_opinion not between", value1, value2, "authOpinion");
            return (Criteria) this;
        }

        public Criteria andCreateIdIsNull() {
            addCriterion("create_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateIdIsNotNull() {
            addCriterion("create_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateIdEqualTo(String value) {
            addCriterion("create_id =", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotEqualTo(String value) {
            addCriterion("create_id <>", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThan(String value) {
            addCriterion("create_id >", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdGreaterThanOrEqualTo(String value) {
            addCriterion("create_id >=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThan(String value) {
            addCriterion("create_id <", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLessThanOrEqualTo(String value) {
            addCriterion("create_id <=", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdLike(String value) {
            addCriterion("create_id like", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotLike(String value) {
            addCriterion("create_id not like", value, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdIn(List<String> values) {
            addCriterion("create_id in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotIn(List<String> values) {
            addCriterion("create_id not in", values, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdBetween(String value1, String value2) {
            addCriterion("create_id between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andCreateIdNotBetween(String value1, String value2) {
            addCriterion("create_id not between", value1, value2, "createId");
            return (Criteria) this;
        }

        public Criteria andModifyIdIsNull() {
            addCriterion("modify_id is null");
            return (Criteria) this;
        }

        public Criteria andModifyIdIsNotNull() {
            addCriterion("modify_id is not null");
            return (Criteria) this;
        }

        public Criteria andModifyIdEqualTo(String value) {
            addCriterion("modify_id =", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotEqualTo(String value) {
            addCriterion("modify_id <>", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdGreaterThan(String value) {
            addCriterion("modify_id >", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdGreaterThanOrEqualTo(String value) {
            addCriterion("modify_id >=", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLessThan(String value) {
            addCriterion("modify_id <", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLessThanOrEqualTo(String value) {
            addCriterion("modify_id <=", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdLike(String value) {
            addCriterion("modify_id like", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotLike(String value) {
            addCriterion("modify_id not like", value, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdIn(List<String> values) {
            addCriterion("modify_id in", values, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotIn(List<String> values) {
            addCriterion("modify_id not in", values, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdBetween(String value1, String value2) {
            addCriterion("modify_id between", value1, value2, "modifyId");
            return (Criteria) this;
        }

        public Criteria andModifyIdNotBetween(String value1, String value2) {
            addCriterion("modify_id not between", value1, value2, "modifyId");
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

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
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