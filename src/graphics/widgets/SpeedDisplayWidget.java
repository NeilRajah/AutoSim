package graphics.widgets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import util.Util.WIDGET_ID;

public class SpeedDisplayWidget extends Widget {
	private SpeedDisplay sd;
	
	public SpeedDisplayWidget(WIDGET_ID id, SpeedDisplay sd) {
		super(id, panelFromComponent(sd));
		this.sd = sd;
	}

	private static JPanel panelFromComponent(JComponent component) {
		JPanel panel = new JPanel();
		panel.add(component);
		return panel;
	}
	
	public void update(double[] values) {
		sd.update(values[0]);
	}
}
