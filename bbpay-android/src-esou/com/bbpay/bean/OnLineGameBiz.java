package com.bbpay.bean;
import java.util.ArrayList;
import java.util.List;

public class OnLineGameBiz extends FeeBean {
    private static final long serialVersionUID = 2903947171136613112L;
    private List<OnlineGameStep> steps = new ArrayList<OnlineGameStep>();
    public List<OnlineGameStep> getSteps() {
        return steps;
    }
    public void setSteps(List<OnlineGameStep> list) {
        steps = list;
    }
}
