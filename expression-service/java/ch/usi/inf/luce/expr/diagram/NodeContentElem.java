package ch.usi.inf.luce.expr.diagram;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Content of a {@link Node}.
 *
 * <p>One of: {@link OtherContent}, {@link NameDef},
 * {@link NameUse} or {@link Hole}.</p>
 */
@JsonSubTypes({
        @JsonSubTypes.Type(value = NodeContentElem.OtherContent.class, name = "other"),
        @JsonSubTypes.Type(value = NodeContentElem.NameDef.class, name = "nameDef"),
        @JsonSubTypes.Type(value = NodeContentElem.NameUse.class, name = "nameUse"),
        @JsonSubTypes.Type(value = NodeContentElem.Hole.class, name = "hole"),
})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@SuppressWarnings({
        "PMD.AbstractClassWithoutAbstractMethod",
        "PMD.CallSuperInConstructor"
})
public abstract class NodeContentElem {

    private NodeContentElem() {
    }

    public static final class OtherContent extends NodeContentElem {
        @NotNull
        public final String content;

        @JsonCreator
        public OtherContent(@JsonProperty("content") @NotNull String content) {
            this.content = content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof OtherContent)) {
                return false;
            }
            final OtherContent that = (OtherContent) o;
            return content.equals(that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(content);
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public static final class NameDef extends NodeContentElem {
        @NotNull
        public final String name;

        @JsonCreator
        @SuppressWarnings("PMD.UnnecessaryAnnotationValueElement")
        public NameDef(@JsonProperty(value = "name") @NotNull String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NameDef)) {
                return false;
            }
            final NameDef other = (NameDef) o;
            return name.equals(other.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final class NameUse extends NodeContentElem {
        @NotNull
        public final String name;

        @JsonCreator
        @SuppressWarnings("PMD.UnnecessaryAnnotationValueElement")
        public NameUse(@JsonProperty(value = "name") @NotNull String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof NameUse)) {
                return false;
            }
            final NameUse that = (NameUse) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final class Hole extends NodeContentElem {
        // Hole placeholder to be replaced using a Plug with appropriate ids.
        public static final Hole PLACEHOLDER = new Hole(new Plug(-1, -1));

        @NotNull
        public final Plug plug;
    
        public static final String CONNECTOR_PLACEHOLDER = "{{}}";

        @JsonCreator
        @SuppressWarnings("PMD.UnnecessaryAnnotationValueElement")
        public Hole(@JsonProperty(value = "plug") @NotNull Plug plug) {
            this.plug = plug;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Hole)) {
                return false;
            }
            final Hole that = (Hole) o;
            return plug.equals(that.plug);
        }

        @Override
        public int hashCode() {
            return Objects.hash(plug);
        }

        @Override
        public String toString() {
            return plug.toString();
        }
    }
}
