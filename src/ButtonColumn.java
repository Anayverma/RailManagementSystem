import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class ButtonColumn extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    private JButton button;
    private ActionListener actionListener;
    private int column;

    public ButtonColumn(JTable table, int column) {
        this.column = column;
        button = new JButton();
        button.setOpaque(true);
        
        // Add an action listener for the button click
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();  // Stop editing after the button is pressed
                if (actionListener != null) {
                    actionListener.actionPerformed(e);  // Notify the action listener
                }
            }
        });
    }

    // Set an action listener for the button
    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    // Get the value when the cell is edited (for our case, button's text)
    public Object getCellEditorValue() {
        return button.getText();  // Returns the text of the button (if needed)
    }

    // Render the button in the table cell
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Set button text, background color, and style when rendering the button
        button.setText("Book");  // Set button text here
        button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return button;
    }

    // When the cell is in edit mode, render the button for editing
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText("Book");  // Set button text here as well
        return button;
    }
}
