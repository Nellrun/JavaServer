package pages;

import accounts.AccountService;
import accounts.UserProfile;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.springframework.context.ApplicationContext;
import tables.UserDAO;
import tables.UserInfoDAO;

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

        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        String middleName = req.getParameter("middleName");

        if (firstName == null) {
            firstName = "";
        }

        if (secondName == null) {
            secondName = "";
        }

        if (middleName == null) {
            middleName = "";
        }

        resp.setContentType("text/html;charset=utf-8");

        if ( (login == null) || (password == null) || (login == "") || (password == "") ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");

        try {
            userDAO.create(login, password);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        Integer userID = userDAO.getIDByLogin(login);

        userInfoDAO.create(userID, firstName, secondName, middleName);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}