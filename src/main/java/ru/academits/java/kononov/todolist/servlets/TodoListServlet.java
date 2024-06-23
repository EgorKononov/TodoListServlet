package ru.academits.java.kononov.todolist.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.text.StringEscapeUtils;
import ru.academits.java.kononov.todolist.data.TodoItem;
import ru.academits.java.kononov.todolist.data.TodoItemInMemoryRepository;
import ru.academits.java.kononov.todolist.data.TodoItemRepository;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class TodoListServlet extends HttpServlet {

    private final TodoItemRepository todoItemRepository = new TodoItemInMemoryRepository();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");

        resp.getWriter().println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>TODO List Servlet</title>
                    <meta charset="UTF-8">
                </head>
                <body>
                    <h1>TODO List Servlet</h1>
                    
                    <form action="%s" method="POST">
                        <input name="text" type="text">
                        <button name="action" value="create" type="submit">Create</button>
                        %s
                    </form>
                    
                    <ul>
                        %s
                    </ul>
                </body>
                </html>
                """.formatted(getBaseUrl(), getCreateErrorHtml(req), getTodolistHtml()));
    }

    private StringBuilder getTodolistHtml() {
        StringBuilder todolistHtml = new StringBuilder();

        for (TodoItem todoItem : todoItemRepository.getAll()) {
            todolistHtml.append("""
                    <li>
                        <form action="%s" method="POST">
                            <input type="text" name="text" value="%s">
                            <button name="action" value="delete" type="submit">Delete</button>
                            <button name="action" value="update" type="submit">Save</button>
                            <input type="hidden" name="id" value="%s">
                        </form>
                    </li>
                    """.formatted(getBaseUrl(), StringEscapeUtils.escapeHtml4(todoItem.getText()), todoItem.getId()));
        }

        return todolistHtml;
    }

    private static String getCreateErrorHtml(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        String createErrorHtml = "";

        if (session != null) {
            String createError = (String) session.getAttribute("createError");

            if (createError != null) {
                createErrorHtml = "<div>%s</div>".formatted(StringEscapeUtils.escapeHtml4(createError));
            }

            session.removeAttribute("createError");
        }

        return createErrorHtml;
    }

    private String getBaseUrl() {
        return getServletContext().getContextPath() + "/";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "create" -> {
                String text = req.getParameter("text").trim();

                if (text.isEmpty()) {
                    HttpSession session = req.getSession();
                    session.setAttribute("createError", "Text must be not empty");
                } else {
                    todoItemRepository.create(new TodoItem(text));
                }
            }
            case "update" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                String text = req.getParameter("text").trim();
                todoItemRepository.update(new TodoItem(id, text));
            }
            case "delete" -> {
                int id = Integer.parseInt(req.getParameter("id"));
                todoItemRepository.delete(id);
            }
        }

        resp.sendRedirect(getBaseUrl());
    }
}
