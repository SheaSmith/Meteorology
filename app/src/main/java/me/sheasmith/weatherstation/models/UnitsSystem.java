package me.sheasmith.weatherstation.models;

/**
 * Created by Shea Smith on 19/07/2020.
 */
public enum UnitsSystem {
    METRIC,
    IMPERIAL,
    UK_HYBRID,
    METRIC_SI;


    public static UnitsSystem fromString(String unitsSystem) {
        switch (unitsSystem) {
            case "imperial":
                return IMPERIAL;
            case "uk_hybrid":
                return UK_HYBRID;
            case "metric_si":
                return METRIC_SI;
            default:
                return METRIC;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case IMPERIAL:
                return "imperial";
            case UK_HYBRID:
                return "uk_hybrid";
            case METRIC_SI:
                return "metric_si";
            default:
                return "metric";
        }
    }

    public String getApiCode() {
        switch (this) {
            case IMPERIAL:
                return "e";
            case UK_HYBRID:
                return "h";
            case METRIC_SI:
                return "s";
            default:
                return "m";
        }
    }

    public String getDisplayName() {
        switch (this) {
            case IMPERIAL:
                return "Imperial";
            case UK_HYBRID:
                return "UK Hybrid";
            case METRIC_SI:
                return "Metric (Scientific)";
            default:
                return "Metric";
        }
    }
}
