package mysql_performance.rest;

import mysql_performance.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zinchenko on 26.09.14.
 */
@Controller
@RequestMapping("/api")
public class Api {

    @Autowired
    private Service service;

    @RequestMapping("/get")
    @ResponseBody
    public void get() {
        service.tGet();
        System.out.println("get");
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
