package pages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errors.*;
import main.Checker;
import org.springframework.context.ApplicationContext;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by root on 11/13/16.
 * Страница входа в ученую запись, если вход удачный то возвращается token доступа,
 * который используется для использования методов от имени пользователя
 */

public class SignInPage extends HttpServlet {

    class SignInAns {
        private int id;
        private int type;
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    private final HashMap<String, String> sessionToLogin;
    private final ApplicationContext context;

    public SignInPage(ApplicationContext context, HashMap<String, String> stl) {
        this.sessionToLogin = stl;
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json;charset=utf-8");

        String login;
        String password;

        try {
            login = Checker.check(req.getParameter("login"), "login", 30);
            password = Checker.check(req.getParameter("password"), "password", 30);
        }
        catch (ParameterError e) {
            String out = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(e);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDAO userDAO = (UserDAO) context.getBean("UserDAO");

        User u = null;

        try {
            u = userDAO.getUserByLogin(login);
        }
        catch (Exception e) {

        }

        if ( (u == null) || (!u.getPassword().equals(password)) ) {
            String out = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.PRIVATE)
                    .create()
                    .toJson(new AuthError());
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        sessionToLogin.put(req.getSession().getId(), login);

        StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");
        TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");

        SignInAns signInAns = new SignInAns();
        signInAns.setToken(req.getSession().getId());

        try {
            Student s = studentDAO.getStudentByLogin(login);
            signInAns.setId(s.getId());
            signInAns.setType(0);
        }
        catch (Exception e) {
            Teacher t = teacherDAO.getTeacherByLogin(login);
            signInAns.setId(t.getId());
            signInAns.setType(1);
        }

        String out = new GsonBuilder().create().toJson(signInAns);

        resp.getWriter().write(out);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
