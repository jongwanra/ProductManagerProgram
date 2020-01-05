package ProductPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class ProductDAOMe {
	String jdbcDriver = "com.mysql.jdbc.Driver";
	String jdbcUrl = "jdbc:mysql://localhost:3306/Products";
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	// 콤보박스 아이템 관리를 위한 벡터
	Vector<String> items = null;
	String sql;
	
	// DB 연결 메서드
		public void connectDB() {
			try {
				Class.forName(jdbcDriver);
				conn = DriverManager.getConnection(jdbcUrl, "root", "dreamele19!");
				System.out.println("Completed connect DB");

			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		// DB 연결 종료 메서드
		void closeDB() {
			try {

				pstmt.close();
				rs.close();
				conn.close();
				System.out.println("Completed close DB");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	// 전체 Product로 구성된 ArrayList를 리턴
	// 전체 상품 목록을 가져오는 메소드

	public ArrayList<ProductMe> getAll() {
		connectDB();
		sql = "select * from product";

		// 전체 검색 데이터를 전달하는 ArrayList
		ArrayList<ProductMe> datas = new ArrayList<ProductMe>();

		// 관리번호 콤보박스 데이터를 위한 벡터 초기화
		items = new Vector<String>();
		items.add("전체");

		try {
			//place that i don't know
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// until here
			while (rs.next()) {
				ProductMe p = new ProductMe();
				p.setPrcode(rs.getInt("prcode"));
				p.setPrname(rs.getString("prname"));
				p.setPrice(rs.getInt("price"));
				p.setManufacture(rs.getString("manufacture"));
				datas.add(p);
				items.add(String.valueOf(rs.getInt("prcode")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return datas;

	}

	



	// 파라미터의 prcode에 해당하는 상품을 리턴
	public ProductMe getProduct(int prcode) {
		connectDB();
		sql = "select * from product where prcode = ?";
		ProductMe p = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			rs = pstmt.executeQuery();

			rs.next();
			p = new ProductMe();
			p.setPrcode(rs.getInt("prcode"));
			p.setPrname(rs.getString("prname"));
			p.setPrice(rs.getInt("price"));
			p.setManufacture(rs.getString("manufacture"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return p;

	}

	// 파라미터의 Product 객체의 내용을 DB에 저장
	public boolean newProduct(ProductMe product) {
		connectDB();
		sql = "insert into product (prname, price, manufacture) values(?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product.getPrname());
			pstmt.setInt(2, product.getPrice());
			pstmt.setString(3, product.getManufacture());
			pstmt.executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	// 파라미터의 prcode 에 해당하는 상품을 삭제
	boolean delProduct(int prcode) {
		connectDB();
		sql = "delete from product where prcode = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, prcode);
			pstmt.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	// 파라미터의 Product 객체의 내용으로 업데이트
	boolean updateProduct(ProductMe product) {
		connectDB();
		sql = "update product set prname = ?, price = ?, manufacture = ? where prcode = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, product.getPrname());
			pstmt.setInt(2, product.getPrice());
			pstmt.setString(3, product.getManufacture());
			pstmt.setInt(4, product.getPrcode());
			pstmt.executeUpdate();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	// 콤보박스용 관리번호 목록을 리턴
	Vector<String> getItems() {
		return items;
	}

}
