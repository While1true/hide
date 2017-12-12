package com.kxjsj.doctorassistant.Appxx.Sicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.CheckPartActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.IDInfoActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.MedicalInfo;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.ReportActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.RoomInfoActivity;
import com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfo.SelfPayActivity;
import com.kxjsj.doctorassistant.R;

import butterknife.OnClick;

import static com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfoF.beans;
import static com.kxjsj.doctorassistant.Appxx.Sicker.QuiryInfoF.patientNo;

/**
 * Created by vange on 2017/12/6.
 */

public class MyFragment extends Fragment {
    int[] buttonIds = {R.id.id, R.id.medicalinfo, R.id.checkinfo, R.id.roominfo, R.id.money, R.id.checke_price};
    Integer[] res = {R.drawable.ic_id, R.drawable.ic_medicine, R.drawable.ic_checkreport, R.drawable.ic_roominfo, R.drawable.ic_money_detail, R.drawable.ic_checkprice};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int position = getArguments().getInt("position");
        View conventView = inflater.inflate(R.layout.cardimageview, container, false);
        ImageView imageView = conventView.findViewById(R.id.imageview);
        TextView tv = conventView.findViewById(R.id.tv);
        conventView.setOnClickListener(v -> {
            v.setTag(buttonIds[position]);
            onViewClicked(v);
        });
        imageView.setImageResource(res[position]);
        tv.setText("想了解更多信息吗？\n快来点击查看吧");
        return conventView;
    }

    public void onViewClicked(View view) {
        switch (null == view.getTag() ? view.getId() : (int) view.getTag()) {
            case R.id.id:
                if (beans == null)
                    return;
                Intent intent = new Intent(getContext(), IDInfoActivity.class);
                intent.putExtra("bean", beans);
                startActivity(intent);
                break;
            case R.id.checkinfo:
                Intent intent1 = new Intent(getContext(), ReportActivity.class);
                intent1.putExtra("patientNo", patientNo);
                startActivity(intent1);
                break;
            case R.id.medicalinfo:
                Intent intent2 = new Intent(getContext(), MedicalInfo.class);
                intent2.putExtra("patientNo", patientNo);
                startActivity(intent2);
                break;
            case R.id.money:
                startActivity(new Intent(getContext(), SelfPayActivity.class));
                break;
            case R.id.checke_price:
                startActivity(new Intent(getContext(), CheckPartActivity.class));
                break;
            case R.id.roominfo:
                startActivity(new Intent(getContext(), RoomInfoActivity.class));
                break;
        }
    }
}
