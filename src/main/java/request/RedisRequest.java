package request;

import database.InMemoryDatabase;
import database.RDBJava;
import database.set.Expiry;
import database.set.SetDatabaseManager;
import database.set.SetStorage;
import handler.Command;
import handler.DataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class RedisRequest {
    private final BufferedReader bufferedReader;
    private final InMemoryDatabase inMemoryDatabase;
    private final String CRLF = "\r\n";
    private String inputString;
    private Command[] commands;
    private Argument argument;

    public RedisRequest(InputStream inputStream, InMemoryDatabase inMemoryDatabase) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        this.inMemoryDatabase = inMemoryDatabase;
    }


    public Object parseProtocol() throws IOException {
        String currentLine = bufferedReader.readLine();

        char c = currentLine.charAt(0);
        switch (c) {
            case '+' -> {
                return currentLine.substring(1);
            }
            case '$' -> {
                int stringSize = Integer.parseInt(currentLine.substring(1));
                char[] s = new char[stringSize];
                bufferedReader.read(s, 0, stringSize);
                bufferedReader.readLine();
                return new String(s);
            }
            case ':' -> {
                return Integer.parseInt(currentLine.substring(1));
            }
            case '*' -> {
                int arraySize = Integer.parseInt(currentLine.substring(1));
                List<Object> elements = new ArrayList<>();
                for (int i = 0; i < arraySize; i++) {
                    elements.add(parseProtocol());
                }
                return elements;
            }
            default -> {
                throw new IOException("Unknown RESP type " + c);
            }
        }
    }

    public String parseCommand(List<Object> commands) {
//        if (commands == null) return "-ERR\r\n";
        String command = (String) commands.get(0);
        switch (Command.valueOf(command.toUpperCase())) {
            case ECHO -> {
                if (commands.size() > 2) {
                    return DataType.ERR.getType() + "ERR Unknown commands" + CRLF;
                }
                String text = (String) commands.get(1);
                return DataType.STRING.getType() + text + "\r\n";
            }
            case PING -> {
                return DataType.STRING.getType() + "PONG\r\n";
            }
            case SET -> {
                SetDatabaseManager setDatabaseManager = new SetDatabaseManager(inMemoryDatabase);
                setDatabaseManager.setValue(commands);


//                SetStorage setStorage = new SetStorage();
//                setStorage.setKey((String) commands.get(1));
//                setStorage.setValue((String) commands.get(2));
//                long ttl = -1;
//                if (commands.size() == 5) {
//                    String expireKey = (String) commands.get(3);
//                    int exp = Integer.parseInt((String) commands.get(4));
//                    if (expireKey.equalsIgnoreCase("px")) {
//                        ttl = Instant.now().plusMillis(exp).toEpochMilli();
//                    }
//                    setStorage.setExpiry(Expiry.valueOf(expireKey.toUpperCase()));
//                }
//                setStorage.setTtl(ttl);
//                RDBJava.getInstance().set((String) commands.get(1), setStorage);

                return DataType.STRING.getType() + "OK" + CRLF;
            }
            case GET -> {
                SetDatabaseManager setDatabaseManager = new SetDatabaseManager(inMemoryDatabase);
                String val = setDatabaseManager.getValue((String) commands.get(1));

//                String val = RDBJava.getInstance().get((String) commands.get(1));

                if (val.equals("-1")) {
                    return DataType.BULK_STRING.getType() + val + CRLF;
                }

                return DataType.BULK_STRING.getType() + val.length() + CRLF + val + CRLF;
            }
            case CONFIG -> {
                String comm = (String) commands.get(1);
                String key = (String) commands.get(2);
                String response = DataType.BULK_STRING.getType();
                String configValue = "";
                if (comm.equalsIgnoreCase("GET")){
                    configValue = inMemoryDatabase.getConfigDatabase().get(key).getValue();
                }
                return DataType.ARRAY.getType() + "2" + CRLF + response + key.length() + CRLF + key + CRLF + DataType.BULK_STRING.getType() + configValue.length() + CRLF + configValue + CRLF;
            }
        }
        return DataType.ERR.getType() + "ERR Unknown commands" + CRLF;
    }
}

class Argument {
    private String argument;

    public Argument(String argument) {
        this.argument = argument;
    }
}
