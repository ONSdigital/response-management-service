## To build
./mvnw clean install


## To be able to log to file
sudo mkdir -p /var/log/ctp/responsemanagement/actionsvc 
sudo chmod -R 777 /var/log/ctp


## To run
The app can be started from the command line using : ./mvnw spring-boot:run


## To test action plans
curl http://localhost:8151/actionplans -v -X GET
200 [{"actionPlanId":1,"surveyId":1,"name":"HH","description":"Household Action Plan","createdBy":"SYSTEM", "createdDatetime":"2016-03-09T11:15:48.002+0000","lastGoodRunDatetime":null},...

curl  -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:8151/actionplans/1 -v -X PUT -d "{\"description\":\"philippe2testing\"}"
200 {"actionPlanId":1,"surveyId":1,"name":"HH","description":"philippetesting","createdBy":"SYSTEM","createdDatetime":"2016-03-10T15:10:39.494+0000","lastGoodRunDatetime":null}


## To test action plan jobs
curl http://localhost:8151/actionplans/1/jobs -v -X GET
200 [{"actionPlanJobId":3,"actionPlanId":1,"createdBy":"philippeb","state":"SUBMITTED","createdDatetime":"2016-04-04T11:04:27.102+0000","updatedDateTime":"2016-04-04T11:04:27.102+0000"}]

curl http://localhost:8151/actionplans/jobs/1 -v -X GET
404 {"error":{"code":"RESOURCE_NOT_FOUND","timestamp":"20160315173011978","message":"ActionPlanJob not found for id 1"}}

curl  -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:8151/actionplans/1/jobs -v -X POST -d "{\"createdBy\":\"philippeb\"}"
200 {"actionPlanJobId":3,"actionPlanId":1,"createdBy":"philippeb","state":"SUBMITTED","createdDatetime":"2016-04-04T11:04:27.102+0000","updatedDateTime":"2016-04-04T11:04:27.102+0000"}

curl  -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:8151/actionplans/1/jobs -v -X POST -d "{\"created\":\"philippeb\"}"
400 {"error":{"code":"VALIDATION_FAILED","timestamp":"20160316151946434","message":"Provided json is incorrect."}}
