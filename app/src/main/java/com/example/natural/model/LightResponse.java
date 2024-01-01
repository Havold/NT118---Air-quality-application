package com.example.natural.model;

public class LightResponse {
    private String id;
    private int version;
    private long createdOn;
    private String name;
    private boolean accessPublicRead;
    private String realm;
    private String type;
    private String[] path;
    private AttributesLight attributes;

    public static class AttributesLight {
        private Notes notes;
        private Brightness brightness;
        private ColourTemperature colourTemperature;
        private LocationLight location;
        private ColourRGB colourRGB;
        private EmailLight email;
        private TagsLight tags;
        private OnOff onOff;

        public Notes getNotes() {
            return notes;
        }

        public Brightness getBrightness() {
            return brightness;
        }

        public ColourTemperature getColourTemperature() {
            return colourTemperature;
        }

        public LocationLight getLocation() {
            return location;
        }

        public ColourRGB getColourRGB() {
            return colourRGB;
        }

        public EmailLight getEmail() {
            return email;
        }

        public TagsLight getTags() {
            return tags;
        }

        public OnOff getOnOff() {
            return onOff;
        }

        public void setNotes(Notes notes) {
            this.notes = notes;
        }

        public void setBrightness(Brightness brightness) {
            this.brightness = brightness;
        }

        public void setColourTemperature(ColourTemperature colourTemperature) {
            this.colourTemperature = colourTemperature;
        }

        public void setLocation(LocationLight location) {
            this.location = location;
        }

        public void setColourRGB(ColourRGB colourRGB) {
            this.colourRGB = colourRGB;
        }

        public void setEmail(EmailLight email) {
            this.email = email;
        }

        public void setTags(TagsLight tags) {
            this.tags = tags;
        }

        public void setOnOff(OnOff onOff) {
            this.onOff = onOff;
        }
    }

    public static class Notes {
        private String type;
        private double value;
        private String name;
        private MetaNotes meta;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public MetaNotes getMeta() {
            return meta;
        }

        public void setMeta(MetaNotes meta) {
            this.meta = meta;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class MetaNotes {
        private AgentLink agentLink;

        public AgentLink getAgentLink() {
            return agentLink;
        }

        public void setAgentLink(AgentLink agentLink) {
            this.agentLink = agentLink;
        }
    }

    public static class AgentLink {
        private String id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class Brightness {
        private String type;
        private double value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class ColourTemperature {
        private String type;
        private int value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class LocationLight {
        private String type;
        private ValueLocation value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public ValueLocation getValue() {
            return value;
        }

        public void setValue(ValueLocation value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class ValueLocation {
        private double[] coordinates;
        private String type;

        public double[] getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(double[] coordinates) {
            this.coordinates = coordinates;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class ColourRGB {
        private String type;
        private double value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class EmailLight {
        private String type;
        private String value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class TagsLight {
        private String type;
        private String[] value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String[] getValue() {
            return value;
        }

        public void setValue(String[] value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class OnOff {
        private String type;
        private boolean value;
        private String name;
        private long timestamp;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isValue() {
            return value;
        }

        public void setValue(boolean value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAccessPublicRead() {
        return accessPublicRead;
    }

    public void setAccessPublicRead(boolean accessPublicRead) {
        this.accessPublicRead = accessPublicRead;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getPath() {
        return path;
    }

    public void setPath(String[] path) {
        this.path = path;
    }

    public AttributesLight getAttributes() {
        return attributes;
    }

    public void setAttributes(AttributesLight attributes) {
        this.attributes = attributes;
    }
}
