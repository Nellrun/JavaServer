package pages;

import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import tables.Schedule;
import tables.ScheduleDAO;
import tables.Teacher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 12/5/16.
 */
public class GetSchedulePage extends HttpServlet{
    private final ApplicationContext context;

    public GetSchedulePage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sId = req.getParameter("id");
        String type = req.getParameter("type");

        resp.setContentType("text/html;charset=utf-8");

        if ( (sId == null) || (sId.equals("")) || (type == null) ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id;
        try {
            id = Integer.valueOf(sId);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        ScheduleDAO scheduleDAO = (ScheduleDAO) context.getBean("ScheduleDAO");

        if (type.equals("0")) {
//            Group
            List<Schedule> scheduleList = scheduleDAO.getScheduleByGroup(id);
            String out = new GsonBuilder().create().toJson(scheduleList);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }

        if (type.equals("1")) {
//            Teacher
            List<Schedule> scheduleList = scheduleDAO.getScheduleByTeacher(id);
            String out = new GsonBuilder().create().toJson(scheduleList);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;

//        super.doGet(req, resp);
    }
}
