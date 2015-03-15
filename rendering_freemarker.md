# Freemarker #

The Freemarker module supplies an implementation of the [Render](Render.md) module using [Freemarker](http://freemarker.sourceforge.net/).

## Tag syntax ##

The tag syntax of freemarker is set to square bracket tag syntax by default. In order to override that you need to override the following binding:

```
bindConstant().annotatedWith(Names.named(FreemarkerModule.TAG_SYNTAX)).to(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
```

## Template loaders ##

Template loaders can be added like this:

```
Multibinder<TemplateLoader> templateLoaders = Multibinder.newSetBinder(binder(), TemplateLoader.class);
templateLoaders.addBinding().to(CustomTemplateLoader.class);
```

The [Website](Website.md) module adds a `TemplateLoader` for `WEB-INF/templates`.

## Model factories ##

If you extend the `FreemarkerModule` class you can use the `bindModelFactory` to bind a Freemarker `ModelFactory` to a specific type.


## Dependencies ##

  * [rendering](rendering.md)
  * [localization](localization.md)
  * [Freemarker](http://freemarker.sourceforge.net/)