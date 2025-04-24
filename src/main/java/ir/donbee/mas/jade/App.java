package ir.donbee.mas.jade;

import jade.core.Profile;
import jade.core.Runtime;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class App {
    public static void main(String[] args) {
        Runtime instance = Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(ProfileImpl.GUI, "false");

        AgentContainer mainContainer = instance.createMainContainer(profile);

        try {

            // Start ControllerAgent
            AgentController controllerAgent = mainContainer
                    .createNewAgent("controller", "ir.donbee.mas.jade.agent.ControllerAgent", null);
            controllerAgent.start();

            Thread.sleep(2000); // Ensure AdaptiveAgent is ready

            // Start AdaptiveAgent first
            AgentController adaptiveAgent = mainContainer
                    .createNewAgent("adaptiveAgent", "ir.donbee.mas.jade.agent.AdaptiveAgent", null);
            adaptiveAgent.start();

        } catch (StaleProxyException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
