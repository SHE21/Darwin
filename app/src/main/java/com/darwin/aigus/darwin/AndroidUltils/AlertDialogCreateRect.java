package com.darwin.aigus.darwin.AndroidUltils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.darwin.aigus.darwin.Geometry.DarwinGeometryAnalyst;
import com.darwin.aigus.darwin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SANTIAGO from AIGUS on 13/04/2018.
 */
public class AlertDialogCreateRect extends DialogFragment {
    private RadioButton onlyThisAmostra, onlyAllAmostra;
    private EditText dimWidthRect, dimHeightRect;
    private RadioGroup optionGroup;
    private ImageButton leftTop, rightTop, centerTop, leftBottom, rightBottom, centerBottom, leftCenter, rightCenter, centerCenter;
    private HelpeAlertDialogRect helpeAlertDialogRect;
    private Button btnCreate;
    private List<ImageButton> imageButtonList = new ArrayList<>();
    private Drawable defaultImageButtom, selectedImageButtom;
    private ImageView previewAngle;
    private int radio = 8;

    private static final int[] previewImages = {
            R.drawable.view_left_top,
            R.drawable.view_center_top,
            R.drawable.view_right_top,
            R.drawable.view_left_bottom,
            R.drawable.view_right_bottom,
            R.drawable.view_center_bottom,
            R.drawable.view_center_left,
            R.drawable.view_center_right,
            R.drawable.view_center
    };

    public HelpeAlertDialogRect getHelpeAlertDialogRect() {
        return helpeAlertDialogRect;
    }

    public void setHelpeAlertDialogRect(HelpeAlertDialogRect helpeAlertDialogRect) {
        this.helpeAlertDialogRect = helpeAlertDialogRect;
    }

    public interface HelpeAlertDialogRect{
        void setValues(double height, double width, int general, int corner);

    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_create_rect, null);

        defaultImageButtom = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_angulo_rect, null);
        selectedImageButtom = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_angulo_rect_green, null);

        onlyThisAmostra = view.findViewById(R.id.onlyThisAmostra);
        onlyAllAmostra = view.findViewById(R.id.onlyAllAmostra);
        dimWidthRect = view.findViewById(R.id.dimWidthRect);
        dimHeightRect = view.findViewById(R.id.dimHeightRect);
        btnCreate = view.findViewById(R.id.btnCreate);
        optionGroup = view.findViewById(R.id.optionGroup);
        previewAngle = view.findViewById(R.id.previewAngle);

        leftTop = view.findViewById(R.id.leftTop);
        centerTop = view.findViewById(R.id.centerTop);
        rightTop = view.findViewById(R.id.rightTop);

        leftBottom = view.findViewById(R.id.leftBottom);
        rightBottom = view.findViewById(R.id.rightBottom);
        centerBottom = view.findViewById(R.id.centerBottom);

        leftCenter = view.findViewById(R.id.leftCenter);
        rightCenter = view.findViewById(R.id.rightCenter);
        centerCenter = view.findViewById(R.id.centerCenter);

        imageButtonList.add(leftTop);
        imageButtonList.add(centerTop);
        imageButtonList.add(rightTop);
        imageButtonList.add(leftBottom);
        imageButtonList.add(rightBottom);
        imageButtonList.add(centerBottom);
        imageButtonList.add(leftCenter);
        imageButtonList.add(rightCenter);
        imageButtonList.add(centerCenter);

        for (int i =0; i < imageButtonList.size(); i++){
            imageButtonList.get(i).setOnClickListener(onClickListener(i));
        }

        imageButtonList.get(radio).setImageDrawable(selectedImageButtom);
        previewAngle.setImageResource(previewImages[8]);


        btnCreate.setOnClickListener(v -> {
            int cheched = 0;
            if(onlyThisAmostra.isChecked()){
                cheched = 1;

            }else if (onlyAllAmostra.isChecked()){
                cheched = 2;
            }

            String height = dimHeightRect.getText().toString();
            String width = dimWidthRect.getText().toString();

            if (cheched != 0){
                if (height.isEmpty() || width.isEmpty()){
                    Toast.makeText(getContext(), "Campos em branco!", Toast.LENGTH_SHORT).show();
                }else{
                    imageButtonList.clear();
                    helpeAlertDialogRect.setValues(Double.parseDouble(height), Double.parseDouble(width), cheched, radio);
                }
            }else{
                Toast.makeText(getContext(), " ->" + cheched, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setTitle(R.string.createAreaRect);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {imageButtonList.clear();});
        builder.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_gray_poly, null));
        builder.setView(view);
        return builder.create();
    }

    private View.OnClickListener onClickListener(int idBtnImage) {
        return v -> {
            onClickBtnImage(idBtnImage);

        };
    }

    public void onClickBtnImage(int idBtnImage){
        imageButtonList.get(radio).setImageDrawable(defaultImageButtom);
        imageButtonList.get(idBtnImage).setImageDrawable(selectedImageButtom);
        previewAngle.setImageResource(previewImages[idBtnImage]);
        radio = idBtnImage;
    }
}
