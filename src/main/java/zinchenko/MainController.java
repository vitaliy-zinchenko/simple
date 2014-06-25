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
    public String session(HttpServletRequest request, HttpServletResponse response){
        System.out.println("session()");
        HttpSession session = request.getSession();
        return session.getId();
    }

    @RequestMapping(value = "/get")
    @ResponseBody
    @Transactional
    public String get(HttpServletRequest request, HttpServletResponse response){
        System.out.println("get()");
        List<Bean> beans = sessionFactory.getCurrentSession().createCriteria(Bean.class).list();
        return beans.toString();
    }

    @RequestMapping(value = "/fill/{value}")
    @ResponseBody
    @Transactional
    public String fill(HttpServletRequest request, HttpServletResponse response,
                       @PathVariable("value") String value){
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
