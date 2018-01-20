#!/bin/sh
echo "copy from c1n2"
sshpass -p 'raspi1234' scp -r pi@c1n2:/home/pi/pklein/Samples/perf/*.out /home/pi/pklein/Samples/perf/.
echo "copy from c1n3"
sshpass -p 'raspi1234' scp -r pi@c1n3:/home/pi/pklein/Samples/perf/*.out /home/pi/pklein/Samples/perf/.
echo "copy from c1n4"
sshpass -p 'raspi1234' scp -r pi@c1n4:/home/pi/pklein/Samples/perf/*.out /home/pi/pklein/Samples/perf/.
echo "copy from c1n5"
sshpass -p 'raspi1234' scp -r pi@c1n5:/home/pi/pklein/Samples/perf/*.out /home/pi/pklein/Samples/perf/.