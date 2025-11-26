package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MealBookingFrame extends JFrame implements ActionListener {

    JComboBox<String> cbMenu;
    JTextField tfDate, tfQty;
    JComboBox<String> cbMealType;
    JButton bBook, bLoad;
    int userId;

    public MealBookingFrame(int userId) {
        this.userId = userId;

        setTitle("Meal Booking");
        setLayout(new GridLayout(5, 1, 5, 5));

        
        JPanel p1 = new JPanel();
        p1.add(new JLabel("Menu Item:"));
        cbMenu = new JComboBox<>();
        p1.add(cbMenu);
        add(p1);

        
        JPanel p2 = new JPanel();
        p2.add(new JLabel("Date (YYYY-MM-DD):"));
        tfDate = new JTextField(10);
        p2.add(tfDate);
        add(p2);

        
        JPanel p3 = new JPanel();
        p3.add(new JLabel("Meal Type:"));
        cbMealType = new JComboBox<>(new String[]{"breakfast", "lunch", "dinner"});
        p3.add(cbMealType);

        p3.add(new JLabel("Qty:"));
        tfQty = new JTextField(4);
        p3.add(tfQty);
        add(p3);

        
        JPanel p4 = new JPanel();
        bBook = new JButton("Book");
        bBook.addActionListener(this);
        p4.add(bBook);

        bLoad = new JButton("Load Menu");
        bLoad.addActionListener(this);
        p4.add(bLoad);

        add(p4);

        
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if (cmd.equals("Load Menu")) {
            loadMenu();
        }
        else if (cmd.equals("Book")) {
            bookMeal();
        }
    }

    
    private void loadMenu() {
        cbMenu.removeAllItems();

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name FROM menu_items WHERE available = 1")) {

            while (rs.next()) {
                cbMenu.addItem(rs.getInt("id") + ":" + rs.getString("name"));
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load err: " + e.getMessage());
        }
    }

    private void bookMeal() {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement()) {

            String item = (String) cbMenu.getSelectedItem();
            if (item == null) {
                JOptionPane.showMessageDialog(this, "Load menu first");
                return;
            }

            int menuId = Integer.parseInt(item.split(":")[0]);
            String date = tfDate.getText();
            String mealType = (String) cbMealType.getSelectedItem();
            int qty = Integer.parseInt(tfQty.getText());

            String sql = "INSERT INTO bookings(user_id, menu_item_id, meal_date, meal_type, qty) VALUES("
                       + userId + "," + menuId + ",'" + date + "','" + mealType + "'," + qty + ")";

            st.executeUpdate(sql);

            JOptionPane.showMessageDialog(this, "Booked");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Book err: " + e.getMessage());
        }
    }
}
