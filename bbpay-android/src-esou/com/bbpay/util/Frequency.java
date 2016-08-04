package com.bbpay.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Frequency {
    private boolean changeFlag;
    private Map<String, Integer> map;
    private Set<Entiry> set;
    public class Entiry implements Comparable<Entiry> {
        private Integer count;
        private String key;
        public Entiry(String s, Integer integer) {
            super();
            key = s;
            count = integer;
        }
        @Override
        public int compareTo(Entiry entiry) {
            int i = count.intValue() - entiry.count.intValue();
            if (i == 0) return key.compareTo(entiry.key);
            else return -i;
        }
        public Integer getCount() {
            return count;
        }
        public String getKey() {
            return key;
        }
        @Override
        public String toString() {
            return new StringBuilder(String.valueOf(key)).append(" 出现的次数为：").append(count).toString();
        }
    }
    public Frequency() {
        map = new HashMap<String, Integer>();
        set = new TreeSet<Entiry>();
        changeFlag = true;
    }
    public void addStatistics(String key) {
        Integer value = map.get(key);
        map.put(key, value == null ? 1 : value + 1);
    }
    private void dataChanged() {
        if (changeFlag) {
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                set.add(new Entiry(s, map.get(s)));
                changeFlag = false;
            }
        }
    }
    public List<Entiry> getDataDesc() {
        dataChanged();
        Iterator<Entiry> iterator = set.iterator();
        ArrayList<Entiry> arraylist = new ArrayList<Entiry>();
        do {
            if (!iterator.hasNext()) return arraylist;
            Entiry entiry = iterator.next();
            arraylist.add(entiry);
            System.out.println(entiry.toString());
        } while (true);
    }
    public Entiry getMaxValueItem() {
        dataChanged();
        Iterator<Entiry> iterator = set.iterator();
        Entiry entiry = null;
        if (iterator.hasNext()) {
            entiry = iterator.next();
            System.out.println(entiry.toString());
        }
        return entiry;
    }
}
