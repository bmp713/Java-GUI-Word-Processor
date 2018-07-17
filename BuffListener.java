// Brandon Piper
// cmps109
// Program #4

import tio.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.PlainDocument;

/* BuffListener handles any BufferMenu event
 * and switches the current buffer based 
 * the event that triggered it.
 */

class BuffListener implements ActionListener{
    private ArrayList<EditBuffer> buffers;
    private JTextArea view;
    private String source;
    
    public BuffListener(ArrayList<EditBuffer> buffers, JTextArea view, 
			String source){
	this.buffers = buffers;
	this.view = view;
	this.source = source;
    }

    public void actionPerformed(ActionEvent e){
	int i = 0;
	int index= 0 ;
	for(i = 0; i < buffers.size(); i++){
	    if( buffers.get(i).getFileName().equals( source ) ){
		break;
	    }
	}
	view.setDocument(  buffers.get(i) );
    }
}
