# Introduction #

I could not find a Java web framework that was simple, lightweight, works with Guice and not full with features I would rarely use. So I decided to write another one.

The WebAppToolkit project was created in order to provide a minimal and lightweight framework for building web applications. The following things were kept in mind when designing the toolkit:

  * provide a way to bind requests to class methods
  * provide a way to convert form information into typed parameters of these objects
  * build the core of the toolkit with Guice as only dependency
  * keep the functionality to a minimum
  * keep it open and allow developers to replace each part of the toolkit
  * add functionality in the form of modules that can be installed