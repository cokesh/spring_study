package com.my.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.my.dto.OrderInfo;
import com.my.dto.OrderLine;
import com.my.dto.Product;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.sql.MyConnection;

public class OrderOracleRepository implements OrderRepository {

   @Override
   public void insert(OrderInfo info) throws AddException {
      Connection con = null;
      try {
         con = MyConnection.getConnection();
         insertInfo(con, info);
         insertLines(con, info.getLines());
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         MyConnection.close(null, con);
      }
   }
   private void insertInfo(Connection con, OrderInfo info) throws SQLException{
      PreparedStatement pstmt = null;
      String insertInfoSQL = 
            "INSERT INTO order_info(ORDER_NO,ORDER_ID,ORDER_DT) VALUES (order_seq.NEXTVAL, ?, SYSDATE)";
      pstmt = con.prepareStatement(insertInfoSQL);
      pstmt.setString(1, info.getOrderId());
      pstmt.executeUpdate();
   }
   private void insertLines(Connection con, List<OrderLine> lines) throws SQLException{
      PreparedStatement pstmt = null;
      String insertLineSQL = 
            "INSERT INTO order_line(ORDER_NO, ORDER_PROD_NO,ORDER_QUANTITY) VALUES (order_seq.CURRVAL, ?, ?)";
      pstmt = con.prepareStatement(insertLineSQL);
      for(OrderLine line: lines) {
         String prodNo = line.getOrderP().getProdNo();
         int orderQuantity = line.getOrderQuantity();
         pstmt.setString(1, prodNo);
         pstmt.setInt(2, orderQuantity);
         pstmt.addBatch();
      }
      pstmt.executeBatch();
   }
    @Override
   public List<OrderInfo> selectById(String orderId) throws FindException {
    	Connection con = null;
	      try {
	         con = MyConnection.getConnection();
	         Statement stmt = null;
	         String selectOrder = "SELECT * FROM ORDER_LINE, ORDER_INFO, PRODUCT WHERE ORDER_LINE.ORDER_NO = ORDER_INFO.ORDER_NO";
	         stmt = con.createStatement();
	         ResultSet rs = stmt.executeQuery(selectOrder);
	         List<OrderInfo> info = new ArrayList<>();
	         
	         while(rs.next()) {
	        	 int orderNo = rs.getInt("order_no");
	        	 String orderIds = rs.getString("order_id");
	        	 Date orderDt = rs.getDate("order_dt");
	        	 int orderQtt = rs.getInt("order_quantity");
	        	 String orderProdNo = rs.getString("order_prod_no");
	        	 
	        	 String prodName = rs.getString("prod_name");
	        	 int prodPrice = rs.getInt("prod_price");
	        	 
	        	 Product orderP = new Product(orderProdNo, prodName, prodPrice);
	        	 List<OrderLine> orderLine = new ArrayList<>();
	        	 // 만약 장바구니의 주문번호가 같다면 기존 번호에 추가 시켜야함.
	        	 // 현재 같은 장바구니의 목록에 들어가지 않고 있는 상태임
	        	 orderLine.add(new OrderLine(orderNo, orderP, orderQtt));
	        	 OrderInfo orderInfo = new OrderInfo(orderNo, orderIds, orderDt, orderLine);
	        	 for ( int i = 0; i < info.size(); i++ ) {
	        		 if (info.get(i).getOrderNo() == orderInfo.getOrderNo()) {
	        			 // 현재주문목록의 번호 받아와서 추가하려는 주문목록번호와 같다면 해당 번호주문목록에 추가함 
	        			 orderInfo.setOrderNo(info.get(i).getOrderNo());
	        			 info.add(orderInfo);
	        			 
	        		 } else {
	        			 info.add(new OrderInfo(orderNo, orderIds, orderDt, orderLine));
	        			 
	        		 }
	        		 
	        	 }
	        	 
	         }
	         return info;
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         MyConnection.close(null, con);
	      }
		return null;
	}

}