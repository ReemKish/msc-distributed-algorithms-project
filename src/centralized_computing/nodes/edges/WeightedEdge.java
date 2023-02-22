package projects.centralized_computing.nodes.edges;

import projects.centralized_computing.CustomGlobal;

import java.awt.Color;
import java.awt.Graphics;

import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.gui.GraphPanel;
import sinalgo.gui.helper.Arrow;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.io.eps.EPSOutputPrintStream;
import sinalgo.nodes.Position;
import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.Tools;

/**
 * This edge draws itself bold whenever it is traversed by a message.
 * <p>
 * This edge requires a configuration file entry that specifies the stroke
 * width at which this edge draws itself. The entry has the following form:
 * <p>
 * &lt;BigEdge strokeWidth="..."&gt;
 */
public class WeightedEdge extends BidirectionalEdge {
	int strokeWidth;
  int weight;
  boolean show;

	public WeightedEdge() {
    strokeWidth = 20;
    weight = CustomGlobal.randomEdgeWeight;
	}

	public void draw(Graphics g, PositionTransformation pt) {
		Position p1 = startNode.getPosition();
		pt.translateToGUIPosition(p1);
		int fromX = pt.guiX, fromY = pt.guiY; // temporarily store
		Position p2 = endNode.getPosition();
		pt.translateToGUIPosition(p2);
		int toX = pt.guiX, toY = pt.guiY; // temporarily store
		pt.translateToGUIPosition(p2);
    int x = (int)(0.95*fromX + 0.05*toX);
    int y = (int)(0.95*fromY + 0.05*toY);
    if (CustomGlobal.showWeights) g.drawString(Integer.toString(weight), x, y);

		if((this.numberOfMessagesOnThisEdge == 0)&&
				(this.oppositeEdge != null)&&
				(this.oppositeEdge.numberOfMessagesOnThisEdge > 0)){
			// only draws the arrowHead (if drawArrows is true) - the line is drawn by the 'opposite' edge
			Arrow.drawArrowHead(fromX, fromY, pt.guiX, pt.guiY, g, pt, getColor());
		} else {
			if(numberOfMessagesOnThisEdge > 0) {
				Arrow.drawArrow(fromX, fromY, pt.guiX, pt.guiY, g, pt, getColor());
				g.setColor(getColor());
				GraphPanel.drawBoldLine(g, fromX, fromY, pt.guiX, pt.guiY, strokeWidth);
			} else {
				Arrow.drawArrow(fromX, fromY, pt.guiX, pt.guiY, g, pt, getColor());
			}
		}
	}

  public String toString() {
    return "Weight: " + weight;

  }

  public Color getColor() {
    return weight < 1000000000 / 5 ? Color.BLACK : Color.LIGHT_GRAY;
  }
}
