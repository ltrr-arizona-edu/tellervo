package org.tellervo.desktop.remarks;

import java.util.Comparator;

public class ReadingRemarkComparator implements Comparator<AbstractRemark> {
    @Override
    public int compare(AbstractRemark o1, AbstractRemark o2) {
        return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
}
