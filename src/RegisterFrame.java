import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField, emailField, phoneField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Register");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); // Center on screen

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel phoneLabel = new JLabel("Phone Number:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        emailField = new JTextField();
        phoneField = new JTextField();
        JButton registerButton = new JButton("Register");

        // Styling labels and fields
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        usernameLabel.setFont(labelFont);
        passwordLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        emailField.setFont(fieldFont);
        phoneField.setFont(fieldFont);

        // Add components to panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(new JLabel()); // Empty for alignment
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);

        // Register button action listener
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String phone = phoneField.getText();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if (registerUser(username, password, email, phone)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose(); // Close the frame on successful registration
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Try again!");
            }
        });

        setVisible(true);
    }

    private boolean registerUser(String username, String password, String email, String phone) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO Users (username, password, email, phone) VALUES (?, SHA2(?, 256), ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // Hashing password with SHA-256
            stmt.setString(3, email);
            stmt.setString(4, phone);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterFrame::new);
    }
}
