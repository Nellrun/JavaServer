package main; /**
 * Created by Danill on 11.09.2016.
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pages.*;
import java.util.HashMap;


public class Main {

    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        AccountService accountService = new AccountService();

        HashMap<String, String> sessionToLogin = new HashMap<String, String>();

        ApplicationContext jdbcContext = new ClassPathXmlApplicationContext("jdbc-config.xml");

        context.addServlet(new ServletHolder(new SignUpPage(jdbcContext)), "/signup");
        context.addServlet(new ServletHolder(new SignInPage(jdbcContext, sessionToLogin)), "/signin");
        context.addServlet(new ServletHolder(new ChangePasswordPage(jdbcContext, sessionToLogin)), "/changePassword");
        context.addServlet(new ServletHolder(new SearchPage(jdbcContext)), "/search");
        context.addServlet(new ServletHolder(new GetSchedulePage(jdbcContext)), "/getSchedule");
        context.addServlet(new ServletHolder(new LogoutPage(sessionToLogin)), "/logout");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}
