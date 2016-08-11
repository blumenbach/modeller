package org.blume.modeller.bag;

import java.io.File;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.loc.repository.bagit.utilities.FilenameHelper;

/**
 * Bagger needs to know where the file came from so that it can be retrieved:
 * rootSrc,
 * and also where the file belongs within the bag: bagSrc.
 * Once the logical location of bagSrc becomes a physical copy isInBag=true
 * If the file is unselected from the BagTree, it is marked to be removed from
 * the bag
 * by setting isIncluded=false.
 * 
 * In order to create a bag, all data files to be included are copied to the bag
 * data dir.
 * If they already exist they are written over, or deleted if marked as
 * isIncluded=false.
 *
 * If the file comes from a pre-existing bag, then the rootSrc and bagSrc will
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

  @Override
  public String toString() {
    return this.getNormalizedName();
  }

  public void setNormalizedName(String name) {
    this.normalizedName = name;
  }

  public String getNormalizedName() {
    return this.normalizedName;
  }

  public void setRootParent(File file) {
    this.rootParent = file;
  }

  public File getRootParent() {
    return this.rootParent;
  }

  public void setRootSrc(File file) {
    this.rootSrc = file;
  }

  public File getRootSrc() {
    return this.rootSrc;
  }

  public void setBagSrc(File bagDir, File src) {
    // TODO given the bag location, create the location the src file will exist
    // within the bag data directory, e.g. strip off parent of src and replace
    // it with the bag data dir
    File localBagSrc = src;
    File f = new File(bagDir, localBagSrc.getPath());
    this.bagSrc = f;
  }

  public void setBagSrc(File file) {
    this.bagSrc = file;
  }

  public File getBagSrc() {
    return this.bagSrc;
  }

  public void setIsInBag(boolean b) {
    this.isInBag = b;
  }

  public boolean getIsInBag() {
    return this.isInBag;
  }

  public void setIsIncluded(boolean b) {
    this.isIncluded = b;
  }

  public boolean getIsIncluded() {
    return this.isIncluded;
  }

  public boolean copyRootToBag() {
    boolean success = false;

    // TODO perform the copy
    this.isInBag = true;
    return success;
  }

  public static String removeBasePath(String basePath, String filename) throws RuntimeException {
    if (filename == null) {
      throw new RuntimeException("Cannot remove basePath from null");
    }
    String normBasePath = normalize(basePath);
    String normFilename = normalize(filename);
    String filenameWithoutBasePath = null;
    if (basePath == null) {
      filenameWithoutBasePath = normFilename;
    }
    else {
      if (!normFilename.startsWith(normBasePath)) {
        throw new RuntimeException(MessageFormat.format("Cannot remove basePath {0} from {1}", basePath, filename));
      }
      if (normBasePath.equals(normFilename)) {
        filenameWithoutBasePath = "";
      }
      else {
        int delta;
        if (normBasePath.endsWith("/") || normBasePath.endsWith("\\")){
          delta = 0;
        }
        else{
          delta = 1;
        }
        filenameWithoutBasePath = normFilename.substring(normBasePath.length() + delta);
        log.trace("filenamewithoutbasepath: {}", filenameWithoutBasePath);
      }
    }
    log.debug(MessageFormat.format("Removing {0} from {1} resulted in {2}", basePath, filename, filenameWithoutBasePath));
    return filenameWithoutBasePath;
  }

  public static String normalize(String filename) {
    return FilenameHelper.normalizePathSeparators(filename);
  }
}
