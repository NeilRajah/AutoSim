/**
 * TextFieldFilter
 * Author: Neil Balaskandarajah
 * Created on: 07/28/2020
 * Filter for a text field
 */

package util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class TextFieldFilter extends DocumentFilter {
	//Attributes
	private String validCharacters; //characters allowed to be typed
	
	public TextFieldFilter(String valid) {
		super();
		this.validCharacters = valid;
	}
	
	public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
		if (str == null) return;
		
		if (validCharacters.contains(str)) {
			if (str.equals(".") && !fb.getDocument().getText(0, fb.getDocument().getLength()).contains("."))
				super.insertString(fb, offs, str, a);
		}
	}
	
	public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
		if (str == null) return;
		
		if (validCharacters.contains(str)) {
			if (str.equals(".") && !fb.getDocument().getText(0, fb.getDocument().getLength()).contains("."))
				super.replace(fb, offs, length, str, a);
		}
	}
}
