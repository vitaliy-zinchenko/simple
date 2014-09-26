package mysql_performance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * Created by zinchenko on 18.09.14.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext appContext =
                new ClassPathXmlApplicationContext("classpath:mysql_performance/dao-appContext.xml");

        Service service = (Service)appContext.getBean("service");
//        service.saveTest();
        service.fill();
//        service.fillNewUser();

//        service.t();
//        service.t2();
//        service.tGet();




    }
}
