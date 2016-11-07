/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.trading_game.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 * Each instance of this class corresponds to a temporary folder for
 * arbitrary purposes. The folder is created by the constructor and
 * removed with close(). No automatic removal is done, so clients
 * should use a try-with-resource block to ensure disposal, unless
 * the folder is intentionally kept (e.g. for diagnostic purposes).
 * 
 * The temporary folder is created as a subfolder of resource/tmp.
 * 
 * TODO convert to an injected service
 */
public class TemporaryFolder implements Closeable {

	/**
	 * the parentFolder
	 */
	private static File parentFolder = null;

	/**
	 * the count
	 */
	private static long count = 0;

	/**
	 * the instanceId
	 */
	private long instanceId;

	/**
	 * the instanceFolder
	 */
	private File instanceFolder;

	//
	static {
		parentFolder = new File("/Users/martin/workspace/server-blob/resource/tmp");
		try {
			FileUtils.deleteDirectory(parentFolder);
		} catch (IOException e) {
			throw new RuntimeException("cannot delete previous temporary parent folder: " + parentFolder.getAbsolutePath());
		}
		if (!parentFolder.mkdir()) {
			throw new RuntimeException("cannot create temporary parent folder: " + parentFolder.getAbsolutePath());
		}
	}

	/**
	 * Allocates a sequential ID number for an instance folder.
	 * @return the id number
	 */
	private static synchronized long allocateId() {
		final long id = count;
		count++;
		return id;
	}

	/**
	 * Constructor.
	 */
	public TemporaryFolder() {
		this.instanceId = allocateId();
		this.instanceFolder = new File(parentFolder, "x" + instanceId);
		if (!instanceFolder.mkdir()) {
			throw new RuntimeException("cannot create temporary folder: " + instanceFolder.getAbsolutePath());
		}
	}

	/**
	 * Getter method for the instanceId.
	 * @return the instanceId
	 */
	public long getInstanceId() {
		return instanceId;
	}

	/**
	 * Getter method for the instanceFolder.
	 * @return the instanceFolder
	 */
	public File getInstanceFolder() {
		return instanceFolder;
	}
	
	/**
	 * Disposes of this folder. The id property is still valid after disposal,
	 * but the temporary folder itself is gone.
	 */
	@Override
	public void close() {
		try {
			FileUtils.deleteDirectory(instanceFolder);
		} catch (IOException e) {
			throw new RuntimeException("cannot delete temporary folder: " + instanceFolder.getAbsolutePath());
		}
		instanceFolder = null;
	}

}
