package in.xplorelogic.inveck.models;


public class LocationNameDAO {
    String id = "";
    String location_name = "";

    public LocationNameDAO() {

    }

    public LocationNameDAO(String id, String location_name) {
        this.id = id;
        this.location_name = location_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    @Override
    public String toString() {
        return location_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocationNameDAO) {
            LocationNameDAO c = (LocationNameDAO) obj;
            if (c.getLocation_name().equals(location_name) && c.getId() == id) return true;
        }

        return false;
    }
}
