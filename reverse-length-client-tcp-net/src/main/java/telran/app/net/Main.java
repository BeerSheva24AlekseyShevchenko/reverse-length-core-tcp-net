package telran.app.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Consumer;

import telran.net.TcpClient;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardIO;

public class Main {
    private static TcpClient tcpClient;

    public static void main(String[] args) {
        connectToServer();
        renderMenu();
    }

    private static void connectToServer() {
        Properties config = loadAppConfig();
        String serverUrl = config.getProperty("server.url");
        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        tcpClient = new TcpClient(serverUrl, serverPort);
    }

    private static Properties loadAppConfig() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("file config.properties not found");
        }
        return properties;
    }

    private static void renderMenu() {
        Item[] items = {
            Item.of("Get string length", Main.sendRequest("LENGTH")),
            Item.of("Get reversed string ", Main.sendRequest("REVERSE")),
            Item.of("Exit", Main::stopSession, true),
        };
        Menu menu = new Menu("Echo Application", items);
        menu.perform(new StandardIO());
    }

    private static Consumer<InputOutput> sendRequest(String requestType) {
        return (InputOutput io) -> {
            String str = io.readString("Enter any string");
            String response = tcpClient.sendAndReceive(requestType, str);
            io.writeLine(response);
        };
    }

    private static void stopSession(InputOutput io) {
        if (tcpClient != null) {
            try {
                tcpClient.close();
            } catch (IOException e) {
                io.writeLine(e.getMessage());
            }
        }
    }
}