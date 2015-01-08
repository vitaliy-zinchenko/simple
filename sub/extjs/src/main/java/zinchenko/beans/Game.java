package zinchenko.beans;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by zinchenko on 31.12.14.
 */
@Entity("game")
public class Game {

    @Id
    private ObjectId id;

    private String name;

    private List<Card> cards;

    private List<CardBundle> cardBundles;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<CardBundle> getCardBundles() {
        return cardBundles;
    }

    public void setCardBundles(List<CardBundle> cardBundles) {
        this.cardBundles = cardBundles;
    }
}
