package edu.cornell.dendro.corina.tridasv2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

import org.tridas.schema.BaseSeries;
import org.tridas.schema.Certainty;
import org.tridas.schema.DateTime;
import org.tridas.schema.PresenceAbsence;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.l2fprod.common.propertysheet.Property;
import com.l2fprod.common.propertysheet.PropertyRendererFactory;
import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;
import com.l2fprod.common.propertysheet.DefaultProperty;
import com.l2fprod.common.propertysheet.PropertySheetTableModel;
import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.platform.Platform;
import edu.cornell.dendro.corina.prefs.Prefs;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.util.Center;


public class TestDialog extends JPanel {
	public TestDialog() {		
	}
	
	private static enum EditType {
		MEASUREMENT_SERIES(TridasMeasurementSeries.class, "Series", "tridas/measurementseries.png"),
		DERIVED_SERIES(TridasMeasurementSeries.class, "Derived Series", "tridas/derivedseries.png"),
		OBJECT(TridasObject.class, "Object", "tridas/object.png"),
		ELEMENT(TridasElement.class, "Element", "tridas/element.png"),
		SAMPLE(TridasSample.class, "Sample", "tridas/sample.png"),
		RADIUS(TridasRadius.class, "Radius", "tridas/radius.png");
		
		private Class<?> type;
		private String displayTitle;
		private String iconPath;
		
		private EditType(Class<?> type, String displayTitle, String iconPath) {
			this.type = type;
			this.displayTitle = displayTitle;
			this.iconPath = iconPath;
		}
		
		public Class<?> getType() {
			return type;
		}
		
		public String getTitle() {
			return displayTitle;
		}
		
		public Icon getIcon() {
			return Builder.getIcon(iconPath, "Icons");
		}
	}
	
	public static PropertySheetPanel getPropertiesPanel() {
		CorinaPropertySheetTable table = new CorinaPropertySheetTable();
		PropertySheetPanel panel = new PropertySheetPanel(table);
		
		table.setEditable(false);
		
		panel.setToolBarVisible(false);
		panel.setDescriptionVisible(true);
		panel.setMode(PropertySheet.VIEW_AS_CATEGORIES);
		
		panel.getTable().setRowHeight(24);
		panel.getTable().setRendererFactory(new TridasPropertyRendererFactory());
		panel.getTable().setEditorFactory(new TridasPropertyEditorFactory());
		
		return panel;
	}

	private static TridasObject tridasObject = new TridasObject();
	private static TridasElement tridasElement = new TridasElement();
	private static TridasRadius tridasRadius = new TridasRadius();
	private static TridasSample tridasSample = new TridasSample();
	private static Object currentObject = null;
	
	public static PropertySheetPanel propertySheet;
	
	public static void buttonAction(EditType type) {
		if(currentObject != null) 
			propertySheet.writeToObject(currentObject);

		switch(type) {
		case OBJECT:
			currentObject = tridasObject;
			break;
		case ELEMENT:
			currentObject = tridasElement;
			break;
		case RADIUS: 
			currentObject = tridasRadius;
			break;
		case SAMPLE:
			currentObject = tridasSample;
			break;
		default:
			currentObject = null;
			return;
		}
		
        List<EntityProperty> properties = TridasEntityDeriver.buildDerivationList(type.getType());
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
		propertySheet.setProperties(propArray);
		propertySheet.readFromObject(currentObject);
	}
	
	public static AbstractButton addButton(final EditType type) {
		Action action = new AbstractAction(type.getTitle(), type.getIcon()) {
			public void actionPerformed(ActionEvent e) {				
				buttonAction(type);
			}
		};
		
		JToggleButton button = new JToggleButton(action);
		
		return button;
	}
	
	public static JButtonBar getButtonPanel(boolean isDerived) {
		JButtonBar toolbar = new JButtonBar(JButtonBar.VERTICAL);
		
		toolbar.setUI(new BlueishButtonBarUI());
		
		AbstractButton button;
		ButtonGroup buttons = new ButtonGroup();

		// special case for derived series
		if(isDerived) {
			button = addButton(EditType.DERIVED_SERIES);
			buttons.add(button);
			toolbar.add(button);
			button.doClick();
		}
		else {
			for(EditType t : EditType.values()) {
				// skip over these series types if we're not dealing with them
				if(t == EditType.DERIVED_SERIES)
					continue;
			
				button = addButton(t);
				buttons.add(button);
				toolbar.add(button);
			
				if (buttons.getSelection() == null) {
					button.doClick();
				}
			}
		}
		
		toolbar.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		
		return toolbar;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		App.platform = new Platform();
		App.platform.init();
		
		App.prefs = new Prefs();
		App.prefs.init();
		
		Dictionary d = new Dictionary();
		
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
		
		DateTime dtv = new DateTime();
		dtv.setCertainty(Certainty.APPROXIMATELY);
		tridasSample.setLastModifiedTimestamp(dtv);
				
		//TridasMeasurementSeries series = new TridasMeasurementSeries();

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		propertySheet = getPropertiesPanel();

		/*
        List<EntityProperty> properties = TridasEntityDeriver.buildDerivationList(radius.getClass());
        Property[] propArray = properties.toArray(new Property[properties.size()]);
        
		propertySheet.setProperties(propArray);
		propertySheet.readFromObject(radius);
		
		*/
		
		JDialog dialog = new JDialog((Frame) null, "Test thingy", true);
		//TestDialog p = new TestDialog();
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JButtonBar buttonPanel = getButtonPanel(false);
		
		content.add(propertySheet, BorderLayout.CENTER);
		content.add(buttonPanel, BorderLayout.WEST);
		
		JButton tmp = new JButton("asdf");
		tmp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CorinaPropertySheetTable table = (CorinaPropertySheetTable) propertySheet.getTable();
				
				table.setEditable(!table.isEditable());
			}
		});
		content.add(tmp, BorderLayout.SOUTH);
		
		dialog.setContentPane(content);
		dialog.pack();
		Center.center(dialog);
		
		dialog.setVisible(true);		
		
		System.exit(0);
	}

	
}
