package com.example.estructuracovid19nuble.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.airbnb.paris.Paris;
import com.amazonaws.amplify.generated.graphql.ListQuestionnairesQuery;
import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.utils.Reply;
import com.example.estructuracovid19nuble.utils.Settings;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Triple;
import type.QuestionType;

public class DiagnoseAdapter extends RecyclerView.Adapter<DiagnoseAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<ListQuestionnairesQuery.Item1> questions;
    private ArrayList<Reply> replies;
    private Integer continueId = 0;

    public DiagnoseAdapter(Context context, ArrayList<ListQuestionnairesQuery.Item1> questions) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.questions = questions;
        this.replies = new ArrayList<>(questions.size());
        for (ListQuestionnairesQuery.Item1 item : this.questions) {
            this.replies.add(new Reply(item));
        }

    }

    @NonNull
    @Override
    public DiagnoseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnoseAdapter.ViewHolder holder, int position) {
        ListQuestionnairesQuery.Item1 item = questions.get(position);
        holder.txtQuestion.setText(item.text_question());
        switch (item.question_type()) {
            case BOOL:
                final RadioGroup rep_bool = holder.rep_bool;
                rep_bool.setTag(position);
                rep_bool.setVisibility(View.VISIBLE);
                rep_bool.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.e("Radio changed:", group.toString() + " " + checkedId + ": tag " + rep_bool.getTag());
                        boolean result = true;
                        switch (checkedId) {
                            case R.id.first:
                                result = true;
                                break;
                            case R.id.second:
                                result = false;
                                break;
                        }
                        replies.get((Integer) rep_bool.getTag()).update_rep_bool(result);
                    }
                });
                break;

            case TEXT:
                final TextInputEditText rep_txt = holder.rep_txt;
                rep_txt.setTag(position);
                rep_txt.setVisibility(View.VISIBLE);
                rep_txt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.e("rep_txt: ", s.toString() + " tag: " + rep_txt.getTag());
                        replies.get((Integer) rep_txt.getTag()).update_rep_txt(s.toString());
                    }
                });
                break;

            case OPTIONS:
                final LinearLayout rep_checkbox = holder.rep_checkbox;
                rep_checkbox.setVisibility(View.VISIBLE);
                rep_checkbox.setTag(position);
                for (int i = 0; i < item.list_options().size(); i++) {
                    final CheckBox checkBox = new CheckBox(context);
                    Paris.style(checkBox).apply(R.style.CheckBoxSurvey);
                    set_checkbox_attributes(checkBox);

                    String option = item.list_options().get(i);
                    checkBox.setText(option);
                    Log.e("tagging", position + " " + i + " " + option);
                    checkBox.setTag(new Triple<>(position, i, option));
                    rep_checkbox.addView(checkBox);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Triple<Integer, Integer, String> tag = (Triple<Integer, Integer, String>) checkBox.getTag();
                            Log.e("listening: ", tag.toString());
                            if (isChecked) {
                                if (is_non_above_option(tag.getThird())) {
                                    //Remove "Others" if selected
                                    //1. Uncheck in replies
                                    replies.get(tag.getFirst()).update_rep_options_clean();
                                    //2. Uncheck in UI
                                    uncheckOther(rep_checkbox);
                                } else {
                                    //Remove "Non Above" if selected
                                    //1. Remove in replies
                                    replies.get(tag.getFirst()).uncheck_nonabove();
                                    //2. Remove in UI
                                    uncheckNonabove(rep_checkbox);
                                }
                                //Check the option
                                replies.get(tag.getFirst()).update_rep_options(tag.getSecond(), tag.getThird());
                            } else {
                                //Uncheck the option
                                replies.get(tag.getFirst()).update_rep_options(tag.getSecond(), "");
                            }
                            Log.e("replies", replies.get(tag.getFirst()).toString());
                        }

                    });

                }
                break;
        }

    }

    private void uncheckOther(LinearLayout rep_checkbox) {
        int count = rep_checkbox.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckBox checkBox = (CheckBox) rep_checkbox.getChildAt(i);
            if (!is_non_above_option(checkBox.getText().toString())) {
                checkBox.setChecked(false);
            }
        }
    }

    private void uncheckNonabove(LinearLayout rep_checkbox) {
        int count = rep_checkbox.getChildCount();
        for (int i = 0; i < count; i++) {
            CheckBox checkBox = (CheckBox) rep_checkbox.getChildAt(i);
            if (is_non_above_option(checkBox.getText().toString())) {
                checkBox.setChecked(false);
            }
        }
    }

    public Pair<Boolean, Integer> is_survey_continue(int curPage) {
        Integer resultId = checkLogic(this.replies, curPage);
        Log.e("Diagnose: ", resultId.intValue() + " " + continueId.intValue());
        if (resultId.intValue() == continueId.intValue()) {
            Integer normalId = R.string.text_result_normal;
            return new Pair<>(Boolean.TRUE, normalId);
        } else {
            return new Pair<>(Boolean.FALSE, resultId);
        }

    }

    private boolean is_three_symptoms_in_q1(ArrayList<Reply> replies) {
        //fever, dry cough and shortness of breath check
        int page = 0; //stands for question 1 in the survey
        String string = replies.get(page).get_rep_options().toString().toLowerCase();
        return string.contains(Settings.LOGIC_BREATH_SHORT) || string.contains(Settings.LOGIC_COUGH)
                || string.contains(Settings.LOGIC_FEVER);
    }

    private boolean is_non_above_option(String text) {
        return text.toLowerCase().contains(Settings.LOGIC_NONE_ABOVE);
    }

    private Integer checkLogic(ArrayList<Reply> replies, int curPage) {
        //Validate question type and question index
        if (curPage >= replies.size() || replies.get(curPage).question_type() != QuestionType.OPTIONS) {
            return continueId;
        }

        //Check for each question
        ArrayList<String> options = replies.get(curPage).get_rep_options();
        //Not select any options OR Select non above option.
        boolean non_above = replies.get(curPage).isDefault() || is_non_above_option(options.toString());
        Log.e("Logic" + curPage, options.toString());
        switch (curPage) {
            case 0:
                //Q1. If answer “none of above” -> first result.
                if (non_above) {
                    return R.string.text_result_normal;
                    //return "Q1: First Screen Result";
                }

                //Q1. If any symptom -> move to Q2.
                for (int i = 0; i < options.size(); i++) {
                    String op = options.get(i).toLowerCase();
                    Log.e("Q " + curPage, "selected " + op);
                }
                return continueId;
            //return "Move to Q2";

            case 1:
                //Q2. "None of above" -> Q3.
                if (non_above) {
                    return continueId;
                    //return "Moving to Q3";
                }

                //Q2. If answer any serious symptoms-> result call emergency.
                for (int i = 0; i < options.size(); i++) {
                    String op = options.get(i).toLowerCase();
                    if (!op.isEmpty()) {
                        return R.string.text_result_urgency;
                        //return "Q2: Result call emergency";
                    }
                }

            case 2:
                // - unlikely to be infected (but have to be careful if the symptoms worsen) with and without pre-existing
                // - very likely to be infected with and without pre-existing.
                // The result with and without preexistences only varies in the text saying that they have to be taken care of more
                if (is_three_symptoms_in_q1(replies)) {
                    //attention
                    if (non_above) {
                        //
                        return R.string.text_result_attention;
                    } else {
                        //++
                        return R.string.text_result_attention_plus;
                    }
                } else {
                    //normal <suspect < attention < urgency
                    //suspect
                    if (non_above) {
                        //
                        return R.string.text_result_suspect;
                    } else {
                        //++
                        return R.string.text_result_suspect_plus;
                    }
                }

            default:
                break;
        }

        //Check local logic
        //1. it’s only three questions at most

        //Q1. There are three symptoms that are delicate in the first question, fever, dry cough and shortness of breath:
        //any of these are answered, the final result may vary, from unlikely to very likely infected, weighted equally.

        //Q3. Four possible outcomes:

        return continueId;
    }

    private void set_checkbox_attributes(CheckBox checkBox) {
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 15, 0, 15);
//        checkBox.setLayoutParams(lp);
        checkBox.setElevation(2);
        checkBox.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        checkBox.setPadding(20, 20, 20, 20);
//        checkBox.setBackground(context.getDrawable(R.drawable.checkbox_bg));
//        checkBox.setButtonDrawable(context.getDrawable(R.drawable.checkbox_btn));
        checkBox.setButtonDrawable(null);
        checkBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkbox_btn, 0);
    }

    public void setItems(ArrayList<ListQuestionnairesQuery.Item1> items) {
        questions = items;
    }

    @Override
    public int getItemCount() {
        return this.questions.size();
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion;
        RadioGroup rep_bool;
        TextInputEditText rep_txt;
        LinearLayout rep_checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.question);
            rep_bool = itemView.findViewById(R.id.rep_radio);
            rep_txt = itemView.findViewById(R.id.rep_txt);
            rep_checkbox = itemView.findViewById(R.id.rep_checkbox);
        }
    }
}
