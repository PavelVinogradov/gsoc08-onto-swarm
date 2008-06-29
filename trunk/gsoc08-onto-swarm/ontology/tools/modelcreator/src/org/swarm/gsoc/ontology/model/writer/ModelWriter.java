package org.swarm.gsoc.ontology.model.writer;

import org.swarm.gsoc.ontology.model.Clazz;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: blaze
 * Date: 29.06.2008
 * Time: 18:18:10
 * To change this template use File | Settings | File Templates.
 */
public interface ModelWriter {
    void generate (List<Clazz> model);
}
