#!/bin/sh
curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/manager/clean
../scenario_1_DPNameCheck/buildContract_DPNameCheck.sh
cd ../scenario_1_DPNameCheck
../perf/deassignAll.sh "contract_Sensor1160629000_114.json"
../perf/assignContract.sh "contract_Sensor1160629000_114.json"
sshpass -p 'raspi1234' ssh pi@c1n2 "killall java"
sshpass -p 'raspi1234' ssh pi@c1n3 "killall java"
sshpass -p 'raspi1234' ssh pi@c1n4 "killall java"
sshpass -p 'raspi1234' ssh pi@c1n5 "killall java"
sleep 5
../perf/deassignAll.sh "contract_Sensor1160629000_114.json"
../perf/assignContract.sh "contract_Sensor1160629000_114.json"
sleep 5
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n2 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_1_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8081 > ../perf/m5b1.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n3 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_2_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8081 > ../perf/m5b2.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n4 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_3_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8081 > ../perf/m5b3.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n5 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_4_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8081 > ../perf/m5b4.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n2 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_5_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8082 > ../perf/m5b5.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n3 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_6_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8082 > ../perf/m5b6.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n4 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_7_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8082 > ../perf/m5b7.out" &
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n5 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensorAtPort.sh 'Sensor1160629000_114_8_1000ev.json' 'topology.json' 'Sensor1160629000_114' 8082 > ../perf/m5b8.out" &



