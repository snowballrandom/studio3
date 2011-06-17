package com.aptana.core.tests;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.debug.DebugOptions;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.aptana.core.CorePlugin;
import com.aptana.core.IDebugScopes;
import com.aptana.core.logging.IdeLog;
import com.aptana.core.util.EclipseUtil;
import com.aptana.core.util.StringUtil;

public class IdeLogTest extends TestCase
{
	public static String LOG_MESSAGE = "IdeLogTest";
	private LogListener listener;

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		// TODO Auto-generated method stub
		super.setUp();
		
		listener = new LogListener();
		CorePlugin.getDefault().getLog().addLogListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		// TODO Auto-generated method stub
		super.tearDown();

		if (listener != null)
		{
			CorePlugin.getDefault().getLog().removeLogListener(listener);
		}

	}

	/**
	 * Get message
	 * 
	 * @param severity
	 * @return
	 */
	private String getCustomMesssage(IdeLog.StatusLevel severity)
	{
		return LOG_MESSAGE + Long.toHexString(Double.doubleToLongBits(Math.random()));
	}

	/**
	 * Test to see if items are logged with correct severity
	 */
	public void testSeverityLogging()
	{
		boolean isDebugging = Platform.inDebugMode();
		if (isDebugging)
		{
			// Have to turn off platform debugging for a moment
			EclipseUtil.setPlatformDebugging(false);
		}
		
		IdeLog.StatusLevel currentSeverityPref = IdeLog.getCurrentSeverity();

		// We should no messages logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.OFF);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[OFF] should find 0 messages. Found " + StringUtil.join(",", listener.getMessages()), 0,
				listener.getMessageCount());

		// We should see errors logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.ERROR);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[OFF] should find 1 messages. Found " + StringUtil.join(",", listener.getMessages()), 1,
				listener.getMessageCount());

		// We should see errors and warnings logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.WARNING);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[OFF] should find 2 messages. Found " + StringUtil.join(",", listener.getMessages()), 2,
				listener.getMessageCount());

		// We should see errors, warnings, and infos logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.INFO);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[OFF] should find 3 messages. Found " + StringUtil.join(",", listener.getMessages()), 3,
				listener.getMessageCount());

		IdeLog.setCurrentSeverity(currentSeverityPref);
		EclipseUtil.setPlatformDebugging(isDebugging);
	}

	/**
	 * Test to see if items are logged with correct severity
	 */
	public void testSeverityLoggingDebuggerOn()
	{
		// even if debugging is on, this should not affect messages with no scopes attached
		boolean isDebugging = Platform.inDebugMode();
		if (!isDebugging)
		{
			// turn on debugging
			EclipseUtil.setPlatformDebugging(true);
		}

		IdeLog.StatusLevel currentSeverityPref = IdeLog.getCurrentSeverity();

		// We should no messages logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.OFF);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[OFF] should find 0 messages. Found " + StringUtil.join(",", listener.getMessages()), 0,
				listener.getMessageCount());

		// We should see errors logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.ERROR);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[ERROR] should find 1 message. Found " + StringUtil.join(",", listener.getMessages()), 1,
				listener.getMessageCount());

		// We should see errors and warnings logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.WARNING);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[WARNING] should find 2 messages. Found " + StringUtil.join(",", listener.getMessages()), 2,
				listener.getMessageCount());

		// We should see errors, warnings, and infos logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.INFO);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null, null);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, null);
		assertEquals("[INFO] should find 3 messages. Found " + StringUtil.join(",", listener.getMessages()), 3,
				listener.getMessageCount());

		IdeLog.setCurrentSeverity(currentSeverityPref);
		EclipseUtil.setPlatformDebugging(isDebugging);
	}

	public void testScopesDebuggerOff()
	{

		// If debugging is off, we write out messages independent of scopes
		// even if debugging is on, this should not affect messages with no scopes attached
		boolean isDebugging = Platform.inDebugMode();
		if (!isDebugging)
		{
			// turn on debugging
			EclipseUtil.setPlatformDebugging(false);
		}

		IdeLog.StatusLevel currentSeverityPref = IdeLog.getCurrentSeverity();

		// We should see all messages logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.INFO);
		listener.reset();
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null,
				IDebugScopes.INDEXER);
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, IDebugScopes.SHELL);
		assertEquals("Debugging off should find 3 messages. Found " + StringUtil.join(",", listener.getMessages()), 3,
				listener.getMessageCount());

		IdeLog.setCurrentSeverity(currentSeverityPref);
		EclipseUtil.setPlatformDebugging(isDebugging);

	}

	public void testScopesDebuggerOn()
	{

		// If debugging is on, we write out messages is the scope is null or there is a match

		// save current scope setting
		// set scope for CorePlugin on
		// write out messages with no scope
		// write out messages with wrong scope
		// write out messages with correct scope

		// If debugging is off, we write out messages independent of scopes
		// even if debugging is on, this should not affect messages with no scopes attached
		boolean isDebugging = Platform.inDebugMode();
		if (!isDebugging)
		{
			// turn on debugging
			EclipseUtil.setPlatformDebugging(false);
		}

		IdeLog.StatusLevel currentSeverityPref = IdeLog.getCurrentSeverity();

		BundleContext context = CorePlugin.getDefault().getContext();
		ServiceReference sRef = context.getServiceReference(DebugOptions.class.getName());
		DebugOptions options = (DebugOptions) context.getService(sRef);

		options.setOption(IDebugScopes.INDEXER, Boolean.toString(true));
		options.setOption(IDebugScopes.SHELL, Boolean.toString(false));

		// We should see all messages logged
		IdeLog.setCurrentSeverity(IdeLog.StatusLevel.INFO);
		listener.reset();
		IdeLog.logWarning(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.WARNING), null, null);
		IdeLog.logInfo(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.INFO), null, IDebugScopes.SHELL);
		IdeLog.logError(CorePlugin.getDefault(), getCustomMesssage(IdeLog.StatusLevel.ERROR), null,
				IDebugScopes.INDEXER);
		assertEquals("Debugging off should find 2 messages. Found " + StringUtil.join(",", listener.getMessages()), 3,
				listener.getMessageCount());

		IdeLog.setCurrentSeverity(currentSeverityPref);
		EclipseUtil.setPlatformDebugging(isDebugging);

	}

	class LogListener implements ILogListener
	{
		ArrayList<String> logMessages = new ArrayList<String>();

		public LogListener()
		{
		}

		public boolean foundMessage(String message)
		{
			return logMessages.contains(message);
		}

		public String[] getMessages()
		{
			return logMessages.toArray(new String[0]);
		}

		public int getMessageCount()
		{
			return logMessages.size();
		}

		public void reset()
		{
			logMessages = new ArrayList<String>();
		}

		public void logging(IStatus status, String plugin)
		{
			if (status.getMessage().contains(LOG_MESSAGE))
			{
				logMessages.add(status.getMessage());
			}
		}
	};

}
