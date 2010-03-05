package edu.cornell.dendro.corina.graph;

import java.util.List;

import javax.swing.JScrollPane;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;

public class GraphController {
	private GraphInfo info;
	public GrapherPanel grapher;
	public List<Graph> graphs;
	public JScrollPane scroller;
	
	public GraphController(GrapherPanel graph, JScrollPane scroller) {
		this.grapher = graph;
		this.info = graph.getGraphInfo();
		this.graphs = graph.getSamples();
		this.scroller = scroller;
	}
	
	// adjust vertical spacing
	public void squeezeTogether() {
		for(Graph graph : graphs) {
			graph.yoffset = 0;
			grapher.fireGrapherEvent(new GrapherEvent(grapher, graph, GrapherEvent.Type.YOFFSET_CHANGED));
		}
		
		grapher.update();
	}

	public void spreadOut(int units) {
		int i = 0;
		for(Graph graph : graphs) {
			graph.yoffset = i++ * units;
			grapher.fireGrapherEvent(new GrapherEvent(grapher, graph, GrapherEvent.Type.YOFFSET_CHANGED));
		}
		grapher.update();
	}

	public void halveScale() {
		for(Graph graph : graphs) {
			graph.scale /= 2;
			grapher.fireGrapherEvent(new GrapherEvent(grapher, graph, GrapherEvent.Type.SCALE_CHANGED));
		}
		grapher.update();		
	}
	
	public void doubleScale() {
		for(Graph graph : graphs) {
			graph.scale *= 2;
			grapher.fireGrapherEvent(new GrapherEvent(grapher, graph, GrapherEvent.Type.SCALE_CHANGED));
		}
		grapher.update();		
	}
	
	public void resetScaling() {
		for(Graph graph : graphs) {
			graph.scale = graph.graph.getScale();
			grapher.fireGrapherEvent(new GrapherEvent(grapher, graph, GrapherEvent.Type.SCALE_CHANGED));
		}
		grapher.update();
	}
	
	public void scaleToFitWidth() {
		int viewportSize = scroller.getViewport().getWidth();
		int nYears = grapher.getGraphingRange().span() + 2;
		
		// viewportSize is the number of pixels.
		// nyears = 
		int ppy = viewportSize / nYears;
		
		info.setYearWidth(ppy);
		scrollTo(grapher.getGraphingRange().getStart());
	}
	
	
	public void zoomInHorizontal(){
		Double ppy = info.getYearWidth()*1.1;
		if(ppy.intValue()==info.getYearWidth()) ppy++;
		info.setYearWidth(ppy.intValue());
		scrollTo(grapher.getGraphingRange().getStart());
	}
	
	public void zoomOutHorizontal(){
		Double ppy = info.getYearWidth()/1.1;
		info.setYearWidth(ppy.intValue());
		scrollTo(grapher.getGraphingRange().getStart());		
	}

	/**
	 * Scale the graph to fit the visual space available
	 * @param minimumTenUnitHeight the minimum ten-unit height
	 */
	public void scaleToFitHeight(int minimumTenUnitHeight) {
		int viewportSize = scroller.getViewport().getExtentSize().height;
		int viewHeight = viewportSize - GrapherPanel.AXIS_HEIGHT;

		System.out.println(scroller.getViewport().getExtentSize().toString());
		
		/*// force default height
		info.setTenUnitHeight(10);
		
		// now, calculate height based on that
		int maxheight = grapher.getMaxPixelHeight();
		int uph = (int) (10.0f * (float) viewHeight / (float) maxheight);
		
		*/
		int[] minmax = grapher.getMinMaxGraphValue();
		int diff = minmax[1] - minmax[0];
		// add on some space (10 or 2.5%, whichever is greater)
		minmax[0] -= Math.max(10, diff * .025);
		minmax[1] += Math.max(10, diff * .025);

		// TODO: maybe only show min -> max range?
		// for now, just ignore min stuff
		
		// use the lowest value we can
		int uph = (int) Math.floor(10.0f * ((float) viewHeight / (float) minmax[1]));
		
		// reset the scaled height...
		info.setTenUnitHeight(Math.max(minimumTenUnitHeight, uph));	
	}
	
	/**
	 * Same as calling scaleToFitHeight(1)
	 */
	public void scaleToFitHeight() {
		scaleToFitHeight(1);
	}
	
	public void squishTogether() {
		// squish together samples in visible window

		// BUG: assumes sample[current] is visible.

		// first, set samples[current] = 0
		graphs.get(grapher.current).yoffset = 0;

		// compute viewport range
		// REFACTOR: write a getYearForPoint() method, and call that on both
		// ends of the visible JViewPane
		Year viewportLeft = grapher.getRange().getStart().add(
				scroller.getHorizontalScrollBar().getValue() / grapher.getYearWidth());
		int viewportSize = scroller.getWidth() / grapher.getYearWidth();
		Range viewport = new Range(viewportLeft, viewportSize);

		// idea: emphasize middle 50% of viewport

		// for each other graph, minimize chi^2 (chi) in viewport
		for (int i = 0; i < graphs.size(); i++) {
			// (skip current)
			if (i == grapher.current)
				continue;
			
			Graph cur = graphs.get(i);

			// make sure it's there at all, otherwise, don't bother.
			// intersect(viewport, graph[i], graph[current])
			Range range = cur.getRange();
			Range overlap = range.intersection(viewport);
			overlap = overlap.intersection(graphs.get(grapher.current).getRange());
			if (overlap.span() == 0)
				continue;

			// now, compute mean of sample[current][y] - sample[i][y]

			List<? extends Number> data = cur.graph.getData();
			int j = overlap.getStart().diff(range.getStart()); // index into
																// data[i]
			double dataScale = cur.scale;

			List<? extends Number> base = ((Graph) graphs.get(grapher.current)).graph.getData();
			int k = overlap.getStart().diff(
					((Graph) graphs.get(grapher.current)).getRange().getStart()); // graph.getStart());
																				// //
																				// index
																				// into
																				// base=data[graph.current]
			double baseScale = ((Graph) graphs.get(grapher.current)).scale;

			double mean = 0.0;
			for (Year y = overlap.getStart(); y.compareTo(overlap.getEnd()) <= 0; y = y
					.add(1)) {
				mean += ((Number) data.get(j++)).doubleValue() * dataScale
						- ((Number) base.get(k++)).doubleValue() * baseScale;
			}
			mean /= overlap.span();

			// make -mean its new offset
			cur.yoffset = (int) -mean;
		}

		// make the lowest one have yoffset=0 now
		int min = ((Graph) graphs.get(0)).yoffset;
		for (int i = 1; i < graphs.size(); i++)
			min = Math.min(min, ((Graph) graphs.get(i)).yoffset);
		
		for(Graph g : graphs) {
			g.yoffset -= min;
			grapher.fireGrapherEvent(new GrapherEvent(grapher, g, GrapherEvent.Type.YOFFSET_CHANGED));
		}

		// graph.update
		grapher.update();
	}

	// scroll the left side to a particular year
	public void scrollTo(Year y) {
		// compute how much to scroll
		int dy = Math.abs(y.diff(grapher.getRange().getStart()));

		// scroll
		scroller.getHorizontalScrollBar().setValue(dy * grapher.getYearWidth());
	}

}
