java -DthingConfFile="Sensor1160629000_114_10ev.json" -DtopologyFile="topology.json" -DthingName="Sensor1160629000_114" -DmqttBrokerUrl="tcp://iot.eclipse.org:1883" -DthingGovernor="http://localhost:8088" -jar "../../ThingSim/target/ThingSim-1.0-SNAPSHOT.jar" --server.port=8081