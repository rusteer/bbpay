package com.bbpay.protocolstack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import com.bbpay.bean.FeeBean;

public class EpayXMLParser {
    private List<FeeBean> allFees;
    private void endTag(XmlPullParser xmlpullparser) {}
    private void startDocument(XmlPullParser xmlpullparser) {
        allFees = new ArrayList<FeeBean>();
    }
    private void startTag(XmlPullParser xmlpullparser) throws XmlPullParserException, IOException {}
}
