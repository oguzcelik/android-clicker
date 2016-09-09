/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clicker;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Cynapsis
 */
public class TrayIconDemo {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TrayIconDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (AWTException ex) {
                Logger.getLogger(TrayIconDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    private static void createAndShowGUI() throws AWTException {
        if(!SystemTray.isSupported()) {
            System.out.println("System tray is not supported !");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(Toolkit.getDefaultToolkit().getImage("icon.jpg"));
        final SystemTray systemTray = SystemTray.getSystemTray();
        MenuItem exitItem = new MenuItem("Exit");

        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException ex) {
            Logger.getLogger(TrayIconDemo.class.getName()).log(Level.SEVERE, null, ex);
        }

        Firebase ref = new Firebase("https://clickermedium.firebaseio.com/");
        Robot r = new Robot();

        ref.addValueEventListener(new ValueEventListener() {
            String read;

            @Override
            public void onDataChange(DataSnapshot ds) {
                read = (String) ds.getValue();
                pressKey(read);
                System.out.println(read);
            }

            @Override
            public void onCancelled(FirebaseError fe) {
            }
            private void pressKey(String read) {
                if (read.equals("l")) {
                    r.keyPress(KeyEvent.VK_LEFT);
                } else if (read.equals("r")) {
                    r.keyPress(KeyEvent.VK_RIGHT);
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                systemTray.remove(trayIcon);
                System.exit(0);
            }
        });
    }
}
