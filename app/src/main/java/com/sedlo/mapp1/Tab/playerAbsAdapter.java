package com.sedlo.mapp1.Tab;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Player;
import com.sedlo.mapp1.UserConnection.PrefManager;

import java.util.ArrayList;

public class playerAbsAdapter extends BaseAdapter
{
    private Context context;

    public static ArrayList<Player> editModelArrayList;

    private String date_id;

    public playerAbsAdapter(Context context, ArrayList<Player> editModelArrayList,String date_id) {
        this.context = context;

        this.date_id =date_id;

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
            convertView = inflater.inflate(R.layout.tab_player_abs, null, true);

            holder.editText =  convertView.findViewById(R.id.edittext_player_name_abs);
            holder.edit =  convertView.findViewById(R.id.switch_abs);

            holder.edit.setChecked(editModelArrayList.get(position).aBoolean);



            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.editText.setText(editModelArrayList.get(position).getName());


        final GestureDetector gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {


               if (holder.edit.isChecked())
               {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                Cursor cursor2 = PrefManager.getInstance(context).getMydatabase().rawQuery(
                        "Select * from "+ PrefManager.getInstance(context).getDbNameAbs()+" WHERE d_id = "+ date_id+" AND p_id = "+editModelArrayList.get(position).getID()+";",null);

                cursor2.moveToFirst();

                alertDialogBuilder.setMessage(cursor2.getString(3));

                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();
               }


                return true;
            }
        });

        holder.editText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        holder.edit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                editModelArrayList.get(position).aBoolean=holder.edit.isChecked();

                PrefManager prefManager= PrefManager.getInstance(context);

                if(!holder.edit.isChecked()) {
                    prefManager.getMydatabase().execSQL(
                            "DELETE FROM " + PrefManager.getDbNameAbs() + " WHERE p_id = " + editModelArrayList.get(position).getID() + " AND d_id = " + date_id+";");



                }else
                    {


                        final String[] m_Text = new String[1];

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(context.getString(R.string.reason));


                        final EditText input = new EditText(context);

                        input.setInputType(InputType.TYPE_CLASS_TEXT);

                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text[0] = input.getText().toString();

                                PrefManager.getInstance(context).getMydatabase().execSQL(
                                        " UPDATE "+  PrefManager.getInstance(context).getDbNameAbs()+" SET reason = \'"+input.getText().toString()+"\' WHERE p_id = "+editModelArrayList.get(position).getID()+" AND d_id = "+date_id+" ;");

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();

                        prefManager.getMydatabase().execSQL(
                                "INSERT INTO "+prefManager.getDbNameAbs()+" VALUES(NULL,"+editModelArrayList.get(position).getID()+","+date_id+",NULL);");


                }

            }
        });




        return convertView;
    }

    private class ViewHolder {

        protected TextView editText;
        protected Switch edit;


    }
}
