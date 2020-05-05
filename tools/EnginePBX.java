package ceeport.tools;

/**
 * Created by Vast on 23.03.15.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class EnginePBX {

    // Test method for
    static Logger log = Logger.getLogger(EnginePBX.class.getName());

    public static void runJar(String cmdLine) {
        //String cmdLine = "/home/ildm/jdk1.7.0_21/bin/java -jar \"/home/ildm/Dropbox/src/NetBeansProjects/Asterisk_config_files_GUI/Netbeans_Project/dist/Asterisk_config_files_GUI.jar\"";
        try {
            Process process = Runtime.getRuntime().exec(cmdLine);
            
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    // move to engine
    public static void runBashScript(String PathToBashFile) {

        try {
            Process p = null;
            String line = null;
            p = Runtime.getRuntime().exec(PathToBashFile);

            try (BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()))) {

                while ((line = is.readLine()) != null) {
                    log.info(line);
                }
                line = is.toString();
                log.info(line);
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        //return null;
    }

    public static ArrayList<String> runShellScript(String command) throws IOException {
        ArrayList<String> result = new ArrayList<String>();
        String resultExecute = null;
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(new String[]{"/bin/bash", "-c", command});
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((resultExecute = bufferedReader.readLine()) != null) {
            result.add(resultExecute);

        }
        //
        //log.info(result);
        System.out.println((result));
        return result;
    }


    public static void main(String[] args) throws IOException {
//        //runShellScript("asterisk -rvvvcd");
//        //runJar("/home/ildm/jdk1.7.0_21/bin/java -jar \"/home/ildm/Dropbox/src/NetBeansProjects/Asterisk_config_files_GUI/Netbeans_Project/dist/Asterisk_config_files_GUI.jar\"");
//        //runBashScript("/home/ildm/jdk1.7.0_21/bin/java -jar \"/home/ildm/Dropbox/src/NetBeansProjects/Asterisk_config_files_GUI/Netbeans_Project/dist/Asterisk_config_files_GUI.jar\"");
        //runJar("ping 192.168.44.237");

    }

}
