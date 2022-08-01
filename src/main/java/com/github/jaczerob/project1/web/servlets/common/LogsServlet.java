package com.github.jaczerob.project1.web.servlets.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.jaczerob.project1.web.servlets.Servlet;

/**
 * A servlet exposing the /logs endpoint for showing all logs
 * @author Jacob
 * @since 0.12
 * @version 0.12
 */
@WebServlet(name="LogsServlet", urlPatterns={"/logs"})
public class LogsServlet extends Servlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<body>");

        try (
            FileReader fileReader = new FileReader("/logs/webapp.log");
            BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String line = reader.readLine();
            while (line != null) {
                out.println(String.format("%s<br>", line));
                line = reader.readLine();
            }
        }

        out.println("</body>");
        out.println("</html>");
    }
}
