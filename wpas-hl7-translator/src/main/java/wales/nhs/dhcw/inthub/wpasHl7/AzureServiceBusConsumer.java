package wales.nhs.dhcw.inthub.wpasHl7;

import com.azure.core.credential.AzureSasCredential;
import com.azure.messaging.servicebus.*;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class AzureServiceBusConsumer {

    public static void main(String[] args) throws InterruptedException {
        final String queueName = "mjqueue";
        final String sasToken = "SharedAccessSignature sr=https%3A%2F%2Fhltransformer-sample.servicebus.windows.net%2Fmjqueue&sig=Dgiv9jigo9BgyulJDcK9n972Nn13mI0XeAsWoWI44CA%3D&se=2025-03-10T16%3A40%3A18Z&skn=postManToken";
//        final String connectionString = "Endpoint=sb://hltransformer-sample.servicebus.windows.net/;SharedAccessKeyName=postManToken;SharedAccessKey=ItO5nttYvrhOjHX4zEiJz8B19Q9U95aPY+ASbL1l1q0=";
        final String fullyQualifiedNamespace = "hltransformer-sample.servicebus.servicebus.windows.net";

        var tokenCredential = new AzureSasCredential(sasToken);

            ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                    .credential(fullyQualifiedNamespace, tokenCredential)
                    .processor()
                    .queueName(queueName)
                    .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                    .disableAutoComplete()  // Make sure to explicitly opt in to manual settlement (e.g. complete, abandon).
                    .processMessage(processMessage)
                    .processError(processError)
                    .disableAutoComplete()
                    .buildProcessorClient();

            processorClient.start();
            TimeUnit.SECONDS.sleep(100);
            processorClient.stop();
            processorClient.close();
    }


    static Consumer<ServiceBusReceivedMessageContext> processMessage = context -> {
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Processing message. Session: %s, Sequence #: %s. Contents: %s%n", message.getMessageId(),
                message.getSequenceNumber(), message.getBody());
    };
    static Consumer<ServiceBusErrorContext> processError = errorContext -> {
        if (errorContext.getException() instanceof ServiceBusException) {
            ServiceBusException exception = (ServiceBusException) errorContext.getException();

            System.out.printf("Error source: %s, reason %s%n", errorContext.getErrorSource(),
                    exception.getReason());
        } else {
            System.out.printf("Error occurred: %s%n", errorContext.getException());
        }
    };

}
