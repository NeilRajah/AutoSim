/**
 * TextInput
 * Author: Neil Balaskandarajah
 * Created on: 07/28/2020
 * Widget for getting user text input
 */
package graphics.components;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import graphics.Painter;
import main.AutoSim;
import util.JComponentUtil;

public class TextInput extends JPanel {
	//Attributes
	private int height;
	private int width;
	private String title;
	private String text;
	private JLabel titleComp;
	private JTextField textComp;
	
	public TextInput(String title, int height,int width) {
		super();
		
		this.height = height;
		this.width = width;
		this.title = title;
		
		layoutView();
	}
	
	private void layoutView() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLayout(layout);
		int h = this.height / 15;
		
		titleComp = new JLabel(title, SwingConstants.CENTER);
		titleComp.setFont(Painter.createFont(Painter.SF_UI_FONT, AutoSim.PPI * 10));
		titleComp.setPreferredSize(new Dimension(width / 6, h));
		this.add(titleComp);

		textComp = JComponentUtil.textField("0.0", (int) (this.width * 0.7), h, AutoSim.PPI * 3);	
		this.add(textComp);
	}
}
