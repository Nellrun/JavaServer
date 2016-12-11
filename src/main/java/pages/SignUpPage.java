package pages;

import com.google.gson.GsonBuilder;
import errors.UserAlreadyExistsError;
import org.eclipse.jetty.io.WriterOutputStream;
import org.springframework.context.ApplicationContext;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String type = req.getParameter("type");

        resp.setContentType("text/html;charset=utf-8");

        if ( (login == null) || (password == null) || (login.equals("")) || (password.equals("")) || (type == null) ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type.equals("0")) {
            studentAuth(req, resp);
            return;
        }

        if (type.equals("1")) {
            teacherAuth(req, resp);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void teacherAuth(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        String sTeacherID = req.getParameter("id");
        String secretKey = req.getParameter("secretKey");

        int teacherID = 0;

        try {
            teacherID = Integer.valueOf(sTeacherID);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");
        DepartmentDAO departmentDAO = (DepartmentDAO) context.getBean("DepartmentDAO");

        Teacher teacher = teacherDAO.getTeacherByID(teacherID);
        if (!secretKey.equals(departmentDAO.getSecretKeyByDepartmentID(teacher.getDepartamentID()))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");

        try {
            userDAO.create(login, password);
        } catch (Exception e) {
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

        String sGroupID = req.getParameter("groupID");
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        String middleName = req.getParameter("middleName");

        firstName = firstName == null ? "" : firstName;
        secondName = secondName == null ? "" : secondName;
        middleName = middleName == null ? "" : middleName;

        int groupID = 0;

        try {
            groupID = Integer.valueOf(sGroupID);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");
        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");
        UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");

        try {
            userDAO.create(login, password);
        } catch (Exception e) {
            String out = new GsonBuilder().create().toJson(new UserAlreadyExistsError());
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