package org.apache.jedis.config;

import org.apache.jedis.utils.RedisExecPool;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName JedisElement
 * @Description TODO
 * @Author 兴盛优选研发中心 技术质量部 杨杰
 * @Date 2021/2/4 15:16
 * @Version 1.0
 */
public class JedisElement extends AbstractTestElement implements ConfigElement, TestStateListener, TestBean {
    private static final Logger log = LoggerFactory.getLogger(JedisElement.class);

    private transient String server;
    private transient String port;
    private transient String password;
    private transient String maxconnection;
    private transient String maxIdle;
    private transient String minIdle;
    private transient String maxWaitTime;
    private transient String isBorrow;
    private transient String isReturn;

    @Override
    public void addConfigElement(ConfigElement config) {

    }

    @Override
    public boolean expectsModification() {
        return false;
    }

    @Override
    public void testStarted() {
        testStarted("localhost");
    }

    @Override
    public void testStarted(String host) {
        this.setRunningVersion(true);
        TestBeanHelper.prepare(this);
        synchronized (this) {
            RedisExecPool.getInstance(getServer(), Integer.parseInt(getPort()), getPassword(), Integer.parseInt(getMaxconnection()),Integer.parseInt(getMaxIdle()),Integer.parseInt(getMinIdle()),Integer.parseInt(getMaxWaitTime()),Boolean.parseBoolean(getIsBorrow()),Boolean.parseBoolean(getIsReturn()));
        }
    }

    public static RedisExecPool getRedisExecPool() {
        return RedisExecPool.getInstance();
    }

    @Override
    public void testEnded() {
        testEnded("localhost");
    }

    @Override
    public void testEnded(String host) {
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaxconnection() {
        return maxconnection;
    }

    public void setMaxconnection(String maxconnection) {
        this.maxconnection = maxconnection;
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    public String getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(String maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public String getIsBorrow() {
        return isBorrow;
    }

    public void setIsBorrow(String isBorrow) {
        this.isBorrow = isBorrow;
    }

    public String getIsReturn() {
        return isReturn;
    }

    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn;
    }
}
