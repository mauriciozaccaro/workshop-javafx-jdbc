package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	//MOCK -- dados de mentirinha .. tipo uma lista dentro do código ao invés de usar o banco de dados
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return dao.findAll(); 
	}
	
	public Integer lastNumberDepartmentId() {
		return dao.lastNumber();
	}
	
	public void saveOrUpdate(Department obj) { // saveOrUpdate Salve ou Atualize
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);		}
	}
}
