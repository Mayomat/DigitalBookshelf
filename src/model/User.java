package src.model;
import java.util.ArrayList;
import java.util.List;

public class User {
    private static int nextId = 1;

    private int id;
    private String name;
    private List<Book> currentBooks;

    public User(String name) {
        this.id = nextId++;
        this.name = name;
        this.currentBooks = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public List<Book> getCurrentBooks() { return currentBooks; }
}
