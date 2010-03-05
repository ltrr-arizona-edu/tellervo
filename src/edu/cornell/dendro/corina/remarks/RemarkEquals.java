package edu.cornell.dendro.corina.remarks;

import org.tridas.schema.TridasRemark;

import edu.cornell.dendro.corina.util.EmptyStringEqualsBuilder;

public class RemarkEquals {
	private RemarkEquals() {}

	public final static boolean remarksEqual(TridasRemark r1, TridasRemark r2) {
		return remarksEqual(r1, r2, true);
	}
	
	public final static boolean remarksEqual(TridasRemark r1, TridasRemark r2, boolean ignoreInheritedCount) {
		EmptyStringEqualsBuilder e = new EmptyStringEqualsBuilder();
		
		e.append(r1.getNormal(), r2.getNormal());
		e.append(r1.getNormalId(), r2.getNormalId());
		e.append(r1.getNormalStd(), r2.getNormalStd());
		e.append(r1.getValue(), r2.getValue());
		e.append(r1.getNormalTridas(), r2.getNormalTridas());
		
		if(!ignoreInheritedCount)
			e.append(r1.getInheritedCount(), r2.getInheritedCount());
		
		return e.isEquals();
	}
}
