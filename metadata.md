# Metadata #

Provides access to components of the [java-metadata-utils](http://code.google.com/p/java-metadata-utils/) project.

You could override the `PropertyMetadataType` using the following construct:

```
install(Modules.override(new MetadataModule()).with(new AbstractModule(){

	@Override
	protected void configure() {
		bind(PropertyMetadataType.class).toInstance(PropertyMetadataType.FIELDS);
	}
}));

```

The default value is `PropertyMetadataType.BOTH`.

## Dependencies ##

  * [java-metadata-utils-core](http://code.google.com/p/java-metadata-utils/) and java-metadata-utils-guice