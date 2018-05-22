package com.barton.utils;

import cn.hutool.core.lang.Assert;
import com.barton.domain.config.Properties;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Xml <-> Object   xml与Java对象之间的转换
 */
public class JaxbMapper {
    private static ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap<Class, JAXBContext>();

    /**
     * Java Object->Xml without encoding.
     */
    public static String toXml(Object root) {
        Class clazz = Reflections.getUserClass(root);
        return toXml(root, clazz, null);
    }

    /**
     * Java Object->Xml with encoding.
     */
    public static String toXml(Object root, String encoding) {
        Class clazz = Reflections.getUserClass(root);
        return toXml(root, clazz, encoding);
    }

    /**
     * Java Object->Xml with encoding.
     */
    public static String toXml(Object root, Class clazz, String encoding) {
        try {
            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(root, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Java Collection->Xml without encoding, 特别支持Root Element是Collection的情形.
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz) {
        return toXml(root, rootName, clazz, null);
    }

    /**
     * Java Collection->Xml with encoding, 特别支持Root Element是Collection的情形.
     */
    public static String toXml(Collection<?> root, String rootName, Class clazz, String encoding) {
        try {
            CollectionWrapper wrapper = new CollectionWrapper();
            wrapper.collection = root;

            JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(new QName(rootName),
                    CollectionWrapper.class, wrapper);

            StringWriter writer = new StringWriter();
            createMarshaller(clazz, encoding).marshal(wrapperElement, writer);

            return writer.toString();
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Xml->Java Object.
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXml(String xml, Class<T> clazz) {
        try {
            StringReader reader = new StringReader(xml);
            return (T) createUnmarshaller(clazz).unmarshal(reader);
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 创建Marshaller并设定encoding(可为null).
     * 线程不安全，需要每次创建或pooling。
     */
    public static Marshaller createMarshaller(Class clazz, String encoding) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (StringUtils.isNotBlank(encoding)) {
                marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            }

            return marshaller;
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 创建UnMarshaller.
     * 线程不安全，需要每次创建或pooling。
     */
    public static Unmarshaller createUnmarshaller(Class clazz) {
        try {
            JAXBContext jaxbContext = getJaxbContext(clazz);
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw Exceptions.unchecked(e);
        }
    }

    protected static JAXBContext getJaxbContext(Class clazz) {
        Assert.notNull(clazz, "'clazz' must not be null");
        JAXBContext jaxbContext = jaxbContexts.get(clazz);
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
                jaxbContexts.putIfAbsent(clazz, jaxbContext);
            } catch (JAXBException ex) {
                ex.printStackTrace();
            }
        }
        return jaxbContext;
    }

    /**
     * 封装Root Element 是 Collection的情况.
     */
    public static class CollectionWrapper {

        @XmlAnyElement
        protected Collection<?> collection;
    }
    public static void main(String [] args){
        String path = PropertiesUtil.class.getClass().getResource("/config.xml").getPath();
        String xmlStr = FileUtil.xml2String(new File(path));
        System.out.println(xmlStr);
        Properties properties = JaxbMapper.fromXml(xmlStr, Properties.class);
        System.out.print(properties);
    }
}
