//only script, to store to registry we need name and version
//"name": "DPNameCheck",
//version:1.0
var checkDPName = @DPName;
if (!Boolean(dataPoint.getName() == checkDPName)) {
  _reason='NOTIFY';_log='access not allowed';
}
