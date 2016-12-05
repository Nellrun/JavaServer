package pages;

import org.springframework.context.ApplicationContext;
import tables.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by root on 12/4/16.
 */
public class ChangePasswordPage extends HttpServlet {

    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public ChangePasswordPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String token = req.getParameter("token");

        if ( (login == null) || (password == null) || (token == null) ||
                (password.equals("")) || (token.equals("")) ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ( (!sessionToLogin.containsKey(token)) || (!sessionToLogin.get(token).equals(login)) ){
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        userDAO.update(login, password);

        resp.setStatus(HttpServletResponse.SC_OK);
        return;
    }
}
