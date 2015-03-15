# Navigation #

The navigation module binds a `SiteMap` instance. This instance is a `Map<String, SiteMap>` and is filled using an `ActionRegistrationListener`. Every action that is registered gets a place in the `SiteMap`.

In order to prevent actions from being included in the `SiteMap` it provides an `@HideFromNavigation` annotation.

By default the action name is used as display name. You can however specify another display name:

```
@NavigationDisplayName(@LocalizedString("navigation.name"))
```

These names should be present in a properties file which should be added (if you are extending the `WebsiteModule` class) like this:

```
//within configureControllers
bindPropertiesToLocale("navigation", Locale.ENGLISH);
```

The [Website](Website.md) module includes the `SiteMap` into the model by default.

### Dependencies ###

  * [core](core.md)
  * [localization](localization.md)