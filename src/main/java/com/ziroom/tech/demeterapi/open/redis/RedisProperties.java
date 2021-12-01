package com.ziroom.tech.demeterapi.open.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis连接、连接池、部署模式相关等属性管理中心
 * @author xuzeyu
 */
@ConfigurationProperties(prefix = RedisProperties.REDIS_PREFIX)
public class RedisProperties {

    /** properties配置项统一前缀标识 **/
    public static final String REDIS_PREFIX = "redis";

    /********** 下边为连接相关配置 **********/

    /** 主机，默认127.0.0.1:6379 **/
    private String hostName = Default.HOST + ":" + Default.PORT;

    /** 密码，默认空 **/
    private String password = Default.PASSWORD;

    /** 数据库，默认0号库 **/
    private int database 	= Default.DATABASE;

    /** 超时时间（单位为毫秒），默认2000，connectTimeout和readTimeout将一同设置上 **/
    private int timeout		= Default.TIMEOUT;


    /********* 下边为连接池相关配置 *******/

    /** 资源池中的最大连接数 **/
    private int maxTotal 	= 20;

    /** 资源池允许的最大空闲连接数，连接池的最佳性能是maxTotal=maxIdle，这样就避免了连接池伸缩带来的性能干扰 **/
    private int maxIdle 	= 20;

    /** 资源池确保的最少空闲连接数 **/
    private int minIdle 	= 10;

    /** 当资源池用尽后，调用者是否要等待。只有当值为true时，下面的maxWaitMillis才会生效。 **/
    private boolean blockWhenExhausted 	= true;

    /** 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。-1（表示永不超时），不建议使用默认值。 **/
    private int maxWaitMillis 			= 1000;

    /** 向资源池借用连接时是否做连接有效性检测（ping）。检测到的无效连接将会被移除。建议设置为false，减少一次ping的开销。**/
    private boolean testOnBorrow		= false;

    /** 向资源池归还连接时是否做连接有效性检测（ping）。检测到无效连接将会被移除。建议设置为false，减少一次ping的开销。 **/
    private boolean testOnReturn 		= false;

    /**
     * 空闲Jedis对象检测由下列四个参数组合完成，testWhileIdle是该功能的开关。
     * 其他包括：timeBetweenEvictionRunsMillis、minEvictableIdleTimeMillis、numTestsPerEvictionRun
     */
    /** 是否开启空闲资源检测。默认值是false，不建议使用false，空闲检测是有必要的 **/
    private boolean testWhileIdle 				= true;

    /** 空闲资源的检测周期（单位为毫秒），-1（不检测），建议设置  **/
    private long timeBetweenEvictionRunsMillis 	= 60000;

    /** 资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。 **/
    private int minEvictableIdleTimeMillis		= 30000;

    /** 做空闲资源检测时，每次检测资源的个数。如果设置为 -1，就是对所有连接做空闲监测。 **/
    private int numTestsPerEvictionRun 			= -1;


    /********* 下边为集群相关配置 *******/

    /** 哨兵模式，哨兵集群名字，默认为mymaster **/
    private String sentinelMasterName = DeployMode.Sentinel.MASTER_NAME;


    /** setter getter **/
    public String getSentinelMasterName() {
        return sentinelMasterName;
    }

    public void setSentinelMasterName(String sentinelMasterName) {
        this.sentinelMasterName = sentinelMasterName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    /**
     *
     * @Description: redis默认连接配置项
     * @author xuzeyu
     */
    public static class Default {

        private static final String HOST 	 = "127.0.0.1";

        private static final int 	PORT 	 = 6379;

        private static final String PASSWORD = "";

        private static final int 	DATABASE = 0;

        private static final int    TIMEOUT  = 2000;

    }


    /**
     *
     * @Description: redis部署模式
     *
     * @author xuzeyu
     */
    public static class DeployMode {

        /** properties配置key名 **/
        public static final String REDIS_MODE = "redis.mode";

        /** 单点模式 **/
        public static final String STANDALONE = "alone";

        /** 哨兵模式 **/
        public static final String SENTINEL   = "sentinel";

        /** 集群模式 **/
        public static final String CLUSTER    = "cluster";

        static class Sentinel {
            /** 哨兵集群名字，默认为mymaster **/
            private static final String MASTER_NAME    = "mymaster";
        }
    }
}
