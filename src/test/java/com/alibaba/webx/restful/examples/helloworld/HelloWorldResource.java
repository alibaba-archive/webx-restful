package com.alibaba.webx.restful.examples.helloworld;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

@Path("helloworld")
public class HelloWorldResource {

    private HttpServletRequest request;

    @Autowired(required = true)
    private HelloWorldService  service;

    public HelloWorldResource(HttpServletRequest request){
        this.request = request;
    }

    public HttpServletRequest getHttpRequest() {
        return request;
    }

    public HelloWorldService getService() {
        return service;
    }

    public void setService(HelloWorldService service) {
        this.service = service;
    }

    @GET
    @Produces("text/plain")
    public String getHello() {
        return "Hello World!";
    }

    @Path("now")
    @GET
    @Produces("text/plain")
    public Date now() {
        Date now = service.now();
        return now;
    }

    @Path("f")
    @GET
    @Produces("text/plain")
    public short f1(byte b1) {
        return b1;
    }

}
