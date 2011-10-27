package ee.webAppToolkit.storage.db4o;

import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.wideplay.warp.persist.PersistenceFilter;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;

import ee.objectCloner.CloneAdvisor;
import ee.objectCloner.guice.ObjectClonerModule;
import ee.webAppToolkit.storage.Store;

public class Db4oModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(Store.class).to(Db4oStore.class).asEagerSingleton();
		
		bind(Db4oInstantiationMemberInjector.class).asEagerSingleton();
		
		install(new ObjectClonerModule());
		install(getDb4oModule());
		
		filter("/*").through(PersistenceFilter.class);
		
		Multibinder<CloneAdvisor> cloneAdvisors = Multibinder.newSetBinder(binder(), CloneAdvisor.class, Names.named("cloneAdvisors"));
		cloneAdvisors.addBinding().to(IdentifiableCloner.class).asEagerSingleton();
	}

	
	/**
	 * If you are using transactions you might want to override this method in order to add a 
	 * matcher for the classes that can contain transactiongs, see: http://www.wideplay.com/transactionsemantics
	 */
	protected Module getDb4oModule() {
		return PersistenceService.usingDb4o().across(UnitOfWork.REQUEST).buildModule();
	}

}
