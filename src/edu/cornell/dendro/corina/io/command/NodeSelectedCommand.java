package edu.cornell.dendro.corina.io.command;

import java.util.List;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.io.control.ImportNodeSelectedEvent;

public class NodeSelectedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ImportNodeSelectedEvent event = (ImportNodeSelectedEvent) argEvent;
		
		if(event.getValue()!=null) event.model.setSelectedNode(event.getValue());
		
	}

}
