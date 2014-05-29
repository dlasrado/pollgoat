package models;

public class FormParamBean {

	private String connectionName;
	
	private String collectionName;
	
	private String criteria;
	
	private String data;

	public FormParamBean(String connectionName,String collectionName,String criteria,String data){
		
		this.connectionName = connectionName;
		this.collectionName = collectionName;
		this.criteria = criteria;
		this.data = data;
	}
	
	public String getConnectionName() {
		return connectionName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public String getCriteria() {
		return criteria;
	}

	public String getData() {
		return data;
	}
	
	
	
	
}
