package com.alibaba.webx.restful.uri;

import java.util.Comparator;

public class UriTemplateComparator implements Comparator<UriTemplate> {

    @Override
    public int compare(UriTemplate a, UriTemplate b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return 1;
        }
        if (b == null) {
            return -1;
        }

        if (a == UriTemplate.EMPTY && b == UriTemplate.EMPTY) {
            return 0;
        }
        if (a == UriTemplate.EMPTY) {
            return 1;
        }
        if (b == UriTemplate.EMPTY) {
            return -1;
        }

        // Compare the number of explicit
        // characters
        // Note that it is important that o2 is
        // compared against o1
        // so that a regular expression with say
        // 10 explicit characters
        // is less than a regular expression with
        // say 5 explicit characters.
        int i = b.getNumberOfExplicitCharacters() - a.getNumberOfExplicitCharacters();
        if (i != 0) {
            return i;
        }

        // If the number of explicit characters
        // is equal
        // compare the number of template
        // variables
        // Note that it is important that o2 is
        // compared against o1
        // so that a regular expression with say
        // 10 template variables
        // is less than a regular expression with
        // say 5 template variables.
        i = b.getNumberOfTemplateVariables() - a.getNumberOfTemplateVariables();
        if (i != 0) {
            return i;
        }

        // If the number of template variables is
        // equal
        // compare the number of explicit regexes
        i = b.getNumberOfExplicitRegexes() - a.getNumberOfExplicitRegexes();
        if (i != 0) {
            return i;
        }

        // If the number of explicit characters
        // and template variables
        // are equal then comapre the regexes
        // The order does not matter as long as
        // templates with different
        // explicit characters are
        // distinguishable
        return b.pattern.getRegex().compareTo(a.pattern.getRegex());
    }
}
