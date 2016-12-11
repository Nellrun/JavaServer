package pages;

import com.google.gson.GsonBuilder;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import org.springframework.context.ApplicationContext;
import tables.StudentDAO;
import tables.TeacherDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by root on 12/11/16.
 */
public class TeacherGetPage extends HttpServlet {
    private final ApplicationContext context;

    public TeacherGetPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sID = req.getParameter("id");

        resp.setContentType("text/html;charset=utf-8");

        if (sID == null) {
            MissingParameterError missingParameterError = new MissingParameterError("id");
            String out = new GsonBuilder().create().toJson(missingParameterError);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = 0;

        try {
            id = Integer.valueOf(sID);
        }
        catch (Exception e) {
            BadParameterFormatError bpfe = new BadParameterFormatError("id");
            String out = new GsonBuilder().create().toJson(bpfe);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");
        String out = null;

        try {
            out = new GsonBuilder().create().toJson(teacherDAO.getTeacherByID(id));
        }
        catch (Exception e) {
            out = new GsonBuilder().create().toJson(new BadParameterFormatError("id"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.getWriter().write(out);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
