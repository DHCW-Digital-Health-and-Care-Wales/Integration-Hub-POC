# Running the POC locally

## Prerequisites

- Docker
    - [Docker Desktop](https://docs.docker.com/desktop/install/windows-install/#:~:text=Install%20Docker%20Desktop%20on%20Windows%201%20Download%20the,on%20your%20choice%20of%20backend.%20...%20More%20items)
- Minimum hardware Requirements:
    - 2 GB RAM
    - 5 GB of Disk space
- WSL Enablement (Only for Windows):
    - [Install Windows Subsystem for Linux (WSL) | Microsoft Learn](https://learn.microsoft.com/en-us/windows/wsl/install)
    - [Configure Docker to use WSL](https://docs.docker.com/desktop/wsl/#:~:text=Turn%20on%20Docker%20Desktop%20WSL%202%201%20Download,engine%20..%20...%206%20Select%20Apply%20%26%20Restart.)
- Create a `.secrets` file in `local` folder and add:
    - `MSSQL_SA_PASSWORD="<insert_a_strong_password>"`
    - `SA_PASSWORD="<insert_a_strong_password>"`
- Create a `.secrets` file in `wpas-validator` folder and add:
  - `LOGGING_DB_PASSWORD=="<same SA_PASSWORD from above>"`
    
### Windows

Before starting the stack, we need to allow execution of unsigned scripts. Run the below command in the PowerShell window:

`$>Start-Process powershell -Verb RunAs -ArgumentList 'Set-ExecutionPolicy Bypass –Scope CurrentUser’`

## Staring the stack
After completing the prerequisites, you need to run `mvn package` in the root directory to build all the JARs.
After JARs have been built you can proceed with the following command in the `/local` directory to run the POC locally:

`docker compose up -d`

## Interact with the Azure service bus emulator

By default, emulator uses [ServiceBusEmulatorConfig.json](./ServiceBusEmulatorConfig.json) configuration file. You can configure entities by making changes to configuration file. To know more, visit [make configuration changes](https://learn.microsoft.com/en-us/azure/service-bus-messaging/overview-emulator#quota-configuration-changes).

You can use the following connection string to connect to the Service Bus emulator:

- When the emulator container and interacting application are running natively on local machine, use following connection string:
```
"Endpoint=sb://127.0.0.1;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=SAS_KEY_VALUE;UseDevelopmentEmulator=true;"
```

## Stopping the stack
To terminate the containers you can proceed with the following command in the `/local` directory:

`docker compose down`
