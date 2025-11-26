package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MealBookingFrame extends JFrame implements ActionListener {

    private JComboBox<String> menuDropdown;
    private JTextField dateField, quantityField;
    private JComboBox<String> typeSelector;
    private JButton confirmBtn, refreshBtn;
    private int sessionUserId;

    public MealBookingFrame(int userId) {
        this.sessionUserId = userId;
        setTitle("Meal Booking");
        setLayout(new GridLayout(5, 1));

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Menu Item:"));
        menuDropdown = new JComboBox<>();
        panel1.add(menuDropdown);
        add(panel1);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(10);
        panel2.add(dateField);
        add(panel2);

        JPanel panel3 = new JPanel();
        panel3.add(new JLabel("Meal Type:"));
        typeSelector = new JComboBox<>(new String[]{"breakfast", "lunch", "dinner"});
        panel3.add(typeSelector);
        panel3.add(new JLabel("Qty:"));
        quantityField = new JTextField(3);
        panel3.add(quantityField);
        add(panel3);

        JPanel panel4 = new JPanel();
        confirmBtn = new JButton("Book");
        confirmBtn.addActionListener(this);
        panel4.add(confirmBtn);

        refreshBtn = new JButton("Load Menu");
        refreshBtn.addActionListener(this);
        panel4.add(refreshBtn);
        add(panel4);

        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Load Menu")) {
            loadAvailableDishes();
        } else if (command.equals("Book")) {
            executeBooking();
        }
    }

    private void loadAvailableDishes() {
        menuDropdown.removeAllItems();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM menu_items WHERE available=1")) {
             
            while (rs.next()) {
                menuDropdown.addItem(rs.getInt("id") + ":" + rs.getString("name"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load err: " + ex.getMessage());
        }
    }

    private void executeBooking() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
             
            String selectedItem = (String) menuDropdown.getSelectedItem();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "Load menu first");
                return;
            }

            int itemId = Integer.parseInt(selectedItem.split(":")[0]);
            String bookDate = dateField.getText();
            String type = (String) typeSelector.getSelectedItem();
            int amount = Integer.parseInt(quantityField.getText());

            String query = "INSERT INTO bookings(user_id, menu_item_id, meal_date, meal_type, qty) " +
                           "VALUES(" + sessionUserId + ", " + itemId + ", '" + bookDate + "', '" + type + "', " + amount + ")";
            
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(this, "Booked");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Book err: " + ex.getMessage());
        }
    }
}
