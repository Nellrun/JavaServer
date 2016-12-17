package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.ParameterError;
import main.Checker;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import tables.Department;
import tables.DepartmentDAO;
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
 * Created by root on 12/17/16.
 */
public class DepartmentChangeSecretKeyPage extends HttpServlet {
    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public DepartmentChangeSecretKeyPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token;
        String secretKey;

        try {
            token = Checker.check(req.getParameter("token"), "token");
            secretKey = Checker.check(req.getParameter("secretKey"), "secretKey", 50);
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!sessionToLogin.containsKey(token)) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String login = sessionToLogin.get(token);

        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");

        Teacher teacher;

        try {
            teacher = teacherDAO.getTeacherByLogin(login);
        }
        catch (EmptyResultDataAccessException e) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AccessDenidedError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("DepartmentDAO");

        departmentDAO.setSecretKey(teacher.getDepartamentID(), secretKey);
    }
}
