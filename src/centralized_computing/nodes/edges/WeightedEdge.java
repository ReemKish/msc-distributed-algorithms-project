package projects.centralized_computing.nodes.edges;
import projects.centralized_computing.CustomGlobal;
import projects.centralized_computing.CustomGlobal.State;
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
  static final int BILLION = 1000000000;
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
    int strokeWidth = (CustomGlobal.state == State.finished && (numberOfMessagesOnThisEdge > 0 || oppositeEdge.numberOfMessagesOnThisEdge > 0)) ? 5 : 2;
    if(endNode.isParentOf(startNode)) {
		  GraphPanel.drawBoldLine(g, fromX, fromY, toX, toY, strokeWidth);
		  Arrow.drawArrowHead(fromX, fromY, toX, toY, g, pt, getColor());
    }
    else if(!startNode.isParentOf(endNode)) {
      g.drawLine(fromX, fromY, toX, toY);
    }
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
      color = CustomGlobal.state == State.finished ? Color.black : startNode.getColor();
      color = setColorAlpha(color, startNode.isRelatedTo(endNode) ? 1.0f : 0.2f);
    } else {  // Interfragment edge.
      color = setColorAlpha(Color.GRAY, 0.2f);
    }
    if(CustomGlobal.state == State.finished && (numberOfMessagesOnThisEdge > 0 || oppositeEdge.numberOfMessagesOnThisEdge > 0))
      color = Color.red;
    return color;
    // return Color.RED;
    // return weight < 1000000000 / 5 ? Color.BLACK : Color.LIGHT_GRAY;
  }
}
