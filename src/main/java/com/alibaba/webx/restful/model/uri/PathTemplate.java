package com.alibaba.webx.restful.model.uri;

public final class PathTemplate extends UriTemplate {

    private static final class PathTemplateParser extends UriTemplateParser {

        public PathTemplateParser(final String path){
            super(path);
        }

        @Override
        protected String encodeLiteralCharacters(final String literalCharacters) {
            return UriComponent.contextualEncode(literalCharacters, UriComponent.Type.PATH);
        }
    }

    public PathTemplate(final String path){
        super(new PathTemplateParser(PathTemplate.prefixWithSlash(path)));
    }

    private static String prefixWithSlash(final String path) {
        return path.startsWith("/") ? path : "/" + path;
    }

}
