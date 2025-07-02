package com.example.utils;

import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Markdown 转 HTML 工具类
 * 支持多种解析模式：CommonMark、Kramdown、MultiMarkdown、Markdown
 * 线程安全，无状态，可通过静态方法直接调用
 */
public class MarkDownUtil {

    // 私有化构造方法，防止实例化
    private MarkDownUtil() {}

    //------------------------------ 预定义配置 ------------------------------
    private static final DataHolder COMMONMARK_OPTIONS = new MutableDataSet();

    private static final DataHolder KRAMDOWN_OPTIONS = new MutableDataSet()
            .set(Parser.EXTENSIONS, Arrays.asList(
                    AbbreviationExtension.create(),
                    DefinitionExtension.create(),
                    FootnoteExtension.create(),
                    TablesExtension.create(),
                    TypographicExtension.create()
            ))
            .setFrom(ParserEmulationProfile.KRAMDOWN);

    private static final DataHolder MULTI_MARKDOWN_OPTIONS = new MutableDataSet()
            .setFrom(ParserEmulationProfile.MULTI_MARKDOWN);

    private static final DataHolder MARKDOWN_OPTIONS = new MutableDataSet()
            .setFrom(ParserEmulationProfile.MARKDOWN);

    //------------------------------ 核心转换方法 ------------------------------

    /**
     * 通用 Markdown 转换方法
     * @param markdown 输入内容
     * @param options 解析配置
     * @return 生成的 HTML
     */
    private static String convert(String markdown, DataHolder options) {
        if (markdown == null || markdown.trim().isEmpty()) {
            return "";
        }

        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    //------------------------------ 快捷方法 ------------------------------

    /**
     * 转换为 CommonMark 规范的 HTML
     * @param markdown 输入内容
     * @return HTML 结果
     */
    public static String toCommonMarkHtml(String markdown) {
        return convert(markdown, COMMONMARK_OPTIONS);
    }

    /**
     * 转换为 Kramdown 风格的 HTML（支持扩展语法）
     * @param markdown 输入内容
     * @return HTML 结果
     */
    public static String toKramdownHtml(String markdown) {
        return convert(markdown, KRAMDOWN_OPTIONS);
    }

    /**
     * 转换为 MultiMarkdown 规范的 HTML
     * @param markdown 输入内容
     * @return HTML 结果
     */
    public static String toMultiMarkdownHtml(String markdown) {
        return convert(markdown, MULTI_MARKDOWN_OPTIONS);
    }

    /**
     * 转换为传统 Markdown 的 HTML
     * @param markdown 输入内容
     * @return HTML 结果
     */
    public static String toClassicMarkdownHtml(String markdown) {
        return convert(markdown, MARKDOWN_OPTIONS);
    }

    //------------------------------ 高级用法 ------------------------------

    /**
     * 自定义转换配置
     * @param markdown 输入内容
     * @param extensions 要启用的扩展列表（例如 TablesExtension.create()）
     * @param profile 解析器模拟配置
     * @return HTML 结果
     */
//    public static String customConvert(String markdown,
//                                       List<Exception> extensions,
//                                       ParserEmulationProfile profile) {
//        MutableDataSet options = new MutableDataSet()
//                .set(Parser.EXTENSIONS, extensions)
//                .setFrom(profile);
//
//        return convert(markdown, options);
//    }

    /**
     * 安全转换（处理异常）
     * @param markdown 输入内容
     * @param options 解析配置
     * @return HTML 结果，若出错返回空字符串
     */
    public static String safeConvert(String markdown, DataHolder options) {
        try {
            return convert(markdown, options);
        } catch (Exception e) {
            // 实际项目应使用日志记录
            System.err.println("Markdown 转换失败: " + e.getMessage());
            return "";
        }
    }
}