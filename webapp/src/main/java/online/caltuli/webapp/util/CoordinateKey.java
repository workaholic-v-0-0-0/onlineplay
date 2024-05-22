package online.caltuli.webapp.util;

import online.caltuli.model.Coordinates;

import java.util.List;
import java.util.Objects;

public class CoordinateKey {
    private List<Coordinates> coordinates;

    public CoordinateKey(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoordinateKey)) return false;
        CoordinateKey that = (CoordinateKey) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    public List<Coordinates> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }
}
