package com.sedlo.mapp1.Tab;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.sedlo.mapp1.Activity.AbsActivity;
import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Team;
import com.sedlo.mapp1.SedloQL.Train;
import com.sedlo.mapp1.UserConnection.PrefManager;

import java.util.ArrayList;


public class teamAdapter extends BaseAdapter
{
    private Context context;

    public static ArrayList<Team> editModelArrayList;

    public teamAdapter(Context context, ArrayList<Team> editModelArrayList) {
        this.context = context;

        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return editModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return editModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.tab_team, null, true);

            holder.editText = (EditText) convertView.findViewById(R.id.edittext_team_date);
            holder.edit = (Button) convertView.findViewById(R.id.btn_team_edit);
            holder.delete = (Button) convertView.findViewById(R.id.btn_team_delete);


            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.editText.setText(editModelArrayList.get(position).getName());

        holder.editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                editModelArrayList.get(position).setName(holder.editText.getText().toString());

              PrefManager prefManager= PrefManager.getInstance(context);

             prefManager.getMydatabase().execSQL(
                         "UPDATE "+PrefManager.getDbNameTeams()+" SET name = \""+editModelArrayList.get(position).getName()+"\" WHERE id = "+editModelArrayList.get(position).getID()+"");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.editText.setEnabled(!holder.editText.isEnabled());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                PrefManager prefManager= PrefManager.getInstance(context);

                prefManager.getMydatabase().execSQL(
                        "DELETE FROM "+PrefManager.getDbNameTeams()+" WHERE id = "+editModelArrayList.get(position).getID()+"");

                prefManager.getMydatabase().execSQL(
                        "DELETE FROM "+PrefManager.getDbNameTrainings()+" WHERE team = "+editModelArrayList.get(position).getID()+"");


                prefManager.getTeamsActivity().refreshList();
            }
        });





        return convertView;
    }

    private class ViewHolder {

        protected EditText editText;
        protected Button edit;
        protected Button delete;



    }
}
