package edu.cornell.dendro.corina.util.openrecent;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "openables"
})
@XmlRootElement(name = "recentDocuments")
public class RecentlyOpenedDocuments {
	@XmlElements({
		@XmlElement(name = "seriesDescriptor", type = SeriesDescriptor.class)
		// add more types in here...!
	})
	protected List<OpenableDocumentDescriptor> openables;
	@XmlAttribute(name = "tag")
	protected String tag;

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the openables
	 */
	public List<OpenableDocumentDescriptor> getOpenables() {
		if(openables == null)
			openables = new ArrayList<OpenableDocumentDescriptor>();
		return openables;
	}

	/**
	 * @param openables the openables to set
	 */
	public void setOpenables(List<OpenableDocumentDescriptor> openables) {
		this.openables = openables;
	}
}
