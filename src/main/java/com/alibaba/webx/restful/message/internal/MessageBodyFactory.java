/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.alibaba.webx.restful.message.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.WriterInterceptor;

import com.alibaba.webx.restful.internal.inject.ServiceProviders;
import com.alibaba.webx.restful.message.MessageBodyWorkers;

/**
 * A factory for managing {@link MessageBodyReader} and {@link MessageBodyWriter} instances.
 * <p/>
 * Note: {@link MessageBodyReader} and {@link MessageBodyWriter} implementation must not inject the instance of this
 * type directly, e.g. {@code @Inject MessageBodyWorkers w;}. Instead a {@link Factory}-based injection should be used
 * to prevent cycles in the injection framework caused by the eager initialization of the providers in the current
 * factory implementation: {@code @Inject Factory<MessageBodyWorkers> w;}
 * 
 * @author Paul Sandoz
 * @author Marek Potociar (marek.potociar at oracle.com)
 * @author Jakub Podlesak (jakub.podlesak at oracle.com)
 */
// FIXME: Remove the note from the javadoc once the issue is fixed.
public class MessageBodyFactory implements MessageBodyWorkers {

    private final ServiceProviders                  serviceProviders;
    private Map<MediaType, List<MessageBodyReader>> readerProviders;
    private Map<MediaType, List<MessageBodyWriter>> writerProviders;
    private List<MessageBodyWriterPair>             writerListProviders;
    private Map<MediaType, List<MessageBodyReader>> customReaderProviders;
    private Map<MediaType, List<MessageBodyWriter>> customWriterProviders;
    private List<MessageBodyWriterPair>             customWriterListProviders;
    private Set<ReaderInterceptor>                  readerInterceptors;
    private Set<WriterInterceptor>                  writerInterceptors;

    @Override
    public Set<ReaderInterceptor> getReaderInterceptors() {
        return readerInterceptors;
    }

    @Override
    public Set<WriterInterceptor> getWriterInterceptors() {
        return writerInterceptors;
    }

    private static class MessageBodyWriterPair {

        final MessageBodyWriter<?> mbw;
        final List<MediaType>      types;

        MessageBodyWriterPair(MessageBodyWriter<?> mbw, List<MediaType> types){
            this.mbw = mbw;
            this.types = types;
        }
    }


    public MessageBodyFactory(ServiceProviders serviceProviders){
        this.serviceProviders = serviceProviders;

        initReaders();
        initWriters();
        initInterceptors();
    }

    private void initInterceptors() {
        // TODO
    }

    private void initReaders() {
        // TODO
    }

    private void initWriters() {
        // TODO
    }

    @Override
    public Map<MediaType, List<MessageBodyReader>> getReaders(MediaType mediaType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<MediaType, List<MessageBodyWriter>> getWriters(MediaType mediaType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String readersToString(Map<MediaType, List<MessageBodyReader>> readers) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String writersToString(Map<MediaType, List<MessageBodyWriter>> writers) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> MessageBodyReader<T> getMessageBodyReader(Class<T> type, Type genericType, Annotation[] annotations,
                                                         MediaType mediaType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<T> type, Type genericType, Annotation[] annotations,
                                                         MediaType mediaType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<MediaType> getMessageBodyReaderMediaTypes(Class<T> type, Type genericType, Annotation[] annotations) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> List<MediaType> getMessageBodyWriterMediaTypes(Class<T> type, Type genericType, Annotation[] annotations) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> MediaType getMessageBodyWriterMediaType(Class<T> type, Type genericType, Annotation[] annotations,
                                                       List<MediaType> acceptableMediaTypes) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> Object readFrom(GenericType<T> genericType, Annotation[] annotations, MediaType mediaType,
                               MultivaluedMap<String, String> httpHeaders, Map<String, Object> properties,
                               InputStream entityStream, boolean intercept) throws WebApplicationException, IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> void writeTo(Object entity, GenericType<T> genericType, Annotation[] annotations, MediaType mediaType,
                            MultivaluedMap<String, Object> httpHeaders, Map<String, Object> properties,
                            OutputStream entityStream, MessageBodySizeCallback sizeCallback, boolean intercept)
                                                                                                               throws IOException,
                                                                                                               WebApplicationException {
        // TODO Auto-generated method stub
        
    }

}
