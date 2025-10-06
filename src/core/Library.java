package src.core;
import src.model.*;

import java.time.LocalDate;
import java.util.*;

public class Library {
    List<Book> books = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Loan> loans = new ArrayList<>();

    public Library(List<Book> books, List<User> users, List<Loan> loans) {
        this.books = books;
        this.users = users;
        this.loans = loans;
    }

    private Book verifyBook(int idBook) throws UnavailableBookException {
        //Goes to each book in the library to see if the book in parameter exists
        //if yes return it, if not throws an exception
        for (Book book: books) {
            if (book.getId() == idBook) {
                return book;
            }
        }
        throw new UnavailableBookException("This book ID does not exists in our library !");
    }

    private User verifyUser(User user) throws UnavailableBookException {
        //Goes to each user in the library to see if the user in parameter exists
        //if yes, return it, if not throws an exception
        for (User u : users) {
            if (u.getId() == user.getId()) {
                return u;
            }
        }
        throw new UnavailableBookException("This user does not exists in our library !");
    }

    public void loanBook(int idBook, User user) throws UnavailableBookException {
        //Verify the book and the user exists in the library
        Book book = verifyBook(idBook);
        User u = verifyUser(user);

        if (!book.isAvailable()) { //Check if the book is already loaned
            throw new UnavailableBookException("This book is already loaned !");
        }
        else { //If available, loan it to the user, set it as loaned, and add the loan into the history
            book.loan();
            u.getBooks().add(book);
            loans.add(new Loan(book.getId(), u.getId(), LocalDate.now()));
        }
    }

    public void returnBook(int idBook, User user) throws UnavailableBookException{
        //Verify the book and the user exists in the library
        Book book = verifyBook(idBook);
        User u = verifyUser(user);

        if (!u.getBooks().contains(book)) {
            throw new UnavailableBookException("You cannot return a book you haven't loaned or already returned !");
        }
        else{
            u.getBooks().remove(book);
            book.bookReturn();
        }
    }

    public void displayAvailableBooks() {

        //Goes through each book of the library
        for (Book book: books) {
            //Prints the title of the available books
            if (book.isAvailable()) {
                System.out.println(book.getTitle());
            }
        }
    }

    public List<Book> searchByTitle(String title) {
        List<Book> result = new ArrayList<>();
        String search = title.toLowerCase().trim();

        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(search)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> searchByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        String search = author.toLowerCase().trim();

        for (Book b : books) {
            if (b.getAuthor().toLowerCase().contains(search)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> searchByGenre(String genre) {
        List<Book> result = new ArrayList<>();
        String search = genre.toLowerCase().trim();

        for (Book b : books) {
            if (b.getGenre().toLowerCase().contains(search)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> getBooks() { return books; }
    public List<User> getUsers() { return users; }
    public List<Loan> getLoans() { return loans; }
}
