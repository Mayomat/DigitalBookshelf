package src.ui;

import src.core.Library;
import src.core.UnavailableBookException;
import src.model.Book;
import src.model.Loan;
import src.model.User;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class App {
    private final Library library;
    private final Scanner scanner = new Scanner(System.in);

    public App(Library library) {
        this.library = library;
    }

    public void run() {
        while (true) {
            System.out.println("\n--- LIBRARY MENU ---");
            System.out.println("1. List available books");
            System.out.println("2. Search for a book");
            System.out.println("3. Borrow a book");
            System.out.println("4. Return a book");
            System.out.println("5. Show statistics");
            System.out.println("0. Quit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> listAvailableBooks();
                case "2" -> searchBooks();
                case "3" -> borrowFlow();
                case "4" -> returnFlow();
                case "5" -> statsFlow();
                case "0" -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void listAvailableBooks() {
        List<Book> available = library.getBooks().stream().filter(Book::isAvailable).toList();
        if (available.isEmpty()) {
            System.out.println("No available books.");
            return;
        }
        for (Book b : available) {
            System.out.println(b.getId() + " - " + b.getTitle() + " | " + b.getAuthor() + " | " + b.getGenre());
        }
    }

    private void searchBooks() {
        System.out.println("1. By title");
        System.out.println("2. By author");
        System.out.println("3. By genre");
        System.out.print("Choice: ");
        String c = scanner.nextLine().trim();
        System.out.print("Keyword: ");
        String q = scanner.nextLine();
        List<Book> result;
        switch (c) {
            case "1" -> result = filterBy(library.getBooks(), q, "title");
            case "2" -> result = filterBy(library.getBooks(), q, "author");
            case "3" -> result = filterBy(library.getBooks(), q, "genre");
            default -> { System.out.println("Invalid choice."); return; }
        }
        if (result.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        for (Book b : result) {
            String flag = b.isAvailable() ? "available" : "borrowed";
            System.out.println(b.getId() + " - " + b.getTitle() + " | " + b.getAuthor() + " | " + b.getGenre() + " | " + flag);
        }
    }

    private List<Book> filterBy(List<Book> books, String query, String field) {
        String q = normalize(query);
        List<Book> out = new ArrayList<>();
        for (Book b : books) {
            String v = switch (field) {
                case "author" -> b.getAuthor();
                case "genre" -> b.getGenre();
                default -> b.getTitle();
            };
            if (normalize(v).contains(q)) out.add(b);
        }
        return out;
    }

    private void borrowFlow() {
        System.out.print("Book ID: ");
        int idBook = readInt(scanner.nextLine());
        System.out.print("User ID: ");
        int idUser = readInt(scanner.nextLine());
        User u = findUserById(idUser);
        if (u == null) {
            System.out.println("User not found.");
            return;
        }
        try {
            library.loanBook(idBook, u);
            System.out.println("Borrow registered successfully.");
        } catch (UnavailableBookException e) {
            System.out.println(e.getMessage());
        }
    }

    private void returnFlow() {
        System.out.print("Book ID: ");
        int idBook = readInt(scanner.nextLine());
        System.out.print("User ID: ");
        int idUser = readInt(scanner.nextLine());
        User u = findUserById(idUser);
        if (u == null) {
            System.out.println("User not found.");
            return;
        }
        try {
            library.returnBook(idBook, u);
            System.out.println("Return registered successfully.");
        } catch (UnavailableBookException e) {
            System.out.println(e.getMessage());
        }
    }

    private void statsFlow() {
        Statistics.printToConsole(library);
        try {
            Statistics.writeToFile(library, Path.of("statistics.txt"));
            System.out.println("statistics.txt generated.");
        } catch (Exception e) {
            System.out.println("Failed to write statistics file: " + e.getMessage());
        }
    }

    private User findUserById(int id) {
        for (User u : library.getUsers()) if (u.getId() == id) return u;
        return null;
    }

    private int readInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return -1; }
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase().replace(" ", "");
    }

    public static void main(String[] args) {
        System.out.println("Run this App from your main project entry point with a populated Library instance.");
    }
}
