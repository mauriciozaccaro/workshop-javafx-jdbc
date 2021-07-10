package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	//MOCK -- dados de mentirinha .. tipo uma lista dentro do código ao invés de usar o banco de dados
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return dao.findAll(); 
	}
	/*
	public Integer lastNumberSellerId() {
		return dao.lastNumber();
	}
	*/
	public void saveOrUpdate(Seller obj) { // saveOrUpdate Salve ou Atualize
		if(obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);		}
	}
	
	public void remove(Seller obj) {
		dao.deleteById(obj.getId());
	}
	
}
