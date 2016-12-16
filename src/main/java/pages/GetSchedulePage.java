package pages;

import com.google.gson.GsonBuilder;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.Schedule;
import tables.ScheduleDAO;
import tables.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by root on 12/5/16.
 * Страница для получения расписания как для группы, так и для преподавателя
 */
public class GetSchedulePage extends HttpServlet{
    private final ApplicationContext context;

    public GetSchedulePage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        int id;
        int type;

        try {
            id = Checker.toInt(req.getParameter("id"), "id");
            type = Checker.toInt(req.getParameter("type"), "type", 0, 1);
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ScheduleDAO scheduleDAO = (ScheduleDAO) context.getBean("ScheduleDAO");

        if (type == 0) {
//            Group
            List<Schedule> scheduleList = scheduleDAO.getScheduleByGroup(id);
            String out = new GsonBuilder().create().toJson(scheduleList);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }
        else {
//            Teacher
            List<Schedule> scheduleList = scheduleDAO.getScheduleByTeacher(id);
            String out = new GsonBuilder().create().toJson(scheduleList);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }

    }
}
