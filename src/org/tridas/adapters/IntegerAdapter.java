package org.tridas.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Integer> {

	@Override
	public String marshal(Integer v) throws Exception {
		return (v == null) ? null : javax.xml.bind.DatatypeConverter.printInt(v);
	}

	@Override
	public Integer unmarshal(String v) throws Exception {
		return javax.xml.bind.DatatypeConverter.parseInt(v);
	}

}
