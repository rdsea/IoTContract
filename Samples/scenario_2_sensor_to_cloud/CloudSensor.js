java.lang.System.out.println("SimpleSensor " + thing.getConfigHandler().getConfigData().getName() + " called with");
if (port == 'T-IN-1') {
	java.lang.System.out.println("DataPoint name=" + dataPoint.getName() + ", value=" + dataPoint.getValue());
	thing.sendDataPoint("T-OUT-1", dataPoint);
} else {
	java.lang.System.out.println("Message to unknown port " + port);
}
