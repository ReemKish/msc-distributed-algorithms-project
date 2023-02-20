package projects.centralized_computing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class ColorMessage extends Message {
	
	public int id;
	public ColorMessage(int id) { this.id = id; }
	@Override
	public Message clone() { return this; }
}
