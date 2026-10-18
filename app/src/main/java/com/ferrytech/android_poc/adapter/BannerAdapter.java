package com.ferrytech.android_poc.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.model.BannerModel;
import com.ferrytech.android_poc.model.ButtonStyle;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BannerModel> arrBanner;
    private LayoutInflater inflater;
    private ButtonStyle style;

    public BannerAdapter(Context context, ArrayList<BannerModel> arrBanner, ButtonStyle style) {
        this.context = context;
        this.arrBanner = arrBanner;
        this.style = style;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_banner, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.ViewHolder holder, int position) {
        BannerModel banner = arrBanner.get(position);
        holder.tv_title.setText(banner.title);
        holder.tv_subtitle.setText(banner.subtitle);

        holder.bannerButtons.bind(banner.button1Title, banner.button2Title);
    }

    @Override
    public int getItemCount() {
        return arrBanner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_subtitle;
        Button btn_offer;
        Button btn_buy;
        BannerButtons bannerButtons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_subtitle = itemView.findViewById(R.id.tv_subtitle);
            btn_offer = itemView.findViewById(R.id.btn_offer);
            btn_buy = itemView.findViewById(R.id.btn_buy);

            bannerButtons = new BannerButtons(btn_buy, btn_offer, style);

        }
    }

    public class BannerButton {
        private Button button;

        public BannerButton(Button button, ButtonStyle style) {

            this.button = button;
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Integer.parseInt(String.valueOf(style.bgColor)));
            drawable.setStroke(style.borderWidth, Integer.parseInt(String.valueOf(style.borderColor)));
            drawable.setCornerRadius(style.radius);

            button.setBackground(drawable);
        }

        public void setText(String text) {
            button.setText(text);
        }
    }

    public class BannerButtons {

        private BannerButton button1;
        private BannerButton button2;

        public BannerButtons(Button btn1, Button btn2, ButtonStyle buttonStyle) {
            button1 = new BannerButton(btn1, buttonStyle);
            button2 = new BannerButton(btn2, buttonStyle);

        }

        public void bind(String title1, String title2) {
            button1.setText(title1);
            button2.setText(title2);
        }
    }
}
