package com.ziroom.tech.demeterapi.open.redis;

import com.ziroom.tech.demeterapi.open.exception.BusinessRunTimeException;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis工具类
 * @author xuzeyu
 */
public class RedisUtil {
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    private static final String TRY_LOCK_SCRIPT = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], KEYS[2]) else return 0 end";

    private RedisTemplate<String, Object> redisTemplate;


    /**
     * @param redisTemplate
     */
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }


    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间
     * @param unit 单位
     * @return
     */
    public boolean expire(String key, long time, TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param keys 集合
     */
    public boolean delByCollection(Collection<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            return true;
        }
        return false;
    }

    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     *  获取字符串缓存
     * @param key
     * @author xuzeyu
     */
    public String getString(String key) {
        if(Objects.isNull(key)){
            return null;
        }
        Object obj = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(obj)){
            return null;
        }
        return obj.toString();
    }

    public boolean mSet(Map<? extends String, ? extends Object> map) {
        if (MapUtils.isEmpty(map) || map.size() > 100) {
            return false;
        }
        try {
            redisTemplate.opsForValue().multiSet(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T> List<T> mGet(Collection<String> keys, Class<T> clazz) {
        if (CollectionUtils.isEmpty(keys) || keys.size() > 100) {
            return null;
        }
        try {
            List<Object> multiGet = redisTemplate.opsForValue().multiGet(keys);
            if (!CollectionUtils.isEmpty(multiGet)) {
                return (List<T>) multiGet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * mGet
     *
     * @param keys
     * @return
     */
    public Map<String, Object> mGet(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        } else {
            List<Object> objects = redisTemplate.opsForValue().multiGet(keys);
            if (CollectionUtils.isEmpty(objects)) {
                return null;
            }
            Map<String, Object> result = new HashMap<>();
            for (int i = 0; i < objects.size(); i++) {
                result.put(keys.get(i), objects.get(i));
            }
            return result;
        }
    }


    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     */
    public boolean set(String key, Object value, long time,TimeUnit unit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, unit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 模糊查询RedisKey
     *
     * @param key 键
     */
    public Set<String> keys(String key) {
        return scan(key);
    }

    /**
     * 模糊查询RedisKey
     *
     * @param key 键
     */
    public Set<String> scan(String key) {
        return scan(key, 1000);
    }

    /**
     * 模糊查询RedisKey
     *
     * @param key   键
     * @param count 扫描数量
     */
    public Set<String> scan(String key, int count) {
        return redisTemplate.execute((RedisCallback<Set<String>>) redisConnection -> {
            Set<String> result = new HashSet<>();
            Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions().count(count).match(key).build());
            while (cursor.hasNext()) {
                result.add(new String(cursor.next()));
            }
            return result;
        });
    }


    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new BusinessRunTimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new BusinessRunTimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);

    }

    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * @Description: 批量获取hash结构指定key下的指定hKeys的value集合
     */
    public List<Object> hmget(String key, Collection<Object> hKeys) {
        return hmget(key, hKeys, Object.class);
    }

    /**
     * @Description: 批量获取hash结构指定key下的指定hKeys的value集合，需手动指定value泛型
     */
    public <T> List<T> hmget(String key, Collection<Object> hKeys, Class<T> vClazz) {
        if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(hKeys) || Objects.isNull(vClazz)) {
            return null;
        }
        try {
            List<Object> result = redisTemplate.opsForHash().multiGet(key, hKeys);
            return buildAdaptedList(result, vClazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <T> boolean hmset(String key, Map<String, T> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public <T> boolean hmset(String key, Map<String, T> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list中的内容
     * @param key
     * @param start
     * @param end
     * @param clazz 泛型
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> lGet(String key, long start, long end, Class<T> clazz) {
        try {
            List<Object> list = redisTemplate.opsForList().range(key, start, end);
            return buildAdaptedList(list, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * <p>
     * 将value作为redis list数据结构中的一个元素rpush到key中
     * --append by xuzeyu
     * </p>
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * <p>
     * 将value作为redis list数据结构中的一个元素rpush到key中
     * --append by xuzeyu
     * </p>
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * <p>
     * 将参数list中的每个元素都rpush到key中，请使用 rPushAll(String key, List<T> list, long time)
     * --append by xuzeyu
     * </p>
     * @param key   键
     * @param value 值
     * @return
     */
    @Deprecated
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    @Deprecated
    public boolean lSet(String key, List<? extends Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key
     * @param list
     * @param time
     * @return
     * @Description:插入list <ul>
     * <li>将参数list中的每个元素都添加到key中, 并指定过期时间</li>
     * <li>读取key使用：lGet(String key, long start, long end)</li>
     * </ul>
     */
    public <T> boolean rPushAll(String key, List<T> list, long time) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            Object[] array = list.toArray();
            redisTemplate.opsForList().rightPushAll(key, array);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key
     * @param list
     * @return
     * @Description:插入list <ul>
     * <li>将参数list中的每个元素都添加到key中</li>
     * <li>读取key使用：lGet(String key, long start, long end)</li>
     * </ul>
     */
    public <T> boolean rPushAll(String key, List<T> list) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            Object[] array = list.toArray();
            redisTemplate.opsForList().rightPushAll(key, array);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 从左侧弹出一个值并删除该值
     * @param key 键
     * @return 移除的个数
     */
    public Object lPop(String key) {
        try {
            Object o = redisTemplate.opsForList().leftPop(key);
            return o;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param key
     * @param
     * @return
     * @Description:插入list <ul>
     * <li>将一个值插入到列表头部</li>
     * </ul>
     */
    public <T> boolean lPush(String key, T value) {
        try {
            if (Objects.isNull(value)) {
                return false;
            }
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param key
     * @param list
     * @return
     * @Description:插入list <ul>
     * <li>将一个或多个值插入到列表头部</li>
     * </ul>
     */
    public <T> boolean lPushAll(String key, List<T> list) {
        try {
            if (CollectionUtils.isEmpty(list)) {
                return false;
            }
            Object[] array = list.toArray();
            redisTemplate.opsForList().leftPushAll(key, array);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 该加锁方法仅针对单实例 Redis 可实现分布式加锁
     * 对于 Redis 集群则无法使用
     * 支持重复，线程安全
     * @param lockKey  加锁键
     * @param clientId 加锁客户端唯一标识(采用UUID)
     * @param seconds  锁过期时间 秒
     * @return
     */
    public Boolean tryLock(String lockKey, String clientId, long seconds) {
        // 兼容Jedis新旧版本,采用lua方式
        return tryLockLua(lockKey, clientId, seconds);

        //        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
        //            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
        //            SetParams setParams = SetParams.setParams().nx().ex(Math.toIntExact(seconds));
        //            String result = jedis.set(lockKey, clientId, setParams);
        //            if (LOCK_SUCCESS.equals(result)) {
        //                return Boolean.TRUE;
        //            }
        //            return Boolean.FALSE;
        //        });
    }

    public Boolean tryLockLua(String lockKey, String clientId, long seconds) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            List<String> KEYS = new ArrayList<>(2);
            KEYS.add(lockKey);
            KEYS.add(String.valueOf(seconds));

            Object nativeConnection = redisConnection.getNativeConnection();
            Object result = null;
            if (nativeConnection instanceof Jedis) {
                result = ((Jedis) nativeConnection).eval(TRY_LOCK_SCRIPT, KEYS, Collections.singletonList(clientId));
            }

            if (RELEASE_SUCCESS.equals(result)) {
                return Boolean.TRUE;
            }

            return Boolean.FALSE;
        });
    }


    /**
     * 獲取redis分布式锁
     * @param key                     要锁定的key
     * @param clientId                加锁客户端唯一标识(采用UUID)
     * @param expireSeconds           key的失效时间(秒)
     * @param maxRetryTimes           最大重试次数,如果获取锁失败，会自动尝试重新获取锁；
     * @param retryIntervalTimeMillis 每次重试之前sleep等待的毫秒数
     * @return
     */
    public Boolean tryLock(final String key, String clientId, final long expireSeconds, int maxRetryTimes,
                           long retryIntervalTimeMillis) {
        int maxTimes = maxRetryTimes + 1;
        for (int i = 0; i < maxTimes; i++) {
            Boolean status = this.setnx(key, clientId, expireSeconds);
            if (status) {
                return Boolean.TRUE;
            }
            if (retryIntervalTimeMillis > 0) {
                try {
                    Thread.sleep(retryIntervalTimeMillis);
                } catch (InterruptedException e) {
                    break;
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }

        return null;
    }

    /**
     * 与 tryLock 相对应，用作释放锁
     * @param lockKey
     * @param clientId
     * @return
     */
    public Boolean releaseLock(String lockKey, String clientId) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
                    Collections.singletonList(clientId));
            if (RELEASE_SUCCESS.equals(result)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        });
    }

    /**
     * @param key
     * @param value
     * @param time
     * @return
     * @Description: setnx
     */
    public boolean setnx(String key, Object value, long time) {
        if (key == null || value == null) {
            return false;
        }
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     *
     * @Description: 类型转换适配 <list>
     * @param <T>
     * @param list
     * @return
     */
    private <T> List<T> buildAdaptedList(List<Object> list, Class<T> clazz) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (Long.class == clazz) {
            Object object = list.get(0);
            if (object instanceof Integer) {
                return (List<T>) list.stream().map(obj -> Long.valueOf((Integer) obj)).collect(Collectors.toList());
            }
        }
        return (List<T>) list;
    }


    /**
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     * @return boolean
     */
    public boolean setnx(String key, Object value, long time,TimeUnit unit) {
        if (key == null || value == null) {
            return false;
        }
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value, time, unit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param <T>
     * @param list
     * @return
     * @Description: 将List<Integer>转为List<Long>
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> buildLongList(List<Object> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        Object object = list.get(0);
        if (object instanceof Integer) {
            List<Long> longList = new ArrayList<>(list.size());
            list.stream().forEach(obj -> {
                longList.add(Long.valueOf((Integer) obj));
            });
            return (List<T>) longList;
        }
        return (List<T>) list;
    }

    /**
     * 值增1并设有效时间，如果key不存在，自动添加key，value默认为1
     * @return
     */
    public String incrAndExpire(String script) {
        return redisTemplate.execute((RedisCallback<String>) con -> {
            Jedis jedis = (Jedis) con.getNativeConnection();
            return jedis.eval(script).toString();
        });
    }


    /**
     * 获取分值
     * @return
     */
    public Double score(String key,String keyWord) {
        return redisTemplate.opsForZSet().score(key, keyWord);
    }

    /**
     * 获取分值
     * @return
     */
    public boolean addScore(String key,String keyWord,double sum) {
        return redisTemplate.opsForZSet().add(key, keyWord,sum);
    }

    /**
     * 累加分数值
     * @return
     */
    public Double incrementScore(String key,String keyWord,double sum) {
        return redisTemplate.opsForZSet().incrementScore(key, keyWord,sum);
    }

    /**
     * 通过索引区间返回有序集合成指定区间内的成员对象，其中有序集成员按分数值递减(从大到小)顺序排列
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start,end);
    }


    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}
