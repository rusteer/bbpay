package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import com.bbpay.server.entity.BizEntity;

public class TTest {
    static List<Integer> check1(Integer[] a, int sum) {
        List<Integer> list = new ArrayList<Integer>();
        for (int ele : a) {
            if (ele == sum) {
                list.add(ele);
            }
        }
        return list;
    }
    static List<List<Integer>> check2(Integer[] source, int targetSum) {
        List<List<Integer>> list = new ArrayList<List<Integer>>();
        for (int index1 = 0; index1 < source.length; index1++) {
            for (int index2 = index1 + 1; index2 < source.length; index2++) {
                if (source[index1] + source[index2] == targetSum) {
                    list.add(Arrays.asList(new Integer[] { source[index1], source[index2] }));
                }
                for (int index3 = index2 + 1; index3 < source.length; index3++) {
                    if (source[index1] + source[index2] + source[index3] == targetSum) {
                        list.add(Arrays.asList(new Integer[] { source[index1], source[index2], source[index3] }));
                    }
                    for (int index4 = index3 + 1; index4 < source.length; index4++) {
                        if (source[index1] + source[index2] + source[index3] + source[index4] == targetSum) {
                            list.add(Arrays.asList(new Integer[] { source[index1], source[index2], source[index3], source[index4] }));
                        }
                        for (int index5 = index4 + 1; index5 < source.length; index5++) {
                            if (source[index1] + source[index2] + source[index3] + source[index4] + source[index5] == targetSum) {
                                list.add(Arrays.asList(new Integer[] { source[index1], source[index2], source[index3], source[index4] }));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
    public static void filter(List<BizEntity> list, int price) {
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i).getPrice() > price) {
                list.remove(i);
            }
        }
    }
    static List<BizEntity> getBizList() {
        Random random = new Random();
        List<BizEntity> bizList = new ArrayList<BizEntity>();
        int[] prices = { 8, 2, 1, 2, 4, 5, 8, 12, 18, 13, 18, 30 };
        for (int element : prices) {
            BizEntity entity = new BizEntity();
            entity.setId(Long.valueOf(String.valueOf(element).hashCode()));
            entity.setPrice(element);
            entity.setHotLevel(random.nextInt(10) + 1);
            bizList.add(entity);
        }
        return bizList;
    }
    public static void main(String args[]) {
        
        
        
        int sum = 16;
        Integer[] a = { 10, 7, 6, 5, 4, 2, 1, 1, 1 };
        //System.out.println(check1(a, sum));
        System.out.println(check2(a, sum));
        /*for (int i = 0; i < numCount; i++) {
            System.out.println(String.format("for (int index%d = %d; index1 < a.length; index1++) {", i,i));
            System.out.println(String.format("  int num%d = a[index%d];", i, i));
            while(i<numCount){
                System.out.println(String.format("for (int index%d = %d; index1 < a.length; index1++) {", i,i));
            }
            System.out.println("}");
        }
        for (int index1 = 0; index1 < a.length; index1++) {
            int num = a[index1];
            for (int index2 = index1 + 1; index2 < a.length; index2++) {
                int num2 = a[index2];
                if (num + num2 >= sum) {
                    if (num + num2 == sum) {
                        System.out.println(num + "," + num2);
                    }
                }
                for (int index3 = index2 + 1; index3 < a.length; index3++) {
                    int num3 = a[index3];
                    if (num + num2 + num3 == sum) {
                        System.out.println(num + "," + num2 + "," + num3);
                    }
                }
            }
        }*/
    }
    public static void sort(List<BizEntity> list) {
        Collections.sort(list, new Comparator<BizEntity>() {
            @Override
            public int compare(BizEntity o1, BizEntity o2) {
                int result = Integer.valueOf(o2.getPrice()).compareTo(o1.getPrice());
                if (result == 0) {
                    result = Integer.valueOf(o2.getHotLevel()).compareTo(o1.getHotLevel());
                }
                return result;
            }
        });
    }
}
