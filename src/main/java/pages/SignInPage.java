package pages;

import accounts.AccountService;
import accounts.UserProfile;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tables.User;
import tables.UserDAO;

import javax.jws.HandlerChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

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
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");

        User u = userDAO.getUserByLogin(login);

        if ( (u == null) || (!u.getPassword().equals(password)) ) {
            resp.setStatus(401);
            return;
        }

        SessionToLogin.put(req.getSession().getId(), login);

        String ans = "{\"token\": \"" + req.getSession().getId() + "\"}";

        resp.getWriter().write(ans);
        resp.setStatus(200);

    }
}
