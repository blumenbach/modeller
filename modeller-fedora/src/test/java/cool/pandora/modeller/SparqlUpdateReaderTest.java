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

package cool.pandora.modeller;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static junit.framework.TestCase.assertEquals;

/**
 * SparqlUpdateReaderTest
 *
 * @author Christopher Johnson
 */
public class SparqlUpdateReaderTest {

    @Test
    public void sparqlUpdateReaderTest() {

        final InputStream requestBodyStream =
                SparqlUpdateReaderTest.class.getResourceAsStream("/data/res.update");

        try {
            final String requestBody = IOUtils.toString(requestBodyStream, UTF_8);
            System.out.println();
            System.out.println("#### ---- Write as application/sparql-update");
            System.out.println(requestBody);
            InputStream is = getClass().getResourceAsStream("/requestBody_out.txt");
            String out = TestUtils.StreamToString(is);
            assertEquals(requestBody, out);
        } catch (final IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}