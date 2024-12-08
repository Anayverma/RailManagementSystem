import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BookTicketFrame {
    private JFrame frame;
    private int loggedInUserId;

    public BookTicketFrame(int loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        frame = new JFrame("Book Ticket");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600); // Increased overall frame size
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Labels and Fields
        JLabel nameLabel = new JLabel("Your Name:");
        JTextField nameField = new JTextField(30); // Increased width

        JLabel sourceLabel = new JLabel("Source:");
        JTextField sourceField = new JTextField(30); // Increased width

        JLabel destinationLabel = new JLabel("Destination:");
        JTextField destinationField = new JTextField(30); // Increased width

        JLabel travelDateLabel = new JLabel("Travel Date (YYYY-MM-DD):");
        JTextField travelDateField = new JTextField(30); // Increased width

        JLabel classLabel = new JLabel("Class (Sleeper/AC):");
        String[] classes = {"Sleeper", "AC"};
        JComboBox<String> classComboBox = new JComboBox<>(classes);
        classComboBox.setPreferredSize(new Dimension(250, 25)); // Wider combo box

        JLabel numTicketsLabel = new JLabel("Number of Tickets:");
        JSpinner numTicketsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        numTicketsSpinner.setPreferredSize(new Dimension(250, 25)); // Wider spinner

        JLabel trainIdLabel = new JLabel("Train ID:");
        JTextField trainIdField = new JTextField(30); // Increased width

        JLabel bookingDateLabel = new JLabel("Booking Date (YYYY-MM-DD):");
        JTextField bookingDateField = new JTextField(30); // Increased width

        JLabel bookingTypeLabel = new JLabel("Booking Type (Regular/Tatkal):");
        String[] bookingTypes = {"Regular", "Tatkal"};
        JComboBox<String> bookingTypeComboBox = new JComboBox<>(bookingTypes);
        bookingTypeComboBox.setPreferredSize(new Dimension(250, 25)); // Wider combo box

        // Setting up layout with wider components
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(sourceLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(sourceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(destinationLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(destinationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(travelDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(travelDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(classLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(classComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(numTicketsLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(numTicketsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(trainIdLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(trainIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        mainPanel.add(bookingDateLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(bookingDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        mainPanel.add(bookingTypeLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(bookingTypeComboBox, gbc);

        // Book Ticket Button
        JButton bookTicketButton = new JButton("Book Ticket");
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        mainPanel.add(bookTicketButton, gbc);

        // Add mainPanel to frame
        frame.add(mainPanel, BorderLayout.CENTER);

        // User info panel
        JPanel userInfoPanel = new JPanel(new BorderLayout());
        JLabel loggedInLabel = new JLabel("Logged in as: " + loggedInUserId);
        userInfoPanel.add(loggedInLabel, BorderLayout.EAST);
        frame.add(userInfoPanel, BorderLayout.NORTH);

        // Action Listener for Book Ticket Button
        bookTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String source = sourceField.getText().trim();
                String destination = destinationField.getText().trim();
                String travelDate = travelDateField.getText().trim();
                String trainId = trainIdField.getText().trim();
                String bookingDate = bookingDateField.getText().trim();
                String travelClass = (String) classComboBox.getSelectedItem();
                int numTickets = (int) numTicketsSpinner.getValue();
                String bookingType = (String) bookingTypeComboBox.getSelectedItem();

                if (validateInput(name, source, destination, travelDate, trainId, bookingDate)) {
                    if (bookTicket(name, source, destination, travelDate, trainId, bookingDate, travelClass, numTickets, bookingType)) {
                        // Show payment confirmation dialog
                        PaymentConfirmationDialog paymentDialog = new PaymentConfirmationDialog(frame, numTickets);
                        paymentDialog.setVisible(true);

                        // Close booking frame after booking
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Failed to book ticket. Please try again.");
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean validateInput(String name, String source, String destination, String travelDate, String trainId, String bookingDate) {
        if (name.isEmpty() || source.isEmpty() || destination.isEmpty() || travelDate.isEmpty() || trainId.isEmpty() || bookingDate.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
            return false;
        }

        if (!isValidDate(travelDate) || !isValidDate(bookingDate)) {
            JOptionPane.showMessageDialog(frame, "Invalid date format. Use YYYY-MM-DD.");
            return false;
        }

        return true;
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean bookTicket(String name, String source, String destination, String travelDate, String trainId, String bookingDate, String travelClass, int numTickets, String bookingType) {
        try {
            Connection conn = DBConnection.getConnection(); // Replace with your DB connection method
            String getLastTicketNumberQuery = "SELECT MAX(ticket_number) FROM Tickets WHERE travel_class = ?";
            PreparedStatement getLastTicketStmt = conn.prepareStatement(getLastTicketNumberQuery);
            getLastTicketStmt.setString(1, travelClass);
            ResultSet rs = getLastTicketStmt.executeQuery();

            int lastTicketNumber = 0;
            if (rs.next()) {
                String lastTicket = rs.getString(1);
                if (lastTicket != null && !lastTicket.isEmpty()) {
                    lastTicketNumber = Integer.parseInt(lastTicket.replaceAll("[^0-9]", "")) + 1;
                }
            }

            String ticketNumber = travelClass.substring(0, 1) + lastTicketNumber;

            String insertTicketQuery = "INSERT INTO Tickets (user_id, train_id, booking_date, travel_date, seats_booked, booking_type, source, destination, travel_class, ticket_count, booked_by, ticket_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertTicketStmt = conn.prepareStatement(insertTicketQuery);
            insertTicketStmt.setInt(1, loggedInUserId);
            insertTicketStmt.setString(2, trainId);
            insertTicketStmt.setString(3, bookingDate);
            insertTicketStmt.setString(4, travelDate);
            insertTicketStmt.setInt(5, numTickets);
            insertTicketStmt.setString(6, bookingType);
            insertTicketStmt.setString(7, source);
            insertTicketStmt.setString(8, destination);
            insertTicketStmt.setString(9, travelClass);
            insertTicketStmt.setInt(10, numTickets);
            insertTicketStmt.setString(11, name);
            insertTicketStmt.setString(12, ticketNumber);

            int rowsInserted = insertTicketStmt.executeUpdate();
            conn.close();
            return rowsInserted > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Inner class for Payment Confirmation Dialog
    class PaymentConfirmationDialog extends JDialog {
        public PaymentConfirmationDialog(JFrame parent, int numTickets) {
            super(parent, "Payment Confirmation", true);
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel paymentLabel = new JLabel("Payment successful!");
            paymentLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(paymentLabel, BorderLayout.NORTH);

            JLabel seatLabel = new JLabel("Seat number: " + generateSeatNumber(numTickets));
            panel.add(seatLabel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            panel.add(closeButton, BorderLayout.SOUTH);

            setContentPane(panel);
            setSize(300, 200);
            setLocationRelativeTo(parent);
        }


        private String generateSeatNumber(int numTickets) {
            // Logic to generate and retrieve seat number from database or another source
            // For demonstration, generating a simple seat number
            return "S" + (int) (Math.random() * 1000); // Example: Generates seat numbers like S123, S456, etc.
        }
    }

    // For testing purposes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BookTicketFrame(1); // Replace 1 with the actual logged in user ID
        });
    }
}
