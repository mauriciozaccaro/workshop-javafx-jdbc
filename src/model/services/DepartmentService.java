package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	//MOCK -- dados de mentirinha .. tipo uma lista dentro do c�digo ao inv�s de usar o banco de dados
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return dao.findAll(); 
	}
}