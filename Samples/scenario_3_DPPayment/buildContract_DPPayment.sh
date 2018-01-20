#!/bin/bash
curl -v -d @"../contracts/script_DPPayment.json" -H "Content-Type: application/json" http://127.0.0.1:8080/salsa-engine/rest/elise/extracdg/script
curl -v -d @"../contracts/termtype_DPPayment.json" -H "Content-Type: application/json" http://127.0.0.1:8080/salsa-engine/rest/elise/extracdg/contracttermtype
curl -v -d @"../contracts/term_DPPayment.json" -H "Content-Type: application/json" http://127.0.0.1:8080/salsa-engine/rest/elise/extracdg/contractterm
curl -v -d @"../contracts/contracttemplate_DPPayment.json" -H "Content-Type: application/json"  http://127.0.0.1:8080/salsa-engine/rest/elise/extracdg/contracttemplate
curl -v -d @"../contracts/servicetemplate_DPPayment.json" -H "Content-Type: application/json" http://127.0.0.1:8080/salsa-engine/rest/elise/servicetemplate