package ir.donbee.mas.jade.behaviour;

import ir.donbee.mas.jade.util.AgentUtil;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HealingBehaviour extends CyclicBehaviour {
    public HealingBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            String content = msg.getContent();
            System.out.println(myAgent.getLocalName() + " received: " + content);

            if (content.startsWith("EXCEPTION:NPE")) {
                System.out.println(myAgent.getLocalName() + ": Simulating LLM assistance for NPE...");
                String fix = AgentUtil.generateFixCode();
                AgentUtil.sendFixMessage(fix, this.getAgent());
            }
        } else {
            block();
        }
    }
}
