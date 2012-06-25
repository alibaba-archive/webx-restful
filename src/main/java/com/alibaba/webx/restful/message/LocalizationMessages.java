package com.alibaba.webx.restful.message;

import com.alibaba.webx.restful.message.l10n.Localizable;
import com.alibaba.webx.restful.message.l10n.LocalizableMessageFactory;
import com.alibaba.webx.restful.message.l10n.Localizer;

public final class LocalizationMessages
{
  private static final LocalizableMessageFactory messageFactory = new LocalizableMessageFactory("org.glassfish.jersey.server.internal.localization");
  private static final Localizer localizer = new Localizer();

  public static Localizable localizableGET_CONSUMES_ENTITY(Object arg0) {
    return messageFactory.getMessage("get.consumes.entity", new Object[] { arg0 });
  }

  public static String GET_CONSUMES_ENTITY(Object arg0)
  {
    return localizer.localize(localizableGET_CONSUMES_ENTITY(arg0));
  }

  public static Localizable localizableDEFAULT_COULD_NOT_PROCESS_METHOD(Object arg0, Object arg1) {
    return messageFactory.getMessage("default.could.not.process.method", new Object[] { arg0, arg1 });
  }

  public static String DEFAULT_COULD_NOT_PROCESS_METHOD(Object arg0, Object arg1)
  {
    return localizer.localize(localizableDEFAULT_COULD_NOT_PROCESS_METHOD(arg0, arg1));
  }

  public static Localizable localizableWARNING_MSG(Object arg0) {
    return messageFactory.getMessage("warning.msg", new Object[] { arg0 });
  }

  public static String WARNING_MSG(Object arg0)
  {
    return localizer.localize(localizableWARNING_MSG(arg0));
  }

  public static Localizable localizableWARNINGS_DETECTED_WITH_RESOURCE_CLASSES(Object arg0) {
    return messageFactory.getMessage("warnings.detected.with.resource.classes", new Object[] { arg0 });
  }

  public static String WARNINGS_DETECTED_WITH_RESOURCE_CLASSES(Object arg0)
  {
    return localizer.localize(localizableWARNINGS_DETECTED_WITH_RESOURCE_CLASSES(arg0));
  }

  public static Localizable localizableGET_CONSUMES_FORM_PARAM(Object arg0) {
    return messageFactory.getMessage("get.consumes.form.param", new Object[] { arg0 });
  }

  public static String GET_CONSUMES_FORM_PARAM(Object arg0)
  {
    return localizer.localize(localizableGET_CONSUMES_FORM_PARAM(arg0));
  }

  public static Localizable localizableSUBRES_LOC_HAS_ENTITY_PARAM(Object arg0) {
    return messageFactory.getMessage("subres.loc.has.entity.param", new Object[] { arg0 });
  }

  public static String SUBRES_LOC_HAS_ENTITY_PARAM(Object arg0)
  {
    return localizer.localize(localizableSUBRES_LOC_HAS_ENTITY_PARAM(arg0));
  }

  public static Localizable localizableNON_PUB_SUB_RES_LOC(Object arg0) {
    return messageFactory.getMessage("non.pub.sub.res.loc", new Object[] { arg0 });
  }

  public static String NON_PUB_SUB_RES_LOC(Object arg0)
  {
    return localizer.localize(localizableNON_PUB_SUB_RES_LOC(arg0));
  }

  public static Localizable localizableRES_URI_PATH_INVALID(Object arg0, Object arg1) {
    return messageFactory.getMessage("res.uri.path.invalid", new Object[] { arg0, arg1 });
  }

  public static String RES_URI_PATH_INVALID(Object arg0, Object arg1)
  {
    return localizer.localize(localizableRES_URI_PATH_INVALID(arg0, arg1));
  }

  public static Localizable localizableNEW_AR_CREATED_BY_INTROSPECTION_MODELER(Object arg0) {
    return messageFactory.getMessage("new.ar.created.by.introspection.modeler", new Object[] { arg0 });
  }

  public static String NEW_AR_CREATED_BY_INTROSPECTION_MODELER(Object arg0)
  {
    return localizer.localize(localizableNEW_AR_CREATED_BY_INTROSPECTION_MODELER(arg0));
  }

  public static Localizable localizableRC_NOT_MODIFIABLE() {
    return messageFactory.getMessage("rc.not.modifiable", new Object[0]);
  }

  public static String RC_NOT_MODIFIABLE()
  {
    return localizer.localize(localizableRC_NOT_MODIFIABLE());
  }

  public static Localizable localizableERROR_MSG(Object arg0) {
    return messageFactory.getMessage("error.msg", new Object[] { arg0 });
  }

