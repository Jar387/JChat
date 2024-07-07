/*
 * Created by JFormDesigner on Sat Jul 06 21:54:17 CST 2024
 */

package com.jar36.jchat.client;

import com.jar36.jchat.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author aiyu
 */
public class LoginInterface extends JFrame {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel title;
    private JTextField username;
    private JPasswordField passwd;
    private JButton confirmbutton;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    public LoginInterface() {
        initComponents();
    }

    private void confirmButtonClicked(ActionEvent e) {
        ClientMain.username = username.getText();
        ClientMain.passwdHash = Util.sha256(new String(passwd.getPassword()));
        ClientMain.channel.pipeline().fireUserEventTriggered(new UserEvent(UserEvent.LOGIN_TRIGGERED));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        title = new JLabel();
        username = new JTextField();
        passwd = new JPasswordField();
        confirmbutton = new JButton();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- title ----
        title.setText("Sign in to JChat");
        contentPane.add(title);
        title.setBounds(new Rectangle(new Point(100, 10), title.getPreferredSize()));
        contentPane.add(username);
        username.setBounds(85, 60, 135, username.getPreferredSize().height);
        contentPane.add(passwd);
        passwd.setBounds(85, 115, 135, passwd.getPreferredSize().height);

        //---- confirmbutton ----
        confirmbutton.setText("Sign in");
        confirmbutton.setBackground(new Color(0x339900));
        confirmbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirmbutton.setForeground(Color.white);
        confirmbutton.addActionListener(e -> confirmButtonClicked(e));
        contentPane.add(confirmbutton);
        confirmbutton.setBounds(85, 155, 135, confirmbutton.getPreferredSize().height);

        //---- label2 ----
        label2.setText("Password");
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(100, 100), label2.getPreferredSize()));

        //---- label3 ----
        label3.setText("Username");
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(95, 45), label3.getPreferredSize()));

        //---- label4 ----
        label4.setIcon(new ImageIcon(getClass().getResource("/userIcon.png")));
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(45, 50), label4.getPreferredSize()));

        //---- label5 ----
        label5.setIcon(new ImageIcon(getClass().getResource("/passwdIcon.png")));
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(45, 115), label5.getPreferredSize()));

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
