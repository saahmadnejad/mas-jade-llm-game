package ir.donbee.mas.jade.behaviour;

import ir.donbee.mas.jade.util.AgentUtil;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class MessageListeningBehaviour extends CyclicBehaviour {

    public MessageListeningBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            String content = msg.getContent();
            System.out.println(myAgent.getLocalName() + " received: " + content);

            if (content.startsWith("FIX_CODE:")) {
                String code = content.substring(9); // Extract code
                System.out.println(myAgent.getLocalName() + ": Received new code! Compiling...");

                boolean compiled = AgentUtil.compileAndLoadCode(code, this.getAgent());
                if (compiled) {
                    AgentUtil.executeNewMethod(this.getAgent());
                } else {
                    System.out.println(myAgent.getLocalName() + ": Compilation failed. Sticking to default behavior.");
                }
            }
        } else {
            block();
        }

    }
}
