package edu.cornell.dendro.corina.tridasv2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasRadius;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyRendererFactory;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.util.Center;


public class TestDialog extends JPanel {
	public TestDialog() {
		
	}
		
		/*
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setAutocreateContainerGaps(true);
		layout.setAutocreateGaps(true);
		
		List<Property> plist = TridasEntityDeriver.buildDerivationList(TridasRadius.class);	
		List<Component> components = new ArrayList<Component>();
		
		for(Property p : plist) {
			components.add(new JLabel(p.getNiceName()));
		}
		
		SequentialGroup hGroup = layout.createSequentialGroup();
		SequentialGroup vGroup = layout.createSequentialGroup();
		
		ParallelGroup cg = layout.createParallelGroup();
		for(Component c : components) {
			vGroup.add(layout.createParallelGroup().add(c));
			cg.add(c);
		}
		hGroup.add(cg);
		
		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
	}*/
	
	static class NoReadWriteProperty extends DefaultProperty {
	      public void readFromObject(Object object) {
	      }
	      public void writeToObject(Object object) {
	      }
	 }
	
	public static class Colorful
	{
		private Color color;

		public Color getColor()
		{
			return color;
		}
		
		public void setColor( Color color )
		{
			this.color = color;
		}

		public int getRed()
		{
			return color.getRed();
		}
		
		public void setRed( int red )
		{
			color = new Color( red, getGreen(), getBlue() );
		}
		
		public int getGreen()
		{
			return color.getGreen();
		}
		
		public void setGreen( int green )
		{
			color = new Color( getRed(), green, getBlue() );
		}
		
		public int getBlue()
		{
			return color.getBlue();
		}
		
		public void setBlue( int blue )
		{
			color = new Color( getRed(), getGreen(), blue );
		}
		
		public String toString()
		{
			return color.toString();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		App.platform = new Platform();
		App.platform.init();
		
		////////////////////////////
		TridasRadius radius = new TridasRadius();
		
		radius.setAzimuth(BigDecimal.valueOf(6.214));
		radius.setTitle("tiiitle");

		TridasIdentifier identifier = new TridasIdentifier();
		identifier.setDomain("yargh.com");
		identifier.setValue("12345");
		
		radius.setIdentifier(identifier);
		
		TridasGenericField gf = new TridasGenericField();
		gf.setName("abc");
		gf.setValue("def");
		radius.getGenericField().add(gf);

		gf = new TridasGenericField();
		gf.setName("123");
		gf.setValue("456");
		radius.getGenericField().add(gf);
		////////////////////////////
		

		PropertySheetPanel panel = new PropertySheetPanel();
		panel.getTable().setRowHeight(24);
		panel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		panel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
		
        List<EntityProperty> properties = TridasEntityDeriver.buildDerivationList(radius.getClass());
        Property[] propArray = new Property[properties.size()];
        properties.toArray(propArray);
        
		panel.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		panel.setProperties(propArray);
		panel.readFromObject(radius);
		
		JDialog dialog = new JDialog((Frame) null, "Test thingy", true);
		TestDialog p = new TestDialog();
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		dialog.setContentPane(panel);
		dialog.pack();
		Center.center(dialog);
		
		dialog.setVisible(true);
		
		panel.writeToObject(radius);
		System.out.println(radius);
		
		System.exit(0);
	}

	
}
