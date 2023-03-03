package com.model2.mvc.view.product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.model2.mvc.framework.Action;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;


public class UpdateProductViewAction extends Action {
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		
		int prodNo=Integer.parseInt(request.getParameter("prodNo"));
		
		ProductService service = new ProductServiceImpl();
		Product product = service.getProduct(prodNo);
		
		request.setAttribute("product", product);
		// 각각 (속성명, 속성값)
		// 꼭 product1이라는 변수와 updateProduct.jsp의 속성 키 값과 같아야 한다.
		//System.out.println("up"+productVO);
		return "forward:/product/updateProduct.jsp"; //JSP 입력
	}
}
