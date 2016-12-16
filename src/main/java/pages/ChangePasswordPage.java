package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
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

        try {
            old_password = Checker.check(req.getParameter("old_password"), "old_password", 30);
            password = Checker.check(req.getParameter("new_password"), "new_password", 30);
            token = Checker.check(req.getParameter("token"), "token");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        if (password.equals("")) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new BadParameterFormatError("password"));
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
