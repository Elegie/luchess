package io.elegie.luchess.core.indexing.workflow.helpers;

import io.elegie.luchess.core.api.build.SourceDataSet;
import io.elegie.luchess.core.api.build.SourceDataUnit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link SourceDataSet} implementation, which iterates through the
 * units passed to the constructor.
 */
@SuppressWarnings("javadoc")
public class SimpleSourceDataSet implements SourceDataSet {

    private List<SingleGameSourceDataUnit> dataUnits;
    private Iterator<SingleGameSourceDataUnit> iteratorDataUnits;

    public SimpleSourceDataSet(SingleGameSourceDataUnit dataUnit) {
        dataUnits = Arrays.asList(dataUnit);
        iteratorDataUnits = this.dataUnits.iterator();
    }

    public SimpleSourceDataSet(List<SingleGameSourceDataUnit> dataUnits) {
        this.dataUnits = dataUnits;
        iteratorDataUnits = this.dataUnits.iterator();
    }

    @Override
    public SourceDataUnit nextUnit() {
        SourceDataUnit next = null;
        if (iteratorDataUnits.hasNext()) {
            next = iteratorDataUnits.next();
        }
        return next;
    }

    /**
     * @return All data units, so that one can assert that lifecycle methods
     *         have been called.
     */
    public List<SingleGameSourceDataUnit> getDataUnits() {
        return dataUnits;
    }

}
