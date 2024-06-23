package ru.academits.java.kononov.todolist.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoItemInMemoryRepository implements TodoItemRepository {
    public static final List<TodoItem> todoItems = new ArrayList<TodoItem>();
    private static final AtomicInteger newId = new AtomicInteger(1);

    @Override
    public List<TodoItem> getAll() {
        synchronized (todoItems) {
            return todoItems.stream()
                    .map(TodoItem::new)
                    .toList();
        }
    }

    @Override
    public void create(TodoItem todoItem) {
        synchronized (todoItems) {
            todoItem.setId(newId.getAndIncrement());
            todoItems.add(todoItem);
        }
    }

    @Override
    public void update(TodoItem todoItem) {
        synchronized (todoItems) {
            int todoItemId = todoItem.getId();

            TodoItem repositoryItem = todoItems.stream()
                    .filter(ti -> ti.getId() == todoItemId)
                    .findFirst()
                    .orElse(null);

            if (repositoryItem != null) {
                repositoryItem.setText(todoItem.getText());
            } else {
                throw new IllegalStateException("No todoItem found with id = " + todoItemId);
            }
        }
    }

    @Override
    public void delete(int todoItemId) {
        synchronized (todoItems) {
            todoItems.removeIf(todoItem -> todoItem.getId() == todoItemId);
        }
    }
}
