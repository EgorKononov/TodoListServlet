package ru.academits.java.kononov.todolist.data;

public class TodoItem {
    private int id;
    private String text;

    public TodoItem(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public TodoItem(String text) {
        this.text = text;
    }

    public TodoItem(TodoItem todoItem) {
        this.id = todoItem.id;
        this.text = todoItem.text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
