package org.avenue1.design.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.avenue1.design.domain.enumeration.UnitOfMeasureEnum;

/**
 * A Container.
 */
@Document(collection = "container")
public class Container implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("container_id")
    private Double containerID;

    @Field("width")
    private Double width;

    @Field("height")
    private Double height;

    @Field("x_pos")
    private Double xPos;

    @Field("y_pos")
    private Double yPos;

    @Field("created")
    private LocalDate created;

    @Field("unit_of_measure")
    private UnitOfMeasureEnum unitOfMeasure;

    @Field("data")
    private String data;

    @Field("modified")
    private LocalDate modified;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Container name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getContainerID() {
        return containerID;
    }

    public Container containerID(Double containerID) {
        this.containerID = containerID;
        return this;
    }

    public void setContainerID(Double containerID) {
        this.containerID = containerID;
    }

    public Double getWidth() {
        return width;
    }

    public Container width(Double width) {
        this.width = width;
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public Container height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getxPos() {
        return xPos;
    }

    public Container xPos(Double xPos) {
        this.xPos = xPos;
        return this;
    }

    public void setxPos(Double xPos) {
        this.xPos = xPos;
    }

    public Double getyPos() {
        return yPos;
    }

    public Container yPos(Double yPos) {
        this.yPos = yPos;
        return this;
    }

    public void setyPos(Double yPos) {
        this.yPos = yPos;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Container created(LocalDate created) {
        this.created = created;
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public UnitOfMeasureEnum getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Container unitOfMeasure(UnitOfMeasureEnum unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public void setUnitOfMeasure(UnitOfMeasureEnum unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getData() {
        return data;
    }

    public Container data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LocalDate getModified() {
        return modified;
    }

    public Container modified(LocalDate modified) {
        this.modified = modified;
        return this;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Container container = (Container) o;
        if (container.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), container.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Container{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", containerID=" + getContainerID() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", xPos=" + getxPos() +
            ", yPos=" + getyPos() +
            ", created='" + getCreated() + "'" +
            ", unitOfMeasure='" + getUnitOfMeasure() + "'" +
            ", data='" + getData() + "'" +
            ", modified='" + getModified() + "'" +
            "}";
    }
}
