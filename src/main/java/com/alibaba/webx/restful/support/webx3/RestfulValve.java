package com.alibaba.webx.restful.support.webx3;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectweb.asm.ClassReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.Valve;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.webx.WebxComponent;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.webx.restful.model.ApplicationImpl;
import com.alibaba.webx.restful.model.Resource;
import com.alibaba.webx.restful.model.ResourceMethod;
import com.alibaba.webx.restful.model.finder.AnnotatedClassVisitor;
import com.alibaba.webx.restful.model.finder.ClassInfo;
import com.alibaba.webx.restful.model.param.ParameterProviderImpl;
import com.alibaba.webx.restful.process.ApplicationHandler;
import com.alibaba.webx.restful.process.RestfulComponent;
import com.alibaba.webx.restful.process.impl.ContainerRequestContextImpl;
import com.alibaba.webx.restful.process.impl.UriInfoImpl;
import com.alibaba.webx.restful.spi.ParameterProvider;
import com.alibaba.webx.restful.util.ResourceUtils;

public class RestfulValve implements Valve {

    private final static Log          LOG              = LogFactory.getLog(RestfulValve.class);

    @Autowired
    private HttpServletRequest        request;

    @Autowired
    private HttpServletResponse       response;

    @Autowired
    private WebxComponent             component;

    private volatile RestfulComponent restfulComponent = null;

    public RestfulValve(){

    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public WebxComponent getComponent() {
        return component;
    }

    public void setComponent(WebxComponent component) {
        this.component = component;
    }

    private synchronized void init() {
        if (restfulComponent != null) {
            return;
        }

        WebApplicationContext applicationContext = component.getApplicationContext();
        ApplicationImpl config = new ApplicationImpl();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanClass = applicationContext.getType(beanName);
            Object bean = applicationContext.getBean(beanName);
            Path pathAnnotation = beanClass.getAnnotation(Path.class);
            if (pathAnnotation == null) {
                continue;
            }

            if (!ResourceUtils.isAcceptable(beanClass)) {
                continue;
            }

            buildResource(config, beanClass, bean);
        }

        restfulComponent = new RestfulComponent(config, applicationContext);
    }

    @SuppressWarnings("unchecked")
    private void buildResource(ApplicationImpl config, Class<?> beanClass, Object bean) {
        String className = beanClass.getName();
        String resourceName = className.replace('.', '/') + ".class";
        InputStream in = null;

        try {
            in = beanClass.getClassLoader().getResourceAsStream(resourceName);

            AnnotatedClassVisitor classVisitor = new AnnotatedClassVisitor();

            ClassReader classReader = new ClassReader(in);
            classReader.accept(classVisitor, 0);

            ClassInfo classInfo = classVisitor.getClassInfo();

            WebApplicationContext applicationContext = component.getApplicationContext();
            ParameterProvider parameterProvider = new ParameterProviderImpl(applicationContext);
            Resource resource = ResourceUtils.buildResource(applicationContext, parameterProvider, beanClass,
                                                            classInfo, bean);
            if (resource != null) {
                config.addResource(resource);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.close(in);
        }
    }

    @Override
    public void invoke(PipelineContext pipelineContext) throws Exception {
        if (restfulComponent == null) {
            init();
        }

        TurbineRunData rundata = (TurbineRunData) getTurbineRunData(request);

        ApplicationHandler handler = restfulComponent.getHandler();

        UriInfoImpl uriInfo = new UriInfoImpl(rundata.getRequest(), rundata.getTarget());

        ContainerRequestContextImpl requestContext = handler.createRequestContext(request, response, uriInfo);

        ResourceMethod resourceMethod = requestContext.getResourceMethod();

        if (resourceMethod != null) {
            handler.service(requestContext);
        } else {
            pipelineContext.invokeNext();
        }
    }

}
