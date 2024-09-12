package com.rede.distributedapp.command;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.StringUtils;

import picocli.CommandLine.Option;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.rede.distributedapp.constant.Constants.baseUrl;

@Component
@Command(
        name = "MyCLI",
        description = "Main entry point of the CLI",
        subcommands = {
                DistributedSystemsCLI.Greet.class,
                DistributedSystemsCLI.Rant.class,
                DistributedSystemsCLI.Post.class,
                DistributedSystemsCLI.Get.class
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
        private String rantUser;

        @ParentCommand
        private DistributedSystemsCLI parent;

        @Override
        public void run() {
            System.out.println("Ranting user: "+rantUser);
        }
    }

    @Command(name = "post", description = "Post request to save the message")
    static class Post implements Runnable {
        @Option(names = { "-m", "--message" }, description = "Message to be stored", defaultValue = "Default")
        private String message;

        @ParentCommand
        private DistributedSystemsCLI parent;

        @Override
        public void run() {
            System.out.println("The message given is " + message);
        }
    }

    @Command(name = "get", description = "Gets all the posts")
    static class Get implements Runnable {
        @Option(names = {"-i", "--id"}, description = "Get ID of specific message")
        private String Id;

        @Override
        public void run() {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();
            if(StringUtils.hasText(Id)) {

            } else {
                // Define headers (optional)
                HttpHeaders headers = new HttpHeaders();
                headers.set("Accept", "application/json");

                HttpEntity<String> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        baseUrl,
                        HttpMethod.GET,
                        requestEntity,
                        String.class);
                try {
                    String bodyContent = objectMapper.readValue(responseEntity.getBody(), String.class);
                    System.out.println("The body of response is " + bodyContent);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(String.format("Error reading contents from response: %s", e.getMessage()),
                            e);
                }
            }
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
