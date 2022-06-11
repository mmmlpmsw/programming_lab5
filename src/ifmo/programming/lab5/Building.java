package ifmo.programming.lab5;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Stack;

import static ifmo.programming.lab5.BuildingChecker.getCollectionFromJSON;
import static ifmo.programming.lab5.Main.getContentFromFile;

/**
 * Хрущёвка
 */
public class Building {
    private static final String FILENAME_ENV = "loadfile";
    private Stack<Room> collection = new Stack<>();
    private Date creationDate = new Date();
    private boolean hasChanged = false;

    /**
     * Добавляет команту в здание
     * @param room комната, которую нужно добавить
     */
    public void add(Room room) {
        collection.push(room);
        hasChanged = true;
    }

    /**
     * Перечитывает коллекцию из файла
     * @return Количество загруженных элементов
     * @throws Exception когда что-то идёт не так
     */
    public int load () throws Exception {
        Map<String, String> env = System.getenv();
        String filename = env.get(FILENAME_ENV);
        int initialSize = collection.size();

        if (filename != null && !filename.isEmpty()) {
            String content = getContentFromFile(filename);
//            collection.clear();
            getCollectionFromJSON(this, content);
        }
        return collection.size() - initialSize;
    }

    /**
     * Удаляет комнату из здания
     * @param room комната, которую нужно удалить
     * @return true, если комната удалена
     */
    public boolean remove(Room room) {
        if (!collection.contains(room))
            return false;
        collection.remove(room);
        hasChanged = true;
        return true;
    }

    /**
     * Удаляет каждую комнату, превыщающую указанную
     * @param room Комната, с которой происходит сравнение
     * @return Количество удалённых комнат
     */
    public int remove_greater(Room room) {
        ArrayList<Room> removingRooms = new ArrayList<>();
        for (Room current : collection) {
            if (current.compareTo(room) > 0)
                removingRooms.add(current);
        }
        for (Room removing : removingRooms)
            collection.remove(removing);

        if (removingRooms.size() > 0)
            hasChanged = true;

        return removingRooms.size();
    }

    /**
     * Удаляет первый элемент стека
     * @return удалённый элемент (вернет null, если стек пуст)
     */
    public Room removeFirst() {
        Stack<Room> snew = new Stack<>();
        while(collection.size() > 1)
            snew.push(collection.pop());

        if (collection.size() == 1) {
            Room removed = collection.pop();
            collection.clear();
            while (snew.size() > 0)
                collection.push(snew.pop());

            hasChanged = true;

            return removed;
        }

        return null;
    }

    public Stack<Room> getCollection() {
        return collection;
    }

    public void setCollection(Stack<Room> collection) {
        this.collection = collection;
        hasChanged = true;
    }

    public int getSize() {
        return collection.size();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date createdDate) {
        this.creationDate = createdDate;
        hasChanged = true;
    }

    /**
     * @return true, если содержимое коллекции было изменено
     */
    public boolean isChanged() { return hasChanged; }

    /**
     * Устанавливает состояние изменённости коллекции. Если передатть true, коллекция
     * будет обозначена как изменённая. Рекомендуется вызывать функцию с переданным false
     * после сохранения состояния коллекции в файл
     * @param hasChanged Если true, коллекция изменена
     */
    public void setChange(boolean hasChanged) { this.hasChanged = hasChanged; }

    /**
     *
     * @return читабельное строковое представление коллекции
     */
    public String getCollectionInfo() {
        return  "Коллекция типа " + collection.getClass().getName() + ",\n" +
                "дата создания: " + creationDate + ",\n" +
                "содержит " + collection.size() + " элементов";
    }

    /**
     * справка по командам, реализуемым приложением
     * @return справка по командам, реализуемым приложением
     */
    public String getHelp() {
        return "Приложение поддерживает выполнение следующих команд:" +
                "\n\t• add {element}: добавить новый элемент в коллекцию;" +
                "\n\t• remove_first: удалить первый элемент из коллекции;" +
                "\n\t• remove_greater {element}: удалить из коллекции все элементы, превышающие заданный;" +
                "\n\t• show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении;" +
                "\n\t• info: вывести в стандартный поток вывода информацию о коллекции;" +
                "\n\t• load: перечитать коллекцию из файла;" +
                "\n\t• remove {element}: удалить элемент из коллекции по его значению;" +
                "\n\t• help: вызов справки." ;


    }
}
