package com.example.estructuracovid19nuble.utils;

import com.amazonaws.amplify.generated.graphql.ListQuestionnairesQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import type.QuestionType;

public class Reply extends ListQuestionnairesQuery.Item1 {
    private String rep_txt;
    private boolean rep_bool;
    private ArrayList<String> rep_options;

    public Reply(ListQuestionnairesQuery.Item1 item) {
        super(item.__typename(), item.id(), item.text_question(), item.question_type(), item.list_options(), item._version(), item._deleted(), item._lastChangedAt());
        rep_txt = "";
        rep_bool = false;
        if(item.question_type() == QuestionType.OPTIONS){
            rep_options = new ArrayList<>(Collections.nCopies(item.list_options().size(), ""));
        }
    }

    public Reply(@Nonnull String __typename, @Nonnull String id, @Nonnull String text_question, @Nonnull QuestionType question_type, @Nullable List<String> list_options, int _version, @Nullable Boolean _deleted, long _lastChangedAt) {
        super(__typename, id, text_question, question_type, list_options, _version, _deleted, _lastChangedAt);
    }

    public void update_rep_txt(String text) {
        rep_txt = text;
    }

    public void update_rep_bool(boolean is_checked) {
        rep_bool = is_checked;
    }

    public void update_rep_options(int position, String text) {
        rep_options.set(position, text);
    }

    public String toString() {
        switch (this.question_type()) {
            case TEXT:
                return rep_txt;
            case BOOL:
                return  rep_bool ? "Si": "Non";
//                return Boolean.toString(rep_bool);
            case OPTIONS:
                return rep_options.toString();
        }
        return "";
    }
}
