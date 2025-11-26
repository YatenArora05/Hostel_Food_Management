package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ComplaintFrame extends JFrame implements ActionListener {
    JTextField tfSubject;
    JTextArea taMessage;
    JButton bSubmit;
    int userId;

    public ComplaintFrame(int userId) {
        this.userId = userId;
        setTitle("File Complaint");
        setLayout(new GridLayout(4,1));
        JPanel p;

        p = new JPanel(); p.add(new JLabel("Subject:")); tfSubject = new JTextField(25); p.add(tfSubject); add(p);
        taMessage = new JTextArea(6,40); add(new JScrollPane(taMessage));
        p = new JPanel(); bSubmit = new JButton("Submit"); bSubmit.addActionListener(this); p.add(bSubmit); add(p);

        setSize(600,300); setVisible(true); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        try (Connection c = DBConnection.getConnection();
             Statement st =  c.createStatement()) {
            String sql = "INSERT INTO complaints(user_id,subject,message) VALUES("
                    + userId + ",'"
                    + tfSubject.getText() + "','"
                    + taMessage.getText().replace("'", "''") + "')";
            st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this,"Complaint submitted");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,"Error: " + e.getMessage());
        }
    }
}
