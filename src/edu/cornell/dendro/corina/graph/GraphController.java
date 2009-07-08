package edu.cornell.dendro.corina.graph;

import java.util.List;

import javax.swing.JScrollPane;

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.Year;

public class GraphController {
	private GraphInfo info;
	public GrapherPanel graph;
	public List<Graph> samples;
	public JScrollPane scroller;
	
	public GraphController(GrapherPanel graph, JScrollPane scroller) {
		this.graph = graph;
		this.info = graph.getGraphInfo();
		this.samples = graph.getSamples();
		this.scroller = scroller;
	}
	
	// adjust vertical spacing
	public void squeezeTogether() {
		for (int i = 0; i < samples.size(); i++)
			((Graph) samples.get(i)).yoffset = 0;
		graph.update();
	}

	public void spreadOut(int units) {
		for (int i = 0; i < samples.size(); i++)
			((Graph) samples.get(i)).yoffset = i * units;
		graph.update();
	}

	public void halveScale() {
		for (int i = 0; i < samples.size(); i++) {
			Graph g = (Graph) samples.get(i);
			g.scale /= 2;
		}
		graph.update();		
	}
	
	public void doubleScale() {
		for (int i = 0; i < samples.size(); i++) {
			Graph g = (Graph) samples.get(i);
			g.scale *= 2;
		}
		graph.update();		
	}
	
	public void resetScaling() {
		for (int i = 0; i < samples.size(); i++) {
			Graph g = (Graph) samples.get(i);
			g.scale = g.graph.getScale();
		}
		graph.update();
	}
	
	public void scaleToFitWidth() {
		int viewportSize = scroller.getViewport().getWidth();
		int nYears = graph.getGraphingRange().span() + 2;
		
		// viewportSize is the number of pixels.
		// nyears = 
		int ppy = viewportSize / nYears;
		info.setYearWidth(ppy);
		scrollTo(graph.getGraphingRange().getStart());
	}
	
	public void scaleToFitHeight() {
		int viewportSize = scroller.getViewport().getExtentSize().height;
		int viewHeight = viewportSize - GrapherPanel.AXIS_HEIGHT;

		// force default height
		info.setTenUnitHeight(10);
		
		// now, calculate height based on that
		int maxheight = graph.getMaxPixelHeight();
		int uph = (int) (10.0f * (float) viewHeight / (float) maxheight);

		// reset the scaled height...
		info.setTenUnitHeight(uph);
		graph.update();
	}
	
	public void squishTogether() {
		// squish together samples in visible window

		// BUG: assumes sample[current] is visible.

		// first, set samples[current] = 0
		samples.get(graph.current).yoffset = 0;

		// compute viewport range
		// REFACTOR: write a getYearForPoint() method, and call that on both
		// ends of the visible JViewPane
		Year viewportLeft = graph.getRange().getStart().add(
				scroller.getHorizontalScrollBar().getValue() / graph.getYearWidth());
		int viewportSize = scroller.getWidth() / graph.getYearWidth();
		Range viewport = new Range(viewportLeft, viewportSize);

		// idea: emphasize middle 50% of viewport

		// for each other graph, minimize chi^2 (chi) in viewport
		for (int i = 0; i < samples.size(); i++) {
			// (skip current)
			if (i == graph.current)
				continue;

			// make sure it's there at all, otherwise, don't bother.
			// intersect(viewport, graph[i], graph[current])
			Range range = ((Graph) samples.get(i)).getRange();
			Range overlap = range.intersection(viewport);
			overlap = overlap.intersection(samples.get(graph.current).getRange());
			if (overlap.span() == 0)
				continue;

			// now, compute mean of sample[current][y] - sample[i][y]

			List<? extends Number> data = samples.get(i).graph.getData();
			int j = overlap.getStart().diff(range.getStart()); // index into
																// data[i]
			double dataScale = samples.get(i).scale;

			List<? extends Number> base = ((Graph) samples.get(graph.current)).graph.getData();
			int k = overlap.getStart().diff(
					((Graph) samples.get(graph.current)).getRange().getStart()); // graph.getStart());
																				// //
																				// index
																				// into
																				// base=data[graph.current]
			double baseScale = ((Graph) samples.get(graph.current)).scale;

			double mean = 0.0;
			for (Year y = overlap.getStart(); y.compareTo(overlap.getEnd()) <= 0; y = y
					.add(1)) {
				mean += ((Number) data.get(j++)).doubleValue() * dataScale
						- ((Number) base.get(k++)).doubleValue() * baseScale;
			}
			mean /= overlap.span();

			// make -mean its new offset
			((Graph) samples.get(i)).yoffset = (int) -mean;
		}

		// make the lowest one have yoffset=0 now
		int min = ((Graph) samples.get(0)).yoffset;
		for (int i = 1; i < samples.size(); i++)
			min = Math.min(min, ((Graph) samples.get(i)).yoffset);
		for (int i = 0; i < samples.size(); i++)
			((Graph) samples.get(i)).yoffset -= min;

		// graph.update
		graph.update();
	}

	// scroll the left side to a particular year
	public void scrollTo(Year y) {
		// compute how much to scroll
		int dy = Math.abs(y.diff(graph.getRange().getStart()));

		// scroll
		scroller.getHorizontalScrollBar().setValue(dy * graph.getYearWidth());
	}

}
