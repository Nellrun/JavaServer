package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.PairDoesntExitsts;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import tables.Schedule;
import tables.ScheduleDAO;
import tables.Teacher;
import tables.TeacherDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 12/16/16.
 */
public class ScheduleDelPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public ScheduleDelPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        String token;
        int id;

        try {
            token = Checker.check(req.getParameter("token"), "token");
            id = Checker.toInt(req.getParameter("id"), "id");
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

        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");

        Teacher teacher;

        try {
            teacher = teacherDAO.getTeacherByLogin(login);
        }
        catch (EmptyResultDataAccessException e) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ScheduleDAO scheduleDAO = (ScheduleDAO) context.getBean("ScheduleDAO");
        Schedule s;

        try {
            s = scheduleDAO.getScheduleByID(id);
        }
        catch (EmptyResultDataAccessException e) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new BadParameterFormatError("id"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (s.getTeacherID() != teacher.getId() && s.getDate() == null) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        scheduleDAO.deleteByID(id);

    }
}
