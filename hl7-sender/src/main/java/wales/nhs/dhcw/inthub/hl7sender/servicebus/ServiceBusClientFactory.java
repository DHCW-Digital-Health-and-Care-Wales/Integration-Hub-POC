package wales.nhs.dhcw.inthub.hl7sender.servicebus;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import wales.nhs.dhcw.inthub.hl7sender.AppConfig;

public class ServiceBusClientFactory {

    private final AppConfig config;
    private final ServiceBusClientBuilder serviceBusClientBuilder;

    public ServiceBusClientFactory(AppConfig config) {
        this.config = config;
        serviceBusClientBuilder = buildServiceBusClientBuilder();
    }

    private ServiceBusClientBuilder buildServiceBusClientBuilder() {
        ServiceBusClientBuilder serviceBusClientBuilder = new ServiceBusClientBuilder();

        if (config.isUsingConnectionString()) {
            serviceBusClientBuilder.connectionString(config.connectionString());
        } else {
            DefaultAzureCredential credential = new DefaultAzureCredentialBuilder().build();
            serviceBusClientBuilder
                .fullyQualifiedNamespace(config.serviceBusNamespace() + ".servicebus.windows.net")
                .credential(credential);
        }

        return serviceBusClientBuilder;
    }

    public MessageReceiverClient createMessageReceiverClient() {
        ServiceBusReceiverClient receiverClient = serviceBusClientBuilder
            .receiver()
            .queueName(config.ingressQueueName())
            .buildClient();

        return new MessageReceiverClient(receiverClient);
    }
}
