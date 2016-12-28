package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by root on 12/28/16.
 */
public class AnnouncementGetPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public AnnouncementGetPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token;
        int count;

        try {
            token = Checker.check(req.getParameter("token"), "token");
            count = Checker.toInt(req.getParameter("count"), "count");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!sessionToLogin.containsKey(token)) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String login = sessionToLogin.get(token);



    }
}
