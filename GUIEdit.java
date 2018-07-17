// Brandon Piper
// cmps109
// Program #4

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.PlainDocument;

class GUIEdit{

    public static void main( String[] args ) {
        JFrame frame = new JFrame("GUIEdit");;
        Container pane = frame.getContentPane();
        JTextArea view = new JTextArea(25,80);
        ArrayList<EditBuffer> buffers = new ArrayList<EditBuffer>();

        JMenuBar menuBar     = new JMenuBar();
        JMenu bufferMenu = createBufferMenu();
        JMenu fileMenu   = createFileMenu( view, buffers, bufferMenu );
        JMenu editMenu   = createEditMenu( view );

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(bufferMenu);
        pane.add(menuBar);
        frame.setJMenuBar(menuBar);
        
        pane.add(new JScrollPane(view), BorderLayout.CENTER);
        loadBuffers( args, buffers, bufferMenu, view );
        
        String fileName = "Unnamed1";
        try{
            BufferedWriter out = null;
            out = new BufferedWriter(new FileWriter(fileName));
            out.write("");
            out.close();
            JMenuItem newBuffer = new JMenuItem( "<"+fileName+">" );
            BuffListener newBufL = 
		new BuffListener( buffers, view, fileName );
            newBuffer.addActionListener(newBufL);
            bufferMenu.add(newBuffer);
            
            buffers.add(new EditBuffer( fileName ) );
                    
            view.setDocument( buffers.get(buffers.size()-1) );
            view.setText( buffers.get(buffers.size()-1).toString() );
        }catch(Exception d){};

        frame.pack();
        frame.setVisible(true);
    }
    
    /* loadBuffers() takes in the args array and inserts new
     * editBuffer objects into the array.
     */
    static void loadBuffers( String[] args, ArrayList<EditBuffer> buffers,
                             JMenu bufferMenu, JTextArea view){
        try{
            for( int i = 0; i < args.length; i++ ){
                
                buffers.add( new EditBuffer( args[i] ) );
                JMenuItem newFile = new JMenuItem( args[i] );
                BuffListener newBuf = 
		    new BuffListener(buffers, view, args[i]);
                newFile.addActionListener(newBuf);
                bufferMenu.add(newFile);
            }
        }
        catch(IOException e){};
    }
    
    /* creates the buffers menu
     */
    static JMenu createBufferMenu(){
        JMenu bufferMenu = new JMenu("Buffers");
        bufferMenu.setMnemonic('B');
        return bufferMenu;
    }

    /* creates the GUI for the file menu and adds listeners
     * for each item in the menu.
     */
    static JMenu createFileMenu( JTextArea view, ArrayList<EditBuffer> buffers,
                                 JMenu bufferMenu){
        JMenu fileMenu   = new JMenu("File");
        JMenuItem New    = new JMenuItem("New");
        JMenuItem open   = new JMenuItem("Open");
        JMenuItem save   = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save As");
        JMenuItem exit   = new JMenuItem("Exit");
        
        JMenuListener newL    = new JMenuListener(
                                "New", view, buffers, bufferMenu);
        JMenuListener openL   = new JMenuListener(
                                "Open", view, buffers, bufferMenu);
        JMenuListener saveL   = new JMenuListener(
                                "Save", view, buffers, bufferMenu);
        JMenuListener saveAsL = new JMenuListener(
                                "SaveAs", view, buffers, bufferMenu);
        JMenuListener ExitL   = new JMenuListener(
                                "Exit", view, buffers,bufferMenu);

        New.addActionListener(newL);
        open.addActionListener(openL);
        save.addActionListener(saveL);
        saveAs.addActionListener(saveAsL);
        exit.addActionListener(ExitL);

        // set the mnemonics
        New.setMnemonic('N');
        open.setMnemonic('O');
        save.setMnemonic('S');
        saveAs.setMnemonic('a');
        exit.setMnemonic('x');

        // add the menus to the menu bar
        fileMenu.add(New);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(exit);
        fileMenu.setMnemonic('F');
        return fileMenu;
    }

    /* creates the GUI for the Edit menu and adds listeners for
     * each item in that menu.
     */
    static JMenu createEditMenu( JTextArea view ){
        JMenu editMenu    = new JMenu("Edit");

        JMenuItem copy    = new JMenuItem("Copy");
        JMenuItem cut     = new JMenuItem("Cut");
        JMenuItem paste   = new JMenuItem("Paste");
        JMenuItem find    = new JMenuItem("Find");
        JMenuItem replace = new JMenuItem("Replace");
        JMenuItem goTo    = new JMenuItem("GoTo");

        JMenuListener copyL    = new JMenuListener("Copy", view, null, null);
        JMenuListener cutL     = new JMenuListener("Cut", view, null, null);
        JMenuListener pasteL   = new JMenuListener("Paste", view, null, null);
        JMenuListener findL    = new JMenuListener("Find", view, null, null);
        JMenuListener replaceL = new JMenuListener("Replace", view, null, null);
        JMenuListener goToL    = new JMenuListener("GoTo", view, null, null);

        copy.addActionListener(copyL);
        cut.addActionListener(cutL);
        paste.addActionListener(pasteL);
        find.addActionListener(findL);
        replace.addActionListener(replaceL);
        goTo.addActionListener(goToL);

        editMenu.setMnemonic('E');
        copy.setMnemonic('C');      //cut.setMnemonic(' ');
        paste.setMnemonic('P');
        find.setMnemonic('F');
        replace.setMnemonic('R');
        goTo.setMnemonic('G');

        editMenu.add(copy);
        editMenu.add(cut);
        editMenu.add(paste);
        editMenu.add(find);
        editMenu.add(replace);
        editMenu.add(goTo);

        return editMenu;
    }
}
