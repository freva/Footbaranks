package com.limon.footbaranks;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.Window;
import android.widget.TableRow;
import android.widget.TextView;

import com.limon.footbaranks.containers.MatchItem;

public class MatchDetailsDialog extends Dialog {
    private Activity parent;
    private TableRow rows[] = new TableRow[7];
    private TextView fields[][] = new TextView[7][7];

    public MatchDetailsDialog(Activity activity) {
        super(activity);

        parent = activity;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.matches_details_dialog);

        rows[0] = (TableRow) findViewById(R.id.matches_details_dialog_row0);
        rows[1] = (TableRow) findViewById(R.id.matches_details_dialog_row1);
        rows[2] = (TableRow) findViewById(R.id.matches_details_dialog_row2);
        rows[3] = (TableRow) findViewById(R.id.matches_details_dialog_row3);
        rows[4] = (TableRow) findViewById(R.id.matches_details_dialog_row4);
        rows[5] = (TableRow) findViewById(R.id.matches_details_dialog_row5);
        rows[6] = (TableRow) findViewById(R.id.matches_details_dialog_row6);

        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        textViewParams.topMargin = (int) (2 * activity.getResources().getDisplayMetrics().density);
        textViewParams.rightMargin = (int) (2 * activity.getResources().getDisplayMetrics().density);

        for(int i=0; i<7; i++) {
            for(int j=0; i+j<7; j++) {
                fields[i][j] = new TextView(activity);
                fields[i][j].setLayoutParams(textViewParams);
                fields[i][j].setBackgroundColor(Color.WHITE);
                rows[i].addView(fields[i][j]);
            }
        }
    }

    public void displayMatchDetails(MatchItem matchItem) {
        for(int i=0; i<7; i++) {
            for (int j = 0; i + j < 7; j++) {
                fields[i][j].setText(String.format("%.1f", matchItem.getScoreProbability(i, j) * 100));
            }
        }

        show();
    }
}
