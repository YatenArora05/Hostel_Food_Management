package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class MealBookingFrame extends JFrame implements ActionListener {
    JComboBox<String> cbMenu;
    JTextField tfDate, tfQty;
    JComboBox<String> cbMealType;
    JButton bBook, bLoad;
    int userId;

    public MealBookingFrame(int userId) {
        this.userId = userId;
        setTitle("Meal Booking");
        setLayout(new GridLayout(5,1));
        JPanel p;
        p = new JPanel(); p.add(new JLabel("Menu Item:")); cbMenu = new JComboBox<>(); p.add(cbMenu); add(p);
        p = new JPanel(); p.add(new JLabel("Date (YYYY-MM-DD):")); tfDate = new JTextField(10); p.add(tfDate); add(p);
        p = new JPanel(); p.add(new JLabel("Meal Type:")); cbMealType = new JComboBox<>(new String[]{"breakfast","lunch","dinner"}); p.add(cbMealType);
                     p.add(new JLabel("Qty:")); tfQty = new JTextField(3); p.add(tfQty); add(p);
        p = new JPanel(); bBook = new JButton("Book"); bBook.addActionListener(this); p.add(bBook);
                     bLoad = new JButton("Load Menu"); bLoad.addActionListener(this); p.add(bLoad); add(p);

        setSize(500,300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("Load Menu")) {
            cbMenu.removeAllItems();
            try(Connection c = DBConnection.getConnection();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT id,name FROM menu_items WHERE available=1")) {
                while(rs.next()){
                    cbMenu.addItem(rs.getInt("id")+":"+rs.getString("name"));
                }
            } catch(Exception e){ JOptionPane.showMessageDialog(this,"Load err: "+e.getMessage()); }
        } else if(cmd.equals("Book")) {
            try(Connection c = DBConnection.getConnection();
                Statement st = c.createStatement()) {
                String item = (String) cbMenu.getSelectedItem();
                if(item==null){ JOptionPane.showMessageDialog(this,"Load menu first"); return; }
                int menuId = Integer.parseInt(item.split(":")[0]);
                String date = tfDate.getText();
                String mealType = (String) cbMealType.getSelectedItem();
                int qty = Integer.parseInt(tfQty.getText());
                String sql = "INSERT INTO bookings(user_id,menu_item_id,meal_date,meal_type,qty) VALUES("+userId+","+menuId+",'"+date+"','"+mealType+"',"+qty+")";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(this,"Booked");
            } catch(Exception e){ JOptionPane.showMessageDialog(this,"Book err: "+e.getMessage()); }
        }
    }
}
