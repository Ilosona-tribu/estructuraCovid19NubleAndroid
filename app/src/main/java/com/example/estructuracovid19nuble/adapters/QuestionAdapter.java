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

import com.amazonaws.amplify.generated.graphql.ListQuestionnairesQuery;
import com.example.estructuracovid19nuble.R;
import com.example.estructuracovid19nuble.utils.Reply;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import kotlin.Triple;

public class QuestionAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ListQuestionnairesQuery.Item1> questions;
    private ArrayList<Reply> replies;
    private ArrayList<String> encoded_rep;

    public QuestionAdapter(Context context, ArrayList<ListQuestionnairesQuery.Item1> questions) {
        this.context = context;
        this.questions = questions;
        this.replies = new ArrayList<>(questions.size());
        for (ListQuestionnairesQuery.Item1 item : this.questions) {
            this.replies.add(new Reply(item));
        }
//        this.encoded_rep = new ArrayList<>(questions.size());
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.cell_question, collection, false);
        TextView txtQuestion = layout.findViewById(R.id.question);

        ListQuestionnairesQuery.Item1 item = questions.get(position);
        txtQuestion.setText(item.text_question());
        switch (item.question_type()) {
            case BOOL:
                final RadioGroup rep_bool = layout.findViewById(R.id.rep_radio);
                rep_bool.setTag(position);
                rep_bool.setVisibility(View.VISIBLE);
                //TODO add radio change.
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
                final TextInputEditText rep_txt = layout.findViewById(R.id.rep_txt);
                rep_txt.setTag(position);
                rep_txt.setVisibility(View.VISIBLE);
                //TODO add text change.
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
                final LinearLayout rep_checkbox = layout.findViewById(R.id.rep_checkbox);
                rep_checkbox.setVisibility(View.VISIBLE);
                rep_checkbox.setTag(position);
                for (int i = 0; i < item.list_options().size(); i++) {
                    final CheckBox checkBox = new CheckBox(context);
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

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return this.questions.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public ArrayList<String> getReps() {
        return encoded_rep;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }
}

//public class InteractiveArrayAdapter extends ArrayAdapter<Model> {
//
//    private final List<Model> list;
//    private final Activity context;
//
//    public InteractiveArrayAdapter(Activity context, List<Model> list) {
//        super(context, R.layout.rowbuttonlayout, list);
//        this.context = context;
//        this.list = list;
//    }
//
//    static class ViewHolder {
//        protected TextView text;
//        protected CheckBox checkbox;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = null;
//        if (convertView == null) {
//            LayoutInflater inflator = context.getLayoutInflater();
//            view = inflator.inflate(R.layout.rowbuttonlayout, null);
//            final ViewHolder viewHolder = new ViewHolder();
//            viewHolder.text = (TextView) view.findViewById(R.id.label);
//            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
//            viewHolder.checkbox
//                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView,
//                                                     boolean isChecked) {
//                            Model element = (Model) viewHolder.checkbox
//                                    .getTag();
//                            element.setSelected(buttonView.isChecked());
//
//                        }
//                    });
//            view.setTag(viewHolder);
//            viewHolder.checkbox.setTag(list.get(position));
//        } else {
//            view = convertView;
//            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
//        }
//        ViewHolder holder = (ViewHolder) view.getTag();
//        holder.text.setText(list.get(position).getName());
//        holder.checkbox.setChecked(list.get(position).isSelected());
//        return view;
//    }
//}