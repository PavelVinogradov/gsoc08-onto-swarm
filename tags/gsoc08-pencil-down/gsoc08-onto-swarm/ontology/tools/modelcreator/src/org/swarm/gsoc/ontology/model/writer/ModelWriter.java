package org.swarm.gsoc.ontology.model.writer;

import org.swarm.gsoc.ontology.model.Clazz;

import java.util.List;

/**
 * Created at NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 29.06.2008
 * Time: 18:18:10
 */
public interface ModelWriter {
    void generate (List<Clazz> model);
}
