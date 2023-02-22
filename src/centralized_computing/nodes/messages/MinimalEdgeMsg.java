package projects.centralized_computing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class MinimalEdgeMsg extends Message {
  int edge;

	public MinimalEdgeMsg(int edge) { this.edge = edge; }

	@Override
	public Message clone() { return this; }
}
