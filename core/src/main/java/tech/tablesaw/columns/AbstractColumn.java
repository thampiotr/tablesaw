package tech.tablesaw.columns;

import org.apache.commons.lang3.StringUtils;

import tech.tablesaw.store.ColumnMetadata;

import java.util.UUID;

/**
 * Partial implementation of the {@link Column} interface
 */
public abstract class AbstractColumn<E extends AbstractColumn> implements Column {

    // this character is sometimes inserted into windows files and needs to be removed
    private static final String UTF8_BOM = "\uFEFF";

    private String id;

    private String name;

    private String comment;

    public AbstractColumn(String name) {
        setName(name);
        this.comment = "";
        this.id = UUID.randomUUID().toString();
    }

    public AbstractColumn(ColumnMetadata metadata) {
        setName(metadata.getName());
        this.comment = "";
        this.id = metadata.getId();
    }

    private static String removeUtf8Bom(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    public String name() {
        return name;
    }

    public String id() {
        return id;
    }

    @Override
    public String metadata() {
        return columnMetadata().toJson();
    }

    /**
     * Sets the columns name to the given string
     *
     * @param name The new name MUST be unique for any table containing this column
     */
    public void setName(String name) {
        name = removeUtf8Bom(name);
        this.name = name.trim();
    }

    public abstract void appendCell(String stringvalue);

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public ColumnMetadata columnMetadata() {
        return new ColumnMetadata(this);
    }

    /**
     * Returns the width of the column in characters, for printing
     */
    @Override
    public int columnWidth() {

        int width = name().length();
        for (int rowNum = 0; rowNum < size(); rowNum++) {
            width = Math.max(width, StringUtils.length(getString(rowNum)));
        }
        return width;
    }

    @Override
    public E difference() {
        throw new UnsupportedOperationException("difference() method not supported for all data types");
    }
}