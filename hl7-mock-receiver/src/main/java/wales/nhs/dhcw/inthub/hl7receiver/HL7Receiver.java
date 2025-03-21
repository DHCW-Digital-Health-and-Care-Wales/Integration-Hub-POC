package wales.nhs.dhcw.inthub.hl7receiver;


import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.HL7Service;
import ca.uhn.hl7v2.protocol.ReceivingApplication;

public class HL7Receiver implements AutoCloseable {

    private final HL7Service server;

    public HL7Receiver(HapiContext context, AppConfig config) {
        server = context.newServer(
            config.receiverMllpPort(),
            config.useTlsForMLLP()
        );
    }

    public void registerHandler(ReceivingApplication handler){
        server.registerApplication("*", "*", handler);
    }

    public void startServer() throws InterruptedException {
        server.startAndWait();
    }

    @Override
    public void close()  {
        server.stopAndWait();
    }

}