  public static String ERROR_MSG(Object arg0)
  {
    return localizer.localize(localizableERROR_MSG(arg0));
  }

  public static Localizable localizableTYPE_OF_METHOD_NOT_RESOLVABLE_TO_CONCRETE_TYPE(Object arg0, Object arg1) {
    return messageFactory.getMessage("type.of.method.not.resolvable.to.concrete.type", new Object[] { arg0, arg1 });
  }

  public static String TYPE_OF_METHOD_NOT_RESOLVABLE_TO_CONCRETE_TYPE(Object arg0, Object arg1)
  {
    return localizer.localize(localizableTYPE_OF_METHOD_NOT_RESOLVABLE_TO_CONCRETE_TYPE(arg0, arg1));
  }

  public static Localizable localizableAMBIGUOUS_RESOURCE_METHOD(Object arg0) {
    return messageFactory.getMessage("ambiguous.resource.method", new Object[] { arg0 });
  }

  public static String AMBIGUOUS_RESOURCE_METHOD(Object arg0)
  {
    return localizer.localize(localizableAMBIGUOUS_RESOURCE_METHOD(arg0));
  }

  public static Localizable localizableNON_PUB_RES_METHOD(Object arg0) {
    return messageFactory.getMessage("non.pub.res.method", new Object[] { arg0 });
  }

  public static String NON_PUB_RES_METHOD(Object arg0)
  {
    return localizer.localize(localizableNON_PUB_RES_METHOD(arg0));
  }

  public static Localizable localizableAMBIGUOUS_RMS_IN(Object arg0, Object arg1, Object arg2, Object arg3) {
    return messageFactory.getMessage("ambiguous.rms.in", new Object[] { arg0, arg1, arg2, arg3 });
  }

  public static String AMBIGUOUS_RMS_IN(Object arg0, Object arg1, Object arg2, Object arg3)
  {
    return localizer.localize(localizableAMBIGUOUS_RMS_IN(arg0, arg1, arg2, arg3));
  }

  public static Localizable localizableSUBRES_METHOD_URI_PATH_INVALID(Object arg0, Object arg1) {
    return messageFactory.getMessage("subres.method.uri.path.invalid", new Object[] { arg0, arg1 });
  }

  public static String SUBRES_METHOD_URI_PATH_INVALID(Object arg0, Object arg1)
  {
    return localizer.localize(localizableSUBRES_METHOD_URI_PATH_INVALID(arg0, arg1));
  }

  public static Localizable localizableMULTIPLE_HTTP_METHOD_DESIGNATORS(Object arg0, Object arg1) {
    return messageFactory.getMessage("multiple.http.method.designators", new Object[] { arg0, arg1 });
  }

  public static String MULTIPLE_HTTP_METHOD_DESIGNATORS(Object arg0, Object arg1)
  {
    return localizer.localize(localizableMULTIPLE_HTTP_METHOD_DESIGNATORS(arg0, arg1));
  }

  public static Localizable localizableERROR_PROCESSING_METHOD(Object arg0, Object arg1) {
    return messageFactory.getMessage("error.processing.method", new Object[] { arg0, arg1 });
  }

  public static String ERROR_PROCESSING_METHOD(Object arg0, Object arg1)
  {
    return localizer.localize(localizableERROR_PROCESSING_METHOD(arg0, arg1));
  }

  public static Localizable localizableSUBRES_LOC_RETURNS_VOID(Object arg0) {
    return messageFactory.getMessage("subres.loc.returns.void", new Object[] { arg0 });
  }

  public static String SUBRES_LOC_RETURNS_VOID(Object arg0)
  {
    return localizer.localize(localizableSUBRES_LOC_RETURNS_VOID(arg0));
  }

  public static Localizable localizableERROR_UNMARSHALLING_JAXB(Object arg0) {
    return messageFactory.getMessage("error.unmarshalling.jaxb", new Object[] { arg0 });
  }

  public static String ERROR_UNMARSHALLING_JAXB(Object arg0)
  {
    return localizer.localize(localizableERROR_UNMARSHALLING_JAXB(arg0));
  }

  public static Localizable localizableERROR_MARSHALLING_JAXB(Object arg0) {
    return messageFactory.getMessage("error.marshalling.jaxb", new Object[] { arg0 });
  }

  public static String ERROR_MARSHALLING_JAXB(Object arg0)
  {
    return localizer.localize(localizableERROR_MARSHALLING_JAXB(arg0));
  }

  public static Localizable localizableSUBRES_LOC_URI_PATH_INVALID(Object arg0, Object arg1) {
    return messageFactory.getMessage("subres.loc.uri.path.invalid", new Object[] { arg0, arg1 });
  }

