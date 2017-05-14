package cool.pandora.modeller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static cool.pandora.modeller.DocManifestBuilder.getPageIdList;

public class canvasPageMapTest {

    public static void main(String[] args) throws IOException {
        List<String> pageIdList;
        String url = "resource://data/test_007.hocr";
        hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
        pageIdList = getPageIdList(hocr);
        CanvasPageMap canvasPageMap = CanvasPageMap.init()
                .canvasContainerURI(URI.create("http://localhost:8080/fcrepo/rest/collection/test/007/canvas"))
                .pageIdList(pageIdList).build();
        System.out.println(canvasPageMap);
    }
}