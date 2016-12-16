package pages;

import com.google.gson.GsonBuilder;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.StudentDAO;
import tables.UserInfoDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Created by root on 12/11/16.
 */
public class StudentGetPage extends HttpServlet {
    private final ApplicationContext context;

    public StudentGetPage(ApplicationContext context){
        this.context = context;
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
        String out = null;

        try {
            out = new GsonBuilder().create().toJson(studentDAO.getStudentByID(id));
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
