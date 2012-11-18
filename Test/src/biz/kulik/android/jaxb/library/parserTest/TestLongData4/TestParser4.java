package biz.kulik.android.jaxb.library.parserTest.TestLongData4;

import android.test.AndroidTestCase;
import biz.kulik.android.jaxb.library.R;
import biz.kulik.android.jaxb.library.parser.AdapterTypes;
import biz.kulik.android.jaxb.library.parser.ParserImpl;

import java.io.InputStream;

/**
 * User: kulik
 * Date: 10/26/12
 * Time: 12:50 PM
 */
public class TestParser4 extends AndroidTestCase {
    private static final String TAG = TestParser4.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testParse4JSON() {
        InputStream inputStream = getContext().getResources().openRawResource(R.raw.test_4_json_big);

        ParserImpl parser = new ParserImpl(AdapterTypes.JSONAdapter);

        RootBusStop lbs;
        lbs = parser.parse(RootBusStop.class, inputStream);

        assertTestDate4(lbs);
    }

//    public void testParse4XML() {
//        InputStream inputStream = getContext().getResources().openRawResource(R.raw.test_1_xml);
//
//        ParserImpl parser = new ParserImpl(AdapterTypes.XMLAdapter);
//
//        BusStops se;
//        se = parser.parse(BusStops.class, inputStream);
//
//        assertTestDate4(lbs);
//    }

    private void assertTestDate4(RootBusStop lbs) {
        assertTrue(true);

    }

}