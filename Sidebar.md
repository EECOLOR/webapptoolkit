`*` Module is an _abstract_ module and can not be used without an implementation. For example [rendering.freemarker] is an implementation of the [rendering](rendering.md) module.

`**` Module can best be extended by your applications module. For example `MyApplicationModule extends WebsiteModule`.

`***` Overrides bindings in another module and should be installed specifically