package com.sedlo.mapp1.Tab;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Player;
import com.sedlo.mapp1.UserConnection.PrefManager;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class playerAdapter extends BaseAdapter
{
    private Context context;

    public static ArrayList<Player> editModelArrayList;

    public playerAdapter(Context context, ArrayList<Player> editModelArrayList) {
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
            convertView = inflater.inflate(R.layout.tab_player, null, true);

            holder.editText = (EditText) convertView.findViewById(R.id.edittext_player_name);
            holder.edit = (Button) convertView.findViewById(R.id.btn_player_edit);
            holder.delete = (Button) convertView.findViewById(R.id.btn_player_delete);
            holder.info = (Button) convertView.findViewById(R.id.btn_player_info);


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
                         "UPDATE "+PrefManager.getDbNamePlayers()+" SET name = \""+editModelArrayList.get(position).getName()+"\" WHERE id = "+editModelArrayList.get(position).getID()+"");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("aaa");
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
                        "DELETE FROM "+PrefManager.getDbNamePlayers()+" WHERE id = "+editModelArrayList.get(position).getID()+"");

                prefManager.getPlayersListActivity().refreshList();
            }
        });


        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ArrayList<String> dates=new ArrayList<>();
                ArrayList<String> reason=new ArrayList<>();
                ArrayList<String> datesName=new ArrayList<>();

                PrefManager prefManager= PrefManager.getInstance(context);

                Cursor cursor =   prefManager.getMydatabase().rawQuery(" SELECT * FROM "+prefManager.getDbNameAbs()+" WHERE p_id = "+editModelArrayList.get(position).getID() ,null);

                cursor.moveToFirst();


                if(cursor != null && cursor.moveToFirst())
                {
                    do {

                        dates.add(cursor.getString(2)+" ");
                        reason.add(cursor.getString(3)+" ");

                    }
                    while (cursor.moveToNext());

                }


                for (String date:dates)
                {

                    Cursor cursor2 =   prefManager.getMydatabase().rawQuery("Select * from "+prefManager.getDbNameTrainings()+" WHERE id = " + date,null);

                    cursor2.moveToFirst();

                    if(cursor2 != null && cursor2.moveToFirst())
                    {
                        do {

                            datesName.add(cursor2.getString(1));

                        }
                        while (cursor2.moveToNext());

                    }



                }

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                arrayAdapter.add("Total missed: "+datesName.size());


                for (int i= 0;i<datesName.size();i++)
                {

                    arrayAdapter.add(datesName.get(i)+"\n  -->"+
                            reason.get(i));
                }




                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                        builderInner.setMessage(strName);
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
            }


            }
        );


        return convertView;
    }

    private class ViewHolder {

        protected EditText editText;
        protected Button edit;
        protected Button delete;
        protected Button info;

    }
}
