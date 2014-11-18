package multi.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zinchenko on 17.11.14.
 */
public class MultiSpringMain {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/multi/spring/context.xml");

        System.out.println("get b bean from context");
        Service service = (Service) applicationContext.getBean("b");

        System.out.println("call m() method");
        service.m();
        System.out.println("called m() method");

        System.out.println("main end");
    }


}
