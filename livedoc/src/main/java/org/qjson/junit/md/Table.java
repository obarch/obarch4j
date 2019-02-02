package org.qjson.junit.md;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table implements Iterable<NamedRow> {

    public Row head;

    public List<Row> body = new ArrayList<>();

    @NotNull
    @Override
    public Iterator<NamedRow> iterator() {
        List<NamedRow> namedRows = new ArrayList<>();
        for (Row row : body) {
            NamedRow namedRow = new NamedRow();
            for (int i = 0; i < head.size(); i++) {
                namedRow.put(head.get(i), row.get(i));
            }
            namedRows.add(namedRow);
        }
        return namedRows.iterator();
    }
}
