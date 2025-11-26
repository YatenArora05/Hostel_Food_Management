package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MenuManagementFrame extends JFrame implements ActionListener {
    JTextField tfName, tfCategory, tfPrice;
    JButton bAdd, bReset, bLoad;
    JTextArea taList;
    int userId; String role;

    public MenuManagementFrame(int userId, String role) {
        this.userId = userId; this.role = role;
        setTitle("Menu Management");
        setLayout(new GridLayout(5,1));
        JPanel p;

        p = new JPanel(); p.add(new JLabel("Name:")); tfName = new JTextField(12); p.add(tfName);
                       p.add(new JLabel("Category:")); tfCategory = new JTextField(8); p.add(tfCategory);
                       p.add(new JLabel("Price:")); tfPrice = new JTextField(6); p.add(tfPrice);
        add(p);

        p = new JPanel();
        bAdd = new JButton("Add"); bAdd.addActionListener(this); p.add(bAdd);
        bReset = new JButton("Reset"); bReset.addActionListener(this); p.add(bReset);
        bLoad = new JButton("Load Menu"); bLoad.addActionListener(this); p.add(bLoad);
        add(p);

        taList = new JTextArea(10,40); add(new JScrollPane(taList));

        setSize(700,500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("Reset")) { tfName.setText(""); tfCategory.setText(""); tfPrice.setText(""); }
        else if(cmd.equals("Add")) {
            try (Connection c = DBConnection.getConnection();
                 Statement st = c.createStatement()) {
                String sql = "INSERT INTO menu_items(name,category,price) VALUES('"+tfName.getText()+"','"+tfCategory.getText()+"',"+Double.parseDouble(tfPrice.getText())+")";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(this,"Added");
            } catch (Exception e){ JOptionPane.showMessageDialog(this,"Error: "+e.getMessage()); }
        } else if(cmd.equals("Load Menu")) {
            taList.setText("");
            try (Connection c = DBConnection.getConnection();
                 Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id,name,category,price,available FROM menu_items")) {
                while(rs.next()) {
                    taList.append(rs.getInt("id") + " | " + rs.getString("name") + " | " + rs.getString("category") + " | " + rs.getDouble("price") + " | avail: "+rs.getBoolean("available") + "\n");
                }
            } catch (Exception e) { taList.setText("Load error: "+e.getMessage()); }
        }
    }
}


