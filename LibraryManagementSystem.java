import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryManagementSystem {

    private static ArrayList<Book> books = new ArrayList<>();
    private static final String FILE_PATH = "/Users/gabbyuriarte/Desktop/lms.txt";

    public static void main(String[] args) {
        // Load books from file
        loadBooksFromFile();

        int choice;

        // Display menu and perform actions until user exits
        do {
            displayMenu();
            choice = getUserChoice();
            performAction(choice);
        } while (choice != 6);

        // Save book to file only if choice is not exit
        if (choice != 6) {
            saveBookCollectionToFile();
        }
        System.out.println("Thank you for using LMS.");
    }

    // Load books from file
    private static void loadBooksFromFile() {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            // Read book information from each line and add to collection
            while (scanner.hasNextLine()) {
                String[] bookInfo = scanner.nextLine().split(",");
                int barcode = Integer.parseInt(bookInfo[0]);
                String title = bookInfo[1];
                String author = bookInfo[2];
                boolean checkedOut = Boolean.parseBoolean(bookInfo[3]);
                String dueDate = bookInfo[4];
                books.add(new Book(barcode, title, author, checkedOut, dueDate));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + FILE_PATH);
        }
    }

    // Display menu options
    private static void displayMenu() {
        System.out.println("\nMenu:\n1. Add a new book\n2. Remove a book by barcode, title, or author\n3. Check out a book\n4. Check in a book\n5. List all books\n6. Exit");
    }

    // Get user's choice from menu
    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        return new Scanner(System.in).nextInt();
    }

    // Perform actions based on user choice
    private static void performAction(int choice) {
        switch (choice) {
            case 1: addNewBook(); break;
            case 2: removeBookByBarcodeTitleOrAuthor(); break;
            case 3: checkOutBook(); break;
            case 4: checkInBook(); break;
            case 5: listAllBooks(); break;
            case 6: System.out.println("Exiting..."); break;
            default: System.out.println("Invalid choice. Try again.");
        }
    }

    // Add new book to collection
    private static void addNewBook() {
        Scanner scanner = new Scanner(System.in);

        // Get book details from user
        System.out.print("Enter book barcode: ");
        int barcode = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        // Add new book to collection
        books.add(new Book(barcode, title, author, false, ""));
        System.out.println("Book added successfully.");
    }

    // Remove book from collection by barcode number, title, or author
    private static void removeBookByBarcodeTitleOrAuthor() {
        Scanner scanner = new Scanner(System.in);

        // Get details of book to remove
        System.out.print("Enter barcode, title, or author to remove: ");
        String searchTerm = scanner.nextLine();

        // Remove book with matching barcode, title, or author
        boolean removed = false;
        for (Book book : new ArrayList<>(books)) {
            if (Integer.toString(book.getBarcode()).equals(searchTerm) || book.getTitle().equalsIgnoreCase(searchTerm) || book.getAuthor().equalsIgnoreCase(searchTerm)) {
                books.remove(book);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Book not found with the provided barcode, title, or author.");
        }
    }

    // Check out book from the collection
    private static void checkOutBook() {
        Scanner scanner = new Scanner(System.in);

        // Get details of book to check out
        System.out.print("Enter book title, author, or barcode to check out: ");
        String searchTerm = scanner.nextLine();

        // Find and check out book
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(searchTerm) || book.getAuthor().equalsIgnoreCase(searchTerm) || Integer.toString(book.getBarcode()).equals(searchTerm)) {
                if (!book.isCheckedOut()) {
                    book.setCheckedOut(true);
                    System.out.println("Book checked out successfully.");
                } else {
                    System.out.println("This book is already checked out.");
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Book not found with the provided title, author, or barcode.");
        }
    }

    // Check in a book to the collection
    private static void checkInBook() {
        Scanner scanner = new Scanner(System.in);

        // Get details of book to check in
        System.out.print("Enter book title, author, or barcode to check in: ");
        String searchTerm = scanner.nextLine();

        // Find and check in the book
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(searchTerm) || book.getAuthor().equalsIgnoreCase(searchTerm) || Integer.toString(book.getBarcode()).equals(searchTerm)) {
                if (book.isCheckedOut()) {
                    book.setCheckedOut(false);
                    System.out.println("Book checked in successfully.");
                } else {
                    System.out.println("This book is not checked out.");
                }
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Book not found with the provided title, author, or barcode.");
        }
    }

    // List all books in collection
    private static void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the collection.");
        } else {
            // Display list of books
            System.out.println("Books:");
            books.forEach(System.out::println);
        }
    }

    // Save book collection to file
    private static void saveBookCollectionToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            // Write each book to file
            for (Book book : books) {
                writer.println(book.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error. Book not saved.");
        }
    }

    // Book class
    private static class Book {
        private int barcode;
        private String title, author;
        private boolean checkedOut;
        private String dueDate;

        public Book(int barcode, String title, String author, boolean checkedOut, String dueDate) {
            this.barcode = barcode;
            this.title = title;
            this.author = author;
            this.checkedOut = checkedOut;
            this.dueDate = dueDate;
        }

        // Override toString method
        @Override
        public String toString() {
            return "Barcode: " + barcode + ", Title: " + title + ", Author: " + author + ", Checked Out: " + (checkedOut ? "Yes" : "No") + ", Due Date: " + dueDate;
        }

        // Getters and setters
        public int getBarcode() {
            return barcode;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isCheckedOut() {
            return checkedOut;
        }

        public void setCheckedOut(boolean checkedOut) {
            this.checkedOut = checkedOut;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        // Format book data for file
        public String toFileString() {
            return barcode + "," + title + "," + author + "," + checkedOut + "," + dueDate;
        }
    }
}
