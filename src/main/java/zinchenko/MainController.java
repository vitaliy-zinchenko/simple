package zinchenko;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by zinchenko on 15.06.14.
 */
@Controller
public class MainController {

    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String session(HttpServletRequest request){
        System.out.println("session()");
        HttpSession session = request.getSession();
        return session.getId();
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    @Transactional
    public String get(){
        System.out.println("get()");
        List<Bean> beans = sessionFactory.getCurrentSession().createCriteria(Bean.class).list();
        return beans.toString();
    }

    @RequestMapping(value = "/getByIdLoad/{id}")
    @ResponseBody
    @Transactional
    public String getByIdLoad(@PathVariable("id") Long id){
        System.out.println("getById()");
        Bean bean = (Bean) sessionFactory.getCurrentSession().load(Bean.class, id);
        return bean.toString();
    }

    @RequestMapping(value = "/getByIdGet/{id}")
    @ResponseBody
    @Transactional
    public String getByIdGet(@PathVariable("id") Long id){
        System.out.println("getById()");
        Bean bean = (Bean) sessionFactory.getCurrentSession().get(Bean.class, id);
        return bean.toString();
    }

    @RequestMapping(value = "/getByIdQ/{id}")
    @ResponseBody
    @Transactional
    public String getByIdQ(@PathVariable("id") Long id){
        System.out.println("getById()");
        Bean bean = (Bean) sessionFactory.getCurrentSession().createQuery("from Bean b where b.id = :id").setCacheable(true).setLong("id", id).uniqueResult();
        return bean.toString();
    }

    @RequestMapping(value = "/fill/{value}")
    @ResponseBody
    @Transactional
    public String fill(@PathVariable("value") String value){
        System.out.println("fill()");
        sessionFactory.getCurrentSession().save(new Bean(value));
        return "filled";
    }

    @RequestMapping(value = "/getByIdSubBean/{id}")
    @ResponseBody
    @Transactional
    public String getByIdSubBean(@PathVariable("id") Long id){
        System.out.println("getById()");
        Bean bean = (Bean) sessionFactory.getCurrentSession().get(SubBean.class, id);
        return bean.toString();
    }

    @RequestMapping(value = "/saveSub/{value}")
    @ResponseBody
    @Transactional
    public String saveSub(@PathVariable("value") String value){
        System.out.println("saveSub()");
        SubBean subBean = new SubBean();
        subBean.setValue(value);
        Long id = (Long) sessionFactory.getCurrentSession().save(subBean);
        System.out.println("end saveSub()");
        return String.valueOf(id);
    }

    @RequestMapping(value = "/test/{id}/{value}")
    @ResponseBody
    @Transactional
    public String test(@PathVariable("id") Long id, @PathVariable("value") String value) throws InterruptedException {
        System.out.println("test()");


        SubBean subBean2 = (SubBean) sessionFactory.getCurrentSession().get(SubBean.class, id);
//        subBean2.setId(id);
        subBean2.setValue(value);
        sessionFactory.getCurrentSession().update(subBean2);

        System.out.println("updated");
        Thread.sleep(3000);
        System.out.println("exit test()");
        return "OK";
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
