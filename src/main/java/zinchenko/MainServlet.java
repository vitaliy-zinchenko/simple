package zinchenko;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by zinchenko on 31.05.14.
 */
public class MainServlet extends HttpServlet {

    private static String SESSION_VALUE_KEY = "value";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        String sessionId = session.getId();
        Date creationTime = new Date(session.getCreationTime());
        String value = (String) req.getParameter(SESSION_VALUE_KEY);
        String realPath = getServletContext().getRealPath("");

        System.out.println("~~~~~~~~~~~ request ~~~~~~~~~~~");
        System.out.println("sessionId = " + sessionId);
        System.out.println("creationTime = " + creationTime);
        System.out.println("realPath = " + realPath);

        if(value != null){
            session.setAttribute(SESSION_VALUE_KEY, value);
        }

       String valueFromSession = (String) session.getAttribute(SESSION_VALUE_KEY);

        PrintWriter writer = resp.getWriter();
        writer.append("response. value from session: ");
        writer.append(valueFromSession);
        writer.append('\n');
        writer.append("sessionId = " + sessionId);
        writer.append('\n');
        writer.append("creationTime = " + creationTime);
        writer.append('\n');
        writer.append("realPath = " + realPath);
        writer.close();
    }
}
