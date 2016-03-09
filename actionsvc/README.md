## To build
./mvnw clean install


## To be able to log to file
sudo mkdir -p /var/log/ctp/responsemanagement/actionsvc 
sudo chmod -R 777 /var/log/ctp


## To run
The app can be started from the command line using : ./mvnw spring-boot:run


## To test action plans
curl http://localhost:8161/actionplans -v -X GET
500 {"error":{"code":"SYSTEM_ERROR","timestamp":"20160309111712510","message":"could not extract ResultSet; SQL [n/a];
nested exception is org.hibernate.exception.SQLGrammarException: could not extract ResultSet"}}


