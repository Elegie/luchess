package io.elegie.luchess.app.lucene4x;

import io.elegie.luchess.app.lucene4x.index.manager.ReaderTemplate;
import io.elegie.luchess.core.api.IndexInfoService;
import io.elegie.luchess.core.api.explore.QueryException;

import java.io.IOException;

/**
 * Our implementation of the {@link IndexInfoService}.
 */
public class IndexInfoServiceImpl extends AbstractService implements IndexInfoService {

    @SuppressWarnings("javadoc")
    public IndexInfoServiceImpl(ServicesFactoryImpl servicesFactory) {
        super(servicesFactory);
    }

    // --- Implementation -----------------------------------------------------

    /**
     * We read the number of games directly from the index. As we have one
     * document per game, this means that we simply return the number of
     * documents contained in the index.
     * 
     * @throws QueryException
     *             When the index is unreachable or does not exist.
     */
    @Override
    public int getGamesCount() throws QueryException {
        try {
            return new ReaderTemplate<Integer>(getServicesFactory().getConfig().getIndexManager()) {
                @Override
                protected Integer doRead(ReaderTemplateArg arg) {
                    return arg.getIndexReader().numDocs();
                }
            }.execute();
        } catch (IOException e) {
            String message = "IOException when executing the ReaderTemplate method.";
            throw new QueryException(message, e);
        }
    }

}
