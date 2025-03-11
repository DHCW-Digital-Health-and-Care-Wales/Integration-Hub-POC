package wales.nhs.dhcw.inthub.wpasHl7;

import com.azure.core.credential.AzureSasCredential;
import com.azure.messaging.servicebus.*;

public class ServiceBusQueueProducer {
    public static void main(String[] args) {
        // Azure connection details
        final String connectionString = "Endpoint=sb://hltransformer-sample.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=X3KgNW59QYYpQce53TCr1TFrrFpQKlwPd+ASbHTjkY4="; // replace with your connection string
        final String queueName = "mjqueue";    // Replace with your queue name

        final String sasToken = "SharedAccessSignature sr=https%3A%2F%2Fhltransformer-sample.servicebus.windows.net%2Fmjqueue&sig=1oaLL83%2BQSQ6EV22iTA%2BzV5YqeMVkTbC8AblEi8SRXM%3D&se=2025-03-11T10%3A45%3A51Z&skn=postManToken";
//        String serviceBusURI = "sb://hltransformer-sample.servicebus.windows.net/";
        final String fullyQualifiedNamespace = "hltransformer-sample.servicebus.servicebus.windows.net";

        var tokenCredential = new AzureSasCredential(sasToken);
        ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .credential(fullyQualifiedNamespace, tokenCredential)
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildClient();

        ServiceBusMessage message = new ServiceBusMessage("Hello, this is a test message from Java!")
                .setMessageId("message-id-123")
                .setSubject("Test message");

        try {
            senderClient.sendMessage(message);
            System.out.println("Message sent successfully!");
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
        } finally {
            senderClient.close();
        }
    }
}

