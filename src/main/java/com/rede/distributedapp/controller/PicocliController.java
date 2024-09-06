package com.rede.distributedapp.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

import com.rede.distributedapp.command.DistributedSystemsCLI;

@Component
public class PicocliController implements CommandLineRunner, ExitCodeGenerator {
    private final DistributedSystemsCLI distributedSystemsCLI;
    private final IFactory factory;
    private int exitCode;

    public PicocliController(DistributedSystemsCLI distributedSystemsCLI, IFactory factory) {
        this.distributedSystemsCLI = distributedSystemsCLI;
        this.factory = factory;
    }

    @Override
    public void run(String ...args) {
         exitCode = new CommandLine(distributedSystemsCLI, factory).execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
