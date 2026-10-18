package com.ferrytech.android_poc.Utils;


import com.ferrytech.android_poc.model.FloatingButtonConfig;

import java.util.List;
import java.util.Map;

public class LayoutConfigResponse {
    private LayoutConfig layout_configuration;
    private List<FloatingButtonConfig> floating_buttons;

    public LayoutConfig getLayoutConfiguration() {
        return layout_configuration;
    }

    public List<FloatingButtonConfig> getFloatingButtons() {
        return floating_buttons;
    }

    public static class LayoutConfig {

        public int global_corner_margin_dp;
        public int global_button_spacing_dp;

        private Map<String, CornerOverride> corner_overrides;

        public Map<String, CornerOverride> getCornerOverrides() {
            return corner_overrides;
        }
        public static class CornerOverride {
            public Integer global_corner_margin_dp;
            public Integer global_button_spacing_dp;
            public Integer horizontal_margin_dp;
            public Integer vertical_margin_dp;
            public Integer horizontal_spacing_dp;
            public Integer vertical_spacing_dp;
        }

    }
}
