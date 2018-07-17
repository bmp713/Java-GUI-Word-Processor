import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

class EventListener implements ActionListener{

    public void actionPerformed( ActionEvent e ){
	JTextField source = (JTextField)e.getSource();
	String message = source.getText();
	System.out.println( message );
    }
}
