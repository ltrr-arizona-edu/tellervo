package corina.prefs;

import corina.Year;
import corina.Range;
import corina.Sample;

import corina.editor.DecadalModel;
import corina.editor.DecadalKeyListener;
import corina.editor.CountRenderer;
import corina.editor.SampleDataView;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

public class EditorPrefsPanel extends JPanel {

    /*

    front-end:

      foreground: [ #### black [v]]   |  Preview:
      background: [ #### white [v]]   |
       selection: [ #### red   [v]]   |  [1001] 36 24 96
       (selection background, too)    |  [1010] 42
                                      |  [1020] ...
                                      |
      [x] show gridlines              |
      grid color: [ #### gray  [v]]   |
      (check disables popup)          |
                                      |
            font: [ helvetica [v]]    |
            size: [ 12 [v]]           |

    back-end:

    corina.edit.foreground
               .background
	       .selection.foreground
	       .selection.background

	       .gridlines
	       .gridcolor

	       .font (2 controls)

    */

    public EditorPrefsPanel() {
	setLayout(new FlowLayout());

	// ----------------------------------------

	JPanel preview = new JPanel();
	// preview.setLayout(new BoxLayout(preview, BoxLayout.Y_AXIS));
	preview.setLayout(new BorderLayout());

	JPanel label = new JPanel();
	label.setLayout(new FlowLayout(FlowLayout.LEFT));
	label.add(new JLabel("Preview:"));
	preview.add(label, BorderLayout.NORTH);

	preview.add(Box.createVerticalStrut(10));

	Sample dummy = new Sample();
	for (int i=0; i<36; i++)
	    dummy.data.add(new Integer(50 + (int)(100*Math.random())));
	dummy.range = new Range(new Year(1001), new Year(1036));

	// this is SampleDataView(dummy), more-or-less
	JTable table = new JTable(new DecadalModel(dummy));
	table.setMinimumSize(new Dimension(100, 100)); // ???  doesn't seem to work
	table.addKeyListener(new DecadalKeyListener(table, dummy));
	table.setRowSelectionAllowed(false);
	table.setRowSelectionInterval(0, 0);
	table.setColumnSelectionInterval(1, 1);
	table.getTableHeader().setReorderingAllowed(false);
	table.getTableHeader().setResizingAllowed(false);
	table.getColumnModel().getColumn(11).setCellRenderer(new CountRenderer(0));
	
	preview.add(new JScrollPane(table), BorderLayout.CENTER);

	// preview.add(new SampleDataView(dummy), BorderLayout.CENTER);

	// ----------------------------------------

	JPanel controls = new JPanel();
	controls.setLayout(new GridLayout(0, 2, 6, 6));

	controls.add(new JLabel("Foreground:", SwingConstants.RIGHT));
	controls.add(new ColorPopup("corina.foreground"));
	controls.add(new JLabel("Background:", SwingConstants.RIGHT));
	controls.add(new ColorPopup("corina.background"));
	controls.add(new JLabel("Selection fore:", SwingConstants.RIGHT));
	controls.add(new ColorPopup("corina.selection.foreground"));
	controls.add(new JLabel("Selection back:", SwingConstants.RIGHT));
	controls.add(new ColorPopup("corina.selection.background"));

	controls.add(Box.createVerticalStrut(5)); // colspan=2
	controls.add(Box.createVerticalStrut(5));

	// (-->skip a little?  or colspan=2?)
	BooleanCheck gridlines = new BooleanCheck("Show Gridlines", "corina.gridlines");
	controls.add(new JPanel()); // ???
	controls.add(gridlines);

	ColorPopup gridcolor = new ColorPopup("corina.gridcolor");
	gridlines.controls(gridcolor);
	controls.add(new JLabel("Grid Color:", SwingConstants.RIGHT));
	controls.add(gridcolor);

	controls.add(Box.createVerticalStrut(5)); // colspan=2
	controls.add(Box.createVerticalStrut(5));

	controls.add(new JLabel("Font:", SwingConstants.RIGHT));
	controls.add(new FontPopup("corina.edit.font", table));
	controls.add(new JLabel("Size:", SwingConstants.RIGHT));
	controls.add(new FontSizePopup("corina.edit.font", table));

	// ----------------------------------------

	add(controls);
	add(Box.createHorizontalStrut(10));
	add(new JSeparator(JSeparator.VERTICAL));
	add(Box.createHorizontalStrut(10));
	add(preview);
    }

    // debug
    public static void main(String args[]) {
	JFrame f = new JFrame();
	JTabbedPane t = new JTabbedPane();
	f.getContentPane().add(t);
	t.add("Editor", new EditorPrefsPanel());
	t.add("Index", new IndexPrefsPanel());
	f.pack();
	f.show();
    }

}
