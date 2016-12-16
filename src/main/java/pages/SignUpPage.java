package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.ParameterError;
import errors.UserAlreadyExistsError;
import main.Checker;
import org.eclipse.jetty.io.WriterOutputStream;
import org.springframework.context.ApplicationContext;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

/**
 * Created by root on 11/13/16.
 * Страница регистрации пользователя.
 * Регистрирует как преподавателей, так и студентов
 */
public class SignUpPage extends HttpServlet {

    private final ApplicationContext context;

    public SignUpPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");
        int type;

        try {
            Checker.check(req.getParameter("login"), "login", 30);
            Checker.check(req.getParameter("password"), "password", 30);
            type = Checker.toInt(req.getParameter("type"), "type", 0, 1);
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type == 0) {
            studentAuth(req, resp);
        }
        else {
            teacherAuth(req, resp);
        }
    }

    private void teacherAuth(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        int teacherID;
        String secretKey;

        try {
            teacherID = Checker.toInt(req.getParameter("id"), "id");
            secretKey = Checker.check(req.getParameter("secretKey"), "secretKey");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            try {
                resp.getWriter().write(out);
            }
            catch (IOException ne) {}
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");
        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("DepartmentDAO");

        Teacher teacher = teacherDAO.getTeacherByID(teacherID);
        if (!secretKey.equals(departmentDAO.getSecretKeyByDepartmentID(teacher.getDepartamentID()))) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AccessDenidedError());
            try {
                resp.getWriter().write(out);
            }
            catch (IOException ne) {}
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");

        try {
            userDAO.create(login, password);
        } catch (Exception e) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new UserAlreadyExistsError());
            try {
                resp.getWriter().write(out);
            }
            catch (IOException ne) {}
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        Integer userID = userDAO.getIDByLogin(login);

        userInfoDAO.update(teacher.getUserID(), userID);
        resp.setStatus(HttpServletResponse.SC_OK);
        return;
    }

    private void studentAuth(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        int groupID;

        try {
            groupID = Checker.toInt(req.getParameter("groupID"), "groupID");
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            try {
                resp.getWriter().write(out);
            }
            catch (IOException en) {}
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        String middleName = req.getParameter("middleName");

        firstName = firstName == null ? "" : firstName;
        secondName = secondName == null ? "" : secondName;
        middleName = middleName == null ? "" : middleName;

        StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");

        try {
            userDAO.create(login, password);
        } catch (Exception e) {
            String out = new GsonBuilder().create().toJson(new UserAlreadyExistsError());
            try {
                resp.getWriter().write(out);
            }
            catch (IOException en) {}
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        Integer userID = userDAO.getIDByLogin(login);

        userInfoDAO.create(userID, firstName, secondName, middleName);

        Integer userInfoID = userInfoDAO.getUserInfoByUserID(userID).getId();
        studentDAO.create(groupID, 0, userInfoID);
        resp.setStatus(HttpServletResponse.SC_OK);
        return;
    }
}