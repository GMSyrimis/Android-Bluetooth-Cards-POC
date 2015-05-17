package edu.uchicago.pkuprys.cards.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ImageView;

import edu.uchicago.pkuprys.cards.R;

public class PlayCardDialog extends DialogFragment {
    private Drawable mDrawable;
    private DialogInterface.OnClickListener mOnClickListener;

    public PlayCardDialog(Drawable drawable, DialogInterface.OnClickListener listener) {
        mDrawable = drawable;
        mOnClickListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ImageView card = new ImageView(getActivity());
        card.setImageDrawable(mDrawable);
        return builder.setMessage(getActivity().getString(R.string.play_card_message))
                .setPositiveButton(android.R.string.ok, mOnClickListener)
                .setNegativeButton(android.R.string.cancel, mOnClickListener)
                .setView(card).create();
    }
}
