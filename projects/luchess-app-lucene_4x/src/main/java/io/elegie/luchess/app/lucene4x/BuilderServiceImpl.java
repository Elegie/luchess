package io.elegie.luchess.app.lucene4x;

import io.elegie.luchess.app.lucene4x.build.LuceneGameIndexer;
import io.elegie.luchess.app.lucene4x.build.LuceneIndexMonitor;
import io.elegie.luchess.app.lucene4x.configuration.AppConfig;
import io.elegie.luchess.app.lucene4x.index.manager.IndexManager;
import io.elegie.luchess.app.lucene4x.index.manager.WriterTemplate;
import io.elegie.luchess.core.api.BuilderService;
import io.elegie.luchess.core.api.build.BuildException;
import io.elegie.luchess.core.api.build.IndexMonitor;
import io.elegie.luchess.core.api.build.SourceDataSet;
import io.elegie.luchess.core.indexing.workflow.IndexingSession;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

/**
 * <p>
 * Our implementation of the builder service. The indexing is done in a
 * background task, in a thread-safe manner.
 * </p>
 * 
 * <p>
 * While clients may call the {@link #index(SourceDataSet, IndexMonitor)} method
 * concurrently, it is not recommended, because each call is allocated the same
 * threading profile, which could result in too many threads being allocated
 * when the profile isn't defined in regards of how many concurrent calls there
 * may be.
 * </p>
 */
public class BuilderServiceImpl extends AbstractService implements BuilderService {

    @SuppressWarnings("javadoc")
    public BuilderServiceImpl(ServicesFactoryImpl servicesFactory) {
        super(servicesFactory);
    }

    @Override
    public void index(final SourceDataSet dataSet, final IndexMonitor monitor) throws BuildException {
        final AppConfig config = getServicesFactory().getConfig();
        IndexManager indexManager = config.getIndexManager();
        try {
            new WriterTemplate(indexManager) {
                @Override
                protected void doWrite(IndexWriter writer, CleanUp cleanUp) {
                    IndexingSession session = new IndexingSession();
                    session.setDataSet(dataSet);
                    session.setMonitor(new LuceneIndexMonitor(monitor, cleanUp));
                    session.setIndexer(new LuceneGameIndexer(writer));
                    session.setProfile(config.getThreadingProfile());
                    new Thread(session, IndexingSession.class.getSimpleName()).start();
                }
            }.execute();
        } catch (IOException e) {
            String message = "IOException when executing the WriterTemplate method.";
            throw new BuildException(message, e);
        }
    }
}
