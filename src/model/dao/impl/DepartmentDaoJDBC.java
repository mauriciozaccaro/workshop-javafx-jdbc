package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bd.DB;
import bd.DbException;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	Connection conn = null;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("INSERT INTO Department (Name) VALUES (?)");
			st.setString(1, obj.getName());
			
			int rowsAffectds = st.executeUpdate();
			if(rowsAffectds > 0) {
				System.out.println(rowsAffectds + " registro inserido!");
			}else {
				throw new DbException("Erro Inexperado! Nenhum dado inserido!");
			}
			
		}catch(SQLException e) {
			System.out.println("Erro ao Buscar Departamento: " + e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE Department SET Name = ? WHERE Id = ?");
			
			st.setInt(2, obj.getId());
			st.setString(1, obj.getName());
			
		int rowsUpdates = st.executeUpdate();
		if(rowsUpdates > 0) {
			System.out.println(rowsUpdates + " cadastros atualizados!");
		}else {
			throw new DbException("Erro Inesperado: Nenhum dado foi atualizado!");
		}
		
		} catch (SQLException e) {
			System.out.println("Erro ao Buscar Departamento: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		PreparedStatement st = null;
		PreparedStatement ss = null;
		try {
			st = conn.prepareStatement("DELETE FROM Department WHERE Department.Id = ?");
			st.setInt(1, id);
			
			 Department dap = departmentDao.findById(id);
			
			int rowsDeleted = st.executeUpdate();
			if(rowsDeleted > 0) {
				System.out.println("\n " + dap + "\n" + rowsDeleted + " registro(s) deletado(s)!");
			}else {
				throw new DbException("Erro Inesperado! Nenhum registro Deletado");
			}
		}catch (SQLException e) {
			System.out.println("Erro ao Buscar Departamento: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
				ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("SELECT * FROM Department WHERE Department.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dp = instanteateDepartment(rs);
				return dp;
			}
			
		}catch(SQLException e) {
			System.out.println("Erro ao Buscar Departamento: " + e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> listDep = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM Department");
			rs = st.executeQuery();
			
			while(rs.next()) {
				Department dep = instanteateDepartment(rs);
				listDep.add(dep);
			}
			return listDep;
			
		}catch(SQLException e) {
			System.out.println("Erro ao Buscar todos: " + e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return null;
	}

	private Department instanteateDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		
		return dep;
	}

	@Override
	public void deleteAll(Integer id) {
		
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> listDep = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement("DELETE FROM Department dt WHERE dt.Id = ?");
			
			listDep = departmentDao.findAllNull(); // puxa o select de tudo
			

			st.setInt(1, id);
			st.executeUpdate();
			
			
			int rowsDeleted = 0;
			for(Department dd : listDep) {
				Department dao = instanteateDepartment(rs);
				
				listDep.remove(dao);
				rowsDeleted ++;
			}
				
			System.out.println("Linhas deletadas: " + rowsDeleted);
		}catch(SQLException e) {
			System.out.println("Erro ao tentar deletar todos: " + e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public List<Department> findAllNull() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> listDep = new ArrayList<>();
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM Department dt WHERE dt.Name IS NULL");
			rs = st.executeQuery();
			
			while(rs.next()) {
				Department dep = instanteateDepartment(rs);
				listDep.add(dep);
			}
			return listDep;
			
		}catch(SQLException e) {
			System.out.println("Erro ao Buscar todos: " + e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return null;
	}

	@Override
	public Integer lastNumber() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		Integer lastNumberId = null;
		
		try {
			
			st = conn.prepareStatement("SELECT * FROM Department dp WHERE dp.Id IN ( "
									  +"SELECT MAX(Id)FROM Department)");
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dep = instanteateDepartment(rs);
				lastNumberId = dep.getId() + 1;
				return lastNumberId;
			}else {
				throw new DbException("Caiu no Else, do lasNumber()");
			}
			
		}catch(SQLException e) {
			System.out.println("Erro ao Buscar último Registro: " + e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		return null;
	}

	
}
