package edu.cornell.dendro.corina.manip;


public interface ReconcileNotifier {
	public void reconcileDataChanged(ReconcileDataView dataview);
	public void reconcileSelectionChanged(ReconcileDataView dataview);
}
