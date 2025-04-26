package ir.donbee.mas.jade.agent;

import ir.donbee.mas.jade.behaviour.HealingBehaviour;
import jade.core.Agent;

public class ControllerAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + ": Controller started and waiting for exceptions...");
        addBehaviour(new HealingBehaviour(this));
    }
}
