//only script. to store into register one needs name and version
//"name": "DPNameCheck",
//"version": 1.0
var checkDPName = @DPName;
if (!Boolean(dataPoint.getName() == checkDPName))
{
  _reason='ABORT';_log='access not allowed';
}
