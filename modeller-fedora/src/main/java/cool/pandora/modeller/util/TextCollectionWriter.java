/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.util;

import static org.apache.jena.graph.NodeFactory.createBlankNode;
import static org.apache.jena.riot.writer.WriterConst.RDF_First;
import static org.apache.jena.riot.writer.WriterConst.RDF_Nil;
import static org.apache.jena.riot.writer.WriterConst.RDF_Rest;

import cool.pandora.modeller.common.uri.IIIFPredicates;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.jena.graph.Node;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.lang.BlankNodeAllocator;
import org.apache.jena.riot.lang.BlankNodeAllocatorHash;

/**
 * TextCollectionWriter.
 *
 * @author Christopher Johnson
 */
public class TextCollectionWriter {
    private final ByteArrayOutputStream rdfCollection;

    /**
     * constructs model.
     *
     * @param idList               List
     * @param collectionPredicate  String
     * @param resourceContainerIRI String
     * @param canvasURI            String
     */
    TextCollectionWriter(final List<String> idList, final String collectionPredicate,
                         final String resourceContainerIRI, final String canvasURI) {

        final Model model = ModelFactory.createDefaultModel();
        final Map<String, Node> bnodeMap = getBNodeKeyMap(idList);

        final Resource s = model.createResource(getIdentitySubject());
        final Property p = model.createProperty(collectionPredicate);
        final Node firstBNode = getSubjNodeForCurrentIndex(0, bnodeMap);
        final Resource o = model.createResource(String.valueOf(firstBNode));
        model.add(s, p, o);

        for (final String id : idList) {
            final int pos = getIDPos(idList, id);
            final String lastId = idList.get(idList.size() - 1);
            //singleton list
            if (pos == 0 && (Objects.equals(id, lastId))) {
                final Node subjNode = getSubjNodeForCurrentIndex(pos, bnodeMap);
                final String objectURI = getResourceURI(resourceContainerIRI, id);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource(objectURI);
                model.add(s0, p0, o0);

                final Resource s1 = model.createResource(String.valueOf(subjNode));
                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(s1, p1, o1);

                final Resource s2 = model.createResource(String.valueOf(subjNode));
                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s2, p2, o2);
            } else if (pos == 0) {
                final Node subjNode = getSubjNodeForCurrentIndex(pos, bnodeMap);
                final Node objNode = getObjNodeForCurrentIndex(pos, bnodeMap);
                final String objectURI = getResourceURI(resourceContainerIRI, id);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource(objectURI);
                model.add(s0, p0, o0);

                final Resource s1 = model.createResource(String.valueOf(subjNode));
                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(s1, p1, o1);

                final Resource s2 = model.createResource(String.valueOf(subjNode));
                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s2, p2, o2);
            } else if (Objects.equals(id, lastId)) {
                final Node subjNode = getObjNodeFromPrevIndex(pos, bnodeMap);
                final String objectURI = getResourceURI(resourceContainerIRI, id);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource(objectURI);
                model.add(s0, p0, o0);

                final Resource s1 = model.createResource(String.valueOf(subjNode));
                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(s1, p1, o1);

                final Resource s2 = model.createResource(String.valueOf(subjNode));
                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(RDF_Nil));
                model.add(s2, p2, o2);
            } else {
                final Node subjNode = getObjNodeFromPrevIndex(pos, bnodeMap);
                final Node objNode = getObjNodeForCurrentIndex(pos, bnodeMap);
                final String objectURI = getResourceURI(resourceContainerIRI, id);

                final Resource s0 = model.createResource(String.valueOf(subjNode));
                final Property p0 = model.createProperty(String.valueOf(RDF_First));
                final Resource o0 = model.createResource(objectURI);
                model.add(s0, p0, o0);

                final Resource s1 = model.createResource(String.valueOf(subjNode));
                final Property p1 = model.createProperty(IIIFPredicates.ON);
                final Resource o1 = model.createResource(canvasURI);
                model.add(s1, p1, o1);

                final Resource s2 = model.createResource(String.valueOf(subjNode));
                final Property p2 = model.createProperty(String.valueOf(RDF_Rest));
                final Resource o2 = model.createResource(String.valueOf(objNode));
                model.add(s2, p2, o2);
            }
        }

        final Dataset dataset = DatasetFactory.create(model);
        dataset.addNamedModel("http://iiif.sequence", model);
        this.rdfCollection = new ByteArrayOutputStream();
        RDFDataMgr.write(this.rdfCollection, model, Lang.NTRIPLES);
    }

    /**
     * returns collection object.
     *
     * @return RDFCollectionBuilder
     */
    public static RDFCollectionBuilder collection() {
        return new RDFCollectionBuilder();
    }

    /**
     * getIDPos.
     *
     * @param idList List
     * @param id     String
     * @return id position
     */
    private static int getIDPos(final List<String> idList, final String id) {
        return idList.indexOf(id);
    }

    /**
     * getBNodeKeyMap.
     *
     * @param idList List
     * @return bnodeMap
     */
    private static Map<String, Node> getBNodeKeyMap(final List<String> idList) {
        final Map<String, Node> bnodeMap = new HashMap<>();

        for (final String id : idList) {
            final int pos = getIDPos(idList, id);
            final Node sNode = getNewBNode();
            final Node oNode = getNewBNode();
            final String subjKey = String.valueOf(pos) + ":subj";
            bnodeMap.put(subjKey, sNode);
            final String objKey = String.valueOf(pos) + ":obj";
            bnodeMap.put(objKey, oNode);
        }
        return bnodeMap;
    }

    /**
     * getObjNodeFromPrevIndex.
     *
     * @param pos      int
     * @param bnodeMap Map
     * @return object Node
     */
    private static Node getObjNodeFromPrevIndex(final int pos, final Map<String, Node> bnodeMap) {
        final int prevIndex = pos - 1;
        final String objKey = String.valueOf(prevIndex) + ":obj";
        return bnodeMap.get(objKey);
    }

    /**
     * getSubjNodeForCurrentIndex.
     *
     * @param pos      int
     * @param bnodeMap Map
     * @return subject Node
     */
    private static Node getSubjNodeForCurrentIndex(final int pos,
                                                   final Map<String, Node> bnodeMap) {
        final String objKey = String.valueOf(pos) + ":subj";
        return bnodeMap.get(objKey);
    }

    /**
     * getObjNodeForCurrentIndex.
     *
     * @param pos      int
     * @param bnodeMap Map
     * @return object Node
     */
    private static Node getObjNodeForCurrentIndex(final int pos, final Map<String, Node> bnodeMap) {
        final String objKey = String.valueOf(pos) + ":obj";
        return bnodeMap.get(objKey);
    }

    /**
     * getIdentitySubject.
     *
     * @return identity
     */
    private static String getIdentitySubject() {
        return "";
    }

    /**
     * getNewBNode.
     *
     * @return bnodeLabel
     */
    private static Node getNewBNode() {
        final String bNodeLabelStart = "_:";
        final RDFCollectionBuilder.Factory fSeededHashAlloc = BlankNodeAllocatorHash::new;
        final BlankNodeAllocator alloc = fSeededHashAlloc.create();
        final Node n = alloc.create();
        final String bnodeLabel = bNodeLabelStart + String.valueOf(n);
        return createBlankNode(bnodeLabel);
    }

    /**
     * getResourceURI.
     *
     * @param resourceContainerIRI String
     * @param resourceID           String
     * @return resourceURI
     */
    private static String getResourceURI(final String resourceContainerIRI,
                                         final String resourceID) {
        return resourceContainerIRI + resourceID;
    }

    /**
     * renders collection to string.
     *
     * @return rdfCollection
     */
    public String render() {
        return this.rdfCollection.toString();
    }

    /**
     * RDFCollectionBuilder.
     */
    public static class RDFCollectionBuilder {

        private List<String> idList;
        private String collectionPredicate;
        private String resourceContainerIRI;
        private String canvasURI;

        /**
         * idList.
         *
         * @param idList List
         * @return this
         */
        public TextCollectionWriter.RDFCollectionBuilder idList(final List<String> idList) {
            this.idList = idList;
            return this;
        }

        /**
         * collectionPredicate.
         *
         * @param collectionPredicate String
         * @return this
         */
        public TextCollectionWriter.RDFCollectionBuilder collectionPredicate(
                final String collectionPredicate) {
            this.collectionPredicate = collectionPredicate;
            return this;
        }

        /**
         * resourceContainerIRI.
         *
         * @param resourceContainerIRI String
         * @return this
         */
        public TextCollectionWriter.RDFCollectionBuilder resourceContainerIRI(
                final String resourceContainerIRI) {
            this.resourceContainerIRI = resourceContainerIRI;
            return this;
        }

        /**
         * canvasURI.
         *
         * @param canvasURI String
         * @return this
         */
        public TextCollectionWriter.RDFCollectionBuilder canvasURI(final String canvasURI) {
            this.canvasURI = canvasURI;
            return this;
        }

        /**
         * build.
         *
         * @return TextCollectionWriter
         */
        public TextCollectionWriter build() {
            return new TextCollectionWriter(this.idList, this.collectionPredicate,
                    this.resourceContainerIRI, this.canvasURI);
        }

        /**
         * Factory.
         */
        public interface Factory {
            /**
             * create.
             *
             * @return blank node
             */
            BlankNodeAllocator create();
        }
    }

}
