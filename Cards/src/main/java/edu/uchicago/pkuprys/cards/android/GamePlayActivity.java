package edu.uchicago.pkuprys.cards.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.domain.Card;
import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.domain.PlayerView;
import edu.uchicago.pkuprys.cards.domain.Rank;
import edu.uchicago.pkuprys.cards.game.Player;

/**
 * Card image resources were found at http://www.jfitz.com/cards/
 */
public class GamePlayActivity extends FragmentActivity implements DialogInterface.OnClickListener {
    public static final String DIALOG_PLAY_CARD = "edu.uchicago.pkuprys.cards.android.DIALOG_PLAY_CARD";
    public static final String CARD_PLAYED = "edu.uchicago.pkuprys.cards.android.CARD_PLAYED";
    private static final int NAME_LENGTH = 8;

    private Card mSelectedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_play);
        LinearLayout playerMoves = (LinearLayout) findViewById(R.id.player_moves);

        Bundle extras = getIntent().getExtras();
        GameView gameView = (GameView) extras.getSerializable(CardsActivity.GAME_VIEW);

        int position = extras.getInt(Player.POSITION);
        List<Card> cards = null;
        for (PlayerView pv : gameView.getPlayerViews()) {
            if (pv.getPosition() == position) {
                cards = pv.getHand().getCards();
            }
            //add players' moves
            View playerMove = getLayoutInflater().inflate(R.layout.player_move, null);
            TextView playerName = (TextView) playerMove.findViewById(R.id.player_name);
            playerName.setText(trim(pv.getName()));
            ImageView cardPlayed = (ImageView) playerMove.findViewById(R.id.card_played);
            PlayerMove move = pv.getPlayerMove();
            String cardName = move == null ? "b2fv" : move.getCard().getDrawableName();
            int cardDrawable = getResources().getIdentifier(cardName, "drawable", getPackageName());
            cardPlayed.setImageDrawable(getResources().getDrawable(cardDrawable));
            if (move != null && !move.isCurrent()) {
                PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                cardPlayed.setColorFilter(colorFilter);
            }
            playerMoves.addView(playerMove);
        }
        Collections.sort(cards);

        TableLayout deckView = (TableLayout) findViewById(R.id.deck);

        TableRow row = new TableRow(this);
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(rowParams);
        Rank prevRank = cards.size() > 0 ? cards.get(0).getRank() : null;
        for (Card card : cards) {
            Rank currentRank = card.getRank();
            if (prevRank != currentRank) {
                deckView.addView(row);
                row = new TableRow(this);
                row.setLayoutParams(rowParams);
                prevRank = currentRank;
            }
            ImageButton cardButton = new ImageButton(this);
            int cardDrawable = getResources().getIdentifier(card.getDrawableName(), "drawable", getPackageName());
            cardButton.setBackgroundResource(cardDrawable);
            cardButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            cardButton.setOnClickListener(new CardClickListener(card));
            row.addView(cardButton);
        }
        if (cards.size() > 0) {
            deckView.addView(row);
        }
    }

    private String trim(String toTrim) {
        int endIndex = toTrim.length() > NAME_LENGTH ? NAME_LENGTH : toTrim.length();
        return toTrim.substring(0, endIndex);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                Intent result = new Intent();
                result.putExtra(CARD_PLAYED, mSelectedCard);
                setResult(Activity.RESULT_OK, result);
                finish();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mSelectedCard = null;
                break;
        }
    }

    private class CardClickListener implements View.OnClickListener {
        private Card mCard;

        @Override
        public void onClick(View view) {
            mSelectedCard = mCard;
            PlayCardDialog dialog = new PlayCardDialog(view.getBackground(), GamePlayActivity.this);
            dialog.show(getSupportFragmentManager(), DIALOG_PLAY_CARD);
        }

        public CardClickListener(Card card) {
            this.mCard = card;
        }
    }
}
