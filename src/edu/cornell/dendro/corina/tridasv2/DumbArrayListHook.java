package edu.cornell.dendro.corina.tridasv2;

import java.util.List;

public interface DumbArrayListHook {
	public void addedElement(List<? extends Number> list, int index, Number e);
	public void changedElement(List<? extends Number> list, int index, Number e);
	public void removedElement(List<? extends Number> list, int index);
	public void cleared(List<? extends Number> list);
	public void getting(List<? extends Number> list, int index);
}
