package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.MissingParameterError;
import org.springframework.context.ApplicationContext;
import tables.UserDAO;
import tables.UserInfoDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by root on 12/13/16.
 */
public class UserChangeNamePage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public UserChangeNamePage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        String middleName = req.getParameter("middleName");

        resp.setContentType("text/html;charset=utf-8");

        if (token == null || firstName == null || secondName == null || middleName == null) {
            String out;

            if (token == null) {
                out = new GsonBuilder().create().toJson(new MissingParameterError("token"));
            }
            else if (firstName == null) {
                out = new GsonBuilder().create().toJson(new MissingParameterError("firstName"));
            }
            else if (secondName == null) {
                out = new GsonBuilder().create().toJson(new MissingParameterError("secondName"));
            }
            else {
                out = new GsonBuilder().create().toJson(new MissingParameterError("middleName"));
            }
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        if (!sessionToLogin.containsKey(token)) {
            String out = new GsonBuilder().create().toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");

        int id = userDAO.getIDByLogin(sessionToLogin.get(token));

        userInfoDAO.update(id, firstName, secondName, middleName);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
