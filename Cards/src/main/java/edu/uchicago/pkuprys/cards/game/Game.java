package edu.uchicago.pkuprys.cards.game;

import java.util.ArrayList;
import java.util.List;

import edu.uchicago.pkuprys.cards.domain.Card;
import edu.uchicago.pkuprys.cards.domain.Deck;
import edu.uchicago.pkuprys.cards.domain.GameState;

public class Game {
    private final int numPlayers;
    private final int cardsPerPlayer;
    private final int usedCards;
    private final int unusedCards;
    private List<Player> players;
    private Deck deck;
    private GameState state;
    private int cardsPlayed = 0;

    public Game(int numPlayers) {
        this.numPlayers = numPlayers;
        this.players = new ArrayList<Player>(numPlayers);
        this.deck = DeckFactory.getDefaultDeck();
        this.cardsPerPlayer = deck.size() / numPlayers;
        this.unusedCards = deck.size() % (cardsPerPlayer);
        this.usedCards = deck.size() - unusedCards;
        this.state = GameState.PRE;
    }

    public int addPlayer(Player player) {
        players.add(player);
        for (int i = 0; i < cardsPerPlayer; i++) {
            player.deal(deck.pullRandom());
        }
        if (players.size() == numPlayers) {
            state = GameState.ACTIVE;
        }
        return players.size() - 1;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public GameState getGameState() {
        return state;
    }

    public void playCard(Card card) {
        cardsPlayed++;
        if (usedCards == cardsPlayed) {
            state = GameState.OVER;
        }
    }
}
