package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.ParameterError;
import main.Checker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by root on 12/6/16.
 * Страница для выхода из ученой записи пользователя
 * принимает на вход token пользователя и которого требуется выйти
 */
public class LogoutPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;

    public LogoutPage(HashMap<String, String> stl) {
        this.sessionToLogin = stl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        String token;

        try {
            token = Checker.check(req.getParameter("token"), "token");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
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

        sessionToLogin.remove(token);

        resp.setStatus(HttpServletResponse.SC_OK);
        return;

    }
}
