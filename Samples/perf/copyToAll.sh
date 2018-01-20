#!/bin/sh
echo "copy to c1n2"
sshpass -p 'raspi1234' scp -r /home/pi/pklein pi@c1n2:/home/pi/.
echo "copy to c1n3"
sshpass -p 'raspi1234' scp -r /home/pi/pklein pi@c1n3:/home/pi/.
echo "copy to c1n4"
sshpass -p 'raspi1234' scp -r /home/pi/pklein pi@c1n4:/home/pi/.
echo "copy to c1n5"
sshpass -p 'raspi1234' scp -r /home/pi/pklein pi@c1n5:/home/pi/.