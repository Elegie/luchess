package io.elegie.luchess.core.indexing.adapters;


import io.elegie.luchess.core.api.build.SourceDataSet;
import io.elegie.luchess.core.api.build.SourceDataUnit;

import java.util.Iterator;

/**
 * Generic algorithm to iterate through data units.
 */
public abstract class AbstractSourceDataSet implements SourceDataSet {

    private Iterator<SourceDataUnit> dataUnitsIterator;

    protected void setDataUnitsIterator(Iterator<SourceDataUnit> dataUnitsIterator) {
        this.dataUnitsIterator = dataUnitsIterator;
    }

    @Override
    public SourceDataUnit nextUnit() {
        SourceDataUnit next = null;
        if (dataUnitsIterator.hasNext()) {
            next = dataUnitsIterator.next();
        }
        return next;
    }
}
