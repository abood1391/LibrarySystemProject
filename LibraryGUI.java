package gui;

import entities.Admin;
import entities.Book;
import entities.User;
import operations.Library;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class LibraryGUI extends JFrame {
    private Library library;
    private Admin admin;
    private User user;
    private ArrayList<Book> booksList;  // To keep track of books for the bookshelf

    // Main panels
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel adminPanel;
    private JPanel userPanel;
    private JPanel bookshelfPanel;

    // Login components
    private JButton adminLoginButton;
    private JButton userLoginButton;

    // Admin components
    private JTextField adminPasswordField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField isbnField;
    private JTextField removeIsbnField;

    // User components
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchIsbnField;

    // Common components
    private JButton backButton;

    public LibraryGUI() {
        // Initialize data
        library = new Library();
        admin = new Admin("admin", "admin");
        user = new User("User");
        booksList = new ArrayList<>();

        // Setup frame
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create panels
        createMainPanel();
        createLoginPanel();
        createAdminPanel();
        createUserPanel();
        createBookshelfPanel();

        // Add main panel to frame
        setContentPane(mainPanel);

        // Show the frame
        setVisible(true);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new CardLayout());
    }

    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Library Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(new EmptyBorder(20, 10, 20, 10));
        loginPanel.add(headerLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(40, 10, 10, 10));
        buttonsPanel.setLayout(new GridLayout(3, 1, 10, 20));

        adminLoginButton = new JButton("Admin Login");
        userLoginButton = new JButton("Enter as User");
        JButton viewBookshelfButton = new JButton("View Bookshelf");

        adminLoginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        userLoginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewBookshelfButton.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add action listeners
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminLogin();
            }
        });

        userLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserPanel();
            }
        });

        viewBookshelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBookshelf();
                showBookshelfPanel();
            }
        });

        buttonsPanel.add(adminLoginButton);
        buttonsPanel.add(userLoginButton);
        buttonsPanel.add(viewBookshelfButton);

        // Center the buttons panel
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(buttonsPanel);
        loginPanel.add(centerPanel, BorderLayout.CENTER);

        // Exit button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        bottomPanel.add(exitButton);
        loginPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add to main panel
        mainPanel.add(loginPanel, "login");
    }

    private void createAdminPanel() {
        adminPanel = new JPanel();
        adminPanel.setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Admin Panel", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        adminPanel.add(headerLabel, BorderLayout.NORTH);

        // Create tabbed pane for admin functions
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Book Panel
        JPanel addBookPanel = new JPanel();
        addBookPanel.setLayout(new BoxLayout(addBookPanel, BoxLayout.Y_AXIS));
        addBookPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(20);
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField(20);
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnField = new JTextField(20);
        JButton addButton = new JButton("Add Book");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        // Add components with some spacing
        addBookPanel.add(titleLabel);
        addBookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addBookPanel.add(titleField);
        addBookPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        addBookPanel.add(authorLabel);
        addBookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addBookPanel.add(authorField);
        addBookPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        addBookPanel.add(isbnLabel);
        addBookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addBookPanel.add(isbnField);
        addBookPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        addBookPanel.add(buttonPanel);

        // Remove Book Panel
        JPanel removeBookPanel = new JPanel();
        removeBookPanel.setLayout(new BoxLayout(removeBookPanel, BoxLayout.Y_AXIS));
        removeBookPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel removeLabel = new JLabel("Enter ISBN to remove:");
        removeIsbnField = new JTextField(20);
        JButton removeButton = new JButton("Remove Book");

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeBook();
            }
        });

        removeBookPanel.add(removeLabel);
        removeBookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        removeBookPanel.add(removeIsbnField);
        removeBookPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel removeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        removeButtonPanel.add(removeButton);
        removeBookPanel.add(removeButtonPanel);

        // Add tabs
        tabbedPane.addTab("Add Book", addBookPanel);
        tabbedPane.addTab("Remove Book", removeBookPanel);

        adminPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton viewBookshelfButton = new JButton("View Bookshelf");
        viewBookshelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBookshelf();
                showBookshelfPanel();
            }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });

        bottomPanel.add(viewBookshelfButton);
        bottomPanel.add(logoutButton);
        adminPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add to main panel
        mainPanel.add(adminPanel, "admin");
    }

    private void createUserPanel() {
        userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("User Panel", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        userPanel.add(headerLabel, BorderLayout.NORTH);

        // Create tabbed pane for user functions
        JTabbedPane tabbedPane = new JTabbedPane();

        // List Books Panel
        JPanel listBooksPanel = new JPanel(new BorderLayout());
        listBooksPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("ISBN");

        booksTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(booksTable);

        JButton refreshButton = new JButton("Refresh Book List");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshBookList();
            }
        });

        listBooksPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPanel.add(refreshButton);
        listBooksPanel.add(refreshPanel, BorderLayout.SOUTH);

        // Search Book Panel
        JPanel searchBookPanel = new JPanel();
        searchBookPanel.setLayout(new BoxLayout(searchBookPanel, BoxLayout.Y_AXIS));
        searchBookPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel searchLabel = new JLabel("Enter ISBN to search:");
        searchIsbnField = new JTextField(20);
        JButton searchButton = new JButton("Search Book");
        JTextArea resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = searchIsbnField.getText().trim();
                if (!isbn.isEmpty()) {
                    // Create a custom class to capture output
                    CustomOutputStream outputStream = new CustomOutputStream();
                    System.setOut(new java.io.PrintStream(outputStream));

                    library.searchBook(isbn);

                    // Restore standard output
                    System.setOut(System.out);

                    resultArea.setText(outputStream.getOutput());
                }
            }
        });

        searchBookPanel.add(searchLabel);
        searchBookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        searchBookPanel.add(searchIsbnField);
        searchBookPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchButtonPanel.add(searchButton);
        searchBookPanel.add(searchButtonPanel);
        searchBookPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        searchBookPanel.add(new JLabel("Search Result:"));
        searchBookPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        searchBookPanel.add(resultScrollPane);

        // Add tabs
        tabbedPane.addTab("List Books", listBooksPanel);
        tabbedPane.addTab("Search Book", searchBookPanel);

        userPanel.add(tabbedPane, BorderLayout.CENTER);

        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton viewBookshelfButton = new JButton("View Bookshelf");
        viewBookshelfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBookshelf();
                showBookshelfPanel();
            }
        });

        JButton backButtonUser = new JButton("Back to Main Menu");
        backButtonUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });

        bottomPanel.add(viewBookshelfButton);
        bottomPanel.add(backButtonUser);
        userPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add to main panel
        mainPanel.add(userPanel, "user");
    }

    private void createBookshelfPanel() {
        bookshelfPanel = new JPanel(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Virtual Bookshelf", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bookshelfPanel.add(headerLabel, BorderLayout.NORTH);

        // Book shelf container (will be populated dynamically)
        JPanel shelfContainer = new JPanel();
        shelfContainer.setLayout(new BoxLayout(shelfContainer, BoxLayout.Y_AXIS));

        // Placeholder text
        JLabel placeholderLabel = new JLabel("Loading bookshelf...", JLabel.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        shelfContainer.add(placeholderLabel);

        JScrollPane scrollPane = new JScrollPane(shelfContainer);
        bookshelfPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButtonShelf = new JButton("Back");
        backButtonShelf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to previous screen
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.previous(mainPanel);
            }
        });
        bottomPanel.add(backButtonShelf);
        bookshelfPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add to main panel
        mainPanel.add(bookshelfPanel, "bookshelf");
    }

    private void updateBookshelf() {
        // Get the container from the scroll pane
        JScrollPane scrollPane = (JScrollPane) bookshelfPanel.getComponent(1);
        JViewport viewport = scrollPane.getViewport();
        JPanel shelfContainer = new JPanel();
        shelfContainer.setLayout(new BoxLayout(shelfContainer, BoxLayout.Y_AXIS));

        // Create the bookshelf
        refreshBookData();

        if (booksList.isEmpty()) {
            JLabel emptyLabel = new JLabel("The bookshelf is empty!", JLabel.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            shelfContainer.add(Box.createVerticalStrut(50));
            shelfContainer.add(emptyLabel);
        } else {
            // Create shelves (rows of books)
            int booksPerShelf = 4;
            int totalShelves = (int) Math.ceil((double) booksList.size() / booksPerShelf);

            for (int shelfIndex = 0; shelfIndex < totalShelves; shelfIndex++) {
                // Add a shelf (wooden panel)
                JPanel shelf = createShelf();
                shelfContainer.add(shelf);

                // Add books to this shelf
                JPanel booksRow = new JPanel(new GridLayout(1, booksPerShelf, 10, 0));
                booksRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
                booksRow.setBackground(new Color(245, 245, 245));

                int startBookIndex = shelfIndex * booksPerShelf;
                int endBookIndex = Math.min((shelfIndex + 1) * booksPerShelf, booksList.size());

                for (int i = startBookIndex; i < endBookIndex; i++) {
                    Book book = booksList.get(i);
                    JPanel bookPanel = createBookPanel(book);
                    booksRow.add(bookPanel);
                }

                // Fill remaining slots with empty spaces if needed
                for (int i = endBookIndex - startBookIndex; i < booksPerShelf; i++) {
                    JPanel emptySlot = new JPanel();
                    emptySlot.setBackground(new Color(245, 245, 245));
                    booksRow.add(emptySlot);
                }

                shelfContainer.add(booksRow);
            }
        }

        viewport.setView(shelfContainer);
    }

    private JPanel createShelf() {
        JPanel shelfPanel = new JPanel();
        shelfPanel.setBackground(new Color(139, 69, 19)); // Brown color for wooden shelf
        shelfPanel.setPreferredSize(new Dimension(100, 20));
        shelfPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        shelfPanel.setBorder(BorderFactory.createBevelBorder(1));
        return shelfPanel;
    }

    private JPanel createBookPanel(Book book) {
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BorderLayout());
        bookPanel.setBackground(getRandomBookColor());
        bookPanel.setBorder(new LineBorder(Color.BLACK, 1));

        // Book title on the spine
        JLabel titleLabel = new JLabel("<html><center>" + book.toString().split("Title=")[1].split(",")[0] + "</center></html>", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 10));
        bookPanel.add(titleLabel, BorderLayout.CENTER);

        // Add tooltip with full book info
        bookPanel.setToolTipText(book.toString());

        // Add click listener to show book details
        bookPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(bookshelfPanel, book.toString(), "Book Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return bookPanel;
    }

    private Color getRandomBookColor() {
        Color[] bookColors = {
                new Color(220, 20, 60),  // Crimson
                new Color(0, 128, 128),  // Teal
                new Color(70, 130, 180), // Steel Blue
                new Color(75, 0, 130),   // Indigo
                new Color(0, 100, 0),    // Dark Green
                new Color(184, 134, 11), // Dark Goldenrod
                new Color(139, 0, 139),  // Dark Magenta
                new Color(85, 107, 47)   // Dark Olive Green
        };

        return bookColors[(int)(Math.random() * bookColors.length)];
    }

    private void refreshBookData() {
        booksList.clear();

        // Create a custom output stream to capture System.out
        CustomOutputStream outputStream = new CustomOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        library.listBooks();

        // Restore standard output
        System.setOut(System.out);

        String output = outputStream.getOutput();

        // Parse the output and add to booksList
        if (!output.contains("No books available.")) {
            String[] lines = output.split("\n");
            for (int i = 1; i < lines.length; i++) { // Skip the first line which is "Books List:"
                String line = lines[i];
                // Parse the book details - assuming format: Book [Title=title, Author=author, ISBN=isbn]
                try {
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

    private void showAdminLogin() {
        JPanel loginDialogPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        loginDialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel passwordLabel = new JLabel("Enter Admin Password:");
        adminPasswordField = new JPasswordField(15);

        loginDialogPanel.add(passwordLabel);
        loginDialogPanel.add(adminPasswordField);

        int result = JOptionPane.showConfirmDialog(this, loginDialogPanel,
                "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String password = adminPasswordField.getText();
            if (admin.login(password)) {
                showAdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showLoginPanel() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "login");
    }

    private void showAdminPanel() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "admin");
    }

    private void showUserPanel() {
        refreshBookList();
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "user");
    }

    private void showBookshelfPanel() {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "bookshelf");
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Book book = new Book(title, author, isbn);

        // Create a custom output stream to capture System.out
        CustomOutputStream outputStream = new CustomOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        library.addBook(book);

        // Restore standard output
        System.setOut(System.out);

        JOptionPane.showMessageDialog(this, outputStream.getOutput(), "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear fields
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
    }

    private void removeBook() {
        String isbn = removeIsbnField.getText().trim();

        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ISBN is required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a custom output stream to capture System.out
        CustomOutputStream outputStream = new CustomOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        library.removeBook(isbn);

        // Restore standard output
        System.setOut(System.out);

        JOptionPane.showMessageDialog(this, outputStream.getOutput(), "Book Removal", JOptionPane.INFORMATION_MESSAGE);

        // Clear field
        removeIsbnField.setText("");
    }

    private void refreshBookList() {
        // Clear previous data
        tableModel.setRowCount(0);

        // Create a custom output stream to capture System.out
        CustomOutputStream outputStream = new CustomOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        library.listBooks();

        // Restore standard output
        System.setOut(System.out);

        String output = outputStream.getOutput();

        // Parse the output and add to table
        // This is a simple parsing that assumes the format from the Library class
        if (!output.contains("No books available.")) {
            String[] lines = output.split("\n");
            for (int i = 1; i < lines.length; i++) { // Skip the first line which is "Books List:"
                String line = lines[i];
                // Parse the book details - assuming format: Book [Title=title, Author=author, ISBN=isbn]
                String title = line.substring(line.indexOf("Title=") + 6, line.indexOf(", Author="));
                String author = line.substring(line.indexOf("Author=") + 7, line.indexOf(", ISBN="));
                String isbn = line.substring(line.indexOf("ISBN=") + 5, line.indexOf("]"));

                tableModel.addRow(new Object[]{title, author, isbn});
            }
        }
    }

    // Custom output stream to capture System.out
    private class CustomOutputStream extends java.io.ByteArrayOutputStream {
        public String getOutput() {
            return toString();
        }
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryGUI();
            }
        });
    }
}