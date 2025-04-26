package ir.donbee.mas.jade.agent;

import ir.donbee.mas.jade.behaviour.BuggyBehaviour;
import ir.donbee.mas.jade.behaviour.MessageListeningBehaviour;
import jade.core.Agent;

public class AdaptiveAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + ": Started and waiting for commands...");
        addBehaviour(new MessageListeningBehaviour(this));
        addBehaviour(new BuggyBehaviour(this));
    }
}
