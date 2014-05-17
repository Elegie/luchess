package io.elegie.luchess.core.api;

/**
 * Represents an injection mechanism of an already initialized services factory.
 */
public interface ServicesFactoryProvider {

    /**
     * @param createMode
     *            True when the index works in a create mode, false when it
     *            works in an update mode.
     * @return An already initialized service factory. The underlying index must
     *         be empty.
     */
    ServicesFactory createServicesFactory(boolean createMode);

}
