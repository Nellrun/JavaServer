package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.PairAlreadyExistsError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by root on 12/16/16.
 */
public class ScheduleAddPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;
    private String[] types = {"Лекция", "Практика", "Лабораторная работа"};

    public ScheduleAddPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        String token;
        int groupID;
        int subjectID;
        int n;
        int type;
        String room;
        int day;
        int month;
        int year;

        try {
            token = Checker.check(req.getParameter("token"), "token");
            groupID = Checker.toInt(req.getParameter("groupID"), "groupID");
            subjectID = Checker.toInt(req.getParameter("subjectID"), "subjectID");
            n = Checker.toInt(req.getParameter("n"), "n", 1, 7);
            type = Checker.toInt(req.getParameter("type"), "type", 0, 2);
            room = Checker.check(req.getParameter("room"), "room");
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

        SubjectDAO subjectDAO = (SubjectDAO) context.getBean("SubjectDAO");
        GroupDAO groupDAO = (GroupDAO) context.getBean("GroupDAO");

        try {
            subjectDAO.getSubjectByID(subjectID);
        }
        catch (EmptyResultDataAccessException e) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new BadParameterFormatError("subjectID"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            groupDAO.getGroupByID(groupID);
        }
        catch (EmptyResultDataAccessException e) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new BadParameterFormatError("groupID"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month - 1, day);

        if (gregorianCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new BadParameterFormatError("year, month, day"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GregorianCalendar calendar = new GregorianCalendar(month > 8? year : year - 1, Calendar.SEPTEMBER, 1);
        Integer dayDifference = calendar.get(Calendar.DAY_OF_WEEK) == 1 ? -6 : (Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DAY_OF_MONTH, dayDifference);

        int dif = ( (int) ( (gregorianCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / (24 * 60 * 60 * 1000) ) * 7 ) % 98;
        dif = dif > 35? dif - 7: dif;

        int nPair = dif + n;

        ScheduleDAO scheduleDAO = (ScheduleDAO) context.getBean("ScheduleDAO");

        String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);

        try{
            scheduleDAO.getScheduleByTeacherIDAndN(teacher.getId(), nPair);
        }
        catch (EmptyResultDataAccessException e) {
            scheduleDAO.create(subjectID, this.types[type], groupID, teacher.getId(), nPair, room, date);
            return;
        }

        String out = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PRIVATE)
                .create()
                .toJson(new PairAlreadyExistsError());

        resp.getWriter().write(out);
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
    }
}
