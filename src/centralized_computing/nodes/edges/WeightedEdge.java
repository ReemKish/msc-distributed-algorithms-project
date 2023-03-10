package projects.centralized_computing.nodes.edges;
import projects.centralized_computing.CustomGlobal;
import projects.centralized_computing.nodes.nodeImplementations.GraphNode;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Objects;

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
  static final int BILLION = 100;
  private static Random rand = sinalgo.tools.Tools.getRandomNumberGenerator(); 
  private static Map<Set<Integer>, Integer> edgeWeights = new HashMap();

  private boolean show;
  public int weight;
  public GraphNode startNode;
  public GraphNode endNode;


  @Override
	public void initializeEdge() {
    super.initializeEdge();
    startNode = (GraphNode) super.startNode;
    endNode = (GraphNode) super.endNode;
    Set<Integer> endpoints = Set.of(startNode.ID, endNode.ID);
    if (edgeWeights.containsKey(endpoints))
      weight = edgeWeights.get(endpoints);
    else {
      weight = rand.nextInt(BILLION) + 1;
      edgeWeights.put(endpoints, weight);
    }
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
		  g.setColor(Color.BLACK);
      int weightLabelX = (int)(0.9*fromX + 0.1*toX);
      int weightLableY = (int)(0.9*fromY + 0.1*toY);
      g.drawString(Integer.toString(weight), weightLabelX, weightLableY);
    }

    // System.out.println(startNode + " " + endNode);
		g.setColor(getColor());
    if(endNode.isParentOf(startNode) && startNode.isParentOf(endNode)) {
		  GraphPanel.drawBoldLine(g, fromX, fromY, toX, toY, 3);
		  Arrow.drawArrowHead(fromX, fromY, toX, toY, g, pt, getColor());
    }
    else if(endNode.isParentOf(startNode))
		  Arrow.drawArrow(fromX, fromY, toX, toY, g, pt, getColor());
    else if(!startNode.isParentOf(endNode)) {
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



  public static Color setColorAlpha(Color color, float alpha) {
    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();
    return new Color(red, green, blue, (int)(alpha * 255));
  }

  public String toString() {
    return "Weight: " + weight;
  }

  public Color getColor() {
    Color color;
    if(startNode.fragID == endNode.fragID) {  // Intrafragment edge.
      color = startNode.getColor();
      color = setColorAlpha(color, startNode.isRelatedTo(endNode) ? 1.0f : 1.0f);
    } else {  // Interfragment edge.
      color = setColorAlpha(Color.GRAY, 0.2f);
    }
    return color;
    // return Color.RED;
    // return weight < 1000000000 / 5 ? Color.BLACK : Color.LIGHT_GRAY;
  }
}
