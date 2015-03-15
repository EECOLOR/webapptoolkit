# Website #

Website is a module that can be used as a base for web applications that are websites. It installs the following modules:

  * [Localization](Localization.md)
  * [Parameters](Parameters.md)
  * [Render](Render.md)
  * [Navigation](Navigation.md)
  * [Freemarker](Freemarker.md)
    * [Freemarker navigation](Freemarker_navigation.md)
    * [Freemarker metadata](Freemarker_metadata.md)

On top of that it does the following:

  * overrides the `ModelWrapper` factory of `RenderModule` in order to supply a new `Model` that extends `SimpleModel` with `ValidationResults` and `SiteMap`.
  * adds a `TemplateLoader` with the path `WEB-INF/templates`.
  * adds an `ExceptionConverter` for `NumberFormatException`.

## Dependencies ##

  * [core](core.md)
  * [localization](localization.md)
  * [parameters](parameters.md)
  * [rendering](rendering.md)
  * [navigation](navigation.md)
  * [rendering](rendering.md)
  * [rendering.freemarker](rendering_freemarker.md)
  * [rendering.freemarker.navigation](rendering_freemarker_navigation.md)
  * [rendering.freemarker.metadata](rendering_freemarker_metadata.md)