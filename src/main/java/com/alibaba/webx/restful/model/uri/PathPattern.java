package com.alibaba.webx.restful.model.uri;

import java.util.Comparator;

public final class PathPattern extends PatternWithGroups {

    public static final PathPattern             EMPTY_PATTERN          = new PathPattern();

    public static final PathPattern             END_OF_PATH_PATTERN    = new PathPattern(
                                                                                         "",
                                                                                         PathPattern.RightHandPath.capturingZeroSegments);

    public static final PathPattern             OPEN_ROOT_PATH_PATTERN = new PathPattern(
                                                                                         "",
                                                                                         RightHandPath.capturingZeroOrMoreSegments);
    /**
     * Path pattern comparator that defers to {@link UriTemplate#COMPARATOR comparing the templates} associated with the
     * patterns.
     */
    public static final Comparator<PathPattern> COMPARATOR             = new PathPatternComparator();

    /**
     * The set of right hand path patterns that may be appended to a path pattern.
     */
    public static enum RightHandPath {

        /**
         * A capturing group that matches zero or more path segments and keeps the matching path template open.
         */
        capturingZeroOrMoreSegments("(/.*)?"),
        /**
         * A capturing group that matches zero segments and effectively closes the matching path template.
         */
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

    /**
     * Return a new path pattern with a same path template but a {@link RightHandPath#capturingZeroSegments closed}
     * right hand path.
     * 
     * @param pattern an (open) path pattern to convert to a closed pattern.
     * @return closed path pattern for the same path template.
     */
    public static PathPattern asClosed(PathPattern pattern) {
        return new PathPattern(pattern.getTemplate().getTemplate(), RightHandPath.capturingZeroSegments);
    }

    //
    final UriTemplate template;

    private PathPattern(){
        super();
        this.template = UriTemplate.EMPTY;
    }

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
