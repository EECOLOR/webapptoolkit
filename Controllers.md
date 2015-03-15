# Mapping controllers #

In the WebAppToolkit a controller is a simple Java class (or POJO) which is constructed using Guice allowing for dependency injection. It's public methods are called 'actions'. URL's are mapped directly to methods.

This example binds the main (or root) controller:

```
handle("").with(MainController.class);

public class MainController {
	public Result index() {
		...
	}

	public Result action1() {
		...
	}
}
```

This gives you the following site map:

```
/
/action1
```

When the WebAppToolkit receives a request it will try to match it to the deepest action available. `/action1/something` will result in a call to `action1`and `/action2` will result in a call to `index`

# Sub controllers #

Controllers can be placed in the context of another controller. This can be done in two ways: using annotations and by binding them in the configuration.

The following example binds `SubController` as a sub controller of the main controller: the url `/subController` now points to the `index` action of the sub controller.

```
@SubController(name="subController", type=SubController.class)
public class MainController {
	...
}
```

When you need to bind multiple sub controller you can use the `SubControllers` annotation.

```
@SubControllers({
	@SubController(name="subController1", type=SubController1.class)
	@SubController(name="subController2", type=SubController2.class)
})
```

If you do not want a controller to have a dependency on a sub controller you can add controllers as sub controllers using the configuration.

```
handle("").with(MainController.class);
handle("/subController").with(SubController.class);
```

This makes `SubController` a sub controller of `MainController`. This allows you to bind controllers as sub controllers of controllers you do not have control over (modules created by someone else or generic controllers).

# Wrapping #

Controllers can have control over the actions or sub controllers that are being executed and their result. This control is gained by implementing `WrappingController` which provides two methods.

Note that the controller and result arguments may be `null`. If the controller argument is `null` a sub controller is called that is not a `WrappingController` itself. This means it's not part of the handling chain and creating an instance might be overhead.

Consider the following request: `/wrappingController1/controller/wrappingController2/action`

`wrappingController1` will not get an instance of controller since there is no need to create an instance of `controller`.

## beforeHandling ##

```
void beforeHandling(String memberName, Object controller)
```

This allows you to do stuff before the action or sub controller is called. This is mostly used to check stuff and perform redirects if certain information is missing or invalid.

## wrapResult ##

```
Result wrapResult(Result result, String memberName, Object controller)
```

This allows you to modify or wrap the result of an action or sub controller. This can be useful to render results into part of the page. You can for example have your main controller render the site header and navigation and put the results of sub controllers into content area's. This allows you to create controllers that can be used in different parts of your application.