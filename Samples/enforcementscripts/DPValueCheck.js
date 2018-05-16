var checkDPMaxValue = @DPMaxValue;
var checkDPMinValue = @DPMinValue;
if (Boolean(dataPoint.getValue() < checkDPMaxValue) && Boolean(dataPoint.getValue() > checkDPMinValue))
{
  _reason='NOTIFY';
  _log='value out of range'
}
