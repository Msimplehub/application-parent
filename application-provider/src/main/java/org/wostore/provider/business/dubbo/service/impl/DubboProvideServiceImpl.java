package org.wostore.provider.business.dubbo.service.impl;

import org.springframework.stereotype.Service;
import org.wostore.provider.business.dubbo.service.DubboProvideService;

@Service
public class DubboProvideServiceImpl implements DubboProvideService {

	public String dubboMethod() {
		return "dubbo";
	}

}
