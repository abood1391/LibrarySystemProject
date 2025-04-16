package gui;

import entities.Admin;
import entities.Book;
import entities.User;
import operations.Library;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class LibraryGUI extends Application {
    // Data models
    private Library library;
    private Admin admin;
    private User user;
    private ArrayList<Book> booksList;
    private ObservableList<BookModel> booksData;
    
    // Root elements
    private Stage primaryStage;
    private BorderPane mainLayout;
    
    // Login elements
    private PasswordField adminPasswordField;
    
    // Admin elements
    private TextField titleField;
    private TextField authorField;
    private TextField isbnField;
    private TextField removeIsbnField;
    
    // User elements
    private TableView<BookModel> booksTable;
    private TextField searchIsbnField;
    private TextArea resultArea;
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        // Initialize data
        library = new Library();
        admin = new Admin("admin", "admin");
        user = new User("User");
        booksList = new ArrayList<>();
        booksData = FXCollections.observableArrayList();
        
        // Set up the main stage
        primaryStage.setTitle("Library Management System");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Show the login screen
        showLoginScreen();
        
        primaryStage.show();
    }
    
    private void showLoginScreen() {
        // Create the main layout
        mainLayout = new BorderPane();
        
        // Create header
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 10, 20, 10));
        headerBox.setStyle("-fx-background-color: #336699;");
        
        Text title = new Text("Library Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.WHITE);
        
        headerBox.getChildren().add(title);
        mainLayout.setTop(headerBox);
        
        // Create buttons panel
        VBox buttonsPanel = new VBox(20);
        buttonsPanel.setAlignment(Pos.CENTER);
        buttonsPanel.setPadding(new Insets(50));
        
        Button adminLoginButton = new Button("Admin Login");
        Button userLoginButton = new Button("Enter as User");
        Button viewBookshelfButton = new Button("View Bookshelf");
        Button exitButton = new Button("Exit");
        
        adminLoginButton.setPrefWidth(200);
        userLoginButton.setPrefWidth(200);
        viewBookshelfButton.setPrefWidth(200);
        exitButton.setPrefWidth(200);
        
        adminLoginButton.setStyle("-fx-font-size: 16px;");
        userLoginButton.setStyle("-fx-font-size: 16px;");
        viewBookshelfButton.setStyle("-fx-font-size: 16px;");
        exitButton.setStyle("-fx-font-size: 16px;");
        
        adminLoginButton.setOnAction(e -> showAdminLoginDialog());
        userLoginButton.setOnAction(e -> showUserScreen());
        viewBookshelfButton.setOnAction(e -> {
            updateBookshelf();
            showBookshelfScreen();
        });
        exitButton.setOnAction(e -> Platform.exit());
        
        buttonsPanel.getChildren().addAll(adminLoginButton, userLoginButton, viewBookshelfButton, exitButton);
        
        // Add to layout
        mainLayout.setCenter(buttonsPanel);
        
        // Set scene
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
    }
    
    private void showAdminLoginDialog() {
        // Create dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Admin Login");
        dialog.setHeaderText("Enter Admin Password");
        
        // Set buttons
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        
        // Create password field
        adminPasswordField = new PasswordField();
        adminPasswordField.setPromptText("Password");
        
        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Password:"), 0, 0);
        grid.add(adminPasswordField, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        // Focus password field by default
        Platform.runLater(() -> adminPasswordField.requestFocus());
        
        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return adminPasswordField.getText();
            }
            return null;
        });
        
        // Show dialog and process result
        dialog.showAndWait().ifPresent(password -> {
            if (admin.login(password)) {
                showAdminScreen();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid password!");
            }
        });
    }
    
    private void showAdminScreen() {
        // Create main layout
        mainLayout = new BorderPane();
        
        // Create header
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 10, 20, 10));
        headerBox.setStyle("-fx-background-color: #336699;");
        
        Text title = new Text("Admin Panel");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.WHITE);
        
        headerBox.getChildren().add(title);
        mainLayout.setTop(headerBox);
        
        // Create tab pane for admin functions
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Add Book Tab
        Tab addBookTab = new Tab("Add Book");
        VBox addBookPane = new VBox(15);
        addBookPane.setPadding(new Insets(20));
        addBookPane.setAlignment(Pos.TOP_CENTER);
        
        GridPane addBookGrid = new GridPane();
        addBookGrid.setHgap(10);
        addBookGrid.setVgap(10);
        addBookGrid.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Title:");
        titleField = new TextField();
        titleField.setPromptText("Enter book title");
        
        Label authorLabel = new Label("Author:");
        authorField = new TextField();
        authorField.setPromptText("Enter author name");
        
        Label isbnLabel = new Label("ISBN:");
        isbnField = new TextField();
        isbnField.setPromptText("Enter ISBN");
        
        Button addButton = new Button("Add Book");
        addButton.setOnAction(e -> addBook());
        
        addBookGrid.add(titleLabel, 0, 0);
        addBookGrid.add(titleField, 1, 0);
        addBookGrid.add(authorLabel, 0, 1);
        addBookGrid.add(authorField, 1, 1);
        addBookGrid.add(isbnLabel, 0, 2);
        addBookGrid.add(isbnField, 1, 2);
        
        addBookPane.getChildren().addAll(addBookGrid, addButton);
        addBookTab.setContent(addBookPane);
        
        // Remove Book Tab
        Tab removeBookTab = new Tab("Remove Book");
        VBox removeBookPane = new VBox(15);
        removeBookPane.setPadding(new Insets(20));
        removeBookPane.setAlignment(Pos.TOP_CENTER);
        
        GridPane removeBookGrid = new GridPane();
        removeBookGrid.setHgap(10);
        removeBookGrid.setVgap(10);
        removeBookGrid.setAlignment(Pos.CENTER);
        
        Label removeLabel = new Label("ISBN:");
        removeIsbnField = new TextField();
        removeIsbnField.setPromptText("Enter ISBN to remove");
        
        Button removeButton = new Button("Remove Book");
        removeButton.setOnAction(e -> removeBook());
        
        removeBookGrid.add(removeLabel, 0, 0);
        removeBookGrid.add(removeIsbnField, 1, 0);
        
        removeBookPane.getChildren().addAll(removeBookGrid, removeButton);
        removeBookTab.setContent(removeBookPane);
        
        // Add tabs to pane
        tabPane.getTabs().addAll(addBookTab, removeBookTab);
        mainLayout.setCenter(tabPane);
        
        // Bottom buttons
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15));
        
        Button viewBookshelfButton = new Button("View Bookshelf");
        Button logoutButton = new Button("Logout");
        
        viewBookshelfButton.setOnAction(e -> {
            updateBookshelf();
            showBookshelfScreen();
        });
        logoutButton.setOnAction(e -> showLoginScreen());
        
        bottomBox.getChildren().addAll(viewBookshelfButton, logoutButton);
        mainLayout.setBottom(bottomBox);
        
        // Set scene
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
    }
    
    private void showUserScreen() {
        // Create main layout
        mainLayout = new BorderPane();
        
        // Create header
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 10, 20, 10));
        headerBox.setStyle("-fx-background-color: #336699;");
        
        Text title = new Text("User Panel");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.WHITE);
        
        headerBox.getChildren().add(title);
        mainLayout.setTop(headerBox);
        
        // Create tab pane for user functions
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // List Books Tab
        Tab listBooksTab = new Tab("List Books");
        VBox listBooksPane = new VBox(15);
        listBooksPane.setPadding(new Insets(20));
        
        // Create table
        booksTable = new TableView<>();
        booksTable.setPlaceholder(new Label("No books available"));
        
        TableColumn<BookModel, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        
        TableColumn<BookModel, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);
        
        TableColumn<BookModel, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(100);
        
        booksTable.getColumns().addAll(titleCol, authorCol, isbnCol);
        booksTable.setItems(booksData);
        
        Button refreshButton = new Button("Refresh Book List");
        refreshButton.setOnAction(e -> refreshBookList());
        
        listBooksPane.getChildren().addAll(booksTable, refreshButton);
        listBooksPane.setVgrow(booksTable, Priority.ALWAYS);
        listBooksTab.setContent(listBooksPane);
        
        // Search Book Tab
        Tab searchBookTab = new Tab("Search Book");
        VBox searchBookPane = new VBox(15);
        searchBookPane.setPadding(new Insets(20));
        searchBookPane.setAlignment(Pos.TOP_CENTER);
        
        GridPane searchBookGrid = new GridPane();
        searchBookGrid.setHgap(10);
        searchBookGrid.setVgap(10);
        searchBookGrid.setAlignment(Pos.CENTER);
        
        Label searchLabel = new Label("ISBN:");
        searchIsbnField = new TextField();
        searchIsbnField.setPromptText("Enter ISBN to search");
        
        Button searchButton = new Button("Search Book");
        resultArea = new TextArea();
        resultArea.setEditable(false);
        resultArea.setPrefRowCount(5);
        
        searchButton.setOnAction(e -> {
            String isbn = searchIsbnField.getText().trim();
            if (!isbn.isEmpty()) {
                // Create a custom output stream to capture System.out
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PrintStream customPrintStream = new PrintStream(outputStream);
                PrintStream oldOut = System.out;
                System.setOut(customPrintStream);
                
                library.searchBook(isbn);
                
                // Restore standard output
                System.setOut(oldOut);
                
                resultArea.setText(outputStream.toString());
            }
        });
        
        searchBookGrid.add(searchLabel, 0, 0);
        searchBookGrid.add(searchIsbnField, 1, 0);
        
        searchBookPane.getChildren().addAll(searchBookGrid, searchButton, new Label("Search Result:"), resultArea);
        searchBookTab.setContent(searchBookPane);
        
        // Add tabs to pane
        tabPane.getTabs().addAll(listBooksTab, searchBookTab);
        
        mainLayout.setCenter(tabPane);
        
        // Bottom buttons
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15));
        
        Button viewBookshelfButton = new Button("View Bookshelf");
        Button backButton = new Button("Back to Main Menu");
        
        viewBookshelfButton.setOnAction(e -> {
            updateBookshelf();
            showBookshelfScreen();
        });
        backButton.setOnAction(e -> showLoginScreen());
        
        bottomBox.getChildren().addAll(viewBookshelfButton, backButton);
        mainLayout.setBottom(bottomBox);
        
        // Refresh book list
        refreshBookList();
        
        // Set scene
        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
    }
    
    private void showBookshelfScreen() {
        // Create main layout
        mainLayout = new BorderPane();
        
        // Create header
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(20, 10, 20, 10));
        headerBox.setStyle("-fx-background-color: #336699;");
        
        Text title = new Text("Virtual Bookshelf");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setFill(Color.WHITE);
        
        headerBox.getChildren().add(title);
        mainLayout.setTop(headerBox);
        
        // Create bookshelf content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        
        VBox shelfContainer = new VBox(20);
        shelfContainer.setPadding(new Insets(20));
        shelfContainer.setAlignment(Pos.TOP_CENTER);
        
        if (booksList.isEmpty()) {
            Label emptyLabel = new Label("The bookshelf is empty!");
            emptyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            shelfContainer.getChildren().add(emptyLabel);
        } else {
            // Create shelves (rows of books)
            int booksPerShelf = 4;
            int totalShelves = (int) Math.ceil((double) booksList.size() / booksPerShelf);
            
            for (int shelfIndex = 0; shelfIndex < totalShelves; shelfIndex++) {
                // Add a shelf (wooden panel)
                Region shelf = new Region();
                shelf.setPrefHeight(20);
                shelf.setMaxWidth(Double.MAX_VALUE);
                shelf.setStyle("-fx-background-color: #8B4513; -fx-border-color: #5D2906; -fx-border-width: 2px;");
                shelfContainer.getChildren().add(shelf);
                
                // Add books to this shelf
                HBox booksRow = new HBox(10);
                booksRow.setPrefHeight(180);
                booksRow.setAlignment(Pos.BOTTOM_CENTER);
                
                int startBookIndex = shelfIndex * booksPerShelf;
                int endBookIndex = Math.min((shelfIndex + 1) * booksPerShelf, booksList.size());
                
                for (int i = startBookIndex; i < endBookIndex; i++) {
                    Book book = booksList.get(i);
                    VBox bookBox = createBookBox(book);
                    booksRow.getChildren().add(bookBox);
                }
                
                shelfContainer.getChildren().add(booksRow);
            }
        }
        
        scrollPane.setContent(shelfContainer);
        mainLayout.setCenter(scrollPane);
        
        // Bottom button
        HBox bottomBox = new HBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15));
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));
        
        bottomBox.getChildren().add(backButton);
        mainLayout.setBottom(bottomBox);
        
        // Set scene
        Scene bookshelfScene = new Scene(mainLayout);
        primaryStage.setScene(bookshelfScene);
    }
    
    private VBox createBookBox(Book book) {
        // Extract book title
        String bookStr = book.toString();
        String title = bookStr.substring(bookStr.indexOf("Title=") + 6, bookStr.indexOf(", Author="));
        
        // Create book container
        VBox bookBox = new VBox();
        bookBox.setPrefWidth(100);
        bookBox.setPrefHeight(150);
        bookBox.setMaxHeight(150);
        
        // Random color for book
        String[] colors = {
            "#DC143C", // Crimson
            "#008080", // Teal
            "#4682B4", // Steel Blue
            "#4B0082", // Indigo
            "#006400", // Dark Green
            "#B8860B", // Dark Goldenrod
            "#8B008B", // Dark Magenta
            "#556B2F"  // Dark Olive Green
        };
        String randomColor = colors[(int)(Math.random() * colors.length)];
        
        bookBox.setStyle("-fx-background-color: " + randomColor + "; " +
                        "-fx-border-color: black; -fx-border-width: 1px;");
        
        // Book title on spine
        Text titleText = new Text(title);
        titleText.setFill(Color.WHITE);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        titleText.setWrappingWidth(90);
        titleText.setTextAlignment(TextAlignment.CENTER);
        
        VBox textBox = new VBox(titleText);
        textBox.setAlignment(Pos.CENTER);
        textBox.setPrefHeight(150);
        
        bookBox.getChildren().add(textBox);
        
        // Tooltip with book info
        Tooltip tooltip = new Tooltip(bookStr);
        Tooltip.install(bookBox, tooltip);
        
        // Click event to show book details
        bookBox.setOnMouseClicked(event -> showAlert(Alert.AlertType.INFORMATION, "Book Details", bookStr));
        
        return bookBox;
    }
    
    private void updateBookshelf() {
        refreshBookData();
    }
    
    private void refreshBookData() {
        booksList.clear();
        
        // Create a custom output stream to capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream customPrintStream = new PrintStream(outputStream);
        PrintStream oldOut = System.out;
        System.setOut(customPrintStream);
        
        library.listBooks();
        
        // Restore standard output
        System.setOut(oldOut);
        
        String output = outputStream.toString();
        
        // Parse the output and add to booksList
        if (!output.contains("No books available.")) {
            String[] lines = output.split("\n");
            for (int i = 1; i < lines.length; i++) { // Skip the first line which is "Books List:"
                String line = lines[i];
                try {
                    // Parse the book details - assuming format: Book [Title=title, Author=author, ISBN=isbn]
                    String title = line.substring(line.indexOf("Title=") + 6, line.indexOf(", Author="));
                    String author = line.substring(line.indexOf("Author=") + 7, line.indexOf(", ISBN="));
                    String isbn = line.substring(line.indexOf("ISBN=") + 5, line.indexOf("]"));
                    
                    Book book = new Book(title, author, isbn);
                    booksList.add(book);
                } catch (Exception e) {
                    System.err.println("Error parsing book: " + line);
                }
            }
        }
    }
    
    private void refreshBookList() {
        // Clear previous data
        booksData.clear();
        
        // Create a custom output stream to capture System.out
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream customPrintStream = new PrintStream(outputStream);
        PrintStream oldOut = System.out;
        System.setOut(customPrintStream);
        
        library.listBooks();
        
        // Restore standard output
        System.setOut(oldOut);
        
        String output = outputStream.toString();
        
        // Parse the output and add to table
        if (!output.contains("No books available.")) {
            String[] lines = output.split("\n");
            for (int i = 1; i < lines.length; i++) { // Skip the first line which is "Books List:"
                String line = lines[i];
                try {
                    // Parse the book details - assuming format: Book [Title=title, Author=author, ISBN=isbn]
                    String title = line.substring(line.indexOf("Title=") + 6, line.indexOf(", Author="));
                    String author = line.substring(line.indexOf("Author=") + 7, line.indexOf(", ISBN="));
                    String isbn = line.substring(line.indexOf("ISBN=") + 5, line.indexOf("]"));
                    
                    booksData.add(new BookModel(title, author, isbn));
                } catch (Exception e) {
                    System.err.println("Error parsing book: " + line);
                }
            }
        }
    }
    
    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();
        
        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields are required!");
            return;
        }
        
        Book book = new Book(title, author, isbn);
        
        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream customPrintStream = new PrintStream(outputStream);
        PrintStream oldOut = System.out;
        System.setOut(customPrintStream);
        
        library.addBook(book);
        
        // Restore standard output
        System.setOut(oldOut);
        
        showAlert(Alert.AlertType.INFORMATION, "Success", outputStream.toString());
        
        // Clear fields
        titleField.clear();
        authorField.clear();
        isbnField.clear();
    }
    
    private void removeBook() {
        String isbn = removeIsbnField.getText().trim();
        
        if (isbn.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ISBN is required!");
            return;
        }
        
        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream customPrintStream = new PrintStream(outputStream);
        PrintStream oldOut = System.out;
        System.setOut(customPrintStream);
        
        library.removeBook(isbn);
        
        // Restore standard output
        System.setOut(oldOut);
        
        showAlert(Alert.AlertType.INFORMATION, "Book Removal", outputStream.toString());
        
        // Clear field
        removeIsbnField.clear();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Model class for TableView
    public static class BookModel {
        private String title;
        private String author;
        private String isbn;
        
        public BookModel(String title, String author, String isbn) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getAuthor() {
            return author;
        }
        
        public String getIsbn() {
            return isbn;
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}