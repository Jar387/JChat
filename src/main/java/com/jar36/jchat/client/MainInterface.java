/*
 * Created by JFormDesigner on Mon Jul 08 18:16:33 CST 2024
 */

package com.jar36.jchat.client;

import javax.swing.*;
import java.awt.*;

/**
 * @author aiyu
 */
public class MainInterface extends JFrame {
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JButton chatButton;
    private JButton contactButton;
    private JButton accountButton;
    private JLabel bottomDecoration;
    public MainInterface() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        chatButton = new JButton();
        contactButton = new JButton();
        accountButton = new JButton();
        bottomDecoration = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- chatButton ----
        chatButton.setText("chat");
        chatButton.setForeground(Color.white);
        chatButton.setBackground(new Color(0x33cc00));
        contentPane.add(chatButton);
        chatButton.setBounds(new Rectangle(new Point(0, 530), chatButton.getPreferredSize()));

        //---- contactButton ----
        contactButton.setText("Contact");
        contentPane.add(contactButton);
        contactButton.setBounds(new Rectangle(new Point(75, 530), contactButton.getPreferredSize()));

        //---- accountButton ----
        accountButton.setText("Account");
        contentPane.add(accountButton);
        accountButton.setBounds(new Rectangle(new Point(150, 530), accountButton.getPreferredSize()));

        //---- bottomDecoration ----
        bottomDecoration.setOpaque(true);
        bottomDecoration.setEnabled(false);
        bottomDecoration.setBackground(new Color(0xcccccc));
        contentPane.add(bottomDecoration);
        bottomDecoration.setBounds(0, 530, 800, 35);

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
