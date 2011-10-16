package ee.webAppToolkit.rendering.freemarker.utils.expert;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.rendering.freemarker.utils.expert.impl.UtilsTemplateModel;
import freemarker.template.TemplateModel;

@ImplementedBy(UtilsTemplateModel.class)
public interface CustomObjectTemplateModel extends TemplateModel
{
}
