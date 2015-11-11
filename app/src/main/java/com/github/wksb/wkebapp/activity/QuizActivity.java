package com.github.wksb.wkebapp.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import com.github.wksb.wkebapp.R;
import com.github.wksb.wkebapp.contentprovider.WeltkulturerbeContentProvider;
import com.github.wksb.wkebapp.database.QuizzesTable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 *
 */
public class QuizActivity extends AppCompatActivity {

    // Current Quiz used in this Activity
    private Quiz mCurrentQuiz;

    // Definition of the different Views represented inside the Layout
    private TextView mTv_quiz_station;
    private TextView mTv_quiz_question;
    private Button mBtn_quiz_more;

    // Definition of the Buttons of the Quiz. These Buttons are not represented inside the Layout
    private Button mBtn_quiz_solution;
    private Button mBtn_quiz_wrongAnswer1;
    private Button mBtn_quiz_wrongAnswer2;
    private Button mBtn_quiz_wrongAnswer3;


    // Definition of the Tags used in Intents send to this Activity
    public static final String TAG_PACKAGE = QuizActivity.class.getPackage().getName();
    /** This TAG tags the ID of the Quiz, which is to be loaded, within an Intent send to this Activity*/
    public static final String TAG_QUIZ_ID = TAG_PACKAGE + "quiz_id";
    /** FLAG for the Quiz ID within an Intent send to this Activity, tagged with the TAG {@link QuizActivity#TAG_QUIZ_ID}, which indicates a Quiz with this ID doesn't exist*/
    public static final int FLAG_QUIZ_ID_ERROR = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialise the Views
        mTv_quiz_station = (TextView) findViewById(R.id.tv_quiz_station);
        mTv_quiz_question = (TextView) findViewById(R.id.tv_quiz_question);
        mBtn_quiz_more = (Button) findViewById(R.id.btn_quiz_more);

        // Set up the ActionBar
        setUpActionBar();

