package com.model2.mvc.service.product.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.util.DBUtil;
import com.model2.mvc.service.domain.Product;

public class ProductDao {

	//field 

	//constructor
	public ProductDao() {
	}
	
	//method
	public void addProduct(Product product) throws Exception{
		
		Connection con = DBUtil.getConnection();
		
		String sql = "INSERT INTO product VALUES (SEQ_PRODUCT_PROD_NO.NEXTVAL,?,?,?,?,?,SYSDATE)";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, product.getProdName());
		pStmt.setString(2, product.getProdDetail());
		pStmt.setDate(3, product.getRegDate());
		pStmt.setInt(4, product.getPrice());
		pStmt.setString(5, product.getFileName());
		pStmt.executeUpdate();
		
		con.close();
	}

	public Product findProduct(int prodNo) throws Exception{
		
		Connection con = DBUtil.getConnection(); // 1. JDBC로 DB에 연결
		// "*" 써도 상관 없음.
		String sql = "SELECT * FROM product WHERE prod_no = ?";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setInt(1, prodNo);
		
		ResultSet rs = pStmt.executeQuery(); // 3. 조회된 결과 데이터
		
		Product product = null;
		
		while (rs.next()) {// 상품상세조회
			
			product = new Product();			
			product.setProdNo(rs.getInt("prod_no"));
			product.setProdName(rs.getString("prod_name"));
			product.setFileName(rs.getString("image_file"));
			product.setProdDetail(rs.getString("prod_detail"));
			product.setManuDate(rs.getString("MANUFACTURE_DAY"));
			product.setPrice(rs.getInt("price"));
			product.setRegDate(rs.getDate("reg_date"));
			
		}
		
		con.close();
		System.out.println(product);
		
		return product;
	}
	
	public Map<String, Object> getProductList(Search search) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 1. Connection 
		Connection con = DBUtil.getConnection();
		// 2. Statement
		String sql = "SELECT * FROM product";
		
		System.out.println("여기서 막힘");
		if (search.getSearchCondition() != null) {
			
			if(search.getSearchCondition().equals("0") &&  !search.getSearchKeyword().equals("") ) {
				sql += " WHERE prod_no LIKE '" + search.getSearchKeyword() +"%'";
			}
			else if(search.getSearchCondition().equals("1")&& !search.getSearchKeyword().equals("")) {
				sql += " WHERE prod_name LIKE '" + search.getSearchKeyword()+"%'";	
			}
			else if(search.getSearchCondition().equals("2")&& !search.getSearchKeyword().equals("")) {
				sql += " WHERE price LIKE '" + search.getSearchKeyword()+"%'";	
			}
		}
		sql += " ORDER BY prod_no";
		
		System.out.println("ProductDAO::Original SQL :: " + sql);
		
		int totalCount = this.getTotalCount(sql);
		System.out.println("ProductDao :: totalCount  :: " + totalCount);	
		
		//==> CurrentPage 게시물만 받도록 Query 다시구성
		sql = makeCurrentPageSql(sql, search);
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		
		System.out.println(search);
		
		
		List<Product> list = new ArrayList<Product>(); 
		
		while(rs.next()) {
			Product product = new Product();
			product.setProdNo(rs.getInt("prod_No"));
			product.setProdName(rs.getString("prod_name"));
			product.setPrice(rs.getInt("Price"));
			product.setRegDate(rs.getDate("reg_date"));;
			list.add(product);
		}
		
		map.put("totalCount", new Integer(totalCount));
		map.put("list", list);
		
		rs.close();
		pStmt.close();
		con.close();
		
		return map;
	}
	
	public void updateProduct(Product vo) throws Exception {
		
		Connection con = DBUtil.getConnection();
		
		String sql = "UPDATE product "+
								"SET prod_name = ? ,"+
								"prod_detail = ? ,"+
								"manufacture_day = ? ,"+
								"price = ? WHERE prod_no=?";
		PreparedStatement pStmt = con.prepareStatement(sql);
		pStmt.setString(1, vo.getProdName());
		pStmt.setString(2, vo.getProdDetail());
		pStmt.setString(3, vo.getManuDate());
		pStmt.setInt(4, vo.getPrice());
		pStmt.setInt(5, vo.getProdNo());
		pStmt.executeUpdate();
		
		con.close();
	}
	
	private int getTotalCount(String sql) throws Exception {
		
		sql = "SELECT COUNT(*) "+
		          "FROM ( " +sql+ ") countTable";
		
		Connection con = DBUtil.getConnection();
		PreparedStatement pStmt = con.prepareStatement(sql);
		ResultSet rs = pStmt.executeQuery();
		
		int totalCount = 0;
		if( rs.next() ){
			totalCount = rs.getInt(1);
		}
		
		pStmt.close();
		con.close();
		rs.close();
		
		return totalCount;
	}
	
	// 게시판 currentPage Row 만  return 
	private String makeCurrentPageSql(String sql , Search search){
		sql = 	"SELECT * "+ 
					"FROM (		SELECT inner_table. * ,  ROWNUM AS row_seq " +
									" 	FROM (	"+sql+" ) inner_table "+
									"	WHERE ROWNUM <="+search.getCurrentPage()*search.getPageSize()+" ) " +
					"WHERE row_seq BETWEEN "+((search.getCurrentPage()-1)*search.getPageSize()+1) +" AND "+search.getCurrentPage()*search.getPageSize();
		
		System.out.println("UserDAO :: make SQL :: "+ sql);	
		
		return sql;
	}
}
