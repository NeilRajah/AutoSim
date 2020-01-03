/**
 * ComponentUtil
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2019
 * Utility for making components
 */
package graphics;

import java.awt.GridBagConstraints;

public class ComponentUtil {
	/**
	 * Create a GridBagConstraints object based on parameters for the layout
	 * int gridx - x position to contrain to
	 * int gridy - y position to constrain to
	 * double weightx - x weight to contrain to
	 * double weighty - y weight to constrain to
	 * return gbc - GridBagConstraints with parameters
	 */
	public static GridBagConstraints createGBC(int gridx, int gridy, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.fill = GridBagConstraints.BOTH; //default to filling entire cell
		
		return gbc;
	} //end createGBC
	
	/**
	 * Create a GridBagConstraints object based on parameters for the layout
	 * int gridx - x position to contrain to
	 * int gridy - y position to constrain to
	 * return gbc - GridBagConstraints with parameters
	 */
	public static GridBagConstraints createGBC(int gridx, int gridy) {
		return createGBC(gridx, gridy, 0, 0);
	} //end createGBC
}
