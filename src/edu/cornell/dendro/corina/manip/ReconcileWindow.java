package edu.cornell.dendro.corina.manip;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import edu.cornell.dendro.corina.editor.ReconcileDataView;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.sample.Sample;

public class ReconcileWindow extends XFrame implements ReconcileNotifier {
	
	private ReconcileDataView dv1, dv2;
	
	public ReconcileWindow(Sample s1, Sample s2) {
		JPanel content = new JPanel(new BorderLayout());

		dv1 = new ReconcileDataView(s1, s2);
		dv2 = new ReconcileDataView(s2, s1);
		
		dv1.setReconcileNotifier(this);
		dv2.setReconcileNotifier(this);
		
		JPanel reconcilePanel = new JPanel(new FlowLayout());
		
		JSeparator sep = new JSeparator(JSeparator.VERTICAL);
		
		reconcilePanel.add(createReconcilePane(s1, dv1));
		reconcilePanel.add(new JSeparator(JSeparator.VERTICAL));
		reconcilePanel.add(createReconcilePane(s2, dv2));
		
		content.add(reconcilePanel, BorderLayout.CENTER);
		
		setContentPane(content);
		
		pack();
		setVisible(true);
	}
	
	private JPanel createReconcilePane(Sample s, ReconcileDataView dv) {
		JPanel p = new JPanel();
		
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.setBorder(BorderFactory.createTitledBorder(s.toString()));
		
		p.add(dv);
		
		return p;
	}

	@Override
	public void reconcileSelectionChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.duplicateSelectionFrom(dataview);
	}

	@Override
	public void reconcileDataChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.forceReconciliation();
	}
}
