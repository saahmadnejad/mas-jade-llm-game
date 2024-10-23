package ir.donbee.mas.jade;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class App {
    public static void main(String[] args) {
        Runtime instance = Runtime.instance();
        ProfileImpl profile = new ProfileImpl();
        profile.setParameter(Profile.GUI, "true");
        AgentContainer mainContainer = instance.createMainContainer(profile);
        AgentController sampleAgent = null;
        try {
            sampleAgent = mainContainer
                    .createNewAgent("sampleAgent", "ir.donbee.mas.jade.agents.SampleAgent", null);
            sampleAgent.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }
}
