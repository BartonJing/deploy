package com.barton.utils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;

/**
 * create by barton on 2018-7-30
 */
public class Test {
    public static  void main(String [] args){
        try
        {
            /* Create a connection instance */
            Connection conn = new Connection("106.12.9.75");

            /* Now connect */
            conn.connect();
            /* Authenticate */
            boolean isAuthenticated = conn.authenticateWithPassword("barton","bartonbcc");
            if (isAuthenticated == false)
                throw new IOException("Authentication failed. Please check hostname, username and password.");
            /* Create a session */
            Session sess = conn.openSession();
            // sess.execCommand("uname -a && date && uptime && who");
            System.out.println("start exec command.......");

            //sess.execCommand("echo /"Text on STDOUT/"; echo /"Text on STDERR/" >&2");
            //sess.execCommand("java -version");
            sess.requestPTY("batch");
            sess.startShell();


            InputStream stdout = new StreamGobbler(sess.getStdout());
            InputStream stderr = new StreamGobbler(sess.getStderr());

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));


            //if you want to use sess.getStdin().write(), here is a sample
            //byte b[]={'j','a','v','a','n'};
            //byte b[]={'e','x','i','t','/n'};
            //String aa = "java";
            //sess.getStdin().write(aa.getBytes());
            //sess.getStdin().write("/n".getBytes());
/*
String str="env";
   String str1="exit";
   System.out.println(str+str1);
   out.write(str.getBytes());
   out.write('/n');
   out.write(str1.getBytes());
   out.write('/n');
*/
            //we used PrintWriter, it makes things simple
            PrintWriter out =new PrintWriter(sess.getStdin());

            //out.println("env");
            out.println("java");
            out.println("exit");

            out.close();
            sess.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);

            System.out.println("Here is the output from stdout:");

            while (true)
            {
                String line = stdoutReader.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }
            System.out.println("Here is the output from stderr:");
            while (true)
            {
                String line = stderrReader.readLine();
                if (line == null)
                    break;
                System.out.println(line);
            }
            /* Show exit status, if available (otherwise "null") */
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close();/* Close this session */
            conn.close();/* Close the connection */

        }
        catch (IOException e)
        {
            e.printStackTrace(System.err);
            System.exit(2);
        }
    }
}
