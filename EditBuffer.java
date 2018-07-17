// Brandon Piper
// cmps109
// Program #4

import tio.*;
import java.io.*;
import javax.swing.text.*;


/**
 *  This class implements an editable text buffer. The buffer maintains a
 * current cursor position. It allows for insertion, deletion, searching, and
 * replacement.
 */
public class EditBuffer extends PlainDocument{
    
    private StringBuffer buf;
    private int position;
    private int mark;
    private String filename;

    /**
     *  Initialize this buffer from a specified file.
     *
     *@param  fileName         The file to read in.
     *@exception  IOException  Opening the file can fail.
     */
    public EditBuffer(String fileName) throws IOException {
        buf = new StringBuffer();
        this.filename = fileName;
        
        // we first read the characters into a char array
        char[] cbuf = new char[1000];
        
        // FileReader is a standard Java class in package java.io.
        FileReader in = new FileReader(fileName);
        
        // read() is an operation implemented by the class FileReader
        // it takes as a parameter a char array. It fills the array
        // and returns how many characters it put into the array.
        int count = in.read(cbuf);
        while (count >= 0) {
            // append() is an operation implemented by the class StringBuffer
            // This call appends the characters from index 0 to index count-1.
            buf.append(cbuf, 0, count);
            count = in.read(cbuf);
        }
	try{
	    super.insertString( 0, buf.toString(), null );
	}
	catch( BadLocationException e){};
        in.close();
    }

    /**
     *  Dump the contents of a StringBuffer to a file.
     *
     *@param  fileName         The file to dump it to.
     *@exception  IOException  Opening the file might fail.
     */
    public void write(String fileName) throws IOException {
        // FileWriter is a standard Java class in package java.io.
        FileWriter out = new FileWriter(fileName);
        // write the entire buffer
        out.write(buf.toString(), 0, buf.length());
        out.close();
    }

    
    /**
     *  Dump the contents of a StringBuffer the file it was read from.
     *
     *@exception  IOException  Opening the file might fail.
     */
    public void write() throws IOException {
        write(filename);
    }

    /**
     *  Print the contents of this buffer to System.out.
     */
    public void print() {
        System.out.print(buf);
        System.out.flush();
    }

    /**
     *  Print 3 lines of this buffer, the one before the line with the cursor
     *, the line with the cursor and the line after the line with the cursor.
     * The position of the cursor will be indicated by printing the cursor
     * character passed to the method at the location of the cursor.
     *
     *@param  cursor  The character to represent the position of the cursor.     
     */
    public void print(char cursor) {
        int savedPosition = position;
        int start = move(-1);
        position = savedPosition;
        int current = move(0);
        position = savedPosition;
        int end = move(2);
        position = savedPosition;
        if (current == end) {
            start = move(-2);
            position = savedPosition;
        }
        // do last 2 lines
        if (start == current) {
            System.out.println("<<<START OF BUFFER>>>");
        }
        System.out.print(buf.substring(start, position) + cursor +
                    buf.substring(position, end));
        int temp = move(1);
        position = savedPosition;
        if (temp == end) {
            System.out.println("<<<END OF BUFFER>>>");
        } else {
            System.out.flush();
        }
    }

    /**
     *  Insert the specified string at the current cursor position.
     *  Leave the cursor at the end of the newly inserted string.
     *
     *@param  insertString  The string to insert.
     */
     public void insert(String insertString) {
         buf.insert(position, insertString);
         position = position + insertString.length();
     }


    /**
     *  Delete the character immediately after the cursor.
     * @return The deleted character as a String.
     */
     public String delete() {
        if (position >= buf.length()) {
            return "";
        }
        char result = buf.charAt(position);
        buf.deleteCharAt(position);
        return String.valueOf(result);
    }


    /**
     *  If the cursor is at the end of a line, delete the newline. Otherwise 
     *  delete from the cursor up to the end of the line leaving the newline.
     *@return The deleted string.
     */
    public String deleteRestOfLine() {
        if (position >= buf.length()) {
            return "";
        }
        if (buf.charAt(position) == '\n') {
            buf.deleteCharAt(position);
            return "\n";
        } else {
            StringBuffer deletedString = new StringBuffer();
            while (position < buf.length() && buf.charAt(position) != '\n') {
                deletedString.append(buf.charAt(position));
                buf.deleteCharAt(position);
            }
            return deletedString.toString();
        }
    }


    /**
     *  Move the cursor to the beginning of the specified line. The first line
     * is line number 1.
     *
     *@param  lineNumber  The line to move to.
     *@return           The new cursor position (offset in characters from the
     * start of the buffer).
     */
    public int goTo(int lineNumber) {
        lineNumber--; // there first line is 1, we want and offset so decrement
        if (lineNumber < 0) {
            return position;
        }
        position = 0;
        while (lineNumber > 0 && position < buf.length()) {
            if (buf.charAt(position) == '\n') {
                lineNumber--;
            }
            // found a newline
            position++;
        }
        return position;
    }

