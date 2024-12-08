import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

public class RailwaySystemGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton loginButton, registerButton, bookTicketButton, showTrainsButton, bookOfflineButton;
    private JLabel loggedInLabel, titleLabel;
    private JTextField sourceField, destinationField;

    public RailwaySystemGUI() {
        // Use system look and feel for a clean, native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main frame setup
        frame = new JFrame("Railway System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);

        // Main panel with simple layout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Simple title
        titleLabel = new JLabel("Railway System", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Center panel for components
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        // Create simple buttons with shadows
        loginButton = createShadowButton("Login");
        registerButton = createShadowButton("Register");
        bookTicketButton = createShadowButton("Book Ticket");
        showTrainsButton = createShadowButton("Show Available Trains");
        bookOfflineButton = createShadowButton("Offline Booking");

        // Simple text fields
        sourceField = createSimpleTextField("Source Station");
        destinationField = createSimpleTextField("Destination Station");

        // Add components to center panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(loginButton, gbc);

        gbc.gridy = 1;
        centerPanel.add(registerButton, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JLabel searchLabel = new JLabel("Search Trains", JLabel.CENTER);
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        centerPanel.add(searchLabel, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        centerPanel.add(new JLabel("Source:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        centerPanel.add(sourceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        centerPanel.add(new JLabel("Destination:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        centerPanel.add(destinationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        centerPanel.add(showTrainsButton, gbc);
        
        gbc.gridy = 6;
        centerPanel.add(bookTicketButton, gbc);

        gbc.gridy = 7;
        centerPanel.add(bookOfflineButton, gbc);

        // Logged in label
        loggedInLabel = new JLabel("Not Logged In", JLabel.RIGHT);
        loggedInLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(loggedInLabel, BorderLayout.SOUTH);

        // Add action listeners
        addActionListeners();

        // Final frame setup
        frame.add(mainPanel);
        frame.setVisible(true);

        // Update logged in label
        updateLoggedInLabel();
    }

    // Button with shadow effect
    private JButton createShadowButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(250, 40));
        
        // Create shadow effect
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(3, 3, 3, 3),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            )
        ));

        // Subtle hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(3, 3, 3, 3),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    )
                ));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(3, 3, 3, 3),
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                    )
                ));
            }
        });

        return button;
    }

    // Simple text field
    private JTextField createSimpleTextField(String placeholder) {
        JTextField textField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(Color.GRAY);
                    g2d.setFont(getFont().deriveFont(Font.ITALIC));
                    g2d.drawString(placeholder, 5, 20);
                    g2d.dispose();
                }
            }
        };
        
        textField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        return textField;
    }
    // Add action listeners (same as before)
    private void addActionListeners() {
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(new RegisterAction());
        bookTicketButton.addActionListener(new BookTicketAction());
        showTrainsButton.addActionListener(new ShowTrainsAction());
        bookOfflineButton.addActionListener(new BookOfflineAction());
    }

    private void updateLoggedInLabel() {
        String username = SessionManager.getInstance().getLoggedInUsername();
        if (username != null) {
            loggedInLabel.setText("Logged in as: " + username);
        } else {
            loggedInLabel.setText("Not Logged In");
        }
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new LoginFrame();
            updateLoggedInLabel(); // Refresh label after login
        }
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new RegisterFrame();
        }
    }

    private class BookTicketAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!SessionManager.getInstance().isLoggedIn()) {
                JOptionPane.showMessageDialog(frame, "You must log in first to book a ticket.");
                new LoginFrame();
            } else {
                String loggedInUsername = SessionManager.getInstance().getLoggedInUsername();
                int userId = getUserIdFromUsername(loggedInUsername);
                if (userId != -1) {
                    new BookTicketFrame(userId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to retrieve user ID.");
                }
            }
        }
    }

    private class ShowTrainsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String source = sourceField.getText();
            String destination = destinationField.getText();
            if (source.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both source and destination.");
            } else {
                showAvailableTrains(source, destination);
            }
        }
    }

    private int getUserIdFromUsername(String username) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT user_id FROM Users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            } else {
                return -1; // User not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private class BookOfflineAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String source = sourceField.getText();
            String destination = destinationField.getText();
            if (source.isEmpty() || destination.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both source and destination.");
            } else {
                showOfflineBookingPopup(source, destination);
            }
        }
    }

    private void showOfflineBookingPopup(String source, String destination) {
        String message = "Please share your details via message on number 3434343434\n\n"
                + "Source: " + source + "\n"
                + "Destination: " + destination;

        JOptionPane.showMessageDialog(frame, message, "Offline Booking Instructions", JOptionPane.INFORMATION_MESSAGE);
    }


    // Function to fetch available trains based on source and destination
    private void showAvailableTrains(String source, String destination) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT train_id, train_name, total_seats FROM Trains WHERE source = ? AND destination = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, source);
            stmt.setString(2, destination);
            ResultSet rs = stmt.executeQuery();

            // Create a table model with columns for train details and the new "Main Options" column
            DefaultTableModel model = new DefaultTableModel(new String[]{"Train ID", "Train Name", "Available Seats", "Book"}, 0);
            while (rs.next()) {
                // Adding a row for each train with a button for "Book"
                model.addRow(new Object[]{
                        rs.getInt("train_id"),
                        rs.getString("train_name"),
                        rs.getInt("total_seats"),
                        "Book" // "Book" button to proceed with booking
                });
            }

            // Set the table model to display in the UI
            JTable trainTable = new JTable(model);
            trainTable.setFillsViewportHeight(true);
            JScrollPane scrollPane = new JScrollPane(trainTable);

            // Create a frame or panel to show the table
            JFrame trainFrame = new JFrame("Available Trains");
            trainFrame.add(scrollPane, BorderLayout.CENTER);
            trainFrame.setSize(600, 400);
            trainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            trainFrame.setVisible(true);

            // Add the ButtonColumn to handle actions when the button is clicked
            ButtonColumn buttonColumn = new ButtonColumn(trainTable, 3);
            buttonColumn.setActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = trainTable.getSelectedRow();
                    int trainId = (int) trainTable.getValueAt(selectedRow, 0);
                    int availableSeats = (int) trainTable.getValueAt(selectedRow, 2);

                    // Check seat availability
                    if (availableSeats > 0) {
                        // Show booking dialog with pre-filled details
                        showBookingDialog(trainId);
                    } else {
                        JOptionPane.showMessageDialog(trainFrame, "No available seats on this train.");
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error fetching available trains.");
        }
    }

    private void showBookingDialog(int trainId) {
        String loggedInUsername = SessionManager.getInstance().getLoggedInUsername();
        int userId = getUserIdFromUsername(loggedInUsername);

        if (userId == -1) {
            JOptionPane.showMessageDialog(frame, "Failed to retrieve user details.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Fetch user details
            String query = "SELECT username, email, source, destination FROM Users WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("username");
                String email = rs.getString("email");
                String source = rs.getString("source");
                String destination = rs.getString("destination");

                // Create a dialog with a combo box for pre-filled user details
                JComboBox<String> userDetailsComboBox = new JComboBox<>();
                userDetailsComboBox.addItem("Name: " + name);
                userDetailsComboBox.addItem("Email: " + email);
                userDetailsComboBox.addItem("Source: " + source);
                userDetailsComboBox.addItem("Destination: " + destination);

                JPanel panel = new JPanel();
                panel.add(userDetailsComboBox);

                int response = JOptionPane.showConfirmDialog(frame, panel, "Booking Details", JOptionPane.OK_CANCEL_OPTION);
                if (response == JOptionPane.OK_OPTION) {
                    // Proceed with booking
                    bookTicket(trainId, userId, source, destination);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void bookTicket(int trainId, int userId, String source, String destination) {
        // Booking logic, such as updating seat availability, inserting a booking record, etc.
        JOptionPane.showMessageDialog(frame, "Booking successful for train " + trainId);
    }

    public static void main(String[] args) {
        new RailwaySystemGUI();
    }
}
