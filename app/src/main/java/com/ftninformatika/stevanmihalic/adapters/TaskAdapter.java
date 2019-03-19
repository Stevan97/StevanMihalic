package com.ftninformatika.stevanmihalic.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ftninformatika.stevanmihalic.R;
import com.ftninformatika.stevanmihalic.db.model.Grupa;
import com.ftninformatika.stevanmihalic.db.model.Task;

import java.util.List;

public class TaskAdapter extends BaseAdapter {

    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    private Spannable message1 = null;
    private Spannable message2 = null;

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.task_adapter, null);

        TextView naziv = convertView.findViewById(R.id.task_adapter_naziv);
        message1 = new SpannableString("Naziv Zadatka: ");
        message2 = new SpannableString(taskList.get(position).getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView zanr = convertView.findViewById(R.id.task_adapter_vreme_kreiranja);
        message1 = new SpannableString("Vreme Kreiranja: ");
        message2 = new SpannableString(taskList.get(position).getVremeKreiranja());
        spannableStyle();
        zanr.setText(message1);
        zanr.append(message2);

        TextView godina = convertView.findViewById(R.id.task_adapter_vreme_zavrsetka);
        message1 = new SpannableString("Vreme Zavrsetka: ");
        message2 = new SpannableString(taskList.get(position).getVremeZavrsetka());
        spannableStyle();
        godina.setText(message1);
        godina.append(message2);


        return convertView;
    }

    public void refreshList(List<Task> taskList1) {
        this.taskList.clear();
        this.taskList.addAll(taskList1);
        notifyDataSetChanged();
    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}

