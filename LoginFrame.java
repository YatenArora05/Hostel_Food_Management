package com.hostel.ui;

import com.hostel.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame implements ActionListener {

    JTextField tfUser;
    JPasswordField pf;
    JButton btnOk, btnReset, btnNew;

    public LoginFrame() {

        setTitle("User Login");
        setLayout(new GridLayout(3, 1));

        // Username
        JPanel p = new JPanel();
        p.add(new JLabel("Username:"));
        tfUser = new JTextField(15);
        p.add(tfUser);
        add(p);

        // Password
        p = new JPanel();
        p.add(new JLabel("Password:"));
        pf = new JPasswordField(15);
        p.add(pf);
        add(p);

        // Buttons
        p = new JPanel();
        btnOk = new JButton("OK");
        btnReset = new JButton("RESET");
        btnNew = new JButton("New User");

        btnOk.addActionListener(this);
        btnReset.addActionListener(this);
        btnNew.addActionListener(this);

        p.add(btnOk);
        p.add(btnReset);
        p.add(btnNew);

        add(p);

        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // center window
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        String cmd = ae.getActionCommand();

        if (cmd.equals("RESET")) {
            tfUser.setText("");
            pf.setText("");
        }

        else if (cmd.equals("New User")) {
            dispose();
            new RegisterFrame();
        }

        else if (cmd.equals("OK")) {
            loginUser();
        }
    }

    private void loginUser() {

        String u = tfUser.getText().trim();
        String p = new String(pf.getPassword());

        // Basic validation
        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }

        boolean ok = false;
        int uid = -1;
        String role = "student";

        // SAFE PreparedStatement
        String sql = "SELECT id, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u);
            ps.setString(2, p);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ok = true;
                uid = rs.getInt("id");
                role = rs.getString("role");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database error");
            e.printStackTrace();
        }

        if (ok) {
            dispose();
            new DashboardFrame(uid, role);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username/password");
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
