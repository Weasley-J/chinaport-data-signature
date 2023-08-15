package cn.alphahub.eport.signature.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 计算两个词的相似度
 */
@SuppressWarnings("deprecation")
public class StringSimilarityExample {
    public static void main(String[] args) {
        String str1 = "hello";

        String str2 = "holla";

        String str3 = "hello";

        int distance = StringUtils.getLevenshteinDistance(str1, str2);
        double similarity = 1.0 - (double) distance / Math.max(str1.length(), str2.length());
        System.out.println("1 Levenshtein distance: " + distance);
        System.out.println("1 Similarity: " + similarity);


        int distance2 = StringUtils.getLevenshteinDistance(str1, str3);
        double similarity2 = 1.0 - (double) distance / Math.max(str1.length(), str3.length());

        System.out.println("2 Levenshtein distance: " + distance2);
        System.out.println("2 Similarity: " + similarity2);

        str1 = "你好啊, 还啊哈";
        str3 = "你好啊！我在春天等你。";
        distance2 = StringUtils.getLevenshteinDistance(str1, str3);
        similarity2 = 1.0 - (double) distance / Math.max(str1.length(), str3.length());

        System.out.println("3 Levenshtein distance: " + distance2);
        System.out.println("3 Similarity: " + similarity2);

    }
}
