package org.tellervo.cpgdb;

import java.lang.reflect.Field;
import java.util.Map;

import junit.framework.TestCase;

public class QueryWrapperTest extends TestCase {

	public void testRedateQueryDelegatesToTwoArgumentDatabaseFunction() throws Exception {
		QueryWrapper wrapper = new QueryWrapper(null);
		Field queriesField = QueryWrapper.class.getDeclaredField("queries");
		queriesField.setAccessible(true);
		Map<?, ?> queries = (Map<?, ?>) queriesField.get(wrapper);
		Object holder = queries.get("qupdVMeasurementResultOpRedate");

		Field queryString = holder.getClass().getDeclaredField("queryString");
		queryString.setAccessible(true);

		assertEquals("SELECT cpgdbj.qupdVMeasurementResultOpRedate(?, ?)",
				queryString.get(holder));
	}
}
