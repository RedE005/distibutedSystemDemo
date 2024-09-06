package com.rede.distributedapp.command;

import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.Arrays;

@Component
@Command(
        name = "MyCLI",
        description = "Main entry point of the CLI",
        subcommands = {
                DistributedSystemsCLI.Greet.class,
                DistributedSystemsCLI.Rant.class,
                DistributedSystemsCLI.Post.class
        }
)
public class DistributedSystemsCLI implements Runnable {

    @Command(name = "greet", description = "Greets the user")
    static class Greet implements Runnable {
        @Parameters( index = "0",
                description = "The name to be greeted",
                defaultValue = "Bot"
        )
        private String name;

        @ParentCommand
        private DistributedSystemsCLI parent;

        @Override
        public void run() {
            System.out.println("Hello "+name);
        }
    }

    @Command(name = "rant", description = "Used to rant a user")
    static class Rant implements Runnable {
        @Parameters(description = "User to be ranted",
                defaultValue = "Bot"
        )
        String rantUser;

        @Override
        public void run() {
            System.out.println("Ranting user: "+rantUser);
        }
    }

    @Command(name = "post", description = "Post request to save the message")
    static class Post implements Runnable {
        @Option(names = { "-m", "--message" }, description = "Message to be stored", defaultValue = "testMessage")
        private String message;

        @Override
        public void run() {
            System.out.println("The message given is " + message);
        }
    }

    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        System.out.println("Welcome to my CLI");
        spec.commandLine().usage(System.err);
    }
}
