package org.tellervo.desktop.curation.inventory;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.curation.BoxCuration;
import org.tellervo.desktop.curation.BoxCuration.BoxCurationType;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.labelgen.LabBarcode;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSIBox;
import org.tellervo.schema.WSICurationEvent;
import org.tridas.schema.TridasIdentifier;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextPane;
import javax.swing.JButton;

/**
 * Simple dialog to set 
 * 
 * @author pbrewer
 *
 */
public class AssignPermanentLocationPanel extends AbstractWizardPanel implements ActionListener{

	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory.getLogger(BoxCuration.class);

	private JButton btnApply;
	private JButton btnClear;
	private JTextPane txtBarcodes;

	public AssignPermanentLocationPanel() {
		super("Assign boxes to permanent location",
			 "Highlight the text box below, then scan the shelf location barcode (or type the name of the location), followed by all the boxes that "
			 + "should be assigned to this location.  Once all boxes are scanned, click the apply button to submit the changes.");
		setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 0,grow");
		
		txtBarcodes = new JTextPane();
		scrollPane.setViewportView(txtBarcodes);
		
		btnApply = new JButton("Apply");
		btnApply.setActionCommand("Apply");
		btnApply.addActionListener(this);
		add(btnApply, "flowx,cell 0 1");
		
		btnClear = new JButton("Clear");
		btnClear.setActionCommand("Clear");
		btnClear.addActionListener(this);
		add(btnClear, "cell 0 1");
		
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("Apply"))
		{
			applyChanges();
		}
		if(evt.getActionCommand().equals("Clear"))
		{
			txtBarcodes.setText("");
		}
		
	}

	private void applyChanges()
	{
		
		String newLocation = getNewLocation();
		ArrayList<WSIBox> boxArray = parseBarcodes(txtBarcodes.getText());
		
		for(WSIBox box: boxArray)
		{
			box.setCurationLocation(newLocation);
			
			// set up a dialog...
			Window parentWindow = SwingUtilities.getWindowAncestor(getParent());

			// Create resource
			EntityResource<WSIBox> resource = new EntityResource<WSIBox>(box, TellervoRequestType.UPDATE, WSIBox.class);
			TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parentWindow, resource);

			resource.query();
			dialog.setVisible(true);

			if(!dialog.isSuccessful()) 
			{ 
				Alert.error("Error", dialog.getFailException().getMessage());
				continue;
			}

			box = resource.getAssociatedResult();			
			
			// Log curation event
			WSICurationEvent curationEvent = new WSICurationEvent();
			curationEvent.setSecurityUser(App.currentUser);
			curationEvent.setStatus(CurationStatus.ARCHIVED);
			curationEvent.setNotes("Assigned to permanent location: "+newLocation);
			curationEvent.setBox(box);
			
			// Create resource
			EntityResource<WSICurationEvent> resource2 = new EntityResource<WSICurationEvent>(curationEvent, TellervoRequestType.CREATE, WSICurationEvent.class);
			TellervoResourceAccessDialog dialog2 = TellervoResourceAccessDialog.forWindow(parentWindow, resource2);

			resource2.query();
			dialog2.setVisible(true);

			if(!dialog2.isSuccessful()) 
			{ 
				Alert.error("Error", dialog.getFailException().getMessage());
				continue;
			}			
			
			dialog2.dispose();
			
		}
		
		
	}
	
	
	private String getNewLocation()
	{
		String[] codes = txtBarcodes.getText().split("\n");
	
		if(codes.length<2)
		{
			return null;
		}
		
		return codes[0];
	}
	
	private ArrayList<WSIBox> parseBarcodes(String barcodes)
	{
		String[] codes = barcodes.split("\n");
		ArrayList<WSIBox> boxArray = new ArrayList<WSIBox>();
		
		if(codes.length<2)
		{
			Alert.error("Error", "Not enough barcodes");
			return null;
		}
		
		
		
		for(int i=1; i< codes.length; i++)
		{
			
			String code = codes[i];
			try {
				LabBarcode.DecodedBarcode barcode = LabBarcode.decode(code);

				if (!(barcode.uuidType == LabBarcode.Type.BOX))
				{
					Alert.error("Barcode error", "This was not a valid box barcode.");
					return null;
				}

				boxArray.add(doBarcodeSearch(barcode));
				

			} catch (IllegalArgumentException iae) {
				Alert.error("Barcode error", "There was a problem with the barcode you scanned:\n"+iae.getMessage());
			}	
		}
		
		return boxArray;

	}
	
	
	/**
	 * Get box details from WS using barcode
	 * 
	 * @param barcode
	 */
	private WSIBox doBarcodeSearch(LabBarcode.DecodedBarcode barcode)
	{
		if (barcode.uuidType != LabBarcode.Type.BOX){
			return null;
		}
		
		String idstr = barcode.uuid.toString();
		

		log.debug("Getting details of box (id"+idstr+") from the webservice");
		// Set return type to box
		SearchParameters param = new SearchParameters(SearchReturnObject.BOX);

		// Set box id
		param.addSearchConstraint(SearchParameterName.BOXID, SearchOperator.EQUALS, idstr);

		TridasIdentifier id = new TridasIdentifier();
		id.setValue(idstr);

		// we want a box returned here
		EntityResource<WSIBox> resource = new EntityResource<WSIBox>(id, EntityType.BOX, TellervoRequestType.READ, WSIBox.class);

		// Query db 
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);

		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting boxes");
			return null;
		}

		return resource.getAssociatedResult();

		
	}
	

}
