# Integration Hub POC

Proof of concept for DHCW Integration Hub.

## Components

- [WPAS schema validator](./wpas-validator/README.md)
- [WPAS xml to HL7v2 translator](./wpas-hl7-translator/README.md)

## Development

This POC bases on Azure Cloud services (Azure Service Bus and Container Apps) and implements microservices with Java 21.

For development Java 21 compatible IDE with Maven support is recommended.

## Running the POC

The POC can be run locally using Docker Compose or deployed to the Azure cloud.

For local deployment instructions view the [README](./local/README.md) inside the `/local`.

For building and deploying to the cloud view respective README files in the components subprojects.

