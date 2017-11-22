package com.devicenut.pixelnutctrl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.devicenut.pixelnutctrl.Main.appContext;
import static com.devicenut.pixelnutctrl.Main.curSegment;
import static com.devicenut.pixelnutctrl.Main.devPatternNames;
import static com.devicenut.pixelnutctrl.Main.listFavorites;
import static com.devicenut.pixelnutctrl.Main.curFavorite;
import static com.devicenut.pixelnutctrl.Main.numSegments;
import static com.devicenut.pixelnutctrl.Main.segPatterns;

public class FragFavs extends Fragment
{
    private static final String LOGNAME = "Favorites";

    private static LinearLayout view_Favs;
    private static ScrollView helpPage;
    private static TextView helpText;

    private final int[] idsButton =
            {
                    R.id.button_Pattern1,
                    R.id.button_Pattern2,
                    R.id.button_Pattern3,
                    R.id.button_Pattern4,
                    R.id.button_Pattern5,
                    R.id.button_Pattern6,
            };
    private static Button[] objsButton;

    interface FavoriteSelectInterface
    {
        void onFavoriteSelect(int favnum);
    }
    private FavoriteSelectInterface mListener;

    public FragFavs() {}
    public static FragFavs newInstance() { return new FragFavs(); }

    @Override public void onCreate(Bundle savedInstanceState)
    {
        Log.d(LOGNAME, ">>onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d(LOGNAME, ">>onCreateView");

        View v = inflater.inflate(R.layout.fragment_favs, container, false);

        view_Favs   = (LinearLayout) v.findViewById(R.id.ll_Favorites);
        helpPage    = (ScrollView)   v.findViewById(R.id.ll_HelpPage_Favs);
        helpText    = (TextView)     v.findViewById(R.id.view_HelpText_Favs);

        objsButton = new Button[idsButton.length];
        for (int i = 0; i < idsButton.length; ++i)
        {
            Log.w(LOGNAME, "Setting favorite: " + listFavorites[i].getPatternName());
            Button b = (Button)v.findViewById(idsButton[i]);
            b.setText(listFavorites[i].getPatternName());
            b.setOnClickListener(mClicker);
            objsButton[i] = b;
        }

        return v;
    }

    @Override public void onDestroyView()
    {
        Log.d(LOGNAME, ">>onDestroyView");
        super.onDestroyView();

        view_Favs = null;
        helpPage = null;
        helpText = null;
    }

    @Override public void onAttach(Context context)
    {
        Log.d(LOGNAME, ">>onAttach");
        super.onAttach(context);
        mListener = (FavoriteSelectInterface)getActivity();
    }

    @Override public void onDetach()
    {
        Log.d(LOGNAME, ">>onDetach");
        super.onDetach();
        mListener = null;
    }

    public void setHelpMode(boolean enable)
    {
        if (enable)
        {
            view_Favs.setVisibility(GONE);
            helpPage.setVisibility(VISIBLE);

            String str = appContext.getResources().getString(R.string.text_help_head);
            str += appContext.getResources().getString(R.string.text_help_favs);
            helpText.setText(str);
        }
        else
        {
            helpPage.setVisibility(GONE);
            view_Favs.setVisibility(VISIBLE);
        }
    }

    private final View.OnClickListener mClicker = new View.OnClickListener()
    {
        @Override public void onClick(View v)
        {
            int id = v.getId();
            for (int i = 0; i < idsButton.length; ++i)
            {
                if (id == idsButton[i])
                {
                    if (curFavorite == i) break;

                    for (int j = 0; j < numSegments; ++j)
                    {
                        segPatterns[j] = listFavorites[i].getPatternNum(j);
                        if (j == curSegment)
                        {
                            if (mListener != null)
                                mListener.onFavoriteSelect(segPatterns[j]);
                        }
                    }

                    ChangeSelection(i);
                    break;
                }
            }
        }
    };

    private void ChangeSelection(int index)
    {
        Log.d(LOGNAME, "FavSelect index=" + index);
        if (curFavorite >= 0)
        {
            objsButton[curFavorite].setText(listFavorites[curFavorite].getPatternName());
            objsButton[curFavorite].setTextColor(ContextCompat.getColor(appContext, R.color.UserChoice));
        }

        if (index >= 0)
        {
            objsButton[index].setText(">>> " + listFavorites[index].getPatternName() + " <<<");
            objsButton[index].setTextColor(ContextCompat.getColor(appContext, R.color.HighLight));
        }

        curFavorite = index;
    }

    public void onPatternSelect(int pnum)
    {
        for (int i = 0; i < listFavorites.length; ++i)
        {
            if (listFavorites[i].getPatternNum(curSegment) == pnum)
            {
                ChangeSelection(i);
                return;
            }
        }

        ChangeSelection(-1); // deselect current choice
    }
}