  public static String SUBRES_LOC_URI_PATH_INVALID(Object arg0, Object arg1)
  {
    return localizer.localize(localizableSUBRES_LOC_URI_PATH_INVALID(arg0, arg1));
  }

  public static Localizable localizableNON_PUB_SUB_RES_METHOD(Object arg0) {
    return messageFactory.getMessage("non.pub.sub.res.method", new Object[] { arg0 });
  }

  public static String NON_PUB_SUB_RES_METHOD(Object arg0)
  {
    return localizer.localize(localizableNON_PUB_SUB_RES_METHOD(arg0));
  }

  public static Localizable localizableNON_INSTANTIATABLE_CLASS(Object arg0) {
    return messageFactory.getMessage("non.instantiatable.class", new Object[] { arg0 });
  }

  public static String NON_INSTANTIATABLE_CLASS(Object arg0)
  {
    return localizer.localize(localizableNON_INSTANTIATABLE_CLASS(arg0));
  }

  public static Localizable localizableAMBIGUOUS_PARAMETER(Object arg0, Object arg1) {
    return messageFactory.getMessage("ambiguous.parameter", new Object[] { arg0, arg1 });
  }

  public static String AMBIGUOUS_PARAMETER(Object arg0, Object arg1)
  {
    return localizer.localize(localizableAMBIGUOUS_PARAMETER(arg0, arg1));
  }

  public static Localizable localizableSUB_RES_METHOD_TREATED_AS_RES_METHOD(Object arg0, Object arg1) {
    return messageFactory.getMessage("sub.res.method.treated.as.res.method", new Object[] { arg0, arg1 });
  }

  public static String SUB_RES_METHOD_TREATED_AS_RES_METHOD(Object arg0, Object arg1)
  {
    return localizer.localize(localizableSUB_RES_METHOD_TREATED_AS_RES_METHOD(arg0, arg1));
  }

  public static Localizable localizableAMBIGUOUS_RMS_OUT(Object arg0, Object arg1, Object arg2, Object arg3) {
    return messageFactory.getMessage("ambiguous.rms.out", new Object[] { arg0, arg1, arg2, arg3 });
  }

  public static String AMBIGUOUS_RMS_OUT(Object arg0, Object arg1, Object arg2, Object arg3)
  {
    return localizer.localize(localizableAMBIGUOUS_RMS_OUT(arg0, arg1, arg2, arg3));
  }

  public static Localizable localizableUNABLE_TO_LOAD_CLASS(Object arg0) {
    return messageFactory.getMessage("unable.to.load.class", new Object[] { arg0 });
  }

  public static String UNABLE_TO_LOAD_CLASS(Object arg0)
  {
    return localizer.localize(localizableUNABLE_TO_LOAD_CLASS(arg0));
  }

  public static Localizable localizableAMBIGUOUS_SRLS(Object arg0, Object arg1, Object arg2) {
    return messageFactory.getMessage("ambiguous.srls", new Object[] { arg0, arg1, arg2 });
  }

  public static String AMBIGUOUS_SRLS(Object arg0, Object arg1, Object arg2)
  {
    return localizer.localize(localizableAMBIGUOUS_SRLS(arg0, arg1, arg2));
  }

  public static Localizable localizablePARAMETER_UNRESOLVABLE(Object arg0, Object arg1, Object arg2) {
    return messageFactory.getMessage("parameter.unresolvable", new Object[] { arg0, arg1, arg2 });
  }

  public static String PARAMETER_UNRESOLVABLE(Object arg0, Object arg1, Object arg2)
  {
    return localizer.localize(localizablePARAMETER_UNRESOLVABLE(arg0, arg1, arg2));
  }

  public static Localizable localizableERRORS_AND_WARNINGS_DETECTED_WITH_RESOURCE_CLASSES(Object arg0) {
    return messageFactory.getMessage("errors.and.warnings.detected.with.resource.classes", new Object[] { arg0 });
  }

  public static String ERRORS_AND_WARNINGS_DETECTED_WITH_RESOURCE_CLASSES(Object arg0)
  {
    return localizer.localize(localizableERRORS_AND_WARNINGS_DETECTED_WITH_RESOURCE_CLASSES(arg0));
  }

  public static Localizable localizableGET_RETURNS_VOID(Object arg0) {
    return messageFactory.getMessage("get.returns.void", new Object[] { arg0 });
  }

  public static String GET_RETURNS_VOID(Object arg0)
  {
    return localizer.localize(localizableGET_RETURNS_VOID(arg0));
  }
}