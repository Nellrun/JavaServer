package pages;

import com.google.gson.GsonBuilder;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import org.springframework.context.ApplicationContext;
import tables.Teacher;
import tables.TeacherDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 12/11/16.
 */
public class TeacherSearchPage extends HttpServlet {
    private final ApplicationContext context;

    public TeacherSearchPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        resp.setContentType("text/html;charset=utf-8");

        if (name == null) {
            MissingParameterError missingParameterError = new MissingParameterError("name");
            String out = new GsonBuilder().create().toJson(missingParameterError);

            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (name.equals("")) {
            String out = new GsonBuilder().create().toJson(new BadParameterFormatError("name"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");

        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        for(String word: name.split(" ")) {
            teachers.addAll(teacherDAO.getTeachersByName(word));
        }

        String out = new GsonBuilder().create().toJson(teachers);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(out);
    }
}
