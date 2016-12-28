package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by root on 12/17/16.
 */
public class AnnouncementAddPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public AnnouncementAddPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        String token;
        int id;
        String text;
        int day;
        int month;
        int year;


        try {
            token = Checker.check(req.getParameter("token"), "token");
            id = Checker.toInt(req.getParameter("id"), "id");
            text = Checker.check(req.getParameter("text"), "text");
            day = Checker.toInt(req.getParameter("day"), "day", 1, 31);
            month = Checker.toInt(req.getParameter("month"), "month", 1, 12);
            year = Checker.toInt(req.getParameter("year"), "year", 2016, 2017);
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
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");

        int userID = userDAO.getIDByLogin(login);

        AnnouncementDAO announcementDAO = (AnnouncementDAO) context.getBean("AnnouncementDAO");

        String date = Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day);

        announcementDAO.create(id, date, 0, userID, text);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
