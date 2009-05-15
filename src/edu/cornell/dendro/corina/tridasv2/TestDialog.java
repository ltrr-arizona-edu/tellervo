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
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

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
		
		//TridasMeasurementSeries series = new TridasMeasurementSeries();
		

		PropertySheetPanel panel = new PropertySheetPanel();
		panel.getTable().setRowHeight(24);
		panel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		panel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
		
        List<EntityProperty> properties = TridasEntityDeriver.buildDerivationList(radius.getClass());
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
		panel.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		panel.setProperties(propArray);
		panel.readFromObject(radius);
		
		JDialog dialog = new JDialog((Frame) null, "Test thingy", true);
		//TestDialog p = new TestDialog();
		
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
