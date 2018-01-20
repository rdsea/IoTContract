Build Salsa:
download stable version of Salsa from https://github.com/tuwiendsg/SALSA
mvn clean install

Run the performance tests
on Salsa machine: 
cd <Salsa dir>/standalone/target; java -jar salsa-engine.jar
open ssh to 128.131.172.56 with reverse ssh tunnel to 8080
on RaspberryPI:
username=pi, password=raspi1234
testrpc --deterministic #simulator for Ethereum blockchain
cd pklein/Samples/general
./run_Governor.sh
open second terminal on RaspberryPI
cd pklein/Samples/perf
./run_M<x> for the choosen scenario # result logs are in m<x>.out

Build IoT Contract Framework: # not required for performance tests
download from https://bitbucket.org/peterklein2308/masterthesis-peterklein
in ThingSim: mvn clean install
in ThingGovernor: mvn clean install