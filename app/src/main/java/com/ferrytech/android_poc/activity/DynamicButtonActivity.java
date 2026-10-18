package com.ferrytech.android_poc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.ferrytech.android_poc.R;
import com.ferrytech.android_poc.Utils.JsonUtils;
import com.ferrytech.android_poc.Utils.LayoutConfigResponse;
import com.ferrytech.android_poc.model.FloatingButtonConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicButtonActivity extends AppCompatActivity {

    private ConstraintLayout rootLayout;
    private static final int FAB_SIZE_DP = 56;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_button);
        rootLayout = findViewById(R.id.main);

        loadData();
    }

    private void loadData() {
        String json = JsonUtils.getJsonFromAssets(this, "button_config.json");

        Gson gson = new Gson();
        LayoutConfigResponse response = gson.fromJson(json, LayoutConfigResponse.class);

        if (response != null) {
            renderDynamicFABs(response);
        }
    }


    private void renderDynamicFABs(LayoutConfigResponse response) {

        List<FloatingButtonConfig> buttonList = response.getFloatingButtons();
        LayoutConfigResponse.LayoutConfig config = response.getLayoutConfiguration();

        float density = getResources().getDisplayMetrics().density;
        int fabSize = (int) (FAB_SIZE_DP * density);

        Map<String, int[][]> cornerGrids = new HashMap<>();
        cornerGrids.put("top_left", new int[3][3]);
        cornerGrids.put("top_right", new int[3][3]);
        cornerGrids.put("bottom_right", new int[3][3]);
        cornerGrids.put("bottom_left", new int[3][3]);


        for(FloatingButtonConfig btnData : buttonList){
            int index = btnData.index;

            int corner = btnData.corner;
            boolean isHorizontal = "horizontal".equalsIgnoreCase(btnData.direction);

            int targetX = isHorizontal ? index : 0;
            int targetY = isHorizontal ? 0: index;

            int[][] grid = cornerGrids.get(corner);
            if (grid == null) continue;

            while (grid[targetX][targetY] != 0){
                if(isHorizontal){
                    targetX++;
                    if(targetX>2) break;
                }else {
                    targetY++;
                    if (targetY >2) break;
                }

            }

            if(targetX >2 || targetY > 2) continue;

            final FloatingActionButton fab = new FloatingActionButton(this);
            fab.setId(View.generateViewId());
            fab.setSize(FloatingActionButton.SIZE_NORMAL);

            int iconResId = getResources().getIdentifier(btnData.icon_url,"drawable",getPackageName());
            if(iconResId != 0) fab.setImageResource(iconResId);

            fab.setOnClickListener(v ->{
                Toast.makeText(this, "clicked" + btnData.action_type, Toast.LENGTH_SHORT).show();
            });


            rootLayout.addView(fab);
            grid[targetX][targetY] = fab.getId();

        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(rootLayout);

        int marginPx = (int) (config.global_corner_margin_dp * density);
        int spacingPx = (int) (config.global_button_spacing_dp * density);


        for (Map.Entry<String, int[][]> entry : cornerGrids.entrySet()) {
            String corner = entry.getKey();
            int[][] grid = entry.getValue();

            LayoutConfigResponse.LayoutConfig.CornerOverride override = null;
            if (config.getCornerOverrides() != null) {
                override = config.getCornerOverrides().get(corner);
            }

            // Use corner global override, if missing use root mandatory global
            int cornerBaseMarginPx = (override != null && override.global_corner_margin_dp != null) ?
                    (int) (override.global_corner_margin_dp * density) : marginPx;

            int cornerBaseSpacingPx = (override != null && override.global_button_spacing_dp != null) ?
                    (int) (override.global_button_spacing_dp * density) : spacingPx;

            // direction rules
            int hMarginPx = (override != null && override.horizontal_margin_dp != null) ?
                    (int) (override.horizontal_margin_dp * density) : cornerBaseMarginPx;

            int vMarginPx = (override != null && override.vertical_margin_dp != null) ?
                    (int) (override.vertical_margin_dp * density) : cornerBaseMarginPx;

            int hSpacingPx = (override != null && override.horizontal_spacing_dp != null) ?
                    (int) (override.horizontal_spacing_dp * density) : cornerBaseSpacingPx;

            int vSpacingPx = (override != null && override.vertical_spacing_dp != null) ?
                    (int) (override.vertical_spacing_dp * density) : cornerBaseSpacingPx;

            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int currentViewId = grid[x][y];
                    if (currentViewId == 0) continue;

                    int leftSiblingId = (x > 0) ? grid[x - 1][y] : 0;
                    int topSiblingId = (y > 0) ? grid[x][y - 1] : 0;

                    // vertical links
                    if (y == 0) {
                        if (corner.contains("top")) {
                            constraintSet.connect(currentViewId, ConstraintSet.TOP, R.id.header, ConstraintSet.BOTTOM, vMarginPx);
                        } else {
                            constraintSet.connect(currentViewId, ConstraintSet.BOTTOM, R.id.footer, ConstraintSet.TOP, vMarginPx);
                        }
                    } else {
                        if (topSiblingId != 0) {
                            if (corner.contains("top")) {
                                constraintSet.connect(currentViewId, ConstraintSet.TOP, topSiblingId, ConstraintSet.BOTTOM, vSpacingPx);
                            } else {
                                constraintSet.connect(currentViewId, ConstraintSet.BOTTOM, topSiblingId, ConstraintSet.TOP, vSpacingPx);
                            }
                        } else {
                            int customVMargin = vMarginPx + (y * fabSize ) + (y * vSpacingPx);
                            if (corner.contains("top")) {
                                constraintSet.connect(currentViewId, ConstraintSet.TOP, R.id.header, ConstraintSet.BOTTOM, customVMargin);
                            } else {
                                constraintSet.connect(currentViewId, ConstraintSet.BOTTOM, R.id.footer, ConstraintSet.TOP, customVMargin);
                            }
                        }
                    }

                    // --- CONFIGURE HORIZONTAL AXIS LINKS ---
                    if (x == 0) {
                        if (corner.contains("left")) {
                            constraintSet.connect(currentViewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, hMarginPx);
                        } else {
                            constraintSet.connect(currentViewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, hMarginPx);
                        }
                    } else {
                        if (leftSiblingId != 0) {
                            if (corner.contains("left")) {
                                constraintSet.connect(currentViewId, ConstraintSet.START, leftSiblingId, ConstraintSet.END, hSpacingPx);
                            } else {
                                constraintSet.connect(currentViewId, ConstraintSet.END, leftSiblingId, ConstraintSet.START, hSpacingPx);
                            }
                        } else {
                            int customHMargin = hMarginPx + (x * fabSize) + (x * hSpacingPx);
                            if (corner.contains("left")) {
                                constraintSet.connect(currentViewId, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, customHMargin);
                            } else {
                                constraintSet.connect(currentViewId, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, customHMargin);
                            }
                        }
                    }
                }
            }
        }
        constraintSet.applyTo(rootLayout);
    }
}




