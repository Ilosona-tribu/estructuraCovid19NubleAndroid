package com.example.estructuracovid19nuble.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Triple;

public class QuestionViewpager2Adapter extends RecyclerView.Adapter<QuestionViewpager2Adapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<ListQuestionnairesQuery.Item1> questions;
    private ArrayList<Reply> replies;

    public QuestionViewpager2Adapter(Context context, ArrayList<ListQuestionnairesQuery.Item1> questions) {
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
    public QuestionViewpager2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cell_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewpager2Adapter.ViewHolder holder, int position) {
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
                            replies.get(tag.getFirst()).update_rep_options(tag.getSecond(), tag.getThird());
                            Log.e("replies", replies.get(tag.getFirst()).toString());
                        }
                    });

                }
                break;
        }

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
