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
  public static GraphNode server = null;
  public static boolean serverReady = false;
  private static Label label = Label.None;

  public int fragID;
  public GraphNode parent;
  public Set<GraphNode> children = new HashSet();
  public int minWeight = Integer.MAX_VALUE;
  public boolean amRoot = false;
  public boolean isServer = false;

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


  public WeightedEdge calcMWOE() {
    int minW = Integer.MAX_VALUE;
    WeightedEdge mwoe = null;
    for (Edge edge_ : outgoingConnections) {
      WeightedEdge edge = (WeightedEdge) edge_;
      if (edge.weight < minW && edge.endNode.fragID != fragID) {
        minW = edge.weight;
        mwoe = edge;
      }
    }
    return mwoe;
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
      case finished:
        handleStateFinished();
        break;
    }
  }

  public void sendToChildren(Message m)
    { for (Node child : children) send(m, child); }

  public void sendToParent(Message m)
    { if(parent != null) send(m, parent); }

  public void handleInitialPhase() {
    // On the 1st round, find the mwoe and connect to parent.
    if(CustomGlobal.round == 1) {
      this.parent = calcMWOE().endNode;
      send(new ConnectMsg(), parent);
    }
    // On the 2nd round, recognize children and find out if am root.
    else if (CustomGlobal.round == 2) {
      while(inbox.hasNext()) {
        ConnectMsg msg = (ConnectMsg) inbox.next();
        GraphNode sender = (GraphNode) inbox.getSender();
        if (sender.equals(parent)) {
          if (ID > parent.ID) {
            this.amRoot = true;
            this.parent = null;
          } else continue;
        }
        this.children.add(sender);
      }
      if(amRoot) sendToChildren(new FragmentIdMsg(ID));
    }
    else {  // On subsequent rounds, forward the fragment id.
      if (!amRoot) {
        while(inbox.hasNext()) {
          FragmentIdMsg msg = (FragmentIdMsg) inbox.next();
          this.fragID = msg.fragID;
          sendToChildren(msg);
        }
      }
    }
  }

  public void handleStateGetMWOE() {
    this.minWeight = Integer.MAX_VALUE;
    WeightedEdge mwoe = calcMWOE();
    if (mwoe != null) {
      this.minWeight = mwoe.weight;
      sendToParent(new MinimalEdgeMsg(minWeight));
    }
  }

  public void handleStateConvergecastMWOE() {
    int minW = this.minWeight;
    while(inbox.hasNext()) {
      int weight = ((MinimalEdgeMsg) inbox.next()).weight;
      minW = weight > minW ? minW : weight;
    }
    if(amRoot) this.minWeight = minW;
    else sendToParent(new MinimalEdgeMsg(minW));
  }

  public void handleStateBroadcastMWOE() {
    if(amRoot) {
      sendToChildren(new MinimalEdgeMsg(minWeight));
      // System.out.println("Root " + ID + " sent minWieght " + minWeight + " to children.");
      WeightedEdge mwoe = calcMWOE();
      if (mwoe == null || mwoe.weight > minWeight) {
        this.amRoot = false;
        this.parent = null;
        // System.out.println("Root " + ID + " is no longer root.");
        this.minWeight = mwoe == null ? Integer.MAX_VALUE : mwoe.weight;
      }
    }
    if(inbox.hasNext()) {
      MinimalEdgeMsg msg = (MinimalEdgeMsg)inbox.next();
      GraphNode sender = (GraphNode) inbox.getSender();
      // System.out.println(ID + " Received minWeight " + msg.weight + " from " + sender.ID + ", minWeight=" + minWeight);
      if (sender.equals(parent)) {
        if (minWeight == msg.weight && minWeight != Integer.MAX_VALUE) {
          amRoot = true;
          // System.out.println("Root " + ID + " is now root.");
        }
        sendToChildren(msg);
      }
    }
  }

  public void handleStateReplaceLeader() {
    if(amRoot && parent != null) {
      sendToParent(new ConnectMsg());
      children.add(parent);
      this.parent = null;
    }
    while(inbox.hasNext()) {
      Message msg = inbox.next();
      GraphNode sender = (GraphNode) inbox.getSender();
      if (msg instanceof ConnectMsg)  {
        if(parent != null) {
          sendToParent(new ConnectMsg());
          children.add(parent);
        }
        children.remove(sender);
        this.parent = sender;
      }
    }
  }

  public void handleStateConnectFragments() {
    if (amRoot) {
      WeightedEdge mwoe = calcMWOE();
      if (mwoe != null) {
        this.parent = mwoe.endNode;
        sendToParent(new ConnectMsg());
      }
    }
    this.amRoot = false;
  }


  public void handleStateUpdateFragID() {
    boolean becameRoot = false;
    while(inbox.hasNext()) {
      Message msg = inbox.next();
      GraphNode sender = (GraphNode) inbox.getSender();
      if (msg instanceof ConnectMsg) {
        // System.out.println("Got ConnectMsg");
        if (sender.equals(parent)) {
          if (fragID > parent.fragID) {
            this.amRoot = true;
            this.parent = null;
            becameRoot = true;
          } else continue;
        }
        this.children.add(sender);
      } else if (msg instanceof FragmentIdMsg) {
        // System.out.println(ID);
        this.fragID = ((FragmentIdMsg)msg).fragID;
        sendToChildren(msg);
      }
    }
    if (becameRoot)
      sendToChildren(new FragmentIdMsg(fragID));
  }


  public void handleStateFinished() {
    if(server == null) return;
    if (!serverReady) {
      if(server.parent != null) {
        server.sendToParent(new InvertEdgeMsg());
        server.children.add(server.parent);
        server.parent = null;
      }
      while(inbox.hasNext()) {
        InvertEdgeMsg msg = (InvertEdgeMsg) inbox.next();
        GraphNode sender = (GraphNode) inbox.getSender();
        if (parent != null) {
          sendToParent(msg);
          this.children.add(parent);
        } else serverReady = true;
        this.children.remove(sender);
        this.parent = sender;
      }
    }
  }

	public void draw(Graphics g, PositionTransformation pt, boolean highlight){
    // if the state is finished, draw the node black
    Color fragColor =  getBrightColor(fragID);
    Color color;
    if (CustomGlobal.state == State.finished)
      color = isServer ? Color.red : getBrightColor(1);
    else color = fragColor;
    if((CustomGlobal.state != State.finished && amRoot) || (CustomGlobal.state == State.finished && isServer)) {
      setColor(setColorAlpha(color, .2f));
      drawNodeAsDiskWithText(g, pt, highlight, "", (int)(defaultDrawingSizeInPixels*3), Color.white);
      setColor(setColorAlpha(color, .4f));
      drawNodeAsDiskWithText(g, pt, highlight, "", (int)(defaultDrawingSizeInPixels*2), Color.white);
    }
    // setColor(Color.gray);
    setColor(color);
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
    return String.format("GraphNode(ID=%d,fragID=%d,amRoot=%b,parentID=%d,childrenIDs=%s,minWeight=%d)", ID, fragID, amRoot, parent != null ? parent.ID : -1, childrenIDs.toString(), minWeight);
  }


	@NodePopupMethod(menuText = "Set as Server")
  public void setAsServer() {
    if (server != null) server.isServer = false;
    server = this;
    serverReady = false;
    this.isServer = true;
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

