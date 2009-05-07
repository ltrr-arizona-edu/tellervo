package edu.cornell.dendro.corina.tridasv2;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.tridas.schema.TridasRadius;

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
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TridasRadius radius = new TridasRadius();
		AbstractNode node = new AbstractNode(Children.LEAF);
		/*
		JDialog dialog = new JDialog((Frame) null, "Test thingy", true);
		TestDialog p = new TestDialog();
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		dialog.setContentPane(p);
		dialog.pack();
		Center.center(dialog);
		
		dialog.setVisible(true);
		
		System.exit(0);
		*/
	}

}
