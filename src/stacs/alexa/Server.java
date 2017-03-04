package stacs.alexa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import stacs.server.ServerWorld;

public class Server implements Runnable {
    
    private ServerWorld world;
    
    public Server(ServerWorld world){
        this.world = world;
    }
    
    public void run() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8060), 0);
            server.createContext("/", (HttpExchange t) -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody()))) {
                    String line = reader.readLine();

                    
                    String response = "I COULD NOT UNDERSTAND THE MESSAGE.";
                    try {
                        JSONObject object = new JSONObject(line);
                        String intent = object.getJSONObject("request").getJSONObject("intent").getString("name");
                        System.out.println("GOT MESSAGE " + intent);
                        world.runCommand(intent);
                        response = "Yes my lord.";
                    } catch (Exception e) {
                        System.out.println("PARSING ERROR");
                        e.printStackTrace();
                    }

                    Writer writer = new OutputStreamWriter(t.getResponseBody());
                    t.getResponseHeaders().put("Content-Type", Arrays.asList("text/plain"));
                    t.sendResponseHeaders(200, 0);
                    String msg = "{ \"version\": \"1.0\", \"response\": { \"outputSpeech\": { \"type\": \"PlainText\", \"text\": \"" + response + "\" }, \"shouldEndSession\": true } }\r\n";
                    writer.write(msg);
                    writer.flush();
                    t.getResponseBody().flush();
                    t.getResponseBody().close();

                } catch (Exception e){
                    e.printStackTrace();
                }
            });
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
