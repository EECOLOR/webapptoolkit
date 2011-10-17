package ee.webAppToolkit.storage.db4o;

import org.modelmapper.ModelMapper;

import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import com.wideplay.warp.persist.PersistenceFilter;
import com.wideplay.warp.persist.PersistenceService;
import com.wideplay.warp.persist.UnitOfWork;

import ee.webAppToolkit.storage.Store;

public class Db4oModule extends ServletModule {

	@Override
	protected void configureServlets() {
		bind(Store.class).to(Db4oStore.class).asEagerSingleton();
		bind(ModelMapper.class).to(GuiceModelMapper.class).asEagerSingleton();
		
		install(getDb4oModule());
		
		filter("/*").through(PersistenceFilter.class);
	}

	
	/**
	 * If you are using transactions you might want to override this method in order to add a 
	 * matcher for the classes that can contain transactiongs, see: http://www.wideplay.com/transactionsemantics
	 */
	protected Module getDb4oModule() {
		return PersistenceService.usingDb4o().across(UnitOfWork.REQUEST).buildModule();
	}

}
