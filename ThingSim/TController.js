java.lang.System.out.println("TController called with");
java.lang.System.out.println("" + dataPoint.getName() + ":" + dataPoint.getValue());
if (port == 'T-IN-1') {
	var tval = Number(dataPoint.getValue());
	if (tval < 10) {
		cmd = new Packages.at.ac.tuwien.dsg.thingsim.model.Command();
		cmd.setName("SetHeating");
		args = new java.util.LinkedList();
		args.add("On");
		cmd.setArguments(args);
		thing.sendCommand("T-OUT-1", cmd);
		java.lang.System.out.println("heating on");
	} else if (tval > 30) {
		cmd = new Packages.at.ac.tuwien.dsg.thingsim.model.Command();
		cmd.setName("SetHeating");
		args = new java.util.LinkedList();
		args.add("Off");
		cmd.setArguments(args);
		thing.sendCommand("T-OUT-1", cmd);
		java.lang.System.out.println("heating off");
	} else {
		java.lang.System.out.println("no command required");
	}
} else {
	java.lang.System.out.println("Message to unknown port " + port);
}
