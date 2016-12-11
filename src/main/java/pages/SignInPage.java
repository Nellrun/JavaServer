package pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errors.AuthError;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import errors.UserAlreadyExistsError;
import org.springframework.context.ApplicationContext;
import tables.User;
import tables.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by root on 11/13/16.
 */

public class SignInPage extends HttpServlet {

    private final HashMap<String, String> SessionToLogin;
    private final ApplicationContext context;

    public SignInPage(ApplicationContext context, HashMap<String, String> stl) {
        this.SessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        resp.setContentType("text/html;charset=utf-8");
//
        if ( (login == null) || (password == null) )  {
            MissingParameterError mpe = new MissingParameterError(login == null ? "login" : "password");
            String out = new GsonBuilder().create().toJson(mpe);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ( login.equals("") || password.equals("") ) {
            BadParameterFormatError bpfe = new BadParameterFormatError(login.equals("") ? "login" : "password");
            String out = new GsonBuilder().create().toJson(bpfe);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");

        User u = null;

        try {
            u = userDAO.getUserByLogin(login);
        }
        catch (Exception e) {

        }

        if ( (u == null) || (!u.getPassword().equals(password)) ) {
            String out = new GsonBuilder().create().toJson(new AuthError());
            resp.getWriter().write(out);
            resp.setStatus(401);
            return;
        }

        SessionToLogin.put(req.getSession().getId(), login);

        String ans = "{\"token\": \"" + req.getSession().getId() + "\"}";

        resp.getWriter().write(ans);
        resp.setStatus(200);

    }
}
