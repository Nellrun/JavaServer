package pages;

import accounts.AccountService;
import accounts.UserProfile;
import org.springframework.context.ApplicationContext;
import tables.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by root on 11/13/16.
 */
public class SignUpPage extends HttpServlet {

    private final ApplicationContext context;

    public SignUpPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        resp.setContentType("text/html;charset=utf-8");

        if ( (login == null) || (password == null) || (login == "") || (password == "") ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");

        userDAO.create(login, password);
//
//        accountService.addNewUser(new UserProfile(login, password, login));

//        resp.getWriter().write(req.getSession().getId());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}