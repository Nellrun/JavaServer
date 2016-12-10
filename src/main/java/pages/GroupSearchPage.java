package pages;

import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 12/4/16.
 */
public class GroupSearchPage extends HttpServlet {

    private final ApplicationContext context;

    public GroupSearchPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        resp.setContentType("text/html;charset=utf-8");

        if ( (name == null) || (name.equals(""))  ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        GroupDAO groupDAO = (GroupDAO) context.getBean("GroupDAO");

        List<Group> groups = groupDAO.getGroupsByName(name);

        String out = new GsonBuilder().create().toJson(groups);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(out);

    }
}
