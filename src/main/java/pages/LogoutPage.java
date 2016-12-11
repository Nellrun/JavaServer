package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by root on 12/6/16.
 */
public class LogoutPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;

    public LogoutPage(HashMap<String, String> stl) {
        this.sessionToLogin = stl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");

        if (token == null) {
            String out = new GsonBuilder().create().toJson(new BadParameterFormatError("token"));
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
