package projects.centralized_computing.nodes.edges;

import projects.centralized_computing.CustomGlobal;
import projects.centralized_computing.nodes.nodeImplementations.GraphNode;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.gui.GraphPanel;
import sinalgo.gui.helper.Arrow;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.io.eps.EPSOutputPrintStream;
import sinalgo.nodes.Position;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.Tools;

public class WeightedEdge extends BidirectionalEdge {
  static final int BILLION = 1000000000;
  private static Random rand = sinalgo.tools.Tools.getRandomNumberGenerator(); 
  private static int currWeight = rand.nextInt(BILLION) + 1;

  private boolean show;
  public int weight;
  public GraphNode startNode;
  public GraphNode endNode;

	public WeightedEdge() {
    // Very hacky way to ensure bidirectional edges have same weight on each direction.
    if (currWeight > BILLION) {
      weight = currWeight - BILLION;
      currWeight = rand.nextInt(BILLION) + 1;
    } else {
      weight = currWeight;
      currWeight += BILLION;
    }
	}

	public void initializeEdge() {
    startNode = (GraphNode) super.startNode;
    endNode = (GraphNode) super.endNode;
	}

	public void draw(Graphics g, PositionTransformation pt) {
		Position p1 = startNode.getPosition();
		pt.translateToGUIPosition(p1);
		int fromX = pt.guiX, fromY = pt.guiY;
		Position p2 = endNode.getPosition();
		pt.translateToGUIPosition(p2);
		int toX = pt.guiX, toY = pt.guiY;

    // Show weight label on the edge near the origin vertex.
    if (CustomGlobal.showWeights) {
      int weightLabelX = (int)(0.95*fromX + 0.05*toX);
      int weightLableY = (int)(0.95*fromY + 0.05*toY);
      g.drawString(Integer.toString(weight), weightLabelX, weightLableY);
    }

    // System.out.println(startNode + " " + endNode);
    if(startNode.isParentOf(endNode))
		  Arrow.drawArrow(fromX, fromY, toX, toY, g, pt, getColor());
    else if (!endNode.isParentOf(startNode)) {
      g.setColor(Color.lightGray);
      g.drawLine(fromX, fromY, toX, toY);
    }
		  // GraphPanel.drawBoldLine(g, fromX, fromY, toX, toY, 3);
    // g.drawLine(fromX, fromY, toX, toY);
		// g.setColor(getColor());
		// GraphPanel.drawBoldLine(g, fromX, fromY, toX, toY, 2);
		// Arrow.drawArrowHead(fromX, fromY, toX, toY, g, pt, getColor());
		// Arrow.drawArrow(fromX, fromY, toX, toY, g, pt, getColor());
		// if((this.numberOfMessagesOnThisEdge == 0)&&
		// 		(this.oppositeEdge != null)&&
		// 		(this.oppositeEdge.numberOfMessagesOnThisEdge > 0)){
		// 	// only draws the arrowHead (if drawArrows is true) - the line is drawn by the 'opposite' edge
		// 	Arrow.drawArrowHead(fromX, fromY, toX, toY, g, pt, getColor());
		// } else {
		// 	if(numberOfMessagesOnThisEdge > 0) {
		// 		// Arrow.drawArrow(fromX, fromY, toX, toY, g, pt, getColor());
		// 	} else {
		// 		// Arrow.drawArrow(fromX, fromY, toX, toY, g, pt, getColor());
		// 	}
		// }
	}

  public String toString() {
    return "Weight: " + weight;
  }

  public Color getColor() {
    return weight < 1000000000 / 5 ? Color.BLACK : Color.LIGHT_GRAY;
  }
}
