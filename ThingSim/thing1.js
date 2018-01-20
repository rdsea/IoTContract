java.lang.System.out.println("Hello from Rhino");
java.lang.System.out.println(dataPoint.getName());
if (port == 'P1') {
	thing.sendDataPoint("P7", dataPoint);
} else if (port == 'P2') {
	java.lang.System.out.println("Processing MQTT message");
	thing.sendDataPoint("P7", dataPoint);
} else {
	java.lang.System.out.println("Message to port " + port);
	java.lang.System.out.println("" + dataPoint.getName() + ":" + dataPoint.getValue());
	
}
