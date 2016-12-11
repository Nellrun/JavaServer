package pages;

import com.google.gson.GsonBuilder;
import errors.AccessDenidedError;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
import org.springframework.context.ApplicationContext;
import tables.UserInfoDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by root on 12/10/16.
 * Возвращает основную информацию о пользователе по его идентификатору.
 * Основная информация: имя, фамилия, отчество, пол
 */
public class UserGetPage extends HttpServlet {
    private final ApplicationContext context;
    private final HashMap<String, String> sessionToLogin;

    public UserGetPage(ApplicationContext context, HashMap<String, String> stl) {
        this.context = context;
        this.sessionToLogin = stl;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sID = req.getParameter("id");
        String token = req.getParameter("token");

        resp.setContentType("text/html;charset=utf-8");

        if ((sID == null) && (token == null)) {
            MissingParameterError missingParameterError = new MissingParameterError("id или token");
            String out = new GsonBuilder().create().toJson(missingParameterError);
            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (sID != null) {

            int id = 0;

            try {
                id = Integer.valueOf(sID);
            }
            catch (Exception e) {
                BadParameterFormatError bpfe = new BadParameterFormatError("id");
                String out = new GsonBuilder().create().toJson(bpfe);
                resp.getWriter().write(out);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");
            String out = new GsonBuilder().create().toJson(userInfoDAO.getUserInfoByID(id));

            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (token != null) {
            if (!sessionToLogin.containsKey(token)) {
                String out = new GsonBuilder().create().toJson(new AccessDenidedError());
                resp.getWriter().write(out);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String login = sessionToLogin.get(token);

            UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");
        }

    }
}
