package edu.cornell.dendro.corina.util;

import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.ListSelectionModel;

/*
  this is a quick hack.

  FIXME: the proper way appears to be to implement ListSelectionModel, or
  subclass DefaultListSelectionModel.  probably not hard, but takes some doing.
*/

public class NoEmptySelection {
    // don't allow deselecting the only selection
    public static void noEmptySelection(JTable table) {
        final JTable glue = table;

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel model = glue.getSelectionModel();
                if (model.isSelectionEmpty()) {
                    // user's trying to screw it up, fixing...
                    int x = model.getAnchorSelectionIndex();
                    model.addSelectionInterval(x, x);
                }
            }
        });
    }
}
