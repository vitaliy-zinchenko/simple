package zinchenko.beans;

import org.omg.PortableInterceptor.DISCARDING;

import java.util.List;

/**
 * Created by zinchenko on 01.01.15.
 */
public class CardBundle extends DeskItem {

    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
