package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FeedbackFrame extends JFrame implements ActionListener {
    JComboBox<String> cbMenu;
    JComboBox<Integer> cbRating;
    JTextArea taComment;
    JButton bSubmit, bLoad;
    int userId;

    public FeedbackFrame(int userId) {
        this.userId = userId;
        setTitle("Feedback & Rating");
        setLayout(new GridLayout(5,1));
        JPanel p;

        p = new JPanel(); p.add(new JLabel("Menu Item:")); cbMenu = new JComboBox<>(); p.add(cbMenu); add(p);
        p = new JPanel(); p.add(new JLabel("Rating:")); cbRating = new JComboBox<>(new Integer[]{1,2,3,4,5}); p.add(cbRating); add(p);
        taComment = new JTextArea(5,40); add(new JScrollPane(taComment));
        p = new JPanel(); bLoad = new JButton("Load Menu"); bLoad.addActionListener(this); p.add(bLoad);
                      bSubmit = new JButton("Submit"); bSubmit.addActionListener(this); p.add(bSubmit); add(p);

        setSize(600,400); setVisible(true); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("Load Menu")) {
            cbMenu.removeAllItems();
            try(Connection c = DBConnection.getConnection();
                Statement st = c.createStatement();
                ResultSet rs = st.executeQuery("SELECT id,name FROM menu_items")) {
                while(rs.next()) {
                    cbMenu.addItem(rs.getInt("id") + ":" + rs.getString("name"));
                }
            } catch(Exception e){
                JOptionPane.showMessageDialog(this,"Load err: " + e.getMessage());
            }
        } else if(cmd.equals("Submit")) {
            try(Connection c = DBConnection.getConnection();
                Statement st = c.createStatement()) {
                String item = (String) cbMenu.getSelectedItem();
                Integer rating = (Integer) cbRating.getSelectedItem();
                String comment = taComment.getText();
                Integer menuId = (item == null) ? null : Integer.parseInt(item.split(":")[0]);

                String sql = "INSERT INTO feedbacks(user_id,menu_item_id,rating,comment) VALUES("
                        + (userId > 0 ? userId : "NULL") + ","
                        + (menuId == null ? "NULL" : menuId) + ","
                        + rating + ",'"
                        + comment.replace("'", "''") + "')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(this,"Thanks for feedback");
            } catch(Exception e){
                JOptionPane.showMessageDialog(this,"Submit err: " + e.getMessage());
            }
        }
    }
}


