package domain;

import domain.solver.PartDifficultyComparator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.value.ValueRange;
import org.optaplanner.core.api.domain.value.ValueRangeType;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.math.BigDecimal;
import java.util.List;

@PlanningEntity(difficultyComparatorClass = PartDifficultyComparator.class)
public class Part {
    private double height;
    private double width;
    private Coordinate coordinates;
    private Platform platform;
    private Long id;

    public Part(Long id, double width, double height) {
        this.id = id;
        setHeight(height);
        setWidth(width);
    }

    public Part() {

    }

    @PlanningVariable
    //TODO: limit coordinate list to those that are possible.
    //Makes score worse.
    //@ValueRange(type = ValueRangeType.FROM_PLANNING_ENTITY_PROPERTY, planningEntityProperty = "possibleCoordinatesList")
    @ValueRange(type = ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty  = "coordinateList")
    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }

    public List<Coordinate> getPossibleCoordinatesList() {
        return getPlatform().getPossibleCoordinatesList();
    }

    public void setCoordinate(BigDecimal x, BigDecimal y) {
        coordinates.setCoordinates(x, y);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //@PlanningVariable
    //@ValueRange(type = ValueRangeType.FROM_SOLUTION_PROPERTY, solutionProperty = "platformList")
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if(height <= 0.0) {
            throw new IllegalArgumentException("Part height must be > 0");
        }
        this.height = height;
    }

    public void setWidth(double width) {
        if(width <= 0.0) {
            throw new IllegalArgumentException("Part width must be > 0");
        }
        this.width = width;
    }

    public double getArea() {
        return width * height;
    }

    public String getLabel() {
        return "Part " + id + ": ";
    }

    public String toString () {
        return getLabel() + " W: " + width + " H: " + height;
    }

    public boolean widthOverlapsPlatform(){
        return (this.coordinates.getX() >= this.platform.getWidth()) ||
                (this.width + this.coordinates.getX() > this.platform.getWidth());
    }

    //returns as integer
    public int widthOverlap() {
        return (int)((this.coordinates.getX() + this.width) - platform.getWidth());
    }

    public boolean heightOverlapsPlatform() {
        return (this.coordinates.getY() >= this.platform.getHeight()) ||
                (this.height + this.coordinates.getY() > this.platform.getHeight());
    }

    //returns as integer
    public int heightOverlap() {
        return (int)((this.coordinates.getY() + this.height) - platform.getHeight());
    }


    /**
     * Checks if another part intersects this part.
     * @param part The other part to compare.
     * @return Returns true if the parts intersect and false if they do not intersect.
     */
    public boolean intersects(Part part) {
        return part.platform.equals(this.platform) &&
                (part.coordinates.getX() + part.getWidth() > this.coordinates.getX() &&
                part.coordinates.getY() + part.getHeight() > this.coordinates.getY() &&
                part.coordinates.getX() < this.coordinates.getX() + this.width &&
                part.coordinates.getY() < this.coordinates.getY() + this.height);
    }

    /**
     * Returns the area to the nearest integer
     * @param part the part to check.
     * @return the area as an integer
     */
    public int intersectArea(Part part) {
        if(intersects(part)) {
            return (int)(Math.abs(((part.getCoordinates().getX() + part.getWidth()) -
                        this.getCoordinates().getX()) *
                    ((part.getCoordinates().getY() + part.getHeight()) -
                        this.getCoordinates().getY())));
        } else {
            return 0;
        }
    }

    public boolean equals(Object o) {     //renamed solutionEquals
        if (this == o) {
            return true;
        } else if (o instanceof Part) {
            Part other = (Part) o;
            return new EqualsBuilder()
                    .append(id, other.id)
                    .append(platform, other.platform)
                    .append(coordinates, other.coordinates)
                    .isEquals();
        } else {
            return false;
        }
    }

    public int hashCode() {         //renamed solutionHashCode
        return new HashCodeBuilder()
                .append(id)
                .append(platform)
                .append(coordinates)
                .toHashCode();
    }
}
