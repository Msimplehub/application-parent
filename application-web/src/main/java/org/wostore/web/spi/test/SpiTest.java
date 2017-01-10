  package org.wostore.web.spi.test;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.wostore.web.spi.interfac.CommandService;

public class SpiTest {

	public static void main(String[] args) {
		ServiceLoader<CommandService> operations = ServiceLoader.load(CommandService.class);
		Iterator<CommandService> operationIterator = operations.iterator();
//		System.out.println("classPath:" + System.getProperty("java.class.path"));
		while (operationIterator.hasNext()) {
			CommandService commandService = operationIterator.next();
			commandService.testMethod();
		}
	}

}
