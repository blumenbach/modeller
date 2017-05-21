package cool.pandora.modeller.ui.handlers.common;

import org.apache.commons.lang.StringUtils;
import cool.pandora.modeller.CanvasPageMap;
import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.PageIdMap;
import cool.pandora.modeller.hOCRData;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.pandora.modeller.DocManifestBuilder.getBboxForId;
import static cool.pandora.modeller.DocManifestBuilder.getAreaIdListforPage;
import static cool.pandora.modeller.DocManifestBuilder.getLineIdListforArea;
import static cool.pandora.modeller.DocManifestBuilder.getWordIdListforLine;
import static cool.pandora.modeller.DocManifestBuilder.getWordIdListforPage;

/**
 * Node Map
 *
 * @author Christopher Johnson
 */
public final class NodeMap {

    private NodeMap() {
    }

    /**
     * @param hocr       hOCRData
     * @param pageIdList List
     * @return Map
     */
    public static Map<String, String> getBBoxMap(final hOCRData hocr, final List<String> pageIdList) {
        final Map<String, String> bboxMap = new HashMap<>();
        for (final String pageId : pageIdList) {
            final String bbox = getBboxForId(hocr, pageId);
            bboxMap.put(pageId, bbox);
        }
        return bboxMap;
    }

    /**
     * @param hocr       hOCRData
     * @param areaIdList List
     * @return Map
     */
    public static Map<String, String> getBBoxAreaMap(final hOCRData hocr, final List<String> areaIdList) {
        final Map<String, String> bboxMap = new HashMap<>();
        for (final String areaId : areaIdList) {
            final String bbox = getBboxForId(hocr, areaId);
            bboxMap.put(areaId, bbox);
        }
        return bboxMap;
    }

    /**
     * @param hocr       hOCRData
     * @param lineIdList List
     * @return Map
     */
    public static Map<String, String> getBBoxLineMap(final hOCRData hocr, final List<String> lineIdList) {
        final Map<String, String> bboxMap = new HashMap<>();
        for (final String lineId : lineIdList) {
            final String bbox = DocManifestBuilder.getBboxForId(hocr, lineId);
            bboxMap.put(lineId, bbox);
        }
        return bboxMap;
    }

    /**
     * @param hocr       hOCRData
     * @param wordIdList List
     * @return Map
     */
    public static Map<String, String> getBBoxWordMap(final hOCRData hocr, final List<String> wordIdList) {
        final Map<String, String> bboxMap = new HashMap<>();
        for (final String wordId : wordIdList) {
            final String bbox = DocManifestBuilder.getBboxForId(hocr, wordId);
            bboxMap.put(wordId, bbox);
        }
        return bboxMap;
    }

    /**
     * @param hocr       hOCRData
     * @param wordIdList List
     * @return Map
     */
    public static Map<String, String> getCharWordMap(final hOCRData hocr, final List<String> wordIdList) {
        final Map<String, String> charMap = new HashMap<>();
        for (final String wordId : wordIdList) {
            final String chars = DocManifestBuilder.getCharsForId(hocr, wordId);
            charMap.put(wordId, chars);
        }
        return charMap;
    }

    /**
     * @param pageIdList         List
     * @param canvasContainerURI URI
     * @return Map
     */
    public static Map<String, String> getCanvasPageMap(final List<String> pageIdList, final URI canvasContainerURI) {
        try {
            final CanvasPageMap canvasPageMap;
            canvasPageMap = CanvasPageMap.init().canvasContainerURI(canvasContainerURI).pageIdList(pageIdList).build();
            return canvasPageMap.render();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param pageIdList String
     * @return Map
     */
    public static Map<String, String> getPageIdMap(final List<String> pageIdList) {
        try {
            final PageIdMap pageIdMap;
            pageIdMap = PageIdMap.init().pageIdList(pageIdList).build();
            return pageIdMap.render();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param hocr       hOCRData
     * @param pageIdList List
     * @return Map
     */
    public static Map<String, List<String>> getAreaIdMap(final hOCRData hocr, final List<String> pageIdList) {
        final Map<String, List<String>> nodemap = new HashMap<>();
        List<String> areaIdList;
        for (String pageId : pageIdList) {
            areaIdList = getAreaIdListforPage(hocr, pageId);
            for (int i = 0; i < areaIdList.size(); i++) {
                final String areaId = StringUtils.substringAfter(areaIdList.get(i), "_");
                areaIdList.set(i, areaId);
            }
            pageId = StringUtils.substringAfter(pageId, "_");
            nodemap.put(pageId, areaIdList);
        }
        return nodemap;
    }

    /**
     * @param hocr       hOCRData
     * @param areaIdList List
     * @return Map
     */
    public static Map<String, List<String>> getLineIdMap(final hOCRData hocr, final List<String> areaIdList) {
        final Map<String, List<String>> nodemap = new HashMap<>();
        List<String> lineIdList;
        for (String areaId : areaIdList) {
            lineIdList = getLineIdListforArea(hocr, areaId);
            for (int i = 0; i < lineIdList.size(); i++) {
                final String lineId = StringUtils.substringAfter(lineIdList.get(i), "_");
                lineIdList.set(i, lineId);
            }
            areaId = StringUtils.substringAfter(areaId, "_");
            nodemap.put(areaId, lineIdList);
        }
        return nodemap;
    }

    /**
     * @param hocr       hOCRData
     * @param lineIdList List
     * @return Map
     */
    public static Map<String, List<String>> getWordIdMap(final hOCRData hocr, final List<String> lineIdList) {
        final Map<String, List<String>> nodemap = new HashMap<>();
        List<String> wordIdList;
        for (String lineId : lineIdList) {
            wordIdList = getWordIdListforLine(hocr, lineId);
            for (int i = 0; i < wordIdList.size(); i++) {
                final String wordId = StringUtils.substringAfter(wordIdList.get(i), "_");
                wordIdList.set(i, wordId);
            }
            lineId = StringUtils.substringAfter(lineId, "_");
            nodemap.put(lineId, wordIdList);
        }
        return nodemap;
    }

    /**
     * @param hocr       hOCRData
     * @param pageIdList List
     * @return Map
     */
    public static Map<String, List<String>> getWordsForPageMap(final hOCRData hocr, final List<String> pageIdList) {
        final Map<String, List<String>> nodemap = new HashMap<>();
        List<String> wordIdList;
        for (String pageId : pageIdList) {
            wordIdList = getWordIdListforPage(hocr, pageId);
            for (int i = 0; i < wordIdList.size(); i++) {
                final String wordId = StringUtils.substringAfter(wordIdList.get(i), "_");
                wordIdList.set(i, wordId);
            }
            pageId = StringUtils.substringAfter(pageId, "_");
            nodemap.put(pageId, wordIdList);
        }
        return nodemap;
    }
}
