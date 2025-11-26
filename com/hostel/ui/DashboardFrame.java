package com.hostel.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame implements ActionListener {
    JButton bMenu, bBook, bFeedback, bComplaint, bPayment, bAdmin, bLogout;
    int userId;
    String role;

    public DashboardFrame(int userId, String role) {
        this.userId = userId; this.role = role;
        setTitle("Dashboard - " + role);
        setLayout(new GridLayout(7,1));
        JPanel p;

        bMenu = new JButton("Menu Management"); bMenu.addActionListener(this); p = new JPanel(); p.add(bMenu); add(p);
        bBook = new JButton("Meal Booking"); bBook.addActionListener(this); p = new JPanel(); p.add(bBook); add(p);
        bFeedback = new JButton("Feedback / Rating"); bFeedback.addActionListener(this); p = new JPanel(); p.add(bFeedback); add(p);
        bComplaint = new JButton("Complaint"); bComplaint.addActionListener(this); p = new JPanel(); p.add(bComplaint); add(p);
        bPayment = new JButton("Payment"); bPayment.addActionListener(this); p = new JPanel(); p.add(bPayment); add(p);
        bAdmin = new JButton("Admin Page"); bAdmin.addActionListener(this); p = new JPanel(); p.add(bAdmin); add(p);
        bLogout = new JButton("Logout"); bLogout.addActionListener(this); p = new JPanel(); p.add(bLogout); add(p);

        // Hide admin button for non-admins:
        if(!"admin".equals(role)) bAdmin.setEnabled(false);

        setSize(500,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();
        if(cmd.equals("Menu Management")) { new MenuManagementFrame(userId, role); }
        else if(cmd.equals("Meal Booking")) { new MealBookingFrame(userId); }
        else if(cmd.equals("Feedback / Rating")) { new FeedbackFrame(userId); }
        else if(cmd.equals("Complaint")) { new ComplaintFrame(userId); }
        else if(cmd.equals("Payment")) { new PaymentFrame(userId); }
        else if(cmd.equals("Admin Page")) { new AdminFrame(); }
        else if(cmd.equals("Logout")) { dispose(); new LoginFrame(); }
    }
}


