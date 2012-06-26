package com.alibaba.webx.restful.model.uri;


public final class PathPattern extends PatternWithGroups {

    public static final PathPattern END_OF_PATH_PATTERN    = new PathPattern(
                                                                             "",
                                                                             PathPattern.RightHandPath.capturingZeroSegments);

    public static final PathPattern OPEN_ROOT_PATH_PATTERN = new PathPattern("",
                                                                             RightHandPath.capturingZeroOrMoreSegments);

    public static enum RightHandPath {
        capturingZeroOrMoreSegments("(/.*)?"),

        capturingZeroSegments("(/)?");

        //
        private final String regex;

        private RightHandPath(String regex){
            this.regex = regex;
        }

        private String getRegex() {
            return regex;
        }
    }

    //
    final UriTemplate template;

    /**
     * Create a path pattern and post fix with {@link RightHandPath#capturingZeroOrMoreSegments}.
     * 
     * @param template the path template.
     * @see #PathPattern(String, PathPattern.RightHandPath)
     */
    public PathPattern(String template){
        this(new PathTemplate(template));
    }

    /**
     * Create a path pattern and post fix with {@link RightHandPath#capturingZeroOrMoreSegments}.
     * 
     * @param template the path template
     * @see #PathPattern(PathTemplate, PathPattern.RightHandPath)
     */
    public PathPattern(PathTemplate template){
        super(postfixWithCapturingGroup(template.getPattern().getRegex()),
              addIndexForRightHandPathCapturingGroup(template.getPattern().getGroupIndexes()));

        this.template = template;
    }

    /**
     * Create a path pattern and post fix with a right hand path pattern.
     * 
     * @param template the path template.
     * @param rhpp the right hand path pattern postfix.
     */
    public PathPattern(String template, RightHandPath rhpp){
        this(new PathTemplate(template), rhpp);
    }

    /**
     * Create a path pattern and post fix with a right hand path pattern.
     * 
     * @param template the path template.
     * @param rhpp the right hand path pattern postfix.
     */
    public PathPattern(PathTemplate template, RightHandPath rhpp){
        super(postfixWithCapturingGroup(template.getPattern().getRegex(), rhpp),
              addIndexForRightHandPathCapturingGroup(template.getPattern().getGroupIndexes()));

        this.template = template;
    }

    public UriTemplate getTemplate() {
        return template;
    }

    private static String postfixWithCapturingGroup(String regex) {
        return postfixWithCapturingGroup(regex, RightHandPath.capturingZeroOrMoreSegments);
    }

    private static String postfixWithCapturingGroup(String regex, RightHandPath rhpp) {
        if (regex.endsWith("/")) {
            regex = regex.substring(0, regex.length() - 1);
        }

        return regex + rhpp.getRegex();
    }

    private static int[] addIndexForRightHandPathCapturingGroup(int[] indexes) {
        if (indexes.length == 0) {
            return indexes;
        }

        int[] cgIndexes = new int[indexes.length + 1];
        System.arraycopy(indexes, 0, cgIndexes, 0, indexes.length);

        cgIndexes[indexes.length] = cgIndexes[indexes.length - 1] + 1;
        return cgIndexes;
    }
}
