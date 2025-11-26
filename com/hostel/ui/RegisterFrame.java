package com.hostel.ui;

import com.hostel.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Statement;

public class RegisterFrame extends JFrame implements ActionListener {
    JTextField tfUsername, tfFullName, tfContact;
    JPasswordField pfPass, pfRePass;
    JButton btnSubmit, btnReset, btnBack;
    JComboBox<String> cbRole;

    public RegisterFrame() {
        setTitle("Register");
        setLayout(new GridLayout(6,1));
        JPanel p;

        p = new JPanel(); p.add(new JLabel("Username:")); tfUsername = new JTextField(15); p.add(tfUsername); add(p);
        p = new JPanel(); p.add(new JLabel("Password:")); pfPass = new JPasswordField(15); p.add(pfPass); add(p);
        p = new JPanel(); p.add(new JLabel("Re-Password:")); pfRePass = new JPasswordField(15); p.add(pfRePass); add(p);
        p = new JPanel(); p.add(new JLabel("Full Name:")); tfFullName = new JTextField(15); p.add(tfFullName); add(p);
        p = new JPanel(); p.add(new JLabel("Contact:")); tfContact = new JTextField(12); p.add(tfContact);
             cbRole = new JComboBox<>(new String[]{"student","staff"}); p.add(new JLabel("Role:")); p.add(cbRole); add(p);
        p = new JPanel();
        btnSubmit = new JButton("Submit"); btnSubmit.addActionListener(this); p.add(btnSubmit);
        btnReset = new JButton("Reset"); btnReset.addActionListener(this); p.add(btnReset);
        btnBack = new JButton("Back"); btnBack.addActionListener(this); p.add(btnBack);
        add(p);

        setSize(400,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("Reset")) {
            tfUsername.setText(""); pfPass.setText(""); pfRePass.setText(""); tfFullName.setText(""); tfContact.setText("");
        } else if(cmd.equals("Back")) {
            dispose();
            new LoginFrame();
        } else if(cmd.equals("Submit")) {
            String u = tfUsername.getText();
            String p = new String(pfPass.getPassword());
            String rp = new String(pfRePass.getPassword());
            String name = tfFullName.getText();
            String contact = tfContact.getText();
            String role = (String) cbRole.getSelectedItem();

            if(!p.equals(rp)) {
                JOptionPane.showMessageDialog(this,"Password mismatch");
                return;
            }
            try (Connection conn = DBConnection.getConnection();
                 Statement st = conn.createStatement()) {
                String sql = "INSERT INTO users (username,password,full_name,role,contact) VALUES('"+u+"','"+p+"','"+name+"','"+role+"','"+contact+"')";
                st.executeUpdate(sql);
                JOptionPane.showMessageDialog(this,"Registered successfully");
                dispose();
                new LoginFrame();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,"Register error: "+e.getMessage());
            }
        }
    }

    public static void main(String[] args) { new RegisterFrame(); }
}


