# Response Management Service
Response Management is part of ONS's Survey Data Collection platform. It covers overall management of the survey (across all survey modes). It manages the survey sample, tracks responses and initiates required follow-up actions during the collection period.

This repository contains the Java services that comprise Response Management. These services communicate with each other over HTTP and [JMS](https://en.wikipedia.org/wiki/Java_Message_Service) as appropriate. Each service has a dedicated database user and schema i.e. there is no database-level integration. The main services are listed below.

## Case Service
The Case service is a RESTful web service implemented using [Spring Boot](http://projects.spring.io/spring-boot/). It manages cases and associated address frame data. A case represents an expected response from an address. Every address in the survey sample must have at least one associated case. A case has a case type such as Household or Individual. Each case can have multiple questionnaires associated with it, but it must have at least one. Each questionnaire has a question set and a unique Internet Access Code (IAC). Interesting things that happen during the life cycle of a case are recorded as case events. Case life cycle transitions are published as JMS messages for interested parties to subscribe to. The Action service described below is one such party.

The [Swagger](http://swagger.io/) specification that documents the Case service's API can be found in `/casesvc-api/swagger.yml`.

## Action Service
The Action service is a RESTful web service implemented using Spring Boot. An action represents an operation that is required for a case. For example, posting out a paper form or arranging a field visit. Actions are grouped into action plans via a set of rules that define, in terms of a day, when a certain action should be taken. Action plans are applied to cases to create actions. Once an action is created it is distributed to a remote handler service to be completed. An examples of a remote handler service is the gateway that interfaces with the Field Work Management Tool (FWMT) to schedule field visits. Another example is the service that generates CSV files containing address data and IACs that a print provider mail merges to create the letters inviting participation in a survey. The Action service can receive feedback from remote handlers to change the state of an action.

The [Swagger](http://swagger.io/) specification that documents the Action service's API can be found in `/actionsvc-api/swagger.yml`.

## Building Response Management
The code in this repository depends on some common framework code in the [rm-common-service](https://github.com/ONSdigital/rm-common-service) repository. Build that code first then use the command below to build Response Management:

```
mvn --update-snapshots
```

## Copyright
Copyright (C) 2016 Crown Copyright (Office for National Statistics)
