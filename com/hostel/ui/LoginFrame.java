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
        setLayout(new GridLayout(4,1));
        JPanel p;

        p = new JPanel(); p.add(new JLabel("Username:")); tfUser = new JTextField(15); p.add(tfUser); add(p);
        p = new JPanel(); p.add(new JLabel("Password:")); pf = new JPasswordField(15); p.add(pf); add(p);
        p = new JPanel(); btnOk = new JButton("OK"); btnOk.addActionListener(this); p.add(btnOk);
                         btnReset = new JButton("RESET"); btnReset.addActionListener(this); p.add(btnReset);
                         btnNew = new JButton("New User"); btnNew.addActionListener(this); p.add(btnNew);
        add(p);

        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("RESET")) { tfUser.setText(""); pf.setText(""); }
        else if(cmd.equals("New User")) { dispose(); new RegisterFrame(); }
        else if(cmd.equals("OK")) {
            String u = tfUser.getText();
            String p = new String(pf.getPassword());
            boolean ok = false; String role = "student"; int uid = -1;
            try (Connection conn = DBConnection.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id, role FROM users WHERE username='"+u+"' AND password='"+p+"'")) {
                if(rs.next()) { ok = true; uid = rs.getInt("id"); role = rs.getString("role"); }
            } catch (Exception e) { System.out.println(e); }
            if(ok) {
                dispose();
                new DashboardFrame(uid, role);
            } else JOptionPane.showMessageDialog(this,"Invalid username/password");
        }
    }

    public static void main(String[] args) { new LoginFrame(); }
}


