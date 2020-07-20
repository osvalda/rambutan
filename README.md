    ██████╗  █████╗ ███╗   ███╗██████╗ ██╗   ██╗████████╗ █████╗ ███╗   ██╗
    ██╔══██╗██╔══██╗████╗ ████║██╔══██╗██║   ██║╚══██╔══╝██╔══██╗████╗  ██║
    ██████╔╝███████║██╔████╔██║██████╔╝██║   ██║   ██║   ███████║██╔██╗ ██║
    ██╔══██╗██╔══██║██║╚██╔╝██║██╔══██╗██║   ██║   ██║   ██╔══██║██║╚██╗██║
    ██║  ██║██║  ██║██║ ╚═╝ ██║██████╔╝╚██████╔╝   ██║   ██║  ██║██║ ╚████║
    ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     ╚═╝╚═════╝  ╚═════╝    ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝
                                                                       
[![GitHub version](https://badge.fury.io/gh/osvalda%2Frambutan.svg)](https://badge.fury.io/gh/osvalda%2Frambutan)

## Description

The repository consists of two subpackages:
- **UI Test Automation** - The goal of Functional testing is to verify proper data acceptance, processing and retrieval, and the appropriate implementation of the business functions (rules). 
- **API Test Automation** - The goal of API testing is to verify the communication and data exchange capabilities of the application via its exposed programming interface. It mainly concentrates on the business logic layer of the software architecture.

## Build
Navigate to **rambutan-api-test** or **rambutan-ui-test** directory and execute the following command:
```bash
gradlew clean build -x test
```
## Test Execution
For whole regression job use the following command:
```bash
gradlew test -i
```
If you want to execute only smoke test set, execute this command:
```bash
gradlew smokeTest -i
```
## Reporting
After test executon use the following command in order to generate [Allure] report:
```bash
gradlew allureReport
```
Once the generation finished, navigate to the **build/reports/allure-report/** directory and open **index.html** in a browser.

Api tests have their own coverage reporting mechanism. To check the actual status of endpoint traceability, navigate to the api-test directory and open the generated **coverageReport.html** file in a browser.

## Tools, Plugins
The following tools are used accross the project.

| Name | Version |
| ------ | ------ |
| selenium-java | 3.141.59 |
| webdrivermanager | 3.8.1 |
| rest-assured | 4.1.2 |
| testng | 7.0.0 |
| assertj-core | 3.14.0	Apache 2.0 |
| jsonassert | 1.5.0 |
| awaitility | 3.0.0 |
| lombok |1.18.6|
|allure-testng|2.13.1|
|allure-environment-writer|1.0.0|
|freemarker|2.3.29|
|guava|28.2-jre|

[Allure]: <http://allure.qatools.ru/>