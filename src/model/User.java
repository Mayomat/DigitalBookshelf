package src.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static int nextId = 1;

    private int id;
    private String name;
    private List<Book> books;


    public User(String name) {
        this.id = nextId++;
        this.name = name;
        this.books = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Book> getBooks() { return books; }

    public void addBook(Book b) {
        books.add(b);
    }

    public void removeBook(Book b) {
        books.remove(b);
    }
}
