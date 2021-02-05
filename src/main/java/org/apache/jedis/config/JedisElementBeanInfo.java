package org.apache.jedis.config;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;

import java.beans.PropertyDescriptor;

/**
 * @ClassName JedisElementBeanInfo
 * @Description TODO
 * @Author 兴盛优选研发中心 技术质量部 杨杰
 * @Date 2021/2/4 15:17
 * @Version 1.0
 */
public class JedisElementBeanInfo extends BeanInfoSupport {
    /**
     * Construct a BeanInfo for the given class.
     *
     */
    public JedisElementBeanInfo() {
        super(JedisElement.class);

        PropertyDescriptor p;

        p = property("server");//默认指定该元件为编辑框
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);//是否必填项
        p.setValue(DEFAULT, "127.0.0.1");//默认值

        p = property("port");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "6379");

        p = property("password", TypeEditor.PasswordEditor);
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "");

        p = property("maxconnection");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "20");

        p = property("maxIdle");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "8");

        p = property("minIdle");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "8");

        p = property("maxWaitTime");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(DEFAULT, "10000");


        String[] array=new String[]{"true","false"};

        p = property("isBorrow");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(TAGS, array);
        p.setValue(DEFAULT, array[0]);

        p = property("isReturn");
        p.setValue(NOT_UNDEFINED, Boolean.TRUE);
        p.setValue(TAGS, array);
        p.setValue(DEFAULT, array[0]);

        createPropertyGroup("connection configuration", new String[]{"server","port","password","maxconnection","maxIdle","minIdle","maxWaitTime","isBorrow","isReturn"});
    }
}
