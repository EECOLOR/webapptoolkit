# Json #

The Json module allows you to communicate with clients using [JSON](http://en.wikipedia.org/wiki/Json).

It adds an `ActionArgumentResolver` for parameters annotated with `@Json`. In order to return results in JSON it provides a `JsonResult` class. Simple usage example:

```
public Result echo(@Json("data") Map<String, String> map)
{
	return new JsonResult(map);
}
```

## Dependencies ##

  * [core](core.md)
  * [gson](http://mavenhub.com/mvn/central/com.google.code.gson/gson/1.7.1)