/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.alphahub.eport.signature.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * DOM and XML accessibility and comfort functions.
 */
@SuppressWarnings({"unchecked"})
public final class XMLUtils {

    private static final WeakObjectPool<DocumentBuilder, ParserConfigurationException>[] pools = new WeakObjectPool[2];

    static {
        pools[0] = new DocumentBuilderPool(false);
        pools[1] = new DocumentBuilderPool(true);
    }

    /**
     * Constructor XMLUtils
     */
    private XMLUtils() {
        // we don't allow instantiation
    }

    /**
     * 移除XML第一行: <?xml version="1.0" encoding="UTF-8"?>
     *
     * @param sourceXml 原始XML
     * @return 原始XML（移除第一行：<?xml version="1.0" encoding="UTF-8"?>）
     */
    public static String removeFirstLineOfSourceXml(String sourceXml) {
        String firstLine = """
                <?xml version="1.0" encoding="UTF-8"?>
                """;
        if (sourceXml.startsWith(firstLine)) {
            int index = sourceXml.indexOf('\n');
            if (index >= 0) {
                return sourceXml.substring(index + 1);
            }
        }
        return sourceXml;
    }

    /**
     * 由XML字符串 -> Java对象
     *
     * @param xml    XML字符串
     * @param entity 返回的实体
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToObject(String xml, Class<T> entity) {
        try {
            JAXBContext context = JAXBContext.newInstance(entity);
            Unmarshaller unmarshal = context.createUnmarshaller();
            StringReader stringReader = new StringReader(xml);
            return (T) unmarshal.unmarshal(stringReader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Java对象 -> XML字符串
     *
     * @param object 根节点对象
     * @return xml字符串
     */
    public static <T> String objectToXml(T object) {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(object.getClass());
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            StringWriter writer = new StringWriter();
            mar.marshal(object, writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对象 -> Map集合
     *
     * @param object 对象
     * @return Map
     * @apiNote 此方法不会返回null，转换出错会返回空集合，obj为null返回空集合
     */
    public static Map<String, Object> objectToMap(Object object) {
        if (object == null) {
            return Collections.emptyMap();
        }
        try {
            Map<String, Object> map = new LinkedHashMap<>();
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(object) : null;
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public static Document read(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
        return read(inputStream, true);
    }

    public static Document read(InputStream inputStream, boolean disAllowDocTypeDeclarations) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder documentBuilder = createDocumentBuilder(disAllowDocTypeDeclarations);
        Document doc = documentBuilder.parse(inputStream);
        repoolDocumentBuilder(documentBuilder);
        return doc;
    }

    private static DocumentBuilder createDocumentBuilder(boolean disAllowDocTypeDeclarations) throws ParserConfigurationException {
        int idx = getPoolsIndex(disAllowDocTypeDeclarations);
        return pools[idx].getObject();
    }

    private static boolean repoolDocumentBuilder(DocumentBuilder db) {
        if (!(db instanceof DocumentBuilderProxy)) {
            return false;
        }
        db.reset();
        boolean disAllowDocTypeDeclarations = ((DocumentBuilderProxy) db).disAllowDocTypeDeclarations();
        int idx = getPoolsIndex(disAllowDocTypeDeclarations);
        return pools[idx].repool(db);
    }

    /**
     * Maps the boolean configuration options for the factories to the array index for the WeakObjectPool
     *
     * @param disAllowDocTypeDeclarations
     * @return the index to the {@link #pools}
     */
    private static int getPoolsIndex(boolean disAllowDocTypeDeclarations) {
        return (disAllowDocTypeDeclarations ? 1 : 0);
    }

    private static final class DocumentBuilderPool extends WeakObjectPool<DocumentBuilder, ParserConfigurationException> {

        private final boolean disAllowDocTypeDeclarations;

        public DocumentBuilderPool(boolean disAllowDocTypeDeclarations) {
            this.disAllowDocTypeDeclarations = disAllowDocTypeDeclarations;
        }

        @Override
        protected DocumentBuilder createObject() throws ParserConfigurationException {
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            dfactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
            if (disAllowDocTypeDeclarations) {
                dfactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            }
            dfactory.setNamespaceAware(true);
            return new DocumentBuilderProxy(dfactory.newDocumentBuilder(), disAllowDocTypeDeclarations);
        }
    }

    /**
     * We need this proxy wrapping DocumentBuilder to record the value
     * passed to disAllowDoctypeDeclarations.  It's needed to figure out
     * on which pool to return.
     */
    private static class DocumentBuilderProxy extends DocumentBuilder {
        private final DocumentBuilder delegate;
        private final boolean disAllowDocTypeDeclarations;

        private DocumentBuilderProxy(DocumentBuilder actual, boolean disAllowDocTypeDeclarations) {
            delegate = actual;
            this.disAllowDocTypeDeclarations = disAllowDocTypeDeclarations;
        }

        boolean disAllowDocTypeDeclarations() {
            return disAllowDocTypeDeclarations;
        }

        @Override
        public void reset() {
            delegate.reset();
        }

        @Override
        public Document parse(InputStream is) throws SAXException, IOException {
            return delegate.parse(is);
        }

        @Override
        public Document parse(InputStream is, String systemId) throws SAXException, IOException {
            return delegate.parse(is, systemId);
        }

        @Override
        public Document parse(String uri) throws SAXException, IOException {
            return delegate.parse(uri);
        }

        @Override
        public Document parse(File f) throws SAXException, IOException {
            return delegate.parse(f);
        }

        @Override
        public Schema getSchema() {
            return delegate.getSchema();
        }

        @Override
        public boolean isXIncludeAware() {
            return delegate.isXIncludeAware();
        }

        @Override
        public Document parse(InputSource is) throws SAXException, IOException {
            return delegate.parse(is);
        }

        @Override
        public boolean isNamespaceAware() {
            return delegate.isNamespaceAware();
        }

        @Override
        public boolean isValidating() {
            return delegate.isValidating();
        }

        @Override
        public void setEntityResolver(EntityResolver er) {
            delegate.setEntityResolver(er);
        }

        @Override
        public void setErrorHandler(ErrorHandler eh) {
            delegate.setErrorHandler(eh);
        }

        @Override
        public Document newDocument() {
            return delegate.newDocument();
        }

        @Override
        public DOMImplementation getDOMImplementation() {
            return delegate.getDOMImplementation();
        }

    }

    /**
     * Abstract base class for pooling objects.  The two public methods are
     * {@link #getObject()} and ({@link #repool(Object)}.  Objects are held through
     * weak references so even objects that are not repooled are subject to garbage collection.
     * <p>
     * Subclasses must implement the abstract {@link #createObject()}.
     * <p>
     * <p>
     * Internally, the pool is stored in a java.util.concurrent.LinkedBlockingDeque
     * instance.
     *
     * @apiNote This class is no longer in use in Santuario 2.1.4, no longer use.
     */
    public abstract static class WeakObjectPool<T, E extends Throwable> {

        private static final Integer MARKER_VALUE = Integer.MAX_VALUE;//once here rather than auto-box it?

        /**
         * created, available objects to be checked out to clients
         */
        private final BlockingQueue<WeakReference<T>> available;

        /**
         * Synchronized, identity map of loaned out objects (WeakHashMap);
         * use to ensure we repool only object originating from here
         * and do it once.
         */
        private final Map<T, Integer> onLoan;

        /**
         * The lone constructor.
         */
        protected WeakObjectPool() {
            //alternative implementations: ArrayBlockingQueue has a fixed size
            //  PriorityBlockingQueue: requires a dummy comparator; less memory but more overhead
            available = new LinkedBlockingDeque<>();
            this.onLoan = Collections.synchronizedMap(new WeakHashMap<>());
        }

        /**
         * Called whenever a new pool object is desired; subclasses must implement.
         *
         * @return object of the type desired by the subclass
         * @throws E Throwable's subclass
         */
        protected abstract T createObject() throws E;


        /**
         * Subclasses can subclass to return a more specific type.
         *
         * @return an object from the pool; will block until an object is available
         * @throws E
         */
        public T getObject() throws E {
            WeakReference<T> ref;
            T retValue = null;
            do {
                //remove any stale entries as well
                ref = available.poll();
            } while (ref != null && (retValue = ref.get()) == null);

            if (retValue == null) {
                //empty pool; create & add new one
                retValue = createObject();
            }
            onLoan.put(retValue, MARKER_VALUE);
            return retValue;
        }


        /**
         * Adds the given object to the pool, provided that the object
         * was created by this pool.
         *
         * @param obj the object to return to the pool
         * @return whether the object was successfully added as available
         */
        public synchronized boolean repool(T obj) {
            if (obj != null && onLoan.containsKey(obj)) {
                if (onLoan.remove(obj) == null) {
                    return false;
                }
                return available.offer(new WeakReference<>(obj));
            }
            return false;
        }
    }
}
