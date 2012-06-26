package com.alibaba.webx.restful.examples.helloworld;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;

@Path("orders/{id}")
public class OrdersResource {

    @Autowired
    private OrderService service;

    @PathParam("id")
    private int          id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderService getService() {
        return service;
    }

    public void setService(OrderService service) {
        this.service = service;
    }

    @GET
    public Order getOrder() {
        return service.findOrder(id);
    }
}
