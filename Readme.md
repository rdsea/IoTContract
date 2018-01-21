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

- The contract model is implemented in SALSA. Download stable version of Salsa from https://github.com/tuwiendsg/SALSA
- build it with "mvn clean install"
- Download the contract framework from https://github.com/rdsea/IoTContract
- in ThingSim: mvn clean install
- in ThingGovernor: mvn clean install
