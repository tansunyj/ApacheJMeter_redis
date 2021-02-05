package org.apache.jedis.sampler;

import org.apache.jedis.config.JedisElement;
import org.apache.jedis.utils.RedisExecPool;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * @author Julian Montejo
 * JMeter sampler that allows perform some operations over redis
 */

public class JedisSampler extends AbstractTestElement implements Sampler, TestBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(JedisSampler.class);

    /**
     * List of the admitted operations
     **/
    public static final String OPERATION_KEYS = "keys";
    public static final String OPERATION_TYPE = "type";
    public static final String OPERATION_SET = "set";
    public static final String OPERATION_GET = "get";
    public static final String OPERATION_DELETE = "del";
    public static final String OPERATION_HGEALL = "hgetAll";
    public static final String OPERATION_HGET = "hget";
    public static final String OPERATION_HDEL = "hdel";
    public static final String OPERATION_HSET = "hset";
    public static final String OPERATION_HSETM = "hsetM";
    public static final String OPERATION_TTL = "ttl";
    public static final String OPERATION_EXPIRE = "expire";
    public static final String OPERATION_EXPIRE_AT = "expireAt";

    private transient String operation;
    private transient String data;
    private transient String key;
    private transient String field;
    private transient String database;

    private RedisExecPool redisExecPool=null;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return this.data;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return this.field;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public SampleResult sample(Entry arg0) {
        StringBuilder devolver = new StringBuilder();
        boolean isSuccess = true;
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();
        Jedis jedis = null;
        try {
            redisExecPool=JedisElement.getRedisExecPool();
            jedis=redisExecPool.getJedis();
            int db=Integer.parseInt(database);
            jedis.select(db);
            sampleResult.setSamplerData(passedParameters());
            switch (operation) {
                case OPERATION_KEYS:
                    devolver.append(jedis.keys(key));
                    break;
                case OPERATION_TYPE:
                    devolver.append(jedis.type(key));
                    break;
                case OPERATION_DELETE:
                    devolver.append("del result: ");
                    devolver.append(jedis.del(key));
                    break;
                case OPERATION_SET:
                    devolver.append(jedis.set(key, data));
                    break;
                case OPERATION_GET:
                    devolver.append(jedis.get(key));
                    break;
                case OPERATION_HGEALL:
                    devolver.append(jedis.hgetAll(key));
                    break;
                case OPERATION_HGET:
                    devolver.append(jedis.hget(key, field));
                    break;
                case OPERATION_HSET:
                    devolver.append("hset result: ");
                    devolver.append(jedis.hset(key, field, data));
                    break;
                case OPERATION_HDEL:
                    devolver.append("hdel result: ");
                    devolver.append(jedis.hdel(key, field));
                    break;
                case OPERATION_HSETM:
                    HashMap<String, String> listaDatos = new HashMap<>();
                    StringTokenizer tokens = new StringTokenizer(data, "\n");
                    while (tokens.hasMoreTokens()) {
                        String token = tokens.nextToken();
                        String[] fieldAndData = token.split("\t");
                        if (fieldAndData.length < 2) {
                            throw new Exception("Data error.\nhsetM data format: <field>\\t<data>\ndata: " + token);
                        }
                        if (fieldAndData[0] == null || fieldAndData[0].length() == 0) {
                            throw new Exception("Data error.\nEmpty field in:\n" + token);
                        }
                        String key = fieldAndData[0];
                        String data = Arrays.stream(fieldAndData).skip(1).collect(Collectors.joining("\t"));
                        listaDatos.put(key, data);
                    }
                    devolver.append("hsetM result: ");
                    devolver.append(jedis.hmset(key, listaDatos));
                    break;
                case OPERATION_TTL:
                    devolver.append(jedis.ttl(key));
                    break;
                case OPERATION_EXPIRE:
                    int seconds = Integer.parseInt(field);
                    devolver.append(jedis.expire(key, seconds));
                    break;
                case OPERATION_EXPIRE_AT:
                    long unixTime = Long.parseLong(field);
                    devolver.append(jedis.expireAt(key, unixTime));
                    break;

            }
            sampleResult.setResponseData(devolver.toString().getBytes("utf-8"));
        } catch (Exception e) {
            devolver.append("\nError:" + e.getMessage() + "\n\n");
            devolver.append(e);
            isSuccess = false;
            sampleResult.setResponseMessage(e.getMessage());
            log.error("Error in JedisSampler:", e);
            sampleResult.setResponseData(devolver.toString().getBytes());
        } finally {
            if (redisExecPool !=null && jedis !=null){
                redisExecPool.close(jedis);
            }
        }
        sampleResult.setDataEncoding("utf-8");
        sampleResult.setSuccessful(isSuccess);
        sampleResult.setDataType(SampleResult.TEXT);
        sampleResult.setSampleLabel(getName());
        return sampleResult;
    }

    /**
     * A list of the parameters passed to the sampler
     *
     * @return
     */
    private String passedParameters() {
        StringBuilder devolver = new StringBuilder();
        devolver.append("host: " + redisExecPool.getHost() + ":" + redisExecPool.getPort());
        devolver.append("\ndatabase: " + database);
        devolver.append("\nOperation: " + operation + "\n");
        devolver.append("\nKey: ");
        devolver.append(key);
        devolver.append("\nField: " + field);
        devolver.append("\nData:\n");
        devolver.append("\t" + data);
        return devolver.toString();
    }
}
