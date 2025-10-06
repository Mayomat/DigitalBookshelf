package src.ui;

import src.core.Library;
import src.model.Book;
import src.model.Loan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Statistics {
    public static void printToConsole(Library lib) {
        int total = lib.getBooks().size();
        long available = lib.getBooks().stream().filter(Book::isAvailable).count();
        System.out.println("\n--- STATISTICS ---");
        System.out.println("Available books: " + available + "/" + total);

        Map<String, Double> rateByGenre = borrowRateByGenre(lib);
        if (rateByGenre.isEmpty()) {
            System.out.println("Borrow rate by genre: no data yet");
        } else {
            System.out.println("Borrow rate by genre:");
            for (Entry<String, Double> e : rateByGenre.entrySet()) {
                System.out.println(e.getKey() + ": " + String.format(Locale.ROOT, "%.0f%%", e.getValue() * 100));
            }
        }

        List<String> topAuthors = topBorrowedAuthors(lib, 3);
        if (topAuthors.isEmpty()) {
            System.out.println("Top borrowed authors: none");
        } else {
            System.out.println("Top borrowed authors:");
            for (int i = 0; i < topAuthors.size(); i++) {
                System.out.println((i + 1) + ". " + topAuthors.get(i));
            }
        }
    }

    public static void writeToFile(Library lib, Path path) throws IOException {
        int total = lib.getBooks().size();
        long available = lib.getBooks().stream().filter(Book::isAvailable).count();
        StringBuilder sb = new StringBuilder();
        sb.append("--- STATISTICS ---\n");
        sb.append("Available books: ").append(available).append("/").append(total).append("\n");

        Map<String, Double> rateByGenre = borrowRateByGenre(lib);
        if (rateByGenre.isEmpty()) {
            sb.append("Borrow rate by genre: no data yet\n");
        } else {
            sb.append("Borrow rate by genre:\n");
            for (Entry<String, Double> e : rateByGenre.entrySet()) {
                sb.append(e.getKey()).append(": ")
                        .append(String.format(Locale.ROOT, "%.0f%%", e.getValue() * 100))
                        .append("\n");
            }
        }

        List<String> topAuthors = topBorrowedAuthors(lib, 3);
        if (topAuthors.isEmpty()) {
            sb.append("Top borrowed authors: none\n");
        } else {
            sb.append("Top borrowed authors:\n");
            for (int i = 0; i < topAuthors.size(); i++) {
                sb.append(i + 1).append(". ").append(topAuthors.get(i)).append("\n");
            }
        }

        Files.writeString(path, sb.toString());
    }

    public static Map<String, Double> borrowRateByGenre(Library lib) {
        List<Loan> loans = lib.getLoans();
        if (loans.isEmpty()) return Collections.emptyMap();
        Map<Integer, Book> byId = lib.getBooks().stream().collect(Collectors.toMap(Book::getId, b -> b));
        Map<String, Long> countByGenre = new HashMap<>();
        for (Loan l : loans) {
            Book b = byId.get(l.getBookId());
            if (b == null) continue;
            String g = b.getGenre();
            countByGenre.put(g, countByGenre.getOrDefault(g, 0L) + 1);
        }
        long totalLoans = loans.size();
        Map<String, Double> rate = new LinkedHashMap<>();
        for (Entry<String, Long> e : countByGenre.entrySet()) {
            rate.put(e.getKey(), e.getValue() / (double) totalLoans);
        }
        return rate;
    }

    public static List<String> topBorrowedAuthors(Library lib, int limit) {
        List<Loan> loans = lib.getLoans();
        if (loans.isEmpty()) return Collections.emptyList();
        Map<Integer, Book> byId = lib.getBooks().stream().collect(Collectors.toMap(Book::getId, b -> b));
        Map<String, Long> countByAuthor = new HashMap<>();
        for (Loan l : loans) {
            Book b = byId.get(l.getBookId());
            if (b == null) continue;
            String a = b.getAuthor();
            countByAuthor.put(a, countByAuthor.getOrDefault(a, 0L) + 1);
        }
        return countByAuthor.entrySet()
                .stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(limit)
                .map(Entry::getKey)
                .toList();
    }
}
