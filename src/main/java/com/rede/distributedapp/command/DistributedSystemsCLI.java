package com.rede.distributedapp.command;

import com.rede.distributedapp.entity.Message;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import java.util.Collections;

import static com.rede.distributedapp.constant.Constants.baseUrl;

@Component
@Command(
        name = "MyCLI",
        description = "Main entry point of the CLI",
        subcommands = {
                DistributedSystemsCLI.Greet.class,
                DistributedSystemsCLI.Rant.class,
                DistributedSystemsCLI.Post.class,
                DistributedSystemsCLI.Get.class,
                DistributedSystemsCLI.Delete.class
        }
)
public class DistributedSystemsCLI implements Runnable {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            Message messageObject = new Message(message);

            HttpEntity<Message> requestEntity = new HttpEntity<>(
                    messageObject,
                    httpHeaders
            );

            String path = "create";
            ResponseEntity<String> response = restTemplate.exchange(
                    String.format("%s/%s",baseUrl,path),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("The post is successful with body: " + response.getBody());

        }
    }

    @Command(name = "get", description = "Gets all the posts")
    static class Get implements Runnable {
        @Option(names = {"-i", "--id"}, description = "Get ID of specific message", arity = "0..1")
        private String Id;

        @Override
        public void run() {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity;
            String path;
            if(StringUtils.hasText(Id)) {
                path = "message";
                responseEntity = restTemplate.exchange(
                        String.format("%s/%s/%s", baseUrl, path, Id),
                        HttpMethod.GET,
                        requestEntity,
                        String.class
                );
            } else {
                path = "allRequests";
                responseEntity = restTemplate.exchange(
                        String.format("%s/%s",baseUrl,path),
                        HttpMethod.GET,
                        requestEntity,
                        String.class
                );
            }
            System.out.println("The body of response is " + responseEntity.getBody());
//            try {
//                String bodyContent = objectMapper.readValue(responseEntity.getBody(), String.class);
//                System.out.println("The body of response is " + bodyContent);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(String.format("Error reading contents from response: %s", e.getMessage()),
//                        e);
//            }
        }
    }

    @Command(name = "delete", description = "Deletes a message with a particular messageId")
    static class Delete implements Runnable {
        @Parameters(description = "Id of message to be deleted", arity = "1")
        public String id;

        @Override
        public void run() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> deleteEntity = new HttpEntity<>(httpHeaders);
            String path = "delete";
            if(StringUtils.hasText(id)) {
                ResponseEntity<String> response = restTemplate.exchange(
                        String.format("%s/%s/%s",baseUrl, path, id),
                        HttpMethod.DELETE,
                        deleteEntity,
                        String.class
                );
                System.out.println("The body of the response is: " + response.getBody());
            } else {
                System.out.println("Enter a valid non empty Id");
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
