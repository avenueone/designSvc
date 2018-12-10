package org.avenue1.design.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.avenue1.design.domain.enumeration.UnitOfMeasureEnum;

/**
 * A Design.
 */
@Document(collection = "design")
public class Design implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Size(min = 4)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("active")
    private Boolean active;

    @Field("instrument_types")
    private String instrumentTypes;

    @Field("width")
    private Double width;

    @Field("height")
    private Double height;

    @Field("unit_of_measure")
    private UnitOfMeasureEnum unitOfMeasure;

    @Field("created")
    private LocalDate created;

    @Field("background")
    private String background;

    @DBRef
    @Field("containers")
    private Set<Container> containers = new HashSet<>();

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

    public Design name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Design description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isActive() {
        return active;
    }

    public Design active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getInstrumentTypes() {
        return instrumentTypes;
    }

    public Design instrumentTypes(String instrumentTypes) {
        this.instrumentTypes = instrumentTypes;
        return this;
    }

    public void setInstrumentTypes(String instrumentTypes) {
        this.instrumentTypes = instrumentTypes;
    }

    public Double getWidth() {
        return width;
    }

    public Design width(Double width) {
        this.width = width;
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public Design height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public UnitOfMeasureEnum getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Design unitOfMeasure(UnitOfMeasureEnum unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
        return this;
    }

    public void setUnitOfMeasure(UnitOfMeasureEnum unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public LocalDate getCreated() {
        return created;
    }

    public Design created(LocalDate created) {
        this.created = created;
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public String getBackground() {
        return background;
    }

    public Design background(String background) {
        this.background = background;
        return this;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Set<Container> getContainers() {
        return containers;
    }

    public Design containers(Set<Container> containers) {
        this.containers = containers;
        return this;
    }

    public Design addContainer(Container container) {
        this.containers.add(container);
        container.getDesigns().add(this);
        return this;
    }

    public Design removeContainer(Container container) {
        this.containers.remove(container);
        container.getDesigns().remove(this);
        return this;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
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
        Design design = (Design) o;
        if (design.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), design.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Design{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", active='" + isActive() + "'" +
            ", instrumentTypes='" + getInstrumentTypes() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", unitOfMeasure='" + getUnitOfMeasure() + "'" +
            ", created='" + getCreated() + "'" +
            ", background='" + getBackground() + "'" +
            "}";
    }
}
