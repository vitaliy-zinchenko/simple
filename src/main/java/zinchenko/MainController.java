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
@RequestMapping("/controller")
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

    @RequestMapping(value = "/getById/{id}")
    @ResponseBody
    @Transactional
    public String getById(@PathVariable("id") Long id){
        System.out.println("getById()");
        Bean bean = (Bean) sessionFactory.getCurrentSession().load(Bean.class, id);
        return bean.toString();
    }

    @RequestMapping(value = "/getByIdQ/{id}")
    @ResponseBody
    @Transactional
    public String getByIdQ(@PathVariable("id") Long id){
        System.out.println("getById()");
        Bean bean = (Bean) sessionFactory.getCurrentSession().createQuery("from Bean b where b.id = :id").setLong("id", id).uniqueResult();
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

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
