package io.itit.p2p;

/**
 * 
 * @author skydu
 *
 */
public class Message {
	//
	/**登录*/
	public static final String CMD_LOGIN="LOGIN";
	/**查询客户端列表*/
	public static final String CMD_LIST="LIST";
	/**P2P聊天*/
	public static final String CMD_P2PCHAT="P2PCHAT";
	//
	public long requestId;
	
	public String cmd;
	
	public String rsp;
}
