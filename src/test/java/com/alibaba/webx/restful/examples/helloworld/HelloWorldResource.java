package com.alibaba.webx.restful.examples.helloworld;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

@Path("helloworld")
public class HelloWorldResource {

    @Autowired(required=true)
    private HelloWorldService service;

    @GET
    @Produces("text/plain")
    public String getHello() {
        return "Hello World!";
    }
    
    public Date now() {
        return service.now();
    }

}
