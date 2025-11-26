package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MenuManagementFrame extends JFrame implements ActionListener {

    private JTextField tfName, tfCategory, tfPrice;
    private JButton bAdd, bReset, bLoad;
    private JTextArea taList;

    private int userId;
    private String role;

    public MenuManagementFrame(int userId, String role) {
        this.userId = userId;
        this.role = role;

        setTitle("Menu Management");
        setLayout(new GridLayout(4, 1, 5, 5));

        
        JPanel p1 = new JPanel();
        p1.add(new JLabel("Name:"));
        tfName = new JTextField(12);
        p1.add(tfName);

        p1.add(new JLabel("Category:"));
        tfCategory = new JTextField(10);
        p1.add(tfCategory);

        p1.add(new JLabel("Price:"));
        tfPrice = new JTextField(6);
        p1.add(tfPrice);

        add(p1);

        
        JPanel p2 = new JPanel();
        bAdd = new JButton("Add");
        bReset = new JButton("Reset");
        bLoad = new JButton("Load Menu");

        bAdd.addActionListener(this);
        bReset.addActionListener(this);
        bLoad.addActionListener(this);

        p2.add(bAdd);
        p2.add(bReset);
        p2.add(bLoad);

        add(p2);

        
        taList = new JTextArea(10, 50);
        taList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(taList));

        
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        switch (cmd) {

            case "Reset":
                tfName.setText("");
                tfCategory.setText("");
                tfPrice.setText("");
                break;

            case "Add":
                addMenuItem();
                break;

            case "Load Menu":
                loadMenuItems();
                break;
        }
    }

    
    private void addMenuItem() {
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement()) {

            String name = tfName.getText();
            String category = tfCategory.getText();
            double price = Double.parseDouble(tfPrice.getText());

            String sql = "INSERT INTO menu_items(name, category, price) VALUES('"
                    + name + "', '" + category + "', " + price + ")";

            st.executeUpdate(sql);

            JOptionPane.showMessageDialog(this, "Added");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    
    private void loadMenuItems() {
        taList.setText("");

        String sql = "SELECT id, name, category, price, available FROM menu_items";

        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String line = rs.getInt("id") + " | "
                        + rs.getString("name") + " | "
                        + rs.getString("category") + " | "
                        + rs.getDouble("price") + " | avail: "
                        + rs.getBoolean("available") + "\n";

                taList.append(line);
            }

        } catch (Exception e) {
            taList.setText("Load error: " + e.getMessage());
        }
    }
}
