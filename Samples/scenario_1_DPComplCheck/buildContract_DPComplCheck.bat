curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/manager/clean
curl -v -d @"..\enforcementscripts\script_DPComplCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/script
curl -v -d @"..\contracts\termtype_DPCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/contracttermtype
curl -v -d @"..\contracts\term_DPComplCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/contractterm
curl -v -d @"..\contracts\contracttemplate_DPComplCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/extracdg/contracttemplate
curl -v -d @"..\contracts\servicetemplate_DPComplCheck.json" -H "Content-Type: application/json" -X POST http://localhost:8080/salsa-engine/rest/elise/servicetemplate