package com.example.matchup;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private Button[] buttons = new Button[12];
    private int[] images = {
            R.drawable.flower1, R.drawable.flower1,
            R.drawable.flower2, R.drawable.flower2,
            R.drawable.flower3, R.drawable.flower3,
            R.drawable.flower4, R.drawable.flower4,
            R.drawable.flower5, R.drawable.flower5,
            R.drawable.flower6, R.drawable.flower6
    };

    private ArrayList<Integer> buttonIds = new ArrayList<>();
    private Button firstSelectedButton;
    private int firstSelectedIndex;
    private boolean isProcessing;
    private int matchesFound;
    private boolean[] matched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeGame();
    }

    private void initializeGame() {
        matched = new boolean[buttons.length];

        for (int i = 0; i < 12; i++) {
            String buttonID = "btn" + (i + 1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
            buttons[i].setBackgroundResource(R.drawable.flower);
            buttonIds.add(buttons[i].getId());

            final int index = i;
            buttons[i].setOnClickListener(v -> onButtonClicked(index));
        }

        shuffleCards();
    }

    private void shuffleCards() {
        Collections.shuffle(buttonIds);
    }

    private void onButtonClicked(int index) {
        if (isProcessing || matched[index]) {
            return;
        }

        buttons[index].setBackgroundResource(images[index]);

        if (firstSelectedButton == null) {
            firstSelectedButton = buttons[index];
            firstSelectedIndex = index;
        } else {
            isProcessing = true;
            new Handler().postDelayed(() -> checkForMatch(index), 1000);
        }
    }

    private void checkForMatch(int secondSelectedIndex) {
        if (images[firstSelectedIndex] == images[secondSelectedIndex]) {
            matchesFound++;
            matched[firstSelectedIndex] = true;
            matched[secondSelectedIndex] = true;
            if (matchesFound == images.length / 2) {
                showWinMessage();
            }
        } else {
            firstSelectedButton.setBackgroundResource(R.drawable.flower);
            buttons[secondSelectedIndex].setBackgroundResource(R.drawable.flower);
        }

        firstSelectedButton = null;
        isProcessing = false;
    }

    private void showWinMessage() {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You have found all the pairs!")
                .setPositiveButton("Play Again", (dialog, which) -> {
                    resetGame();
                })
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    private void resetGame() {
        matchesFound = 0;
        shuffleCards();
        for (int i = 0; i < buttons.length; i++) {
            matched[i] = false;
            buttons[i].setBackgroundResource(R.drawable.flower);
        }
    }
}