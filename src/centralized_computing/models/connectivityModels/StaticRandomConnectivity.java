package projects.centralized_computing.models.connectivityModels;

import sinalgo.models.ConnectivityModel;
import sinalgo.nodes.Node;

public class StaticRandomConnectivity extends RandomConnectivity {
	private boolean firstTime = true;

	public boolean updateConnections(Node n) {
		if(firstTime) {
			firstTime = false;
			return super.updateConnections(n);
		} else {
			return false;
		}
	}
}
