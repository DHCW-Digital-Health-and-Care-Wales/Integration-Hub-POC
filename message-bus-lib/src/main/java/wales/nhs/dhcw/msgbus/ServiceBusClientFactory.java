package wales.nhs.dhcw.msgbus;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverClient;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

public class ServiceBusClientFactory {

    private final ConnectionConfig config;
    private final ServiceBusClientBuilder serviceBusClientBuilder;

    public ServiceBusClientFactory(ConnectionConfig config) {
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

    public MessageSenderClient createMessageSenderClient(String topicName) {
        ServiceBusSenderClient senderClient = serviceBusClientBuilder
            .sender()
            .topicName(topicName)
            .buildClient();

        return new MessageSenderClient(senderClient, topicName);
    }

    public MessageReceiverClient createMessageReceiverClient(String queueName) {
        ServiceBusReceiverClient receiverClient = serviceBusClientBuilder
            .receiver()
            .queueName(queueName)
            .buildClient();

        return new MessageReceiverClient(receiverClient);
    }
}
