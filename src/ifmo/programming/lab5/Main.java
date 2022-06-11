package ifmo.programming.lab5;

import com.company.lib.*;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Map;

import static ifmo.programming.lab5.BuildingChecker.getCollectionFromJSON;
import static ifmo.programming.lab5.RoomFactory.makeRoomFromJSON;

public class Main {

    private static final String FILENAME_ENV = "LOADFILE";
    private static final String FILE_FOR_AUTOSAVE = "autosave.json";

    public static void main(String[] args) {

        try {
            // Изменение кодировки вывода для поддержки Git Bash, необязательно
            System.setOut(new PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {}

        System.out.println("Добро пожаловать! \nИ помните, что в конце каждой команды должен стоять символ \";\"." +
                "\nЧтобы получить справку по командам, введите команду help." );
        Building building = new Building();

        Map<String, String> env = System.getenv();
        String filename = env.get(FILENAME_ENV);

        if (filename != null && !filename.isEmpty()) {
            try {
                String content = getContentFromFile(filename);
                getCollectionFromJSON(building, content);
                System.out.println("Состояние загружено из файла");
                System.out.println("Загружено " + building.getSize() + " комнат");

            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден");
            } catch (AccessDeniedException e) {
                System.out.println("Отсутствует соответствующий доступ к файлу");
            } catch (IOException e) {
                System.out.println("Ошибка чтения/записи");
            } catch (Exception e) {
                System.out.println("Не получилось загрузить состояние: " + e.getMessage());
            }
        }

        saveAfterPressKey(building);

        try {
            while (true) {
                System.out.print("Введите команду >>> ");
                String response = processCommand(getNextCommand(), building);
                System.out.println(response);
            }
        } catch (IOException e) {
            System.out.println("Совершенно неожиданная ввода/вывода");
        }
    }

    private static String processCommand(String request, Building building) {
        if (request == null) {
            System.exit(0);
        }
        request = request.trim();
        UserCommand command = divideCommand(request);
        String result = new String("");

        if (command.name.isEmpty())
            return "Следует ввести команду";

        switch (command.name) {
            case "exit":
                System.exit(0);


            case "add":
                try {
                    if (command.value == null)
                        return "Введите данные в формате json после команды add";
                    building.add(makeRoomFromJSON(command.value));
                    return "Комната добавлена";
                } catch (JSONParseException | IllegalArgumentException e) {
                    return e.getMessage();
                }

            case "remove_first":
                if (building.getSize() == 0) {
                    return "В здании нет ни одной комнаты";
                }
                return "Удалено комнат: " + building.removeFirst();

            case "remove_greater":
                try {
                    Room room = makeRoomFromJSON(command.value);
                    return "Удалено " + building.remove_greater(room) + " комнат";
                } catch (JSONParseException | IllegalArgumentException e) {
                    return e.getMessage();
                }

            case "show":
                if (building.getSize() == 0) {
                    return "В здании нет ни одной комнаты";
                }
                result = result.concat("Комнаты: \n");
                for (Room room : building.getCollection()) {
                    result = result.concat(room.toString() + "\n");
                }
                return result;

            case "info":
                return building.getCollectionInfo();

            case "save":
                if (command.value == null)
                    return "Укажите имя файла";
                try {
                    BuildingChecker.saveCollection(building, new BufferedWriter(new FileWriter(command.value)));
                    return "Сохранено";
                } catch (IOException e) {
                    return "Ошибка чтения/записи";
                }

            case "load":
                try {
                    int loaded = building.load();
                    return "Загружено " + loaded + " элементов";
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден");
                } catch (IOException e) {
                    System.out.println("Ошибка чтения/записи");
                } catch (Exception e) {
                    System.out.println("Не получилось загрузить состояние: " + e.getMessage());
                }

            case "remove":
                try {
                    boolean removed = building.remove(makeRoomFromJSON(command.value));
                    if (removed) {
                        return "Комната удалена";
                    }
                    return "Нет такой комнаты";
                } catch (JSONParseException | IllegalArgumentException e) {
                    return e.getMessage();
                }

            case "help":
                return building.getHelp();


            default:
                return "Неизвестная команда: " + command.name;
        }
    }

    protected static String getContentFromFile(String filename) throws IOException {
        try (FileReader reader = new FileReader(filename)) {
            String filecontent = new String("");
            int running;
            do {
                running = reader.read();
                if (running != -1) {
                    filecontent = filecontent.concat(String.valueOf((char)running));

                }
            } while (running != -1);
            return filecontent;
        }
    }


    private static String getNextCommand() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder builder = new StringBuilder();

        boolean inString = false;
        loop: do {
            String raw = reader.readLine();
            if (raw == null) return null;
            char[] data = raw.toCharArray();
            for (char current : data) {
                if (current != ';' || inString) {  builder.append(current); }
                if (current == '"') { inString = !inString;}
                if (current == ';' && !inString) break loop;
            }
        } while (true);
        return builder.toString();
    }
    private static UserCommand divideCommand(String request) {
        int spacePosition = request.indexOf(' ');
        if (spacePosition == -1) {return new UserCommand(request, null);}
        else {return new UserCommand(request.substring(0, spacePosition), request.substring(spacePosition+1));}
    }


    private static void saveAfterPressKey(Building building) {
        Runnable saveCode = new Runnable() {
            @Override
            public void run() {
                try {
                    if (building.isChanged()) {
                        System.out.println("\nСохраняю...");
                        BuildingChecker.saveCollection(building, new BufferedWriter(new FileWriter(FILE_FOR_AUTOSAVE)));
                        System.out.println("Сохранено");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread savingThread = new Thread(saveCode);
        Runtime.getRuntime().addShutdownHook(savingThread);
    }

    private static class UserCommand {
        String name;
        String value;

        UserCommand (String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
}