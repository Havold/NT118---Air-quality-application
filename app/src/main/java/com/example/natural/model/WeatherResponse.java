package com.example.natural.model;

import java.util.jar.Attributes;

public class WeatherResponse {
    private String id;
    private double version;
    private long createdOn;
    private String name;
    private boolean accessPublicRead;
    private String parentId;
    private String realm;
    private String type;
    private String[] path;

    private AttributesAPI attributes;

    public String getId() {
        return id;
    }

    public double getVersion() {
        return version;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public String getName() {
        return name;
    }

    public boolean isAccessPublicRead() {
        return accessPublicRead;
    }

    public String getParentId() {
        return parentId;
    }

    public String getRealm() {
        return realm;
    }

    public String getType() {
        return type;
    }

    public String[] getPath() {
        return path;
    }

    public AttributesAPI getAttributes() {
        return attributes;
    }

    public static class AttributesAPI {
        private SunIrradiance sunIrradiance;
        private Rainfall rainfall;
        private Notes notes;
        private UVIndex uvIndex;
        private SunAzimuth sunAzimuth;
        private SunZenith sunZenith;
        private Tags tags;
        private Manufacturer manufacturer;
        private Temperature temperature;
        private Humidity humidity;
        private Location location;
        private Place place;
        private WindDirection windDirection;
        private WindSpeed windSpeed;
        private SunAltitude sunAltitude;

        public SunIrradiance getSunIrradiance() {
            return sunIrradiance;
        }

        public Rainfall getRainfall() {
            return rainfall;
        }

        public Notes getNotes() {
            return notes;
        }

        public UVIndex getUvIndex() {
            return uvIndex;
        }

        public SunAzimuth getSunAzimuth() {
            return sunAzimuth;
        }

        public SunZenith getSunZenith() {
            return sunZenith;
        }

        public Tags getTags() {
            return tags;
        }

        public Manufacturer getManufacturer() {
            return manufacturer;
        }

        public Temperature getTemperature() {
            return temperature;
        }

        public Humidity getHumidity() {
            return humidity;
        }

        public Location getLocation() {
            return location;
        }

        public Place getPlace() {
            return place;
        }

        public WindDirection getWindDirection() {
            return windDirection;
        }

        public WindSpeed getWindSpeed() {
            return windSpeed;
        }

        public SunAltitude getSunAltitude() {
            return sunAltitude;
        }
    }

    public static class SunIrradiance {
        private String type;
        private float value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Rainfall {
        private String type;
        private float value;
        private String name;
        private MetaRF meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public MetaRF getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    private static class Notes {
        private String type;
        private float value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    private static class UVIndex {
        private String type;
        private float value;
        private String name;
        private MetaUV meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public MetaUV getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class SunAzimuth {
        private String type;
        private float value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class SunZenith {
        private String type;
        private float value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Tags {
        private String type;
        private String[] value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public String[] getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Manufacturer {
        private String type;
        private String value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Temperature {
        private String type;
        private float value;
        private String name;
        private MetaTemp meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public MetaTemp getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Humidity {
        private String type;
        private float value;
        private String name;
        private MetaRF meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public MetaRF getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Location {
        private String type;
        private ValueLc value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public ValueLc getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Place {
        private String type;
        private String value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class WindDirection {
        private String type;
        private float value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class WindSpeed {
        private String type;
        private float value;
        private String name;
        private MetaRF meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public MetaRF getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class SunAltitude {
        private String type;
        private float value;
        private String name;
        private Meta meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public float getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public Meta getMeta() {
            return meta;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public static class Meta {
        private boolean readOnly;

        public boolean isReadOnly() {
            return readOnly;
        }
    }

    public static class MetaRF {
        private boolean storeDataPoints;
        private boolean readOnly;

        public boolean isStoreDataPoints() {
            return storeDataPoints;
        }

        public boolean isReadOnly() {
            return readOnly;
        }
    }

    public static class MetaUV {
        private boolean readOnly;
        private String label;

        public boolean isReadOnly() {
            return readOnly;
        }

        public String getLabel() {
            return label;
        }
    }

    public static class MetaTemp {
        private boolean ruleState;
        private boolean storeDataPoints;
        private boolean readOnly;

        public boolean isRuleState() {
            return ruleState;
        }

        public boolean isStoreDataPoints() {
            return storeDataPoints;
        }

        public boolean isReadOnly() {
            return readOnly;
        }
    }

    public static class ValueLc {
        private double[] coordinates;
        private String type;

        public double[] getCoordinates() {
            return coordinates;
        }

        public String getType() {
            return type;
        }
    }
}
