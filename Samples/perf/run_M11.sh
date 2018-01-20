#!/bin/sh
curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/manager/clean
../scenario_1_DPNameCheck/buildContract_DPNameCheck.sh
cd ../scenario_1_DPNameCheck
../perf/deassignAll.sh "contract_Sensor1160629000_114.json"
../perf/assignContract.sh "contract_Sensor1160629000_114.json"
sshpass -p 'raspi1234' ssh pi@c1n2 "killall java"
sleep 5
#../perf/runSensor.sh "Sensor1160629000_114_100ev_60fail.json" "topology.json" "Sensor1160629000_114"
sshpass -p 'raspi1234' ssh -R 8088:localhost:8088 pi@c1n2 "cd /home/pi/pklein/Samples/scenario_1_DPNameCheck; ../perf/runSensor.sh 'Sensor1160629000_114_1000ev_60fail.json' 'topology.json' 'Sensor1160629000_114' > ../perf/m11.out" &