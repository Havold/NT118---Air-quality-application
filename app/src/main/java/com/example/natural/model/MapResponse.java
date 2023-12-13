package com.example.natural.model;

import com.google.gson.annotations.SerializedName;

public class MapResponse {
    @SerializedName("options")
    private Options options;
    private int version;
    private String sprite;
    private String glyphs;
    private Sources sources;
    private Layers[] layers;

    public Sources getSources() {
        return sources;
    }

    public Layers[] getLayers() {
        return layers;
    }

    public static class Layers {
        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public Paint getPaint() {
            return paint;
        }

        private Paint paint;
    }

    public static class Paint {
        @SerializedName("background-color")
        private String backgroundColor;

        public String getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static class Sources{
        private Vector_tiles vector_tiles;

        public Vector_tiles getVector_tiles() {
            return vector_tiles;
        }
    }

    public static class Vector_tiles {
        private String vector;

        public String getVector() {
            return vector;
        }

        public String getAttribution() {
            return attribution;
        }

        public int getMaxzoom() {
            return maxzoom;
        }

        public int getMinzoom() {
            return minzoom;
        }

        public Vector_layers[] getVector_layers() {
            return vector_layers;
        }

        public String[] getTiles() {
            return tiles;
        }

        private String attribution;
        private int maxzoom;
        private int minzoom;
        private Vector_layers[] vector_layers;
        private String[] tiles;
    }

    public static class Vector_layers {
        public int getMaxzoom() {
            return maxzoom;
        }

        public Fields getFields() {
            return fields;
        }

        public int getMinzoom() {
            return minzoom;
        }

        public String getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        private int maxzoom;
        private Fields fields;
        private int minzoom;
        private String id;
        private String description;
    }

    private static class Fields {
        private String brunnel;

        @SerializedName("class")
        private String classAPI;
        private String intermittent;

        public String getBrunnel() {
            return brunnel;
        }

        public String getClassAPI() {
            return classAPI;
        }

        public String getIntermittent() {
            return intermittent;
        }
    }



    public int getVersion() {
        return version;
    }

    public String getSprite() {
        return sprite;
    }

    public String getGlyphs() {
        return glyphs;
    }

    public Options getOptions() {
        return options;
    }

    public static class Options {
        @SerializedName("default")
        private DefaultOptions defaultOptions;

        public DefaultOptions getDefaultOptions() {
            return defaultOptions;
        }
    }

    public static class DefaultOptions {
        private double[] center;
        private double[] bounds;
        private float zoom;
        private float minZoom;
        private float maxZoom;
        private boolean boxZoom;
        private String geocodeUrl;

        public boolean isBoxZoom() {
            return boxZoom;
        }

        public String getGeocodeUrl() {
            return geocodeUrl;
        }

        public double[] getCenter() {
            return center;
        }
        public double[] getBounds() {
            return bounds;
        }
        public float getZoom() {
            return zoom;
        }

        public float getMinZoom() {
            return minZoom;
        }

        public float getMaxZoom() {
            return maxZoom;
        }
        public boolean getBoxZoom() {
            return boxZoom;
        }
    }
}
