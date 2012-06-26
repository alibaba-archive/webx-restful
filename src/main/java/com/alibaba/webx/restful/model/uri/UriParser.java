package com.alibaba.webx.restful.model.uri;

public class UriParser {

    private static final String ERROR_STATE = "The parser was not executed yet. Call the parse() method first.";
    private final String        input;
    private CharacterIterator   ci;
    private String              scheme;
    private String              userInfo;
    private String              host;
    private String              port;
    private String              query;
    private String              path;
    private String              fragment;
    private String              ssp;
    private String              authority;
    private boolean             opaque;
    private boolean             parserExecuted;

    UriParser(String uri){
        this.input = uri;
    }

    private String parseComponentWithIP(String delimiters, boolean mayEnd) {
        return parseComponent(delimiters, mayEnd, true);
    }

    private String parseComponent(String delimiters, boolean mayEnd) {
        return parseComponent(delimiters, mayEnd, false);
    }

    private String parseComponent(String delimiters, boolean mayEnd, boolean isIp) {

        int curlyBracketsCount = 0;
        int squareBracketsCount = 0;
        Character c = null;

        StringBuilder sb = new StringBuilder();

        boolean endOfInput = false;
        c = ci.current();
        while (!endOfInput) {
            if (c == '{') {
                curlyBracketsCount++;
                sb.append(c);
            } else if (c == '}') {
                curlyBracketsCount--;
                sb.append(c);
            } else if (isIp && c == '[') {
                squareBracketsCount++;
                sb.append(c);
            } else if (isIp && c == ']') {
                squareBracketsCount--;
                sb.append(c);

                // test IPv6 or reqular expressions in the template params
            } else if ((delimiters != null && delimiters.indexOf(c) >= 0) && (!isIp || squareBracketsCount == 0)
                       && (curlyBracketsCount == 0)) {
                return sb.length() == 0 ? null : sb.toString();
            } else {
                sb.append(c);
            }
            endOfInput = !ci.hasNext();
            if (!endOfInput) {
                c = ci.next();
            }
        }
        if (mayEnd) {
            return sb.length() == 0 ? null : sb.toString();
        }
        throw new IllegalArgumentException("does not end by a delimiter '" + delimiters + "'");
    }

    /**
     * Parses the input string URI. After calling this method The result components can be retrieved by calling
     * appropriate getter methods like {@link #getHost()}, {@link #getPort()}, etc.
     */
    public void parse() {
        this.parserExecuted = true;
        this.ci = new CharacterIterator(input);
        ci.next();
        String comp = parseComponent(":/?#", true);

        if (ci.hasNext()) {
            this.ssp = ci.getInput().substring(ci.pos() + 1);
        }

        this.opaque = false;
        if (ci.current() == ':') {
            // absolute
            scheme = comp;
            char c = ci.next();
            if (c == '/') {
                // hierarchical
                parseHierarchicalUri();

            } else {
                // opaque
                this.opaque = true;
            }
        } else {
            ci.setPosition(0);
            // relative
            if (ci.current() == '/') {
                parseHierarchicalUri();
            } else {
                parsePath();
            }
        }
    }

    private void parseHierarchicalUri() {
        if (ci.peek() == '/') {
            // authority
            ci.next();
            ci.next();
            parseAuthority();

        }
        if (!ci.hasNext()) {
            return;
        }
        parsePath();

    }

    private void parseAuthority() {
        int start = ci.pos();
        String comp = parseComponentWithIP("@:/?#", true);
        if (ci.current() == '@') {
            this.userInfo = comp;
            if (!ci.hasNext()) {
                return;
            }
            ci.next();
            comp = parseComponentWithIP(":/?#", true);
        }

        this.host = comp;

        if (ci.current() == ':') {
            if (!ci.hasNext()) {
                return;
            }
            ci.next();
            this.port = parseComponent("/?#", true);
        }
        this.authority = ci.getInput().substring(start, ci.pos());
        if (this.authority.length() == 0) {
            this.authority = null;
        }

    }

    private void parsePath() {
        this.path = parseComponent("?#", true);

        if (ci.current() == '?') {
            if (!ci.hasNext()) {
                return;
            }
            ci.next(); // skip ?

            this.query = parseComponent("#", true);
        }

        if (ci.current() == '#') {
            if (!ci.hasNext()) {
                return;
            }
            ci.next(); // skip #

            this.fragment = parseComponent(null, true);
        }
    }

    /**
     * Returns parsed scheme specific part. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Scheme specific part.
     */
    public String getSsp() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return ssp;
    }

    /**
     * Returns parsed scheme component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Scheme.
     */
    public String getScheme() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return scheme;
    }

    /**
     * Returns parsed user info component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return User info.
     */
    public String getUserInfo() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return userInfo;
    }

    /**
     * Returns parsed host component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Host.
     */
    public String getHost() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return host;
    }

    /**
     * Returns parsed port component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Port.
     */
    public String getPort() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return port;
    }

    /**
     * Returns parsed query component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Query.
     */
    public String getQuery() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return query;
    }

    /**
     * Returns parsed path component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Path.
     */
    public String getPath() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return path;
    }

    /**
     * Returns parsed fragment component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Fragment.
     */
    public String getFragment() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return fragment;
    }

    /**
     * Returns parsed authority component. The {@link #parse() method} must be called before executing this method.
     * 
     * @return Authority.
     */
    public String getAuthority() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return authority;
    }

    /**
     * Returns whether the input string URI is opaque. The {@link #parse() method} must be called before executing this
     * method.
     * 
     * @return True if the uri is opaque.
     */
    public boolean isOpaque() {
        if (!parserExecuted) {
            throw new IllegalStateException(ERROR_STATE);
        }
        return opaque;
    }
}
