jSpringBot Libraries [![Build Status](https://buildhive.cloudbees.com/job/jspringbot/job/jspringbot-libraries/badge/icon)](https://buildhive.cloudbees.com/job/jspringbot/job/jspringbot-libraries/)
====

### Introduction

For end-to-end acceptance testing using Robot Framework with Java, Spring and Maven.

For details please see: http://jspringbot.org/

### Libraries

The following are the supported libraries.

#### Web Application Testing

Web Application Testing is supported using JSpringBot SeleniumLibrary.

#### Restful Services Testing

Restful Services Testing is supported using Http, XML and JSON Libraries.

#### Database Validation

Database Validation is supported using DBLibrary.

#### Config Support

Contains support to access values in properties file using ConfigLibrary.

#### Internationalization Support

Contains support for internationalization.

#### SSH Support

Enables support to access and execute command on a remote machine over an SSH connection using SSHLibrary.

#### CSV Support

Contains support to parse and query a CSV string or resource using CSVLibrary.

#### Expresssions

Added support for expression language to all jspringbot library keywords.

#### Other Utilities

Other utilities like TestDataLibrary that enable create of test-data on a csv file.

### Java, Maven, Spring

All libraries were built using [Java](http://www.java.com/en/) and [Spring Framework](http://www.springsource.org/spring-framework). Dependencies and test execution are done through [Maven](http://maven.apache.org/).


## To generate documentation
mvn compile jspringbot:libdoc
Copy the generated html file to `gh-pages`/docs

## Copyright and license

Copyright 2012 JSpringBot

Code licensed under [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
