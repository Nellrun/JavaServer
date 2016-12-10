package pages;

import com.google.gson.GsonBuilder;
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

        if ((sID == null) && (token == null)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (sID != null) {

            int id = 0;

            try {
                id = Integer.valueOf(sID);
            }
            catch (Exception e) {
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
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String login = sessionToLogin.get(token);

            UserInfoDAO userInfoDAO = (UserInfoDAO) context.getBean("UserInfoDAO");
        }



    }
}
