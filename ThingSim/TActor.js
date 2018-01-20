java.lang.System.out.println("TActor called with");
java.lang.System.out.println("" + command.getName() + ":" + command.getArguments().get(0));
if (port == 'T-IN-1') {
	if (command.getName() == "SetHeating") {
		if (command.getArguments().get(0) == "On") {
			java.lang.System.out.println("switch heating on");
		}
		if (command.getArguments().get(0) == "Off") {
			java.lang.System.out.println("switch heating off");
		}
	}
} else {
	java.lang.System.out.println("Message to unknown port " + port);
}
