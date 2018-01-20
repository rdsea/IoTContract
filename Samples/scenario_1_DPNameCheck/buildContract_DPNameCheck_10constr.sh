curl -v -d @"../contracts/script_DPNameCheckNotify.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/script
curl -v -d @"../contracts/termtype_DPCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/contracttermtype
curl -v -d @"../contracts/term_DPNameCheck_10constr.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/contractterm
curl -v -d @"../contracts/contracttemplate_DPNameCheck_10constr.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/contracttemplate
curl -v -d @"../contracts/servicetemplate_DPNameCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/servicetemplate
