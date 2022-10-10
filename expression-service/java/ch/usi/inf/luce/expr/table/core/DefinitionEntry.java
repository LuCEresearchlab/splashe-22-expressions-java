package ch.usi.inf.luce.expr.table.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Data structure that represents
 * an entry in the definitions input file
 * so that the documentation table can be
 * generated.
 */
public final class DefinitionEntry {
    // Top-level, human-readable grouping
    // (e.g., "Literal").
    @NotNull
    public final String group;

    // Human-readable name for this construct
    // (e.g., "Class Instance Creation").
    @NotNull
    public final String constructName;

    // Human-readable description for this entry
    // (e.g., "Qualified class instance creation").
    @NotNull
    public final String description;

    // Section/Chapter in the authoritative reference (specification) of the language.
    // so that one can build a URL (e.g., by adding a prefix).
    @NotNull
    public final String reference;

    // Sequence of statements (as Java source code string).
    @NotNull
    @JsonIgnore
    public final String example;

    // 0-based index to identify the expression of interest in the source code.
    public final int expressionIndex;

    // Simple name of a subclass of ASTNode (e.g., "ClassInstanceCreation") that
    // is expected to match the root of the selected expression.
    public final String expressionNode;

    // Length of the selected expression measured in number of tokens.
    public final int expressionTokens;
    
    // List of "tags" for this example,
    // e.g. "Number,Literal"
    public final String tags;

    @JsonCreator
    public DefinitionEntry(@JsonProperty("group") @NotNull String group,
                           @JsonProperty("constructName") @NotNull String constructName,
                           @JsonProperty("description") @NotNull String description,
                           @JsonProperty("reference") @NotNull String reference,
                           @JsonProperty("example") @NotNull String example,
                           @JsonProperty(value = "expressionIndex", defaultValue = "0") int expressionIndex,
                           @JsonProperty("expressionNode") String expressionNode,
                           @JsonProperty(value = "expressionTokens", defaultValue = "0") int expressionTokens,
                           @JsonProperty("tags") String tags) {
        this.group = group;
        this.constructName = constructName;
        this.description = description;
        this.reference = reference;
        this.example = example;
        this.expressionIndex = expressionIndex;
        this.expressionNode = expressionNode;
        this.expressionTokens = expressionTokens;
        this.tags = tags;
    }
}
