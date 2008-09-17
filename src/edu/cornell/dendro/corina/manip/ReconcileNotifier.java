package edu.cornell.dendro.corina.manip;

import edu.cornell.dendro.corina.editor.ReconcileDataView;

public interface ReconcileNotifier {
	public void reconcileDataChanged(ReconcileDataView dataview);
	public void reconcileSelectionChanged(ReconcileDataView dataview);
}
