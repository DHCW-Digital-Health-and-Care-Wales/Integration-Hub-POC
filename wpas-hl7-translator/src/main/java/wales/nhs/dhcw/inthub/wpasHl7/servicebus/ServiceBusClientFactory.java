package wales.nhs.dhcw.inthub.wpasHl7.servicebus;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import wales.nhs.dhcw.inthub.wpasHl7.AppConfig;

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
                .fullyQualifiedNamespace(config.fullyQualifiedNamespace() + ".servicebus.windows.net")
                .credential(credential);
        }

        return serviceBusClientBuilder;
    }

    public MessageSenderClient createMessageSenderClient() {
        ServiceBusSenderClient senderClient = serviceBusClientBuilder
            .sender()
            .queueName(config.reRoutingQueueName())
            .buildClient();

        return new MessageSenderClient(senderClient, config.reRoutingQueueName());
    }

    public MessageReceiverClient createMessageReceiverClient() {
        ServiceBusReceiverClient receiverClient = serviceBusClientBuilder
            .receiver()
            .queueName(config.ingressQueueName())
            .buildClient();

        return new MessageReceiverClient(receiverClient);
    }
}
