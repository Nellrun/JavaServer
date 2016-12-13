package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import org.springframework.context.ApplicationContext;
import tables.Student;
import tables.StudentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        String sID = req.getParameter("id");
        String token = req.getParameter("token");

        resp.setContentType("text/html;charset=utf-8");

        if (token == null || sID == null) {
            String out;
            if (token == null) {
                out = new GsonBuilder().create().toJson(new MissingParameterError("token"));
            }
            else {
                out = new GsonBuilder().create().toJson(new MissingParameterError("id"));
            }

            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = 0;

        try {
            id = Integer.valueOf(sID);
        }
        catch (NumberFormatException e) {
            String out = new GsonBuilder().create().toJson(new BadParameterFormatError("id"));
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
