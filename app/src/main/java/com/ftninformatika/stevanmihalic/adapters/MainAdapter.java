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

public class MainAdapter extends BaseAdapter {
    private Context context;
    private List<Grupa> grupaList;

    public MainAdapter(Context context, List<Grupa> grupaList) {
        this.context = context;
        this.grupaList = grupaList;
    }

    private Spannable message1 = null;
    private Spannable message2 = null;

    @Override
    public int getCount() {
        return grupaList.size();
    }

    @Override
    public Object getItem(int position) {
        return grupaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "ViewHolder", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.main_adapter, null);

        TextView naziv = convertView.findViewById(R.id.main_adapter_naziv);
        message1 = new SpannableString("Naziv Grupe: ");
        message2 = new SpannableString(grupaList.get(position).getNaziv());
        spannableStyle();
        naziv.setText(message1);
        naziv.append(message2);

        TextView zanr = convertView.findViewById(R.id.main_adapter_vreme_kreiranja);
        message1 = new SpannableString("Vreme Kreiranja: ");
        message2 = new SpannableString(grupaList.get(position).getVremeKreiranjaGrupe());
        spannableStyle();
        zanr.setText(message1);
        zanr.append(message2);


        return convertView;
    }

    public void refreshList(List<Grupa> grupaList) {
        this.grupaList.clear();
        this.grupaList.addAll(grupaList);
        notifyDataSetChanged();
    }

    private void spannableStyle() {
        message1.setSpan(new StyleSpan(Typeface.BOLD), 0, message1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        message2.setSpan(new ForegroundColorSpan(Color.RED), 0, message2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
