/**
 * Copyright (c) 2015 Martin Geisse
 * <p>
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.gui.wicket;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import name.martingeisse.trading_game.game.GlobalGameLock;
import name.martingeisse.trading_game.gui.MainPage;
import name.martingeisse.trading_game.gui.MapPage;
import name.martingeisse.trading_game.gui.wicket.page.AbstractPage;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.BufferedResponseRequestHandler;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.IProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * The Wicket {@link WebApplication} implementation.
 */
public class MyWicketApplication extends WebApplication {

	/**
	 * the RENDER_PROFILING
	 */
	public static final boolean RENDER_PROFILING = false;

	private final Injector injector;

	/**
	 * Constructor.
	 *
	 * @param injector the Guice injector
	 */
	@Inject
	public MyWicketApplication(final Injector injector) {
		this.injector = injector;
	}

	/**
	 * Retrieves a dependency from the injector.
	 *
	 * @param type the type of the object to retrieve
	 * @return the object
	 */
	public <T> T getDependency(final Class<T> type) {
		return injector.getInstance(type);
	}

	/**
	 * Retrieves a set of dependencies from the injector.
	 *
	 * @param type the type of the objects to retrieve
	 * @return the objects
	 */
	public <T> Set<T> getDependencies(final Class<T> type) {
		@SuppressWarnings("unchecked")
		TypeLiteral<Set<T>> typeLiteral = (TypeLiteral<Set<T>>) TypeLiteral.get(Types.setOf(type));
		return injector.getInstance(Key.get(typeLiteral));
	}

	/**
	 * Getter method for the injector.
	 *
	 * @return the injector
	 */
	public Injector getInjector() {
		return injector;
	}

	/**
	 * @return the application
	 */
	public static MyWicketApplication get() {
		return (MyWicketApplication) WebApplication.get();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();

		// settings
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("utf-8");

		// register merged resources ("resource bundles")
		/*
		CssResourceReference[] mergedStylesheets = {
			new CssResourceReference(AbstractFrontendPage.class, "common.css"),
		};
		JavaScriptResourceReference[] mergedJavascripts = {
			JQueryResourceReference.get(),
			(JavaScriptResourceReference)getJavaScriptLibrarySettings().getWicketEventReference(),
			(JavaScriptResourceReference)(getDebugSettings().isAjaxDebugModeEnabled() ? getJavaScriptLibrarySettings().getWicketAjaxDebugReference() : getJavaScriptLibrarySettings().getWicketAjaxReference()),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "fastclick.js"),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "jquery.tools.min.js"),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "jquery.scrollTo.min.js"),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "common.js"),
		};
		getResourceBundles().addCssBundle(AbstractFrontendPage.class, "merged.css", mergedStylesheets);
		getResourceBundles().addJavaScriptBundle(AbstractFrontendPage.class, "merged.js", mergedJavascripts);


		// create CSS sprite atlases
		getSpriteRegistry().register(true,
			new PackageResourceReference(PaymentIcons.class, "invoice.png"),
			new PackageResourceReference(PaymentIcons.class, "mastercard.png"),
			new PackageResourceReference(PaymentIcons.class, "paypal.png"),
			new PackageResourceReference(PaymentIcons.class, "payu.png"),
			new PackageResourceReference(PaymentIcons.class, "prepay.png"),
			new PackageResourceReference(PaymentIcons.class, "sue.png"),
			new PackageResourceReference(PaymentIcons.class, "visa.png"),
			new PackageResourceReference(PaymentIcons.class, "cc.png"),
			new PackageResourceReference(PaymentIcons.class, "coll_store.png"),
			new PackageResourceReference(PaymentIcons.class, "cod.png"),
			new PackageResourceReference(PaymentIcons.class, "mcm.png"),
			new PackageResourceReference(PaymentIcons.class, "debit.png")
		);
		*/
		//		ApplicationSpriteSupport.initialize(this);
		//		ApplicationSpriteSupport.get().getSpriteRegistry().register(true,
		//			new PackageResourceReference(Dummy.class, "de.png"),
		//			new PackageResourceReference(Dummy.class, "gb.png"),
		//			new PackageResourceReference(Dummy.class, "us.png")
		//		);

		// --- mount pages ---
		// main pages
		//		mountPage("foo", FooPage.class);
		//		mountPage("bar/${id}", BarPage.class);
		// internal

		mountPage("map", MapPage.class);

		// mount Bootstrap fonts
		{
			final String[] bootstrapFontFiles = new String[]{
				"glyphicons-halflings-regular.eot",
				"glyphicons-halflings-regular.woff",
				"glyphicons-halflings-regular.woff2",
				"glyphicons-halflings-regular.ttf",
				"glyphicons-halflings-regular.svg",
			};
			for (final String fontFile : bootstrapFontFiles) {
				mountResource("/fonts/" + fontFile, new PackageResourceReference(AbstractPage.class, fontFile));
			}
		}

		// acquire the global lock for the Game object during all requests except a few whitelisted ones that don't
		// need that object. In particular, both rendering requests and listener requests need the lock.
		// TODO render-buffer requests probably don't!
		getRequestCycleListeners().add(new AbstractRequestCycleListener() {

			@Override
			public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
				if (handler instanceof BufferedResponseRequestHandler) {
					// this just renders the actions from a buffered response, so the Game object isn't involved
					return;
				}
				if (handler instanceof ResourceReferenceRequestHandler) {
					// TODO should limit this to package resources because other resources might use the Game object
					return;
				}
				GlobalGameLock.onBeginRequest((HttpServletRequest) cycle.getRequest().getContainerRequest());
			}

			@Override
			public void onDetach(RequestCycle cycle) {
				GlobalGameLock.onFinishRequest((HttpServletRequest) cycle.getRequest().getContainerRequest());
			}
		});

	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return MainPage.class;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getExceptionMapperProvider()
	 */
	@Override
	public IProvider<IExceptionMapper> getExceptionMapperProvider() {
		return new IProvider<IExceptionMapper>() {

			@Override
			public IExceptionMapper get() {
				return new MyExceptionMapper();
			}
		};
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new MyWicketSession(request);
	}

}
