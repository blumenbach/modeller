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

package cool.pandora.modeller.bag;

import gov.loc.repository.bagit.utilities.FilenameHelper;
import java.io.File;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bagger needs to know where the file came from so that it can be retrieved:
 * rootSrc,
 * and also where the file belongs within the bag: bagSrc.
 * Once the logical location of bagSrc becomes a physical copy isInBag=true
 * If the file is unselected from the BagTree, it is marked to be removed from
 * the bag
 * by setting isIncluded=false.
 *
 * <p>In order to create a bag, all data files to be included are copied to the bag
 * data dir.
 * If they already exist they are written over, or deleted if marked as
 * isIncluded=false.
 *
 * <p>If the file comes from a pre-existing bag, then the rootSrc and bagSrc will
 * be the same,
 * and isInBag=true, otherwise it comes from somewhere else and needs to be
 * placed in the
 * current bag.
 *
 * @author Jon Steinbach
 */
public class BaggerFileEntity {
    protected static final Logger log = LoggerFactory.getLogger(BaggerFileEntity.class);

    private File rootParent; // c:\\user\my documents\
    private File rootSrc; // c:\\user\my documents\datadir\dir1\file1
    private File bagSrc; // c:\\user\my documents\bag\data\datadir\dir1\file
    private String normalizedName; // datadir\dir1\file1
    private boolean isInBag = false;
    private boolean isIncluded = true;

    /**
     * removeBasePath.
     *
     * @param basePath String
     * @param filename String
     * @return filenameWithoutBasePath
     * @throws RuntimeException RuntimeException
     */
    public static String removeBasePath(final String basePath, final String filename)
            throws RuntimeException {
        if (filename == null) {
            throw new RuntimeException("Cannot remove basePath from null");
        }
        final String normBasePath = normalize(basePath);
        final String normFilename = normalize(filename);
        final String filenameWithoutBasePath;
        if (basePath == null) {
            filenameWithoutBasePath = normFilename;
        } else {
            if (!normFilename.startsWith(normBasePath)) {
                throw new RuntimeException(MessageFormat
                        .format("Cannot remove basePath {0} from {1}", basePath, filename));
            }
            if (normBasePath.equals(normFilename)) {
                filenameWithoutBasePath = "";
            } else {
                final int delta;
                if (normBasePath.endsWith("/") || normBasePath.endsWith("\\")) {
                    delta = 0;
                } else {
                    delta = 1;
                }
                filenameWithoutBasePath = normFilename.substring(normBasePath.length() + delta);
                log.trace("filenamewithoutbasepath: {}", filenameWithoutBasePath);
            }
        }
        log.debug(MessageFormat.format("Removing {0} from {1} resulted in {2}", basePath, filename,
                filenameWithoutBasePath));
        return filenameWithoutBasePath;
    }

    /**
     * removeFileExtension.
     *
     * @param filename String
     * @return filename (no extension)
     * @throws RuntimeException RuntimeException
     */
    public static String removeFileExtension(final String filename) throws RuntimeException {
        if (filename == null) {
            throw new RuntimeException("Cannot remove file extension from null");
        }
        return filename.split("\\.[^\\.]*$")[0];
    }

    /**
     * normalize.
     *
     * @param filename String
     * @return normalizedFilename
     */
    public static String normalize(final String filename) {
        return FilenameHelper.normalizePathSeparators(filename);
    }

    @Override
    public String toString() {
        return this.getNormalizedName();
    }

    /**
     * getNormalizedName.
     *
     * @return normalizedName
     */
    private String getNormalizedName() {
        return this.normalizedName;
    }

    /**
     * setNormalizedName.
     *
     * @param name String
     */
    public void setNormalizedName(final String name) {
        this.normalizedName = name;
    }

    /**
     * getRootParent.
     *
     * @return rootParent
     */
    public File getRootParent() {
        return this.rootParent;
    }

    /**
     * setRootParent.
     *
     * @param file File
     */
    public void setRootParent(final File file) {
        this.rootParent = file;
    }

    /**
     * getRootSrc.
     *
     * @return rootSrc
     */
    public File getRootSrc() {
        return this.rootSrc;
    }

    /**
     * setRootSrc.
     *
     * @param file File
     */
    public void setRootSrc(final File file) {
        this.rootSrc = file;
    }

    /**
     * setBagSrc.
     *
     * @param bagDir File
     * @param src    File
     */
    public void setBagSrc(final File bagDir, final File src) {
        // TODO given the bag location, create the location the src file will exist
        // within the bag data directory, e.g. strip off parent of src and replace
        // it with the bag data dir
        this.bagSrc = new File(bagDir, src.getPath());
    }

    public void setBagSrc(final File file) {
        this.bagSrc = file;
    }

    /**
     * getBagSrc.
     *
     * @return bagSrc
     */
    public File getBagSrc() {
        return this.bagSrc;
    }

    /**
     * setBagSrc.
     *
     * @param file File
     */

    /**
     * getIsInBag.
     *
     * @return isInBag
     */
    public boolean getIsInBag() {
        return this.isInBag;
    }

    /**
     * setIsInBag.
     *
     * @param b boolean
     */
    public void setIsInBag(final boolean b) {
        this.isInBag = b;
    }

    /**
     * getIsIncluded.
     *
     * @return isIncluded
     */
    public boolean getIsIncluded() {
        return this.isIncluded;
    }

    /**
     * setIsIncluded.
     *
     * @param b boolean
     */
    public void setIsIncluded(final boolean b) {
        this.isIncluded = b;
    }

    /**
     * copyRootToBag.
     *
     * @return success
     */
    public boolean copyRootToBag() {
        final boolean success = false;

        // TODO perform the copy
        this.isInBag = true;
        return success;
    }
}
