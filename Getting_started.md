# Requirements #

In order to use this toolkit you need to have the following:

  * A servlet container
  * [Google Guice](http://code.google.com/p/google-guice/) with `assisted inject`, `multibinding` and `servlet` extensions
  * [Guava collections](http://code.google.com/p/guava-libraries/)
  * WebAppToolkit [core](core.md)
  * `servlet api 2.5`
  * (Optional) Other WebAppToolkit extensions

# Server configuration #

Create a `web.xml` in the `WEB-INF` directory with the following content:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<!DOCTYPE web-app 
    PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" 
    "http://java.sun.com/dtd/web-app_2_3.dtd">
<web-app>
	<filter>
		<filter-name>guiceFilter</filter-name>
		<filter-class>com.google.inject.servlet.GuiceFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>guiceFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<listener>
		<listener-class>package.ApplicationConfig</listener-class>
	</listener>
</web-app>
```

Replace `package.ApplicationConfig` with your own implementation which should look like this:

```
public class ApplicationConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ApplicationModule());
	}
}
```

`ApplicationModule` is the class where you enter the Guice ecosystem and can set up bindings and controller mappings.

```
public class ApplicationModule extends WebAppToolkitModule {

	@Override
	protected void configureControllers() {
	}
}
```

# Creating a controller #

Create a class like this:

```
public class MainController extends BasicController {

	public Result index() {
		return output("Hello world!");
	}
}
```

_Note: web app toolkit does not require you to extend BasicController, we extended it as a convenience; it provides the output method_

# Mapping a controller #

Place the following line of code in the `configureControllers` method of `ApplicationModule`.

```
handle("").with(MainController.class);
```

# Test #

Run the application to find `Hello world!` in your browser.

# Website #

For more out-of-the-box features you can use the [website](website.md) module as a dependency. After adding the required dependencies you need to modify you `ApplicationModule` like this:

```
public class ApplicationModule extends WebsiteModule {

	@Override
	protected void configureApplication() {
		
	}

	@Override
	protected Class<?> getRootController() {
		return MainController.class;
	}
}
```