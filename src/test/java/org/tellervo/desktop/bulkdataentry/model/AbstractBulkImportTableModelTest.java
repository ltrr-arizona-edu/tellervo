package org.tellervo.desktop.bulkdataentry.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasIdentifier;

import com.dmurph.mvc.model.HashModel;
import com.dmurph.mvc.model.MVCArrayList;

import junit.framework.TestCase;

public class AbstractBulkImportTableModelTest extends TestCase {
	static {
		System.setProperty("java.awt.headless", "true");
	}

	public void testSelectedRowsAreReturnedInModelOrder() throws Exception {
		final TestSectionModel section = new TestSectionModel();
		// Intentionally identical: row identity, not HashModel equality, controls selection.
		final TestRow first = new TestRow("same");
		final TestRow second = new TestRow("same");
		final TestRow third = new TestRow("same");
		section.rows.addAll(Arrays.asList(first, second, third));
		final TestTableModel table = new TestTableModel(section);

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				table.selectNone();
				table.setSelected(third, true);
				table.setSelected(first, true);
			}
		});

		ArrayList<IBulkImportSingleRowModel> selected = new ArrayList<IBulkImportSingleRowModel>();
		table.getSelected(selected);
		assertEquals(Arrays.asList(first, third), selected);
	}

	public void testRowPropertyAndDirtyChangesRefreshTheirCells() throws Exception {
		final TestSectionModel section = new TestSectionModel();
		final TestRow row = new TestRow("initial");
		section.rows.add(row);
		final TestTableModel table = new TestTableModel(section);
		final List<TableModelEvent> events = recordEvents(table);

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				row.setProperty(TestRow.VALUE, "changed");
			}
		});

		assertTrue(hasUpdate(events, 0, 2));
		assertTrue("Dirty state should refresh the derived Imported status",
				hasUpdate(events, 0, 1));

		events.clear();
		final TridasIdentifier identifier = new TridasIdentifier();
		identifier.setValue("id-1");
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				row.setImported(identifier);
			}
		});
		assertTrue(hasUpdate(events, 0, 1));
	}

	public void testCollectionEventsUseCorrectRangesAndMaintainRowListeners() throws Exception {
		final TestSectionModel section = new TestSectionModel();
		final TestRow first = new TestRow("first");
		section.rows.add(first);
		final TestTableModel table = new TestTableModel(section);
		final List<TableModelEvent> events = recordEvents(table);
		final TestRow second = new TestRow("second");
		final TestRow third = new TestRow("third");

		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				section.rows.addAll(Arrays.asList(second, third));
			}
		});
		assertTrue(hasEvent(events, TableModelEvent.INSERT, 1, 2, TableModelEvent.ALL_COLUMNS));

		events.clear();
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				third.setProperty(TestRow.VALUE, "third changed");
			}
		});
		assertTrue("Rows added with addAll must be observed", hasUpdate(events, 2, 2));

		events.clear();
		final TestRow replacement = new TestRow("replacement");
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				section.rows.set(1, replacement);
			}
		});
		assertTrue(hasEvent(events, TableModelEvent.UPDATE, 1, 1, TableModelEvent.ALL_COLUMNS));

		events.clear();
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				section.rows.remove(third);
				third.setProperty(TestRow.VALUE, "detached");
			}
		});
		assertFalse("Removed rows must no longer update the table", hasUpdate(events, 2, 2));
	}

	public void testRealRowImportedSetterEmitsChange() {
		SingleRadiusModel row = new SingleRadiusModel();
		final List<String> changedProperties = new ArrayList<String>();
		row.addPropertyChangeListener(evt -> changedProperties.add(evt.getPropertyName()));

		TridasIdentifier identifier = new TridasIdentifier();
		identifier.setValue("radius-id");
		row.setImported(identifier);

		assertTrue(changedProperties.contains(IBulkImportSingleRowModel.IMPORTED));
	}

	private List<TableModelEvent> recordEvents(TestTableModel table) {
		final List<TableModelEvent> events = new ArrayList<TableModelEvent>();
		table.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				assertTrue("Table events must run on the event dispatch thread",
						SwingUtilities.isEventDispatchThread());
				events.add(e);
			}
		});
		return events;
	}

	private boolean hasUpdate(List<TableModelEvent> events, int row, int column) {
		return hasEvent(events, TableModelEvent.UPDATE, row, row, column);
	}

	private boolean hasEvent(List<TableModelEvent> events, int type, int firstRow,
			int lastRow, int column) {
		for(TableModelEvent event : events){
			if(event.getType() == type && event.getFirstRow() == firstRow
					&& event.getLastRow() == lastRow && event.getColumn() == column){
				return true;
			}
		}
		return false;
	}

	private static class TestTableModel extends AbstractBulkImportTableModel {
		private static final long serialVersionUID = 1L;

		TestTableModel(TestSectionModel section) {
			super(section);
		}

		@Override
		public Class<?> getColumnClass(String column) {
			return IBulkImportSingleRowModel.IMPORTED.equals(column)
					? ImportStatus.class : String.class;
		}

		@Override
		public void setValueAt(Object value, String column,
				IBulkImportSingleRowModel row, int rowIndex) {
			row.setProperty(column, value);
		}
	}

	private static class TestRow extends HashModel implements IBulkImportSingleRowModel {
		private static final long serialVersionUID = 1L;
		static final String VALUE = "Value";

		TestRow(String value) {
			registerProperty(VALUE, PropertyType.READ_WRITE, value);
			registerProperty(IMPORTED, PropertyType.READ_ONLY, null);
			setDirty(false);
		}

		void setImported(TridasIdentifier imported) {
			TridasIdentifier old = getImported();
			registerProperty(IMPORTED, PropertyType.READ_ONLY, imported);
			firePropertyChange(IMPORTED, old, imported);
		}

		@Override
		public TridasIdentifier getImported() {
			return (TridasIdentifier) getProperty(IMPORTED);
		}
	}

	private static class TestSectionModel extends HashModel implements IBulkImportSectionModel {
		private static final long serialVersionUID = 1L;
		final MVCArrayList<IBulkImportSingleRowModel> rows = new MVCArrayList<IBulkImportSingleRowModel>();
		final ColumnListModel columns = new ColumnListModel();
		final MVCArrayList<ITridas> imported = new MVCArrayList<ITridas>();

		TestSectionModel() {
			columns.add(IBulkImportSingleRowModel.IMPORTED);
			columns.add(TestRow.VALUE);
		}

		@Override
		public void removeSelected() {
		}

		@Override
		public IBulkImportSingleRowModel createRowInstance() {
			return new TestRow(null);
		}

		@Override
		public String[] getPossibleColumns() {
			return new String[] { IBulkImportSingleRowModel.IMPORTED, TestRow.VALUE };
		}

		@Override
		public ColumnListModel getColumnModel() {
			return columns;
		}

		@Override
		public IBulkImportTableModel getTableModel() {
			return null;
		}

		@Override
		public MVCArrayList<ITridas> getImportedList() {
			return imported;
		}

		@Override
		public String[] getImportedListStrings() {
			return new String[0];
		}

		@Override
		public String getImportedDynamicComboBoxKey() {
			return "test";
		}

		@SuppressWarnings("rawtypes")
		@Override
		public MVCArrayList getRows() {
			return rows;
		}
	}
}
