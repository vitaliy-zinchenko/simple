package zinchenko;

/**
 * Created by zinchenko on 25.06.14.
 */

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import zinchenko.MainController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:zinchenko/dao-appContext.xml"})
//@DatabaseSetup("classpath:zinchenko/dataset.xml")
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
//        DbUnitTestExecutionListener.class})
public class TTest {


//    @Autowired
    MainController mainController;

    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void before(){
        mainController = new MainController();
        mainController.setSessionFactory(sessionFactory);
    }

    @Test
    @Transactional
    public void test(){
        mainController.fill("first");
        System.out.println(mainController.get());
//        System.out.println();
    }


}
