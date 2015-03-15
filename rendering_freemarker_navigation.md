# Freemarker navigation #

This module provides a Freemarker template model for `SiteMap` and a navigation macro for Freemarker.

The macro is used like this:

```
[#include "/navigation.ftl" /]

[@navigation siteMap /]	
```

## Dependencies ##

  * [navigation](navigation.md)
  * [rendering.freemarker](rendering_freemarker.md)