package ch.usi.inf.luce.expr.table.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * Parse a list of {@link DefinitionEntry} to
 * obtain a documentation table.
 */
public class TableEntryParser {
    @NotNull
    private final TypeReference<List<DefinitionEntry>> definitionEntriesType = new TypeReference<>() {
    };
    @NotNull
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger logger = Logger.getLogger("TableEntryParser");

    /**
     * Parse an input JSON and obtain the respective
     * {@link DefinitionEntry} objects.
     */
    @NotNull
    public final List<DefinitionEntry> getDefinitionEntries(@NotNull String definitionSource) {
        try {
            return mapper.readValue(definitionSource, definitionEntriesType);
        } catch (JsonProcessingException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Failed to parse input", e);
            }
            return List.of();
        }
    }

}
