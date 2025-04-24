package ir.donbee.mas.jade.agent;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URL;
import java.net.URLClassLoader;

public class AdaptiveAgent extends Agent {
    protected void setup() {
        System.out.println(getLocalName() + ": Started and waiting for commands...");

        // Message listener
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println(getLocalName() + " received: " + content);

                    if (content.startsWith("FIX_CODE:")) {
                        String code = content.substring(9); // Extract code
                        System.out.println(getLocalName() + ": Received new code! Compiling...");

                        boolean compiled = compileAndLoadCode(code);
                        if (compiled) {
                            executeNewMethod();
                        } else {
                            System.out.println(getLocalName() + ": Compilation failed. Sticking to default behavior.");
                        }
                    }
                } else {
                    block();
                }
            }
        });

        // Execution loop (Triggers NPE)
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                try {
                    System.out.println(getLocalName() + ": Trying to execute buggyMethod...");
                    buggyMethod(); // This method will cause an NPE
                } catch (Exception e) {
                    System.out.println(getLocalName() + ": ERROR OCCURRED - " + e.getMessage());
                    notifyController("EXCEPTION:NPE:" + e.getMessage());
                }
                block();
            }
        });
    }

    // Simulated faulty method (Throws NPE)
    private void buggyMethod() {
        String data = null;
        int length = data.length(); // 🚨 This will cause an NPE
    }

    // Notify ControllerAgent about an issue
    private void notifyController(String message) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new jade.core.AID("controller", jade.core.AID.ISLOCALNAME));
        msg.setContent(message);
        send(msg);
    }

    // Compile and Load Received Code
    private boolean compileAndLoadCode(String code) {
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
                System.out.println(getLocalName() + ": Compilation Successful!");
                return true;
            } else {
                System.out.println(getLocalName() + ": Compilation Failed!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Load and Execute New Code
    private void executeNewMethod() {
        try {
            String directory = "./generated";

            // Check if the class exists in the directory before loading it
            File classFile = new File(directory, "GeneratedFix.class");
            if (!classFile.exists()) {
                System.out.println(getLocalName() + ": Class file not found. Compilation might have failed.");
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
}
