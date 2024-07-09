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
    private JScrollPane scrollPane1;
    private JList list1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
    public MainInterface(){
        initComponents();
        list1.add(new Label("11111111"));
        list1.add(new Label("22222222"));
        list1.add(new Label("33333333"));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        scrollPane1 = new JScrollPane();
        list1 = new JList();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(list1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(0, 0, 400, 565);

        contentPane.setPreferredSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }
}
