package zinchenko;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zinchenko on 09.06.14.
 */
public class QuartzMain {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("quartz.xml");
    }
}
