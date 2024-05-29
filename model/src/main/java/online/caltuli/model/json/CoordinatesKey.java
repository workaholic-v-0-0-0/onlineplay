package online.caltuli.model.json;

import online.caltuli.model.Coordinates;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoordinatesKey {
    private final List<Coordinates> coordinates;

    public CoordinatesKey(List<Coordinates> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinatesKey that = (CoordinatesKey) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    @Override
    public String toString() {
        return coordinates.stream()
                .map(coord -> String.format("(%d, %d)", coord.getX(), coord.getY()))
                .collect(Collectors.joining(", "));
    }
}