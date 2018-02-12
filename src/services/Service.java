package services;

import java.io.IOException;

import main.Client;
import main.Console;

public abstract class Service {
	public abstract void executeRequest(Console console, Client client) throws IOException;
}
