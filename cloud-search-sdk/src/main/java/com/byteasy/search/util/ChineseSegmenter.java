package com.byteasy.search.util;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import java.util.List;
import java.util.stream.Collectors;

public class ChineseSegmenter {

    // 创建专用的分词器实例
    private static final Segment INDEX_SEGMENTER;
    private static final Segment STANDARD_SEGMENTER;

    static {
        // 1. 将业务词频调大（例如 50000），词性标注为外国人名 nrf 或专有名词 nz
        CustomDictionary.add("特朗普", "nrf 50000");
        CustomDictionary.add("通电话", "v 10000"); // 甚至把连带的常用动宾短语也加入

        // 2. 配置标准分词器
        STANDARD_SEGMENTER = HanLP.newSegment()
                .enableCustomDictionary(true)      // 启用自定义词典
                .enablePlaceRecognize(true)
                .enableCustomDictionaryForcing(true)
                .enableNameRecognize(true)         // 开启中国人名识别
                .enableTranslatedNameRecognize(true); // 【关键】：开启音译/外国人名识别（识别特朗普、普京、拜登等）

        // 3. 配置索引分词器（兼顾细粒度切分与自定义词典）
        INDEX_SEGMENTER = HanLP.newSegment()
                .enableCustomDictionary(true)
                .enablePlaceRecognize(true)
                .enableCustomDictionaryForcing(true)// 【关键】：强制优先匹配自定义词典
                .enableIndexMode(true)             // 【关键】：开启索引模式（细粒度二次切分）
                .enableNameRecognize(true)
                .enableTranslatedNameRecognize(true);
    }

    /**
     * 标准分词（搜索 Query 用）
     */
    public static String segmentStandard(String text) {
        if (text == null || text.isBlank()) return "";
        List<Term> termList = STANDARD_SEGMENTER.seg(text);
        return termList.stream()
                .map(term -> term.word.trim())
                .filter(w -> !w.isEmpty())
                .collect(Collectors.joining(" "));
    }

    /**
     * 索引分词（建索引 Index 用）
     */
    public static String segmentForIndex(String text) {
        if (text == null || text.isBlank()) return "";
        List<Term> termList = INDEX_SEGMENTER.seg(text);
        return termList.stream()
                .map(term -> term.word.trim())
                .filter(w -> !w.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public static void main(String[] args) {
        String text = "HanLP是面向生产环境的自然语言处理工具包。\n" +
                "2021年HanLPv2.1为生产环境带来次世代最先进的多语种NLP技术。\n" +
                "HanLP只做我们认为正确、先进的事情，而不一定是流行、权威的事情。\n" +
                "晓美焰来到北京立方庭参观自然语义科技公司。后来又去了山东临沂莒南\n" +
                "当下雨天地面积水分外严重。\n" +
                "总统普京与特朗普通电话讨论美国太空探索技术公司。\n" +
                "采用优等生鲜肉，欢迎新老师生前来就餐。";
        System.out.println("标准分词：" + segmentStandard(text));
        System.out.println("索引分词：" + segmentForIndex(text));
    }
}