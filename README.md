# ResponseManagement Service
This project contains the server side components for the ResponseManagement product


# casesvc
Provides restful services for :

 - Address
 - Case
 - CaseType
 - LocalAuthority
 - Msoa
 - Questionnaire
 - QuestionnaireSet
 - Region
 - Sample
 - Survey


# actionsvc
Provides restful services for :

 - Action
 - ActionPlan
 - ActionPlanJob


# To build: mvn clean site
    - Note that you must comment out the configuration sections of maven-checkstyle-plugin in actionsvc-api and casesvc-api for it to work.
    - unit test coverage reports can be found by going to:
        - /actionsvc/target/site/cobertura/index.html
        - /casesvc/target/site/cobertura/index.html
