package address.model;

public class Address {
	
	private int address_id;
	private String basic_address;
	private String detail_address;
	
	
	public Address(int address_id, String basic_address, String detail_address) {
		this.address_id = address_id;
		this.basic_address = basic_address;
		this.detail_address = detail_address;
	}
	
	public int getAddress_id() {
		return address_id;
	}
	public void setAddress_id(int address_id) {
		this.address_id = address_id;
	}
	public String getBasic_address() {
		return basic_address;
	}
	public void setBasic_address(String basic_address) {
		this.basic_address = basic_address;
	}
	public String getDetail_address() {
		return detail_address;
	}
	public void setDetail_address(String detail_address) {
		this.detail_address = detail_address;
	}
}
