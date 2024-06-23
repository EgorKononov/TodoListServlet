package ru.academits.java.kononov.todolist.data;

import java.util.List;

public interface TodoItemRepository {
    List<TodoItem> getAll();

    void create(TodoItem todoItem);

    void update(TodoItem todoItem);

    void delete(int todoItemId);
}
