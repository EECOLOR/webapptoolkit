package ee.webAppToolkit.freemarker.metadata.expert;

import com.google.inject.ImplementedBy;

import ee.webAppToolkit.freemarker.metadata.expert.impl.MetadataTemplateModel;
import freemarker.template.TemplateModel;

@ImplementedBy(MetadataTemplateModel.class)
public interface CustomObjectTemplateModel extends TemplateModel
{
}
