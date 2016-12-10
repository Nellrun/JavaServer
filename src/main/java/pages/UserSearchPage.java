package pages;

import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import tables.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 12/10/16.
 */
public class UserSearchPage extends HttpServlet{
    private final ApplicationContext context;

    public UserSearchPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);

        String name = req.getParameter("name");
        String type = req.getParameter("type");

        resp.setContentType("text/html;charset=utf-8");


        if ( (name == null) || (name.equals("")) || (type == null) ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type.equals("0")) {
//            Student
            StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");

            ArrayList<Student> students = new ArrayList<Student>();

            for (String word: name.split(" ")) {
                students.addAll(studentDAO.getStudentByName(word));
            }

            String out = new GsonBuilder().create().toJson(students);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }

        if (type.equals("1")) {
//            Teacher
            TeacherDAO teacherDAO = (TeacherDAO) context.getBean("TeacherDAO");

            ArrayList<Teacher> teachers = new ArrayList<Teacher>();

            for(String word: name.split(" ")) {
                teachers.addAll(teacherDAO.getTeachersByName(word));
            }

            String out = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(teachers);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
