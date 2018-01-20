Build SALSA:
SALSA is a prerequisite and holds the contract repository
download stable version of Salsa from https://github.com/tuwiendsg/SALSA
mvn clean install

Build IoT Contract Framework:
in ThingSim: mvn clean install
in ThingGovernor: mvn clean install

Run the performance tests on RaspberryPI cluster in TU net #requires TUNET VPN access
on Salsa machine: 
cd <Salsa dir>/standalone/target; java -jar salsa-engine.jar
open ssh to <RaspberryPI IP address> with reverse ssh tunnel to 8080

on RaspberryPI:
username, password <ask for it>
testrpc --deterministic #simulator for Ethereum blockchain
copy Samples to <username>/Samples/general
cd <username>/Samples/general
./run_Governor.sh
open second terminal on RaspberryPI
cd <username>/Samples/perf
./run_M<x> for the choosen scenario # result logs are in m<x>.out

