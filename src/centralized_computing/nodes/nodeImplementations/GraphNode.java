package projects.centralized_computing.nodes.nodeImplementations;

import java.awt.Graphics;
import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import projects.centralized_computing.nodes.messages.*;
import projects.centralized_computing.CustomGlobal;
import projects.centralized_computing.CustomGlobal.State;

import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class GraphNode extends Node {

  public enum Label {
    NodeID,
    FragID,
    None
  }

	public static int nodeIdCounter = 0;
	public static Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA};
  private static Label label = Label.None;

  public int fragID;
  public GraphNode parent;
  public Set<GraphNode> children = new HashSet();
  public WeightedEdge mwoe;
  public boolean amRoot;

	public GraphNode() {
    fragID = ID;
	}

  public static void setLabelNone()
    { GraphNode.label = Label.None; Tools.repaintGUI(); }


  public static void setLabelID()
    { GraphNode.label = Label.NodeID; Tools.repaintGUI(); }

  public static void setLabelFragID()
    { GraphNode.label = Label.FragID; Tools.repaintGUI(); }

  public boolean isParentOf(GraphNode other)
    { return this.equals(other.parent); }

  public boolean isRelatedTo(GraphNode other)
    { return this.isParentOf(other) || other.isParentOf(this); }

	@Override
	public void init() {
		// this.setColor(Color.RED);
	}
	
	
	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}


  public void calcMWOE() {
    int minW = Integer.MAX_VALUE;
    this.mwoe = null;
    for (Edge edge_ : outgoingConnections) {
      WeightedEdge edge = (WeightedEdge) edge_;
      if (edge.weight < minW && edge.endNode.fragID != fragID) {
        minW = edge.weight;
        this.mwoe = edge;
      }
    }
  }
  
	
	@Override
	public void handleMessages(Inbox inbox) {
    handleState(CustomGlobal.state);
	}

  public void handleState(State state) {
    if (CustomGlobal.phase == 0) handleInitialPhase();
    else switch(CustomGlobal.state) {
      case getMWOE:
        handleStateGetMWOE();
        break;
      case convergecastMWOE:
        handleStateConvergecastMWOE();
        break;
      case broadcastMWOE:
        handleStateBroadcastMWOE();
        break;
      case replaceLeader:
        handleStateReplaceLeader();
        break;
      case connectFragments:
        handleStateConnectFragments();
        break;
      case updateFragID:
        handleStateUpdateFragID();
        break;
    }
  }

  public void sendToChildren(Message m)
    { for (Node child : children) send(m, child); }

  public void handleInitialPhase() {
    // On the 1st round, find the mwoe and connect to parent.
    if(CustomGlobal.round == 1) {
      calcMWOE();
      this.parent = mwoe.endNode;
      send(new ConnectMsg(), parent);
    }
    // On the 2nd round, recognize children and find out if am root.
    else if (CustomGlobal.round == 2) {
      while(inbox.hasNext()) {
        ConnectMsg msg = (ConnectMsg) inbox.next();
        GraphNode sender = (GraphNode) inbox.getSender();
        this.children.add(sender);
      }
      this.amRoot = children.contains(parent) && ID > parent.ID;
      if(amRoot) sendToChildren(new FragmentIdMsg(ID));
    }
    else {  // On subsequent rounds, forward the fragment id.
      if (!amRoot) {
        while(inbox.hasNext()) {
          System.out.println(ID);
          Message msg = inbox.next();
          GraphNode sender = (GraphNode) inbox.getSender();
          if(msg instanceof FragmentIdMsg) {
            this.fragID = ((FragmentIdMsg) msg).fragID;
            sendToChildren(new FragmentIdMsg(fragID));
          }
        }
      }
    }

  }

  public void handleStateGetMWOE() {
    calcMWOE();
  }

  public void handleStateConvergecastMWOE() {
    ;
  }

  public void handleStateBroadcastMWOE() {
    ;
  }

  public void handleStateReplaceLeader() {
    ;
  }

  public void handleStateConnectFragments() {
    ;
  }


  public void handleStateUpdateFragID() {
    ;
  }

	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
    Color fragColor = getBrightColor(fragID);
    if(amRoot) {
      setColor(setColorAlpha(fragColor, .2f));
      drawNodeAsDiskWithText(g, pt, highlight, "", (int)(defaultDrawingSizeInPixels*3), Color.white);
      setColor(setColorAlpha(fragColor, .4f));
      drawNodeAsDiskWithText(g, pt, highlight, "", (int)(defaultDrawingSizeInPixels*2), Color.white);
    }
    // setColor(Color.gray);
    setColor(fragColor);
    switch (label) {
      case NodeID:
        drawNodeAsDiskWithText(g, pt, false, Integer.toString(ID), defaultDrawingSizeInPixels, Color.white);
        break;
      case FragID:
        drawNodeAsDiskWithText(g, pt, false, Integer.toString(fragID), defaultDrawingSizeInPixels, Color.yellow);
        break;
      case None:
        drawNodeAsDiskWithText(g, pt, false, "", defaultDrawingSizeInPixels, Color.white);
    }
	}


  public static Color getBrightColor(int number) {
    double goldenRatio = 0.618033988749895;
    double inverseGoldenRatio = 1 - goldenRatio;
    double hue = (number * goldenRatio) % 1;
    float saturation = 0.8f;
    float brightness = 0.9f;
    return Color.getHSBColor((float)hue, saturation, brightness);
  }

  public static Color setColorAlpha(Color color, float alpha) {
    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();
    return new Color(red, green, blue, (int)(alpha * 255));
  }

	@Override
  public String toString() {
    Set<Integer> childrenIDs = new HashSet();
    for (Node child : children) childrenIDs.add(child.ID);
    return String.format("GraphNode(ID=%d,fragID=%d,amRoot=%b,parentID=%d,childrenIDs=%s)", ID, fragID, amRoot, parent.ID, childrenIDs.toString());
  }


	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void preStep() {
	}

	@Override
	public void postStep() {
	}
}

