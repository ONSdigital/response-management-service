## To build
./mvnw clean install checkstyle:checkstyle


## To be able to log to file
sudo mkdir -p /var/log/ctp/responsemanagement/actionsvc sudo chmod -R 777 /var/log/ctp


## To run
The app can be started from the command line using : ./mvnw spring-boot:run


## To test actions
curl http://localhost:8171/actions/123 -v -X GET
404 {"error":{"code":"RESOURCE_NOT_FOUND","timestamp":"20160304102431729","message":"HTTP 404 Not Found"}}


