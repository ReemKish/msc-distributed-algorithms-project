package projects.centralized_computing.nodes.messages;

import sinalgo.nodes.messages.Message;

public class ConnectMsg extends Message {
	
	public ConnectMsg() {}
	@Override
	public Message clone() { return this; }
}
