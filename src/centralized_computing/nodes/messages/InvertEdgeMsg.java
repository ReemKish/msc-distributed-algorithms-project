package projects.centralized_computing.nodes.messages;

import projects.centralized_computing.nodes.edges.WeightedEdge;
import sinalgo.nodes.messages.Message;

public class InvertEdgeMsg extends Message {
	public InvertEdgeMsg() {}

	@Override
	public Message clone() { return this; }
}
