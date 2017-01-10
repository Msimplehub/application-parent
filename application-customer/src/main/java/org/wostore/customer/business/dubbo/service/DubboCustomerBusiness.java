package org.wostore.customer.business.dubbo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wostore.provider.business.dubbo.service.DubboProvideService;

@Component
public class DubboCustomerBusiness {
	
	@Autowired
	private DubboProvideService dubboProvideService;
	
	public void customerTest(){
		String dubboMethod = dubboProvideService.dubboMethod();
		System.out.println(dubboMethod);
		System.out.println("----------------------");
	}
}
