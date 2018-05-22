# Config-Server
Choerodon's configuration service, configuration center for unified management of service configuration files. The local service directly applies the configuration of the configuration file without starting the service. The configurations of all services except `manager-service`, `register-server`, and `config-server` are pulled from `config-sever`, and `config-server` obtains configuration from the `manager-service` management service. The configuration of the service needs to import the service configuration into the `manager-service` database through `choerodon-tool-config`.

- Pull configuration rules.
  
	If the service includes the configuration version when pulling configurations, the corresponding version configuration is pulled, otherwise, the default configuration is pulled.

## Requirements
- This project based on eureka client. The local executing needs to cooperate with `register-server`, and the online executing needs to cooperate with `go-register-server`.
- The `config-server` obtains the configuration from the `manager-service` management service and needs to start the `manager-service`.
- The configuration of other services needs to be imported into the `manager-service` database by using `choerodon-tool-config`.

## Installation and Getting Started
- run `register-server`
- switch into the project directory and run `mvn spring-boot:run`

## Dependencies
- `go-register-server`: Registration service
- `manager-service`: Management service
- `kafka`

## Reporting Issues
If you find any shortcomings or bugs, please describe them in the [issue](https://github.com/choerodon/choerodon/issues/new?template=issue_template.md).

## How to Contribute
Pull requests are welcome! [Follow](https://github.com/choerodon/choerodon/blob/master/CONTRIBUTING.md) to know for more information on how to contribute.

## Note
- Local service uses the configuration in the configuration file directly, without starting the service
- It need to work with `manager-service` when execute online, and  Online needs to be used in conjunction with `manager-service`, And the configuration of the service needs to import service configuration into `manager-service` database by `choerodon-tool-config`.
