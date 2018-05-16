var checkMaxTime = @DPMaxTime;
var newTS = new Date().getTime();
var lastTS = monitor.scratchpad.get(dataPoint.getName());
if (lastTS == null) {
	monitor.scratchpad.put(dataPoint.getName(), newTS);
} else {
	if ((newTS - lastTS) > checkMaxTime * 1000) {
		monitor.log("max wait time for" + dataPoint.getName() + "has been reached, contract violated"};
	] else {
		monitor.scratchpad.put(dataPoint.getName(), newTS);
	}
}
