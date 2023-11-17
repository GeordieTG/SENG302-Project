package nz.ac.canterbury.seng302.tab.unit.utility;

import java.util.List;
import nz.ac.canterbury.seng302.tab.utility.ConvertingUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for testing converting utility
 */
class ConvertingUtilTest {

    @Test
    void convertingStringToArrayEmptyString() {
        String emptyString = "";
        List<String[]> returned = ConvertingUtil.positionStringToArray(emptyString);
        Assertions.assertEquals(1, returned.size());
        Assertions.assertEquals(1, returned.get(0).length);
        Assertions.assertEquals("", returned.get(0)[0]);
    }

    @Test
    void convertingStringTo1Row() {
        String oneRow = "1,2,3";
        List<String[]> returned = ConvertingUtil.positionStringToArray(oneRow);
        Assertions.assertEquals(1, returned.size());
        Assertions.assertEquals(3, returned.get(0).length);
        Assertions.assertEquals("1", returned.get(0)[0]);
        Assertions.assertEquals("2", returned.get(0)[1]);
        Assertions.assertEquals("3", returned.get(0)[2]);
    }

    @Test
    void convertingStringTo2Row() {
        String twoRow = "1,2,3-4,5,6";
        List<String[]> returned = ConvertingUtil.positionStringToArray(twoRow);
        Assertions.assertEquals(2, returned.size());
        Assertions.assertEquals(3, returned.get(0).length);
        Assertions.assertEquals(3, returned.get(1).length);
        Assertions.assertEquals("1", returned.get(0)[0]);
        Assertions.assertEquals("2", returned.get(0)[1]);
        Assertions.assertEquals("3", returned.get(0)[2]);
        Assertions.assertEquals("4", returned.get(1)[0]);
        Assertions.assertEquals("5", returned.get(1)[1]);
        Assertions.assertEquals("6", returned.get(1)[2]);
    }

    @Test
    void convertingStringTo2RowSecondRowEmpty() {
        String twoRow = "1,2,3-";
        List<String[]> returned = ConvertingUtil.positionStringToArray(twoRow);
        Assertions.assertEquals(1, returned.size());
        Assertions.assertEquals(3, returned.get(0).length);
        Assertions.assertEquals("1", returned.get(0)[0]);
        Assertions.assertEquals("2", returned.get(0)[1]);
        Assertions.assertEquals("3", returned.get(0)[2]);
    }

    @Test
    void convertingStringTo2RowFirstRowEmpty() {
        String twoRow = "-1,2,3";
        List<String[]> returned = ConvertingUtil.positionStringToArray(twoRow);
        Assertions.assertEquals(2, returned.size());
        Assertions.assertEquals(1, returned.get(0).length);
        Assertions.assertEquals(3, returned.get(1).length);
        Assertions.assertEquals("", returned.get(0)[0]);
        Assertions.assertEquals("1", returned.get(1)[0]);
        Assertions.assertEquals("2", returned.get(1)[1]);
        Assertions.assertEquals("3", returned.get(1)[2]);
    }
}
