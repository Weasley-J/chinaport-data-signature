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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Collections;
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
    private static final String dsPrefix = "ds";
    private static final String ds11Prefix = "dsig11";
    private static final String xencPrefix = "xenc";
    private static final String xenc11Prefix = "xenc11";

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

        public void reset() {
            delegate.reset();
        }

        public Document parse(InputStream is) throws SAXException, IOException {
            return delegate.parse(is);
        }

        public Document parse(InputStream is, String systemId) throws SAXException, IOException {
            return delegate.parse(is, systemId);
        }

        public Document parse(String uri) throws SAXException, IOException {
            return delegate.parse(uri);
        }

        public Document parse(File f) throws SAXException, IOException {
            return delegate.parse(f);
        }

        public Schema getSchema() {
            return delegate.getSchema();
        }

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
        public boolean repool(T obj) {
            if (obj != null && onLoan.containsKey(obj)) {
                //synchronize to protect against a caller returning the same object again...
                synchronized (obj) {
                    //...and check to see that it was removed
                    if (onLoan.remove(obj) != null) {
                        return available.offer(new WeakReference<T>(obj));
                    }
                }
            }
            return false;
        }
    }
}
