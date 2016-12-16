package pages;

import com.google.gson.GsonBuilder;
import com.oracle.webservices.internal.api.message.ContentType;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.SubjectDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Created by root on 12/16/16.
 */
public class TeacherGetSubjects extends HttpServlet {
    private final ApplicationContext context;

    public TeacherGetSubjects(ApplicationContext context){
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

        SubjectDAO subjectDAO = (SubjectDAO) context.getBean("SubjectDAO");

        String out = new GsonBuilder().create().toJson(subjectDAO.getSubjectsByTeacherID(id));
        resp.getWriter().write(out);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
