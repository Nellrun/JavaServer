package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
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
 * Страница для смены пароля для пользователя
 * На вход принимает старый пароль, новый пароль и token пользователя
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
        String old_password = req.getParameter("old_password");
        String password = req.getParameter("new_password");
        String token = req.getParameter("token");

        if ( (old_password == null) || (password == null) || (token == null) ||
                (password.equals("")) || (token.equals("")) ) {

            BadParameterFormatError badParameterFormatError;

            if ( (old_password == null) || old_password.equals("") ) {
                badParameterFormatError = new BadParameterFormatError("old_password");
            }
            else if ( (password == null) || (password.equals("")) ) {
                badParameterFormatError = new BadParameterFormatError("password");
            }
            else {
                badParameterFormatError = new BadParameterFormatError("token");
            }

            String out = new GsonBuilder().create().toJson(badParameterFormatError);
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

        String login = sessionToLogin.get(token);

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        if (!userDAO.getUserByLogin(login).getPassword().equals(old_password)) {
            String out = new GsonBuilder().create().toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        userDAO.update(login, password);

        resp.setStatus(HttpServletResponse.SC_OK);
        return;
    }
}
