package io.elegie.luchess.web.framework.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any class can act as a controller, provided one of its public methods is
 * marked as a<strong> controller method</strong>, using the current annotation.
 * A controller method must however respect the following signature:
 * <ul>
 * <li>It must return a {@link io.elegie.luchess.web.framework.payload.Result}
 * when invoked.</li>
 * <li>It may have zero or one parameter. The parameter, if present, must
 * respect the JavaBean convention, and will be filled automatically from the
 * request, using property-name matching.</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Controller {
}