    /**
     *  Move the cursor to the start of the next occurance of the specified
     * string. Searching begins at the current cursor postion, and ends
     * when the end of the buffer is reached.
     *
     *@param  pattern  The string to search for.
     *@return           The new cursor position (offset in characters from the
     * start of the buffer).
     */
    public int find(String pattern) {
        int newPosition = buf.toString().indexOf(pattern, position);
        if (newPosition == -1) {
            return position;
        } else {
            position = newPosition;
            return newPosition;
        }
    }


    /**
     *  Advance the cursor a specified number of characters.
     *
     *@param  count  The number of characters to move. It can be positive or
     * negative.
     *@return           The new cursor position (offset in characters from the
     * start of the buffer).
     */
    public int moveChar(int count) {
        position = position + count;
        if (position < 0) {
            position = 0;
        } else if (position > buf.length()) {
            position = buf.length();
        }
        return position;
    }


    /**
     *  Replace the next occurance of pattern with the replacement string.
     *  Position the cursor in front of the newly inserted replacement text.
     *
     *@param  pattern       String to be found and replaced.
     *@param  replacement  String to insert in place of pattern.
     *@return           The new cursor position (offset in characters from the
     * start of the buffer). If the pattern is not found the cursor is not moved
     * and -1 is returned.
     */
    public int replace(String pattern, String replacement) {
        int newPosition = buf.toString().indexOf(pattern, position);
        if (newPosition != -1) {
            buf.replace(newPosition, newPosition + pattern.length(),
                replacement);
            position = newPosition;
        }
        return newPosition;
    }


    /**
     *  Move the cursor to the start of count lines from the current line. If
     * count is 0 move to the start of the current line.
     *
     *@param  count  The number of lines to move. It can be positive (forward)
     * or negative (backward). If it is 0, move to the start of the current
     * line.
     *@return           The new cursor position (offset in characters from the
     * start of the buffer).
     */
    public int moveLine(int count) {
        return move(count);
    }
    
    
    /**
     * Return the string between the current mark and the cursor. The mark is
     * initially 0 (start of the buffer). The mark may be before or after the
     * current cursor position. If they are at the same location an empty string
     * is returned.
     *
     *@return The string between mark and cursor.
     */
    public String copy() {
        if (mark < position)
            return buf.substring(mark, position);
        else if (mark > position)
            return buf.substring(position, mark);
        else
            return "";
    }
    
    /**
     * Set the mark for this buffer at the current cursor position.
     * @return the marked position (index into the buffer).
     */
    public int mark() {
        mark = position;
        return mark;
    }


    /**
     *  Helper used by moveLine and print. Figures out where the cursor
     *  would be if moved to the beginning of the count'th subsequent line.
     *  count can be either negative or positive.
     *  count == 0 moves to the beginning of the current line
     *  This method does NOT actually move the cursor position.
     *
     *@param  count     The number of lines to move.
     *@return     The new cursor position.
     */
    private int move(int count) {
        if (count == 0) {
            // move to start of the line.
            if (position > buf.length() - 1) {
                position--;
            }
            while (position > 0 && buf.charAt(position) != '\n') {
                position--;
            }
            if (buf.charAt(position) == '\n') {
                position++;
            }
            // move past the newline found
            return position;
        }
        else if (count > 0) {
            while (count > 0 && position < buf.length()) {
                if (buf.charAt(position) == '\n') {
                    count--;
                }
                // found a newline
                position++;
            }
        } else {
            if (position > 0) {
                position--;
            }
            // start with the character before current pos
            while (count <= 0 && position > 0) {
                if (buf.charAt(position) == '\n') {
                    count++;
                }
                position--;
            }
            if (count == 1) {
                position = position + 2;
            }
            // move back ahead of the last newline
        }
        return position;
    }
    
    public String toString(){
	return buf.toString();
    }
    public String getFileName(){
	return filename;
    }

    /* insertString is overridden to handle changes in the text area.
     */
    public void insertString(int offset, String str, AttributeSet as) {
	try{
	    position = offset;
	    insert( str );
	   
	    super.insertString(offset, str, null);
	    System.out.println( toString() );
	}
	catch(BadLocationException e){}
    }
    
    /* remove is overridden to handle changes in the text area.
     */
    public void remove(int offset, int length) {
	try{
	    super.remove(offset, length);
	    position = offset;
	    if( length > 0){
		for( int i = 0 ; i < length; i++ ){ 
		    delete();
		}
	    }
	    else delete();
	    System.out.println( toString() );
	}
	catch(BadLocationException e){}
    }

}

