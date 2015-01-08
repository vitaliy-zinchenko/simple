package zinchenko.controllers;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import zinchenko.beans.Game;

@Controller
public class TestController {

    @Autowired
    private Morphia morphia;

    @Autowired
    private Datastore datastore;

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public Game save() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Game game = objectMapper.readValue(TestController.class.getResourceAsStream("/zinchenko/controllers/test.json"), Game.class);

        Key<Game> key = datastore.save(game);

        System.out.println(key.getId());

        return game;
    }

    @ResponseBody
    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public Game test(@PathVariable String id) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Game game = objectMapper.readValue(TestController.class.getResourceAsStream("/zinchenko/controllers/test.json"), Game.class);

        ObjectId objectId = new ObjectId(id);

        Game game = datastore.get(Game.class, objectId);

        return game;
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public Datastore getDatastore() {
        return datastore;
    }

    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }
}
