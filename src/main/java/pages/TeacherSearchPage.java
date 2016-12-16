package pages;

import com.google.gson.GsonBuilder;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.Teacher;
import tables.TeacherDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
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
        resp.setContentType("text/json;charset=utf-8");

        String name;

        try {
            name = Checker.check(req.getParameter("name"), "name");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
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
