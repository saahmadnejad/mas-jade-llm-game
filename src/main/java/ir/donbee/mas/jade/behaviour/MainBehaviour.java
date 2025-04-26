package ir.donbee.mas.jade.behaviour;

import ir.donbee.mas.jade.util.AgentUtil;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class MainBehaviour extends CyclicBehaviour {
    public MainBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    public void action() {
        try {
            System.out.println(myAgent.getLocalName() + ": Trying to execute buggyMethod...");
            AgentUtil.buggyMethod(); // This method will cause an NPE
        } catch (Exception e) {
            System.out.println(myAgent.getLocalName() + ": ERROR OCCURRED - " + e.getMessage());
            AgentUtil.notifyController("EXCEPTION:NPE:" + e.getMessage(), this.getAgent());
        }
        block();
    }
}
