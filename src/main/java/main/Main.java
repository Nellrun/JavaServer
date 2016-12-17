package main;

/**
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

        HashMap<String, String> sessionToLogin = new HashMap<String, String>();

        ApplicationContext jdbcContext = new ClassPathXmlApplicationContext("jdbc-config.xml");

        context.addServlet(new ServletHolder(new SignUpPage(jdbcContext)), "/api/user.signup");
        context.addServlet(new ServletHolder(new SignInPage(jdbcContext, sessionToLogin)), "/api/user.signin");
        context.addServlet(new ServletHolder(new ChangePasswordPage(jdbcContext, sessionToLogin)), "/api/user.changePassword");
        context.addServlet(new ServletHolder(new UserChangeNamePage(jdbcContext, sessionToLogin)), "/api/user.changeName");
        context.addServlet(new ServletHolder(new LogoutPage(sessionToLogin)), "/api/user.logout");

        context.addServlet(new ServletHolder(new StudentSearchPage(jdbcContext)), "/api/student.search");
        context.addServlet(new ServletHolder(new StudentGetPage(jdbcContext)), "/api/student.get");
        context.addServlet(new ServletHolder(new StudentChangeGroupPage(jdbcContext, sessionToLogin)), "/api/student.changeGroup");

        context.addServlet(new ServletHolder(new TeacherSearchPage(jdbcContext)), "/api/teacher.search");
        context.addServlet(new ServletHolder(new TeacherGetPage(jdbcContext)), "/api/teacher.get");
        context.addServlet(new ServletHolder(new TeacherGetSubjects(jdbcContext)), "/api/teacher.getSubjects");

        context.addServlet(new ServletHolder(new StudentChangeGroupPage(jdbcContext, sessionToLogin)), "/api/department.changeSecretKey");

        context.addServlet(new ServletHolder(new GroupSearchPage(jdbcContext)), "/api/group.search");
        context.addServlet(new ServletHolder(new GroupGetPage(jdbcContext)), "/api/group.get");
        context.addServlet(new ServletHolder(new GroupGetMembersPage(jdbcContext)), "/api/group.getMembers");
        context.addServlet(new ServletHolder(new GroupChangeAccessPage(jdbcContext, sessionToLogin)), "/api/group.changeAccess");

        context.addServlet(new ServletHolder(new GetSchedulePage(jdbcContext)), "/api/schedule.get");
        context.addServlet(new ServletHolder(new ScheduleAddPage(jdbcContext, sessionToLogin)), "/api/schedule.add");
        context.addServlet(new ServletHolder(new ScheduleDelPage(jdbcContext, sessionToLogin)), "/api/schedule.del");
        context.addServlet(new ServletHolder(new ScheduleAddGroupPage(jdbcContext, sessionToLogin)), "/api/schedule.addGroup");
        context.addServlet(new ServletHolder(new ScheduleDelGroupPage(jdbcContext, sessionToLogin)), "/api/schedule.delGroup");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}
