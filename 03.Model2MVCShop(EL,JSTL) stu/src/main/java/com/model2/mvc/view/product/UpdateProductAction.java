package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class UpdateProductAction extends Action {
	
	public String execute(	HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		int prodNo = Integer.parseInt(request.getParameter("prodNo"));
		//System.out.println(prodNo);

		//UpdateProductViewAction VO = new UpdateProductViewAction();
		Product product = new Product();
		//VO.execute(request, response);
		
		
		//System.out.println("EE"+product);
		//service.getProduct(prodNo);
		product.setProdNo(prodNo);
		product.setProdName(request.getParameter("prodName"));
		product.setProdDetail(request.getParameter("prodDetail"));
		product.setManuDate(request.getParameter("manuDate"));
		product.setPrice(Integer.parseInt(request.getParameter("price")));
		product.setFileName(request.getParameter("fileName"));
		ProductService service = new ProductServiceImpl();
		service.updateProduct(product);
		System.out.println("EE"+product);
		
		return "forward:/getProduct.do?prodNo="+prodNo;
	}
}
