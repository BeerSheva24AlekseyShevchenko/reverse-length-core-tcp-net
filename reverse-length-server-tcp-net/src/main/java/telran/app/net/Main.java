package telran.app.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import telran.net.TcpServer;

public class Main {

    public static void main(String[] args) {
        Properties config = loadAppConfig();
        int serverPort = Integer.parseInt(config.getProperty("server.port"));
        ReverseLengthProtocol protocol = new ReverseLengthProtocol();
        TcpServer tcpServer = new TcpServer(protocol, serverPort);
        tcpServer.run();
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
}