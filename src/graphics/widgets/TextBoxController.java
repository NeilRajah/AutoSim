/**
 * TextBoxController
 * Author: Neil Balaskandarajah
 * Created on: 21/03/2020
 * Controller for a text box
 */

package graphics.widgets;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.stream.IntStream;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class TextBoxController implements FocusListener, KeyListener {
	//Attributes
	private JTextComponent textArea; //component being controlled
	private String key; //key from the textArea
	
	//Constants
	private static final int[] VALID_KEYCODES = new int[] {		
		KeyEvent.VK_0,
		KeyEvent.VK_1,
		KeyEvent.VK_2,
		KeyEvent.VK_3,
		KeyEvent.VK_4,
		KeyEvent.VK_5,
		KeyEvent.VK_6,
		KeyEvent.VK_7,
		KeyEvent.VK_8,
		KeyEvent.VK_9,
		KeyEvent.VK_PERIOD,
		KeyEvent.VK_BACK_SPACE,
		KeyEvent.VK_DELETE,
		KeyEvent.VK_LEFT,
		KeyEvent.VK_RIGHT,
		KeyEvent.VK_KP_LEFT,
		KeyEvent.VK_KP_RIGHT};
	
	/**
	 * Create a controller for a text field
	 * @param textArea JTextField to be controlled
	 */
	public TextBoxController(JTextComponent textArea) {
		this.textArea = textArea;
		this.key = this.textArea.getText();
		
		//set the text to light gray
		this.textArea.setForeground(Color.LIGHT_GRAY);
	} //end constructor

	/**
	 * Only allow valid keys and update the textbox
	 */
	public void keyTyped(KeyEvent k) {		
		//if key pressed is not valid
		if (!IntStream.of(VALID_KEYCODES).anyMatch(x -> x == (int) k.getKeyChar())) {
			k.consume(); //consume the event and ignore it
		} //if
		
		//update textArea
	} //end keyTyped

	/**
	 * Change the color of the text in the box to the primary color when focused
	 * @param f Event created when component gains focus
	 */
	public void focusGained(FocusEvent f) {
		if (textArea.getText().equals(key)) {
			textArea.setForeground(Color.BLACK);
			textArea.setText("");
		} //if
	} //end focusGained

	/**
	 * Set the ghost text if nothing has been typed
	 */
	public void focusLost(FocusEvent f) {
		if (textArea.getText().equals("")) {
			textArea.setForeground(Color.LIGHT_GRAY);
			textArea.setText(key);
		} //if
	} //end focusLost
	
	/**
	 * Unimplemented
	 */
	public void keyPressed(KeyEvent arg0) {}

	/**
	 * Unimplemented
	 */
	public void keyReleased(KeyEvent arg0) {}
} //end class