        // Set up the Quiz
        setUpQuiz();
    }

    private void setUpActionBar() {
        if (getSupportActionBar() == null)return;

        // Use Custom ActionBar Layout and Display BackButton
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_HOME_AS_UP);

        // Set Custom ActionBar Layout
        getSupportActionBar().setCustomView(R.layout.actionbar_title);
    }

    /**
     * This Method sets up the Quiz Activity. The Quiz depends on the committed QuizID in the starting Intent
     */
    private void setUpQuiz(){
        // Load the current Quiz from the SQL Database
        mCurrentQuiz = getQuizByID(getQuizIDFromIntent());

        // Set the Text of the Station to the location of the current Quiz
        mTv_quiz_station.setText(mCurrentQuiz.getLocation());

        //Set the Text of the Question TextView to the question of the current Quiz
        mTv_quiz_question.setText(mCurrentQuiz.getQuestion());

        //Get the List of all wrong answers to the current Quiz and shuffle them
        List<String> wrongAnswers = mCurrentQuiz.getWrongAnswers();
        Collections.shuffle(wrongAnswers);

        // Get the solution of the current Quiz
        String solution = mCurrentQuiz.getSolution();

        // Shuffel the Button IDs of the Quiz Buttons
        List<Integer> btnIds = new ArrayList<>();
        btnIds.add(R.id.btn_quiz_answer1);
        btnIds.add(R.id.btn_quiz_answer2);
        btnIds.add(R.id.btn_quiz_answer3);
        btnIds.add(R.id.btn_quiz_answer4);
        Collections.shuffle(btnIds);

        // Assign the Quiz Buttons in the Layout to their representations in the Activity
        mBtn_quiz_solution = (Button) findViewById(btnIds.remove(0));
        mBtn_quiz_wrongAnswer1 = (Button) findViewById(btnIds.remove(0));
        mBtn_quiz_wrongAnswer2 = (Button) findViewById(btnIds.remove(0));
        mBtn_quiz_wrongAnswer3 = (Button) findViewById(btnIds.remove(0));

        // Set the Texts of the Buttons to the different answers to the current Quiz
        mBtn_quiz_solution.setText(solution);
        mBtn_quiz_wrongAnswer1.setText(wrongAnswers.remove(0));
        mBtn_quiz_wrongAnswer2.setText(wrongAnswers.remove(0));
        mBtn_quiz_wrongAnswer3.setText(wrongAnswers.remove(0));
    }

    /**
     *
     * @return returns IntExtra with the Quiz ID from the starting Intent
     */
    private int getQuizIDFromIntent(){
        return getIntent().getIntExtra(TAG_QUIZ_ID, FLAG_QUIZ_ID_ERROR);
    }

    /**
     * This Method queries the SQLite Database for quizzes with the committed ID and returns it as a {@link com.github.wksb.wkebapp.activity.QuizActivity.Quiz}
     * @param quizID The QuizID of the quiz to load
     * @return {@link com.github.wksb.wkebapp.activity.QuizActivity.Quiz} with the first found query result
     */
    private Quiz getQuizByID(int quizID){
        String[] projection = {QuizzesTable.COLUMN_QUIZ_ID, QuizzesTable.COLUMN_LOCATION, QuizzesTable.COLUMN_QUESTION, QuizzesTable.COLUMN_SOLUTION, QuizzesTable.COLUMN_WRONG_ANSWER_1,
                                QuizzesTable.COLUMN_WRONG_ANSWER_2, QuizzesTable.COLUMN_WRONG_ANSWER_3, QuizzesTable.COLUMN_INFO_ID};
        String selection = QuizzesTable.COLUMN_QUIZ_ID + "=?";
        String[] selectionArgs = {Integer.toString(quizID)};
        String sortOrder = null;
        Cursor cursor = getContentResolver().query(WeltkulturerbeContentProvider.URI_TABLE_QUIZZES, projection, selection, selectionArgs, sortOrder);

        if (cursor.isBeforeFirst()) cursor.moveToNext();
        String location = cursor.getString(cursor.getColumnIndex(QuizzesTable.COLUMN_LOCATION));
        String question = cursor.getString(cursor.getColumnIndex(QuizzesTable.COLUMN_QUESTION));
        String solution = cursor.getString(cursor.getColumnIndex(QuizzesTable.COLUMN_SOLUTION));
        String wrongAnswer1 = cursor.getString(cursor.getColumnIndex(QuizzesTable.COLUMN_WRONG_ANSWER_1));
        String wrongAnswer2 = cursor.getString(cursor.getColumnIndex(QuizzesTable.COLUMN_WRONG_ANSWER_2));
        String wrongAnswer3 = cursor.getString(cursor.getColumnIndex(QuizzesTable.COLUMN_WRONG_ANSWER_3));
        int infoID = cursor.getInt(cursor.getColumnIndex(QuizzesTable.COLUMN_INFO_ID));

        return new Quiz(quizID, location, question, solution, new String[]{wrongAnswer1, wrongAnswer2, wrongAnswer3}, infoID);
    }

    /**
     * Logic executed when the user entered the right answer in the current Quiz
     * @param answerEntered The {@link View} ({@link Button}) the user clicked
     */
    private void onRightAnswerEntered(View answerEntered) {
        mBtn_quiz_solution.setBackgroundColor(getResources().getColor(R.color.green));

        mBtn_quiz_solution.setClickable(false);
        mBtn_quiz_wrongAnswer1.setClickable(false);
        mBtn_quiz_wrongAnswer2.setClickable(false);
        mBtn_quiz_wrongAnswer3.setClickable(false);

        mBtn_quiz_more.setClickable(true);
        mBtn_quiz_more.setVisibility(View.VISIBLE);

        if (mCurrentQuiz.getQuizId() == getSharedPreferences("TOUR", MODE_PRIVATE).getInt("CURRENT_QUIZ_ID", -1)) {
            getSharedPreferences("TOUR", MODE_PRIVATE).edit().putInt("PROGRESS", getSharedPreferences("TOUR", MODE_PRIVATE).getInt("PROGRESS", 1)+1).commit();
        }
    }

    /**
     * Logic executed when the user entered the wrong answer in the current Quiz
     * @param answerEntered The {@link View} ({@link Button}) the user clicked
     */
    private void onWrongAnswerEntered(View answerEntered) {
        mBtn_quiz_solution.setBackgroundColor(getResources().getColor(R.color.green));
        answerEntered.setBackgroundColor(getResources().getColor(R.color.red));

        mBtn_quiz_solution.setClickable(false);
        mBtn_quiz_wrongAnswer1.setClickable(false);
        mBtn_quiz_wrongAnswer2.setClickable(false);
        mBtn_quiz_wrongAnswer3.setClickable(false);

        mBtn_quiz_more.setClickable(true);
        mBtn_quiz_more.setVisibility(View.VISIBLE);

        if (mCurrentQuiz.getQuizId() == getSharedPreferences("TOUR", MODE_PRIVATE).getInt("CURRENT_QUIZ_ID", -1)) {
            getSharedPreferences("TOUR", MODE_PRIVATE).edit().putInt("PROGRESS", getSharedPreferences("TOUR", MODE_PRIVATE).getInt("PROGRESS", 1)+1).commit();
        }
    }

    // TODO Documentation
    public void onBtnClickAnswer(View view)
    {
        if(view.getId() == mBtn_quiz_solution.getId())
        {
            onRightAnswerEntered(view);
        }
        else
        {
            onWrongAnswerEntered(view);
        }
    }

    public void onBtnClickMore(View view)
    {
        Intent i = new Intent(this, InformationActivity.class);
        i.putExtra(InformationActivity.TAG_INFORMATION_ID, mCurrentQuiz.getInfoID());
        startActivity(i);
    }

    public void onBtnClickTipp(View view)
    {
        //Popup mit Tipp
    }

    /**
     * This class represents a Quiz with one question and four different answers. Only one answer is the solution
     */
    private class Quiz {

        private int quizID;
        private final String location;
        private final String question;
        private final String solution;
        private final int infoID;
        private final List<String> wrongAnswers;

        public Quiz(int quizID, String location, String question, String solution, String[] wrong_answers, int infoID){
            this.quizID = quizID;
            this.location = location;
            this.question = question;
            this.solution = solution;
            this.infoID = infoID;
            this.wrongAnswers = new ArrayList<>(Arrays.asList(wrong_answers));
        }

        public int getQuizId(){
            return this.quizID;
        }

        public String getLocation() {
            return this.location;
        }

        public String getQuestion(){
            return this.question;
        }

        public String getSolution(){
            return this.solution;
        }

        public int getInfoID() {
            return infoID;
        }

        public List<String> getWrongAnswers(){
            return this.wrongAnswers;
        }
    }
}