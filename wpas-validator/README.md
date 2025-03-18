# WPAS Validator

Service for validating WPAS messages.

The service validates incoming messages against an [included schema](./src/main/resources/WPAS_Schema.xsd).

## Building

```shell
# Build packages
mvn package

# Build docker image
cd ../local/ && docker compose build 
```

## Running the POC locally

Use included Docker Compose setup in '/local' folder (see [README](../local/README.md)).

## Running validator service in Azure Cloud

Dependencies:

- Azure Service Bus queue for incoming messages
- Azure Service Bus topic for validated messages
- Azure Container Apps Environment

 Configuration / environment variables:
```text
SERVICE_BUS_NAMESPACE: 
INGRESS_QUEUE_NAME: 
VALIDATED_WPAS_EGRESS_TOPIC_NAME:
LOG_LEVEL: INFO
LOGGING_LEVEL_COM_AZURE: WARN
```
