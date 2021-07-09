package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	//MOCK -- dados de mentirinha .. tipo uma lista dentro do código ao invés de usar o banco de dados
	public List<Department> findAll(){
		 
		List<Department> list = new ArrayList<>();
		list.add( new Department(1, "Vestuário"));
		list.add( new Department(2, "Eletrônicos"));
		list.add( new Department(3, "Linha Branca"));
		list.add( new Department(4, "Tapetes e Cortinas"));
		list.add( new Department(5, "Cama Mesa e Banho"));
		list.add( new Department(6, "Celulares"));
		
		return list;
	}
}
