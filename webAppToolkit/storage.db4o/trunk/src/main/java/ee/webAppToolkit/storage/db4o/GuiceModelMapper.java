package ee.webAppToolkit.storage.db4o;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.config.Configuration;
import org.modelmapper.spi.ConditionalConverter;

import com.google.inject.Injector;

import ee.metadataUtils.ClassUtils;

@Singleton
public class GuiceModelMapper extends ModelMapper {

	@Inject
	public GuiceModelMapper(final Injector injector) {
		Configuration configuration = getConfiguration();
		configuration.enableFieldMatching(true);
		configuration.setProvider(new Provider<Object>() {

			@Override
			public Object get(Provider.ProvisionRequest<Object> request) {
				Class<Object> requestedType = request.getRequestedType();

				if (requestedType.isEnum()
						|| ClassUtils.isWrapper(requestedType)
						|| ClassUtils.isPrimitive(requestedType)) {
					return null;
				}

				return injector.getInstance(requestedType);
			}

		});

		List<ConditionalConverter<?, ?>> converters = configuration
				.getConverters();

		for (ConditionalConverter<?, ?> converter : converters) {
			String simpleName = converter.getClass().getSimpleName();
			if (simpleName.equals("AssignableConverter")) {
				converters.remove(converter);
				break;
			}
		}
	}
}
