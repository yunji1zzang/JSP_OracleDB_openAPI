package address.service;
//AddressService 클래스는 조회할 데이터가 존재하지 않으면
//익셉션을 발생시킨다. 먼저, address 테이블에 데이터가 존재하지 않을 때
//발생할 익셉션인 AddressInsertFailException 예외 처리를 구현한다.
public class AddressInsertFailException extends Exception {

}
