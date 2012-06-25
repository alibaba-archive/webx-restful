package com.alibaba.webx.restful.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Variant;

public class VariantListBuilder extends Variant.VariantListBuilder {

    private List<Variant>         variants;
    private final List<MediaType> mediaTypes = new ArrayList<MediaType>();
    private final List<Locale>    languages  = new ArrayList<Locale>();
    private final List<String>    charsets   = new ArrayList<String>();
    private final List<String>    encodings  = new ArrayList<String>();

    @Override
    public List<Variant> build() {
        if (variants == null) {
            variants = new ArrayList<Variant>();
        }

        return variants;
    }

    @Override
    public VariantListBuilder add() {
        if (variants == null) {
            variants = new ArrayList<Variant>();
        }

        addMediaTypes();

        charsets.clear();
        languages.clear();
        encodings.clear();
        mediaTypes.clear();

        return this;
    }

    private void addMediaTypes() {
        if (mediaTypes.isEmpty()) {
            addLanguages(null);
        } else {
            for (MediaType mediaType : mediaTypes) {
                addLanguages(mediaType);
            }
        }
    }

    private void addLanguages(MediaType mediaType) {
        if (languages.isEmpty()) {
            addEncodings(mediaType, null);
        } else {
            for (Locale language : languages) {
                addEncodings(mediaType, language);
            }
        }
    }

    private void addEncodings(MediaType mediaType, Locale language) {
        if (encodings.isEmpty()) {
            addVariant(mediaType, language, null);
        } else {
            for (String encoding : encodings) {
                addVariant(mediaType, language, encoding);
            }
        }
    }

    private void addVariant(MediaType mediaType, Locale language, String encoding) {
        variants.add(new Variant(mediaType, language, encoding));
    }

    @Override
    public VariantListBuilder languages(Locale... languages) {
        this.languages.addAll(Arrays.asList(languages));
        return this;
    }

    @Override
    public VariantListBuilder encodings(String... encodings) {
        this.encodings.addAll(Arrays.asList(encodings));
        return this;
    }

    @Override
    public VariantListBuilder mediaTypes(MediaType... mediaTypes) {
        this.mediaTypes.addAll(Arrays.asList(mediaTypes));
        return this;
    }
}
