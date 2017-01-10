package org.wostore.web.business.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.wostore.customer.business.dubbo.service.DubboCustomerBusiness;
import org.wostore.web.business.test.vo.TestVo;

/**
 * 返回多视图
 * @className  MutiViewController 
 * @author  M.simple 
 * @datetime  2016年6月30日 上午10:58:36
 */
@Controller
@RequestMapping("testxx/")
public class MutiViewController {
	
	@Autowired
	private DubboCustomerBusiness dubboCustomerBusiness;
	
	@RequestMapping(value="test", method = RequestMethod.POST)
//	@ResponseBody
	public ModelAndView test(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		dubboCustomerBusiness.customerTest();
		TestVo testVo = new TestVo();
		testVo.setName("M.simple");
		testVo.setValue("26");
		System.out.println("dssssssssssssssgasgregerg");
		
		modelAndView.addObject("user", testVo);
		modelAndView.setViewName("/user/list");
	    return modelAndView;  
	}
	
	
}
