# WPAS HL7 Translator

Service for translating WPAS xml messages to HL7v2.

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
- Azure Service Bus topic for translated messages
- Azure Container Apps Environment 

Configuration / environment variables:
```text
SERVICE_BUS_NAMESPACE: 
TRANSLATION_QUEUE_NAME: 
SENDERS_TOPIC_NAME:
LOG_LEVEL: INFO
LOGGING_LEVEL_COM_AZURE: WARN
```

## WPAS xml schema update

Java classes were generated from [WPAS schema](./src/main/resources/xsd/WPAS_Schema.xsd) file with Jakarta xjc tool.

To regenerate them:

1. download Jakarta distribution from https://eclipse-ee4j.github.io/jaxb-ri/ 
2. Run the xjc tool: 
```shell
jaxb-ri/bin/xjc.sh -p wales.nhs.dhcw.inthub.wpasHl7.xml -mark-generated ./WPAS_Schema.xsd
```
3. Copy generated classes to the proper source folder.

