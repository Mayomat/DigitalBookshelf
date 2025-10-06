package src.core;

import java.time.LocalDate;
import java.util.*;

public class Library {
    List<Book> books = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Loan> loans = new ArrayList<>();

    Library(List<Book> books, List<User> users) {
        this.books = books;
        this.users = users;
        for (Book book:books) {
            loans.add(new Loan(book));
        }
    }

    private Book verifyBook(int idBook) throws UnavailableBookException {
        //Goes to each book in the library to see if the book in parameter exists
        //if yes return it, if not throws an exception
        for (Book book: books) {
            if (book.getID() == idBook) {
                return book;
            }
        }
        throw new UnavailableBookException("This book ID does not exists in our library !");
    }

    private User verifyUser(User user) throws UnavailableBookException {
        //Goes to each user in the library to see if the user in parameter exists
        //if yes, return it, if not throws an exception
        for (User u: users) {
            if (u.equals(user)) {
                userFounded = true;
            }
        }
        throw new UnavailableBookException("This user does not exists in our library !");
    }

    public void loanBook(int idBook, User user) throws UnavailableBookException {
        //Verify the book and the user exists in the library
        Book book = verifyBook(idBook);
        User u = verifyUser(User);

        if (!book.isAvailable()) { //Check if the book is already loaned
            throw new UnavailableBookException("This book is already loaned !");
        }
        else { //If available, loan it to the user, set it as loaned, and add the loan into the history
            book.loan();
            u.setBook(book);
            loans.add(new Loan(book, u, LocalDate.now()));
        }
    }

    public void returnBook(int idBook, User user) throws UnavailableBookException{
        //Verify the book and the user exists in the library
        Book book = verifyBook(idBook);
        User u = verifyUser(User);

        if (u.getBook.isAvailable()) {
            throw new UnavailableBookException("You cannot return a book you haven't loaned or already returned !");
        }
        else if (!u.getBook.equals(book)) {
            throw new UnavailableBookException("You loaned a different book than the one you are returning !")
        }
        else {
            book.bookReturn();
        }
    }

    public void displayAvailableBooks() {

        //Goes through each book of the library
        for (book: books) {
            //Prints the title of the available books
            if (book.isAvailable()) {
                System.out.println(book.getTitle());
            }
        }
    }
}
