package ir.saitech.jlightcast;


import ir.saitech.jlightcast.Controller.Handler;
import ir.saitech.jlightcast.Utils.Out;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        // Setup Logger
        Out.initFileLog();
        Out.setVerbosity(4);

        // Default Values
        String port = "9096";
        String conf = "jlc_default.conf";

        // write your code here
        CommandLine commandLine;
        Option option_port = new Option("p",true,"Port number");
        Option option_conf = new Option("c",true,"Config file address");
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(option_port);
        options.addOption(option_conf);

        try
        {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("p"))
            {
                port = commandLine.getOptionValue("p");
                Out.printInfo("Running on port : "+port);
                Out.ilog("main","port set to "+port);
            }else {
                Out.printWarning("no port specified, trying default port : 9096");
            }

            if (commandLine.hasOption("c"))
            {
                conf = commandLine.getOptionValue("c");
                Out.printInfo("Using config file : "+conf);
                Out.ilog("main","config file specified : "+conf);
            }else {
                Out.printWarning("no config file specified, trying default config file : jlc_default.conf");
            }

        }
        catch (ParseException exception)
        {
            Out.elog("main","ArgParse error: ");
            Out.elog("main",exception.getMessage());
            Out.printError("ArgParse Error : "+exception.getMessage());
            System.exit(-1);
        }

        new Main(port,conf);
    }

    Main(String port,String configAddr){
        Out.ilog("Main","port : "+port);
        Out.ilog("Main","configAddr : "+configAddr);

        new Handler(Integer.valueOf(port)).run();
    }
}
