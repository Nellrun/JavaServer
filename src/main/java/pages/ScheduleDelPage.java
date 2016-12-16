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
        int groupID;
        int n;
        int day;
        int month;
        int year;

        try {
            token = Checker.check(req.getParameter("token"), "token");
            groupID = Checker.toInt(req.getParameter("groupID"), "groupID");
            n = Checker.toInt(req.getParameter("n"), "n", 1, 7);
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

//        Schedule schedule;

        List<Schedule> s = scheduleDAO.getScheduleByGroup(groupID);

        for (int i = 0; i < s.size(); i++) {
            Schedule schedule = s.get(i);
            if (schedule.getDate() != null && schedule.getnPair() == n) {
                scheduleDAO.deleteByID(schedule.getId());
                break;
            }
        }

//        try {
//            schedule = scheduleDAO.getScheduleByGroupIDAndN(groupID, n);
//        }
//        catch (EmptyResultDataAccessException e) {
//            String out = new GsonBuilder()
//                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
//                    .create()
//                    .toJson(new PairDoesntExitsts());
//            resp.getWriter().write(out);
//            resp.setStatus(HttpServletResponse.SC_CONFLICT);
//            return;
//        }

    }
}
