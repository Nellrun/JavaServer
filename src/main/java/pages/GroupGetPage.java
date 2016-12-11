package pages;

import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import tables.GroupDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by root on 12/10/16.
 * Страница, которая возвращает информацию о группе:
 * Короткое имя
 * Длинное имя
 * Форма обучения
 * Степень
 */
public class GroupGetPage extends HttpServlet {
    private final ApplicationContext context;

    public GroupGetPage(ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sID = req.getParameter("id");

        if (sID == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = 0;
        try {
            id = Integer.valueOf(sID);
        }
        catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GroupDAO groupDAO = (GroupDAO) context.getBean("GroupDAO");

        String out = new GsonBuilder().create().toJson(groupDAO.getGroupByID(id));
        resp.getWriter().write(out);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
