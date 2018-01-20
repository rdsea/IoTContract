java.lang.System.out.println("TSensor called with");
java.lang.System.out.println("" + dataPoint.getName() + ":" + dataPoint.getValue());
if (port == 'T-IN-1') {
	thing.sendDataPoint("T-OUT-1", dataPoint);
} else {
	java.lang.System.out.println("Message to unknown port " + port);
}
