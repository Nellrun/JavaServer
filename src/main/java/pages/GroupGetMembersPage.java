package pages;

import com.google.gson.GsonBuilder;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.Student;
import tables.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by root on 12/11/16.
 */
public class GroupGetMembersPage extends HttpServlet {
    private final ApplicationContext context;

    public GroupGetMembersPage(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        int id;

        try {
            id = Checker.toInt(req.getParameter("id"), "id");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");
        List<Student> students = studentDAO.getStudentsByGroupID(id);

        String out = new GsonBuilder().create().toJson(students);
        resp.getWriter().write(out);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
