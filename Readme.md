# IoTContract
## Introduction

IoTContract provides a framework for IoT contract definition, monitoring and enforcement. Contracts are defined as objects according to a contract model and assigned to IoT units. Monitoring of IoT units is derived from contracts and provides information to contract enforcement. Figure 1 gives an overview of the
framework and its embedding into IoT units and service:

![N|Solid](https://github.com/rdsea/IoTContract/blob/master/documents/images/architecture.png)
Figure 1: High Level Overview of IoT Contract Framework.

Followng components are included in the framework:
- The Contract Manager is responsible to build contracts from contract models and assign them to IoT units.
- The Contract Models contains building blocks for creation of contracts.
- The Contract Governance is responsible for recording of contract violations identified by monitoring and enforcement and making them available to contract parties.

The framework provides the base to ensure that IoT units and services can fulfill their contracts as well as work together deployed on an IoT edge gateway or cloud in a coordinated manner with proper resources available for each service.

## Installation

- The contract model is implemented in SALSA. Download stable version from https://github.com/tuwiendsg/SALSA
- build it with "mvn clean install"
- Download the contract framework from https://github.com/rdsea/IoTContract
- in ThingSim: "mvn clean install"
- in ThingGovernor: "mvn clean install"

## Run an Example with the IoT Unit Simulator
- run SALSA with "java -jar <SALSA dir>/standalone/target/salsa-engine.jar", default port for SALSA web service is 8080
- cd <IoTContract dir>/Samples/general
- run ThingGovernor with "./run_Governor.sh", default port for ThingGovernor web service is 8088
- cd <IoTContract dir>/Samples/scenario_1_DPNameCheck
- ./buildContract_DPNameCheck.sh to create a sample contract
- ./assignContract_Sensor1160629000_114.sh to assign an IoT unit to the contract
- ./run_Sensor1160629000_114.sh to run a simulated sensor with a set of data
  
## Run with Ethereum Blockchain for Logging
- install node (https://nodejs.org/en)
- install Ethereum simulator with "npm install -g ethereumjs-testrpc"
- run Ethereum simulator with "testrpc -- deterministic"
- run ThingGovernor with "./run_Governor_bc.sh", default port for ThingGovernor web service is 8088

## Contract Model
Figure 2 shows the contract model and the connection to the SALSA data model.

![N|Solid](https://github.com/rdsea/IoTContract/blob/master/documents/images/contract-model.png)
Figure 1: Contract Model.

Elements of the contract model are:
- ContractItem links between contracts and services.
- ContractPartner holds information such as name, address or role of contract partners.
- MetaData manages arbitrary additional information about contracts such as validity, start data or version.
- ContractTemplate manages blueprints for contracts composed of contract terms.
- ContractTerm manages the basic building blocks of contracts.
- ContractTermType manages the different types of contract terms defined.
- Constraint manages enforcement of contract terms by referencing scripts.
- Scripts contain logic interpreted and executed when the contract is attached to man IoT unit. In the prototype Javascript is used as execution language for scripts.
- ParameterTemplate manages names and types of script parameters.
- Parameter manages concrete parameter values of scripts.

## REST API

### Contract Management
Access to IoT contract model entities is provided by a set of REST web services. They
are built as an extension to the already existing web services of ELISE in the /salsaengine/
rest/elise/extracdg/script namespace. It is always checked by the web services
that creation is only possible with a new unique name to prevent double creation (in case
of violation an error 409 CONFLICT is reported back) and that modification, deletion
and reading is only possible for existing objects (an error 404 NOTFOUND is returned
in such a case).

Each entity supports following access methods:

| Function        | Method | Pattern          | Description                                |
|-----------------|--------|------------------|--------------------------------------------|
| ReadAll{Entity} | GET    | /{entity}        | read all instances                         |
| Read{Entity}    | GET    | /{entity}/{name} | read a specific instance defined by name   |
| Save{Entity}    | POST   | /{entity}        | creates a new instance                     |
| Delete{Entity}  | DELETE | /{entity}/{name} | delete a specific instance defined by name |

Each of the functions described above can be applied to following entities (resources in REST terminology).

| Resource         | URL                                 |Description                                          |
|------------------|-------------------------------------|-----------------------------------------------------|
| Contract         |.../elise/servicetemplate            | Contract is linked to SALSA ServiceTemplate         |
| ContractTemplate |.../elise/extracdg/contracttemplate  | Contract template as blueprint for contract         |
| ContractTerm     | .../elise/extracdg/contractterm     | Contract term as basic building blocks of contracts |
| ContractTermType | .../elise/extracdg/contracttermtype | Definition of types of contract terms               |
| Script           | .../elise/extracdg/script           | Definition of scripts for enforcement               |

### Governance Controller

It is implemented in Java and Spring and offers web services to attach a contract to an
IoT unit, build the concrete enforcement scripts based on the contract and make them
available for download by the IoT unit, serve as a registry for IoT units and handle
contract violation messaging and logging.

Following web service resources are provided:

| Resource     | Method  | URL                             | Description                         |
|--------------|---------|---------------------------------|-------------------------------------|
| Assignment   | POST    | /governor/assign                | Assign a contract to a unit         |
| Script       | GET     | /governor/scripts/{unit}        | Retrieve scripts for a unit         |
| Registration | GET     | /governor/register/{unit}       | Retrieve registrations for a unit   |
| Registration | POST    | /governor/register Create       | Registration for a unit             |
| Logging      | POST    | /governor/log                   | Log a contract violation to the log |
| Logging      | GET     | /governor/log/{contract}        | Retrieve the logs for a contract    |
| Logging      | GET     | /governor/sclog/{contract}/{id} | Retrieve the fingerprint of a log   |
| Payment      | POST    | /governor/payment               | Perform a paymenton the blockchain  |

