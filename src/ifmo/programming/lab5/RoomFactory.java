package ifmo.programming.lab5;

import com.company.lib.*;
import java.util.ArrayList;

/**
 * Советский завод по производству комнат для хрущёвок
 */
public class RoomFactory {
    static Room makeRoomFromJSON(String json) throws JSONParseException, IllegalArgumentException {
        JSONEntity entity = JSONParser.parse(json);

        if (!entity.isObject()) {
            throw new IllegalArgumentException("Данный json должен быть объектом, но имеет тип " + entity.getType().toString().toLowerCase());
        }

        JSONObject object = (JSONObject)entity;

        JSONEntity widthEntity = object.getItem("width");
        JSONEntity heightEntity = object.getItem("height");
        JSONEntity lengthEntity = object.getItem("length");

        if (widthEntity == null || heightEntity == null || lengthEntity == null) {
            throw new IllegalArgumentException("width, height и length должны быть обязательно указаны");
        }

        if (!(widthEntity.isNumber() && heightEntity.isNumber() && lengthEntity.isNumber())) {
            throw new IllegalArgumentException("width, height и length должны быть числами");
        }

        int width = (int)((JSONNumber)widthEntity).getValue();
        int height = (int)((JSONNumber)heightEntity).getValue();
        int length = (int)((JSONNumber)lengthEntity).getValue();

        String wallcolor = "";
        JSONEntity wallcolorEntity = object.getItem("wallcolor");
        if (wallcolorEntity != null) {
            if (wallcolorEntity.isString()) {
                wallcolor = ((JSONString)wallcolorEntity).getContent();
            }
            else {
                throw new IllegalArgumentException("wallcolor должен быть строкой, но имеет тип " + wallcolorEntity.getType().toString().toLowerCase());
            }
        }

        Room.Thing[] shelfArray = new Room.Thing[0];
        JSONEntity thingsEntity = object.getItem("shelf");

        if (thingsEntity != null) {
            if (!thingsEntity.isArray()) {
                throw new IllegalArgumentException("shelf должен быть массивом, но имеет тип" + thingsEntity.getType().toString().toLowerCase());
            }

            ArrayList<JSONEntity> entities = ((JSONArray)thingsEntity).getItems();
            shelfArray = new Room.Thing[entities.size()];

            for (int i = 0; i < entities.size(); i++) {
                if (!entities.get(i).isObject()) {
                    throw new IllegalArgumentException("Все элементы массива shelf должны быть объектами");
                }
                JSONObject thingObject = (JSONObject) entities.get(i);
                String name = "";
                int size;

                JSONEntity nameEntity = thingObject.getItem("name");
                if (nameEntity != null) {
                    if (nameEntity.isString()) {
                        name = ((JSONString) nameEntity).getContent();
                    }
                    else {
                        throw new IllegalArgumentException("Поля name в элементах массива shelf должны быть строками, но одно из них имеет тип " + nameEntity.getType().toString().toLowerCase());
                    }
                }

                JSONEntity sizeEntity = thingObject.getItem("size");
                if (sizeEntity == null)
                    throw new IllegalArgumentException("Поле size в элементах массива shelf являются обязательными");

                if (sizeEntity.isNumber()) { size = (int)((JSONNumber) sizeEntity).getValue();}
                else {
                    throw new IllegalArgumentException("Поля size в элементах массива shelf должны быть числами, но одно из них имеет тип " + sizeEntity.getType().toString().toLowerCase());
                }

                shelfArray[i] = new Room.Thing(name, size);
            }
        }

        return new Room(width, height, length, wallcolor, shelfArray);
    }
}
