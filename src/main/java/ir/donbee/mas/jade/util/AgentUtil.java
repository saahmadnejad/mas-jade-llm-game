package ir.donbee.mas.jade.util;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class AgentUtil {

    private AgentUtil() {
        throw new IllegalStateException("Utility class");
    }

    // Simulated faulty method (Throws NPE)
    public static void buggyMethod() {
        String data = null;
        int length = data.length(); // 🚨 This will cause an NPE
    }

    // Notify ControllerAgent about an issue
    public static void notifyController(String message, Agent agent) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new jade.core.AID("controller", jade.core.AID.ISLOCALNAME));
        msg.setContent(message);
        agent.send(msg);
    }

    // Compile and Load Received Code
    public static boolean compileAndLoadCode(String code, Agent agent) {
        try {
            String className = "GeneratedFix";
            String fileName = "GeneratedFix.java";
            String directory = "./generated"; // Directory to store compiled classes

            // Create directory if doesn't exist
            Files.createDirectories(Paths.get(directory));

            // Save received code to file
            Files.write(Paths.get(directory, fileName), code.getBytes());

            // Compile the code
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            int result = compiler.run(null, null, null, directory + "/" + fileName);

            if (result == 0) {
                System.out.println(agent.getLocalName() + ": Compilation Successful!");
                return true;
            } else {
                System.out.println(agent.getLocalName() + ": Compilation Failed!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Load and Execute New Code
    public static void executeNewMethod(Agent agent) {
        try {
            String directory = "./generated";

            // Check if the class exists in the directory before loading it
            File classFile = new File(directory, "GeneratedFix.class");
            if (!classFile.exists()) {
                System.out.println(agent.getLocalName() + ": Class file not found. Compilation might have failed.");
                return;
            }

            // Add the directory to classpath dynamically
            URL[] urls = new URL[]{new File(directory).toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls);
            Class<?> loadedClass = classLoader.loadClass("GeneratedFix");

            Method method = loadedClass.getDeclaredMethod("fixedMethod");
            method.invoke(loadedClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simulate LLM Generating a Fix (Returning Java Code)
    public static String generateFixCode() {
        return "public class GeneratedFix { \n"
                + "    public void fixedMethod() { \n"
                + "        System.out.println(\"[GeneratedFix] Successfully executed fixedMethod (LLM suggestion)!\"); \n"
                + "    }\n"
                + "}";
    }

    public static void sendFixMessage(String newCode, Agent agent) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("adaptiveAgent", AID.ISLOCALNAME));
        msg.setContent("FIX_CODE:" + newCode);
        agent.send(msg);
        System.out.println(agent.getLocalName() + ": Sent new FIX_CODE to AdaptiveAgent.");
    }
}
