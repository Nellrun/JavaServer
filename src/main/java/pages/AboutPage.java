package pages;

import accounts.AccountService;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by root on 11/18/16.
 */
public class AboutPage extends HttpServlet {

    private final AccountService accountService;

    public AboutPage(AccountService as) {
        accountService = as;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");

        GsonBuilder gb = new GsonBuilder();
        String text = gb.create().toJson(accountService.getUserByLogin(login));

        resp.setStatus(200);
        resp.getWriter().write(text);
//        super.doGet(req, resp);
    }
}
