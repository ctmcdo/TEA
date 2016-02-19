package com.tea.cmcdona2.casper.ParticularEnt;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tea.cmcdona2.casper.R;


public class ParticularEntFrag extends Fragment {

    private static TextView eventName;

    private static TextView eventDescription;

    EventDescriptionListener activityCommander;

    public interface EventDescriptionListener {

        void GMAP();

        void Calendar();
    }

    @Override

    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {

            activityCommander = (EventDescriptionListener) activity;

        } catch (ClassCastException e)

        {

            throw new ClassCastException(activity.toString());

        }

    }

    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.particular_ent_activity, container, false);

        eventName = (TextView) view.findViewById(R.id.eventName);


        eventDescription = (TextView) view.findViewById(R.id.eventDescription);

        final ImageButton getDirections = (ImageButton) view.findViewById(R.id.getDirections);

        final ImageButton going = (ImageButton) view.findViewById(R.id.going);
        going.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        goingCLicked(v);
                    }
                }
        );
        getDirections.setOnClickListener(

                new View.OnClickListener() {

                    public void onClick(View v)

                    {

                        buttonCLicked(v);

                    }

                }

        );

        return view;

    }

    public void buttonCLicked(View view)

    {

        activityCommander.GMAP();

    }

    public void goingCLicked(View view) {
        activityCommander.Calendar();
    }

}
