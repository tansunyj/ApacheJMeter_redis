package org.apache.jedis.sampler;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;

import java.beans.PropertyDescriptor;

/**
 * @ClassName JedisSamplerBeanInfo
 * @Description TODO
 * @Author 兴盛优选研发中心 技术质量部 杨杰
 * @Date 2021/2/4 16:45
 * @Version 1.0
 */
public class JedisSamplerBeanInfo extends BeanInfoSupport {

    /**
     * Construct a BeanInfo for the given class.
     *
     */
    public JedisSamplerBeanInfo() {
        super(JedisSampler.class);

        String[] Operations = new String[] {
                JedisSampler.OPERATION_KEYS,
                JedisSampler.OPERATION_TYPE,
                JedisSampler.OPERATION_SET,
                JedisSampler.OPERATION_GET,
                JedisSampler.OPERATION_DELETE,
                JedisSampler.OPERATION_HGEALL,
                JedisSampler.OPERATION_HGET,
                JedisSampler.OPERATION_HDEL,
                JedisSampler.OPERATION_HSET,
                JedisSampler.OPERATION_HSETM,
                JedisSampler.OPERATION_TTL,
                JedisSampler.OPERATION_EXPIRE,
                JedisSampler.OPERATION_EXPIRE_AT
        };

        PropertyDescriptor p;

        p=property("database");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "0");

        p = property("operation");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(TAGS, Operations);
        p.setValue(DEFAULT, Operations[0]);

        p = property("key");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "key");

        p = property("field");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "field");

        p = property("data", TypeEditor.TextAreaEditor);
        p.setValue(NOT_UNDEFINED, Boolean.FALSE);
        p.setValue(MULTILINE, Boolean.TRUE);
        p.setValue(DEFAULT, "");

    //    createPropertyGroup("database && operation", new String[]{ "database","operation"});

        createPropertyGroup("data",new String[]{"database","operation","key","field","data"});
    }
}
