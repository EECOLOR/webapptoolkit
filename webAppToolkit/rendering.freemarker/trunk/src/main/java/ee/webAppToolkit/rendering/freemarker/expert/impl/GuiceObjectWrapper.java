package ee.webAppToolkit.rendering.freemarker.expert.impl;

import com.google.inject.ImplementedBy;

import freemarker.template.ObjectWrapper;

@ImplementedBy(DynamicObjectWrapper.class)
public interface GuiceObjectWrapper extends ObjectWrapper {

}
