package com.hostel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame implements ActionListener {
    JButton Bookings, Menu, bViewComplaints, bReports, bBack;

    public AdminFrame() {
        setTitle("Admin Page");
        setLayout(new GridLayout(5,1));
        JPanel p;

        Bookings = new JButton("View All Bookings"); Bookings.addActionListener(this); p = new JPanel(); p.add(Bookings); add(p);
        Menu = new JButton("Menu Management"); Menu.addActionListener(this); p = new JPanel(); p.add(Menu); add(p);
        bViewComplaints = new JButton("View Complaints"); bViewComplaints.addActionListener(this); p = new JPanel(); p.add(bViewComplaints); add(p);
        bReports = new JButton("Reports/Payments"); bReports.addActionListener(this); p = new JPanel(); p.add(bReports); add(p);
        bBack = new JButton("Back"); bBack.addActionListener(this); p = new JPanel(); p.add(bBack); add(p);

        setSize(600,500); setVisible(true); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("Menu Management")) new MenuManagementFrame(-1,"admin");
        else if(cmd.equals("Back")) dispose();
        else JOptionPane.showMessageDialog(this,"Admin action: " + cmd + " (implement listing/report as needed)");
    }
}


