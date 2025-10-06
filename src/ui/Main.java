package src.ui;

import src.core.Library;
import src.core.UnavailableBookException;
import src.model.Book;
import src.model.User;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();
        List<User> users = new ArrayList<>();

        users.add(new User("Alice"));
        users.add(new User("Bob"));
        users.add(new User("Charlie"));

        Path csvPath = findCsvPath();
        loadBooksFromCsv(csvPath, books);

        Library library = new Library(books, users, new ArrayList<>());

        try {
            if (!books.isEmpty()) {
                library.loanBook(books.get(0).getId(), users.get(0));
                library.loanBook(books.get(1).getId(), users.get(1));
            }
        } catch (UnavailableBookException e) {
            System.out.println("Error during initial loans: " + e.getMessage());
        }

        src.ui.App app = new src.ui.App(library);
        app.run();
    }

    private static Path findCsvPath() {
        String wd = System.getProperty("user.dir");
        System.out.println("Current working directory: " + wd);

        Path p1 = Path.of("books.csv");             // root project directory
        Path p2 = Path.of("src", "books.csv");      // if placed inside /src
        Path p3 = Path.of("..", "books.csv");       // if launched from /out or /build

        if (Files.exists(p1)) return p1;
        if (Files.exists(p2)) return p2;
        if (Files.exists(p3)) return p3;

        System.out.println("⚠️  books.csv not found. Expected in: " + p1.toAbsolutePath());
        return p1; // fallback
    }

    private static void loadBooksFromCsv(Path csvPath, List<Book> books) {
        if (!Files.exists(csvPath)) {
            System.out.println("Error: books.csv not found at " + csvPath.toAbsolutePath());
            return;
        }

        try {
            List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
            int before = books.size();
            for (int i = 1; i < lines.size(); i++) { // skip header
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;
                books.add(new Book(parts[1].trim(), parts[2].trim(), parts[3].trim()));
            }
            System.out.println((books.size() - before) + " books successfully loaded from " + csvPath.getFileName());
        } catch (IOException e) {
            System.out.println("Error reading books.csv: " + e.getMessage());
        }
    }
}
