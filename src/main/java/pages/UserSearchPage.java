package pages;

import com.google.gson.GsonBuilder;
import errors.BadParameterFormatError;
import errors.MissingParameterError;
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
 * Страница, которая отвечает за поиск преподавателей
 * и студентов по их имени, фамилии и отчеству
 */
public class UserSearchPage extends HttpServlet{
    private final ApplicationContext context;

    public UserSearchPage(ApplicationContext context){
        this.context = context;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String type = req.getParameter("type");

        resp.setContentType("text/html;charset=utf-8");

        if ( (name == null) || (name.equals("")) || (type == null) ) {
            String out = null;

            if (name == null || type == null) {
                MissingParameterError missingParameterError = new MissingParameterError(name == null? "name" : "type");
                out = new GsonBuilder().create().toJson(missingParameterError);
            }
            else if (name.equals("")) {
                out = new GsonBuilder().create().toJson(new BadParameterFormatError("name"));
            }
            else if (!type.equals("1") && type.equals("")) {
                out = new GsonBuilder().create().toJson(new BadParameterFormatError("type"));
            }

            resp.getWriter().write(out);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (type.equals("0")) {
//            Student
            StudentDAO studentDAO = (StudentDAO) context.getBean("StudentDAO");

            ArrayList<Student> students = new ArrayList<Student>();

            for (String word: name.split(" ")) {
                students.addAll(studentDAO.getStudentsByName(word));
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

            String out = new GsonBuilder().create().toJson(teachers);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(out);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
