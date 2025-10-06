package src.model;

import java.time.LocalDate;

public class Loan {
    private int bookId;
    private int userId;
    private LocalDate date;

    public Loan(int bookId, int userId, LocalDate date) {
        this.bookId = bookId;
        this.userId = userId;
        this.date = date;
    }

    public int getBookId() { return bookId; }
    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
}
