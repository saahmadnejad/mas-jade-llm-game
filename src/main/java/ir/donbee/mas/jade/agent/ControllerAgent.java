package ir.donbee.mas.jade.agent;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

public class ControllerAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + ": Controller started and waiting for exceptions...");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(getLocalName() + " received: " + content);

                    if (content.startsWith("EXCEPTION:NPE")) {
                        System.out.println(getLocalName() + ": Simulating LLM assistance for NPE...");
                        String fix = generateFixCode();
                        sendFixMessage(fix);
                    }
                } else {
                    block();
                }
            }
        });
    }

    // Simulate LLM Generating a Fix (Returning Java Code)
    private String generateFixCode() {
        return "public class GeneratedFix { \n"
                + "    public void fixedMethod() { \n"
                + "        System.out.println(\"[GeneratedFix] Successfully executed fixedMethod (LLM suggestion)!\"); \n"
                + "    }\n"
                + "}";
    }

    private void sendFixMessage(String newCode) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("adaptiveAgent", AID.ISLOCALNAME));
        msg.setContent("FIX_CODE:" + newCode);
        send(msg);
        System.out.println(getLocalName() + ": Sent new FIX_CODE to AdaptiveAgent.");
    }
}
