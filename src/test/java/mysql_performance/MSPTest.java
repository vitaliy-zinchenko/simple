package mysql_performance;

/**
 * Created by zinchenko on 25.06.14.
 */

import mysql_performance.entity.Role;
import mysql_performance.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:mysql_performance/dao-appContext.xml"})
public class MSPTest {

    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void before(){
    }

    @Test
//    @Transactional
    public void prepare(){
        System.out.println("Saving....");
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        int n = 100000;
        for (int i = 0; i < n; i++) {
            User user = new User();
            user.setName("name-" + System.currentTimeMillis());

            Role role = new Role();
            role.setName("role_name-" + System.currentTimeMillis());

            session.save(role);

            user.setRole(role);

            session.save(user);
//            System.out.println("saved: " + user.toString());
        }
        transaction.commit();
        System.out.println("Prepared.");
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save() {
        Role role = new Role();
        role.setName("role_name-" + System.currentTimeMillis());
        sessionFactory.getCurrentSession().save(role);
        System.out.println("end");
    }

    @Test
    @Transactional
    public void test(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        //sessionFactory.getCurrentSession().createQuery("select User as user where user.role.name = 'role_name-1410348783381'").list();

        stopWatch.stop();
        System.out.println("time: " + stopWatch.getTotalTimeMillis());
    }


    @Test
    @Transactional
    public void testGetNewUserEntity(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<NewUser> newUsers = sessionFactory.getCurrentSession().createCriteria(NewUser.class).list();
        System.out.println(newUsers.toString());

        stopWatch.stop();
        System.out.println("time: " + stopWatch.getTotalTimeMillis());
    }

    @Test
    @Transactional
    public void testGetNewProfileEntity(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<NewUser> newUsers = sessionFactory.getCurrentSession().createCriteria(NewProfile.class).list();
        System.out.println(newUsers.toString());

        stopWatch.stop();
        System.out.println("time: " + stopWatch.getTotalTimeMillis());
    }

    @Test
    @Transactional
    public void save2(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        NewUser user = new NewUser();
        user.setId(4);

        NewProfile newProfile = new NewProfile();
        newProfile.setName("nnnn");
        newProfile.setUser(user);
        newProfile.setUserId(4);

        sessionFactory.getCurrentSession().save(newProfile);
//        System.out.println(newUsers.toString());

        stopWatch.stop();
        System.out.println("time: " + stopWatch.getTotalTimeMillis());
    }

}
