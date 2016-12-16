package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
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
import java.util.HashMap;

/**
 * Created by root on 12/13/16.
 */
public class StudentChangeGroupPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public StudentChangeGroupPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        int id;
        String token;

        try {
            id = Checker.toInt(req.getParameter("id"), "id");
            token = Checker.check(req.getParameter("token"), "token");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!sessionToLogin.containsKey(token)) {
            String out = new GsonBuilder().create().toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String login = sessionToLogin.get(token);
        StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");

        Student student = studentDAO.getStudentByLogin(login);
        studentDAO.updateGroupID(student.getId(), id);

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
