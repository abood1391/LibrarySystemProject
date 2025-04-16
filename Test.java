package main;

import entities.Admin;
import entities.Book;
import entities.User;
import operations.Library;
import gui.LibraryGUI;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Library library = new Library();
        Admin admin = new Admin("admin", "admin");
        User user = new User("User");
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to Library Management System");
            System.out.println("1. Admin");
            System.out.println("2. User");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

             switch (choice) {
                case 1:
                    adminMenu(library, admin, scanner);
                    break;
                case 2:
                    userMenu(library, scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
            new LibraryGUI();
    }


    private static void adminMenu(Library library, Admin admin, Scanner scanner) {
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (admin.login(password)) {
            boolean adminRunning = true;
            while (adminRunning) {
                System.out.println("\nAdmin Menu");
                System.out.println("1. Add Book");
                System.out.println("2. Remove Book");
                System.out.println("3. Exit to Main Menu");
                System.out.print("Choose an option: ");

                int adminChoice = scanner.nextInt();
                scanner.nextLine();

                switch (adminChoice) {
                    case 1:
                        System.out.print("Enter Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter Author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter ISBN: ");
                        String isbn = scanner.nextLine();
                        library.addBook(new Book(title, author, isbn));
                        break;
                    case 2:
                        System.out.print("Enter ISBN to remove: ");
                        String isbnToRemove = scanner.nextLine();
                        library.removeBook(isbnToRemove);
                        break;
                    case 3:
                        adminRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } else {
            System.out.println("Invalid password. Returning to main menu...");
        }
    }

    private static void userMenu(Library library, Scanner scanner) {
        boolean userRunning = true;
        while (userRunning) {
            System.out.println("\nUser Menu");
            System.out.println("1. List Books");
            System.out.println("2. Search Book");
            System.out.println("3. Exit to Main Menu");
            System.out.print("Choose an option: ");

            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:
                    library.listBooks();
                    break;
                case 2:
                    System.out.print("Enter ISBN to search: ");
                    String isbn = scanner.nextLine();
                    library.searchBook(isbn);
                    break;
                case 3:
                    userRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
