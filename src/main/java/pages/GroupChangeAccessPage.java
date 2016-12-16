package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import main.Checker;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import tables.Student;
import tables.StudentDAO;
import tables.Teacher;
import tables.TeacherDAO;

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
public class GroupChangeAccessPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public GroupChangeAccessPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");

        String token;
        int studentID;
        int levelOfAccess;

        try {
            token = Checker.check(req.getParameter("token"), "token");
            studentID = Checker.toInt(req.getParameter("studentID"), "studentID");
            levelOfAccess = Checker.toInt(req.getParameter("access"), "access", 0, 3);
        }
        catch (MissingParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        catch (BadParameterFormatError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!sessionToLogin.containsKey(token)) {
            String out = new GsonBuilder().create().toJson(new BadParameterFormatError("token"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String login = sessionToLogin.get(token);

        StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");

        Student targetStudent;

        try {
            targetStudent = studentDAO.getStudentByID(studentID);
        }
        catch (EmptyResultDataAccessException e) {
            String out = new GsonBuilder().create().toJson(new BadParameterFormatError("studentID"));
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Student s = studentDAO.getStudentByLogin(login);
            if (s.getLevelOfAccess() <= levelOfAccess || s.getGroupID() != targetStudent.getGroupID()) {
                throw new AccessDenidedError();
            }
        }
        catch (EmptyResultDataAccessException e) {}
        catch (AccessDenidedError e) {
            String out = new GsonBuilder().create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        studentDAO.updateLevelOfAccess(studentID, levelOfAccess);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
