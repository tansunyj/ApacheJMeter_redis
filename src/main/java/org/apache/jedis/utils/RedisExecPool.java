package org.apache.jedis.utils;

import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 注：该类的部分代码来自于me.lty.redis插件中的代码
 * @ClassName RedisExecPool
 * @Description TODO
 * @Author lty 
 * @Date 2021/2/5 15:04
 * @Version 1.0
 */
public class RedisExecPool {
    private static final Logger log = LoggerFactory.getLogger(RedisExecPool.class);
    private static JedisPool jedisPool = null;
    private static final Object object = new Object();
    private static RedisExecPool redisExecPool = null;
    private int maxConnection = 10;
    private int maxIdle = 8;
    private int minIdle = 4;
    private int maxWaitTime = 10000;
    private boolean isBorrow = true;
    private boolean isReturn = true;
    private JedisPoolConfig config;
    private String host;
    private int port;
    private String password;

    private RedisExecPool() {
        this.config = new JedisPoolConfig();
        this.config.setMaxTotal(maxConnection);
        this.config.setMaxIdle(maxIdle);
        this.config.setMinIdle(minIdle);
        this.config.setMaxWaitMillis(maxWaitTime);
        this.config.setTestOnBorrow(isBorrow);
        this.config.setTestOnReturn(isReturn);
    }

    private RedisExecPool(String ip, int port, String password,int maxConnection,int maxIdle,int minIdle,int maxWaitTime,boolean isBorrow,boolean isReturn) {
        this();
        host=ip;
        this.port=port;
        config.setMaxTotal(maxConnection);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxWaitMillis(maxWaitTime);
        config.setTestOnBorrow(isBorrow);
        config.setTestOnReturn(isReturn);
        this.password=password;
        if (!JOrphanUtils.isBlank(password)){
            jedisPool = new JedisPool(config, ip, port, 10000, password);
        }else {
            jedisPool = new JedisPool(config, ip, port, 10000);
        }
    }

    public static RedisExecPool getInstance(String ip, int port, String password,int maxConnection,int maxIdle,int minIdle,int maxWaitTime,boolean isBorrow,boolean isReturn) {
        if (redisExecPool == null) {
            synchronized(object) {
                if (redisExecPool == null) {
                    redisExecPool = new RedisExecPool(ip, port, password,maxConnection,maxIdle,minIdle,maxWaitTime,isBorrow,isReturn);
                }
            }
        }
        return redisExecPool;
    }

    public static RedisExecPool getInstance(){
        return redisExecPool;
    }

    public void clearPool() {
        redisExecPool.clearPool();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public void close(Jedis jedis) {
        jedis.close();
    }

    public String getHost(){
        return host;
    }
    public int getPort(){
        return port;
    }
    public String getPassword(){
        return password;
    }
}
