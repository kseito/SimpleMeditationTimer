package kzt.com.simplemeditationtimer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

/**
 * Created by kazuto-seito on 2016/11/07.
 */

public class NumberPickerDialog extends AppCompatDialogFragment {

    public interface OnClickListener {
        void clickOk(int minute);
        void clickCancel();
    }
    private OnClickListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_numberpicker, null, false);
        listener = (OnClickListener) getActivity();

        final MaterialNumberPicker numberPicker = (MaterialNumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setValue(5);

        return new AlertDialog.Builder(getActivity())
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.clickOk(numberPicker.getValue());
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.clickCancel();
                    }
                })
                .setView(view)
                .create();
    }
}
