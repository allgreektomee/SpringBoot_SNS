package com.sns.board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;


@Component
public class SnsDAO {
	final String JDBC_DRIVER = "org.h2.Driver";
	final String JDBC_URL = "jdbc:h2:tcp://localhost/~/test";
	
	
	// DB 연결을 가져오는 메서드, DBCP를 사용하는 것이 좋음
	public Connection open() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL,"sa","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public Long addPost(Sns n) throws Exception {
		Connection conn = open();
		
		String sql = "insert into sns(title,img,date,content) values(?,?,CURRENT_TIMESTAMP(),?)";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
		PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	
		Long sid = (long) -10;
		
		try(conn; pstmt) {
			pstmt.setString(1, n.getTitle());
			pstmt.setString(2, n.getImg());
			pstmt.setString(3, n.getContent());
			pstmt.executeUpdate();
			
			
			//insert 후 sns sid 가져오기 
			ResultSet rs = pstmt.getGeneratedKeys();

			if (rs.next()) {
				
				sid = rs.getLong(1);
		
				return sid;
			}

		}
		
		return sid;
		
	}

	

	public List<Sns> getAllPost() throws Exception {
		Connection conn = open();
		List<Sns> newsList = new ArrayList<>();
		
//		String sql = "select sid, title, PARSEDATETIME(date,'yyyy-MM-dd hh:mm:ss') as cdate from sns";
		String sql = "select * from sns";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs) {
			while(rs.next()) {
				Sns n = new Sns();
				n.setSid(rs.getInt("sid"));
				n.setTitle(rs.getString("title"));
				n.setDate(rs.getString("date")); 
				n.setImg(rs.getString("img")); 
				n.setContent(rs.getString("content")); 
				
				newsList.add(n);
			}
			return newsList;			
		}
	}
	
	public Sns getPost(int sid) throws SQLException {
		Connection conn = open();
		
		Sns n = new Sns();
		String sql = "select sid, title, img, date, content from sns where sid=?";
	
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, sid);
		ResultSet rs = pstmt.executeQuery();
		
		rs.next();
		
		try(conn; pstmt; rs) {
			n.setSid(rs.getInt("sid"));
			n.setTitle(rs.getString("title"));
			n.setImg(rs.getString("img"));
			n.setDate(rs.getString("date"));
			n.setContent(rs.getString("content"));
			pstmt.executeQuery();
			return n;
		}
	}
	
	public void delPost(int sid) throws SQLException {
		Connection conn = open();
		
		String sql = "delete from sns where sid = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setInt(1, sid);
			// 삭제된 post 없을 경우
			if(pstmt.executeUpdate() == 0) {
				throw new SQLException("DB에러");
			}
		}
	}
	
	public void updatePost(Sns n, int sid) throws Exception {
		Connection conn = open();
		
		String sql = "update sns set title = ?, date = CURRENT_TIMESTAMP(), content = ? where sid = ? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
	
		
		try(conn; pstmt) {
			pstmt.setString(1, n.getTitle());
			pstmt.setString(2, n.getContent());
			pstmt.setInt(3, sid);
			pstmt.executeUpdate();
		}
		
	}
	
	// 
	// 다중파일 업로드 
	//
	public void addFile(String fileName, String fileCode) throws Exception {
		Connection conn = open();
		
		String sql = "insert into file(filename,filecode) values(?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
	
		
		try(conn; pstmt) {
			pstmt.setString(1, fileName);
			pstmt.setString(2, fileCode);
			pstmt.executeUpdate();
		}
		
	}
	
	public List<String> getImgaes(String fileCode) throws Exception {
		Connection conn = open();
		List<String> newsList = new ArrayList<>();
		
		
		String sql = "select filename from file where filecode = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, fileCode);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs) {
			while(rs.next()) {
				
				newsList.add(fileCode+"/"+rs.getString("filename"));
			}
			return newsList;			
		}
	}
	
	public void delFile(String fileCode) throws SQLException {
		Connection conn = open();
		
		String sql = "delete from file where filecode = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setString(1, fileCode);
			// 삭제된 post 없을 경우
			if(pstmt.executeUpdate() == 0) {
				throw new SQLException("DB에러");
			}
		}
	}
}

/* 파일 테이블 추가 
 Create table file(
	id INT NOT NULL AUTO_INCREMENT,
	filename VARCHAR NOT NULL  PRIMARY KEY,
	filecode VARCHAR NOT NULL
)
*/
