package operations;

import entities.Book;
import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added: " + book);
    }

    public void removeBook(String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                books.remove(i);
        System.out.println("Book with ISBN " + isbn + " has been removed.");
        return;
    }
        }
    }
    
    public void listBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            System.out.println("Books List:");
            for (int i = 0; i < books.size(); i++) {
                System.out.println(books.get(i));
        }
        }
    }

    public void searchBook(String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                System.out.println("Found: " + books.get(i));
                return;
            }
    }
        System.out.println("Book not found.");
    }
}
