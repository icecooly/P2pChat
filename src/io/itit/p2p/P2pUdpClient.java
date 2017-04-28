package io.itit.p2p;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author skydu
 *
 */
public class P2pUdpClient {
	//
	private AtomicLong requestId=new AtomicLong(1);
	//
	private static Logger logger=LoggerFactory.getLogger(P2pUdpClient.class);
	//
	private DatagramSocket client=null;
	private String clientId;
	//
	public void start() throws Exception{
		client = new DatagramSocket();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					byte[] buffer = new byte[1024];
					DatagramPacket receievePacket = new DatagramPacket(buffer,buffer.length);
					try {
						client.receive(receievePacket);
						onReceieveMessage(receievePacket);
						Thread.sleep(10000);//sleep
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	protected void onReceieveMessage(DatagramPacket packet) throws Exception {
		if(logger.isInfoEnabled()){
			logger.info("onReceieveMessage data:{} from {}:{}",new String(packet.getData())
				,packet.getAddress(),packet.getPort());
		}
		String json=new String(packet.getData(),"UTF-8");
		Message	message=JsonUtil.fromJson(json,Message.class);
		if(message.cmd.equals(Message.CMD_LOGIN)){//LOGIN
			clientId=message.rsp;
		}else if(message.cmd.equals(Message.CMD_LIST)){//LIST
			Map<String,String> clients=JsonUtil.fromJson(message.rsp,Map.class);
			if(clients.size()>1){
				for(Map.Entry<String,String> entry:clients.entrySet()){
					if(entry.getValue().equals(clientId)){
						continue;
					}
					//找一个客户端 发起P2PChat
					String[] ipPort=entry.getKey().split(":");
					int port=Integer.valueOf(ipPort[1]);
					InetAddress address = InetAddress.getByName(ipPort[0]);
					message=new Message();
					message.requestId=requestId.getAndIncrement();
					message.cmd=Message.CMD_P2PCHAT;
					byte[] data = JsonUtil.toJson(message).getBytes();
					DatagramPacket thepacket = new DatagramPacket(data,data.length,address,port);
					client.send(thepacket);
					break;
				}
			}
		}else if(message.cmd.equals(Message.CMD_P2PCHAT)){//P2P CHAT
			byte[] data=JsonUtil.toJson(message).getBytes();
			DatagramPacket rsp=new DatagramPacket(data,data.length,packet.getAddress(),packet.getPort());
			client.send(rsp);
		}
	}
	//
	private void send(String cmd) throws Exception{
		Message message=new Message();
		message.requestId=requestId.getAndIncrement();
		message.cmd=cmd;
		byte[] data = JsonUtil.toJson(message).getBytes();
		InetAddress server = InetAddress.getByName(P2pUdpServer.HOST);
		DatagramPacket thepacket = new DatagramPacket(data,data.length,server,P2pUdpServer.PORT);
		client.send(thepacket);
	}
	//
	public void login() throws Exception{
		send(Message.CMD_LOGIN);
	}
	//
	public void list() throws Exception{
		send(Message.CMD_LIST);
	}
	//
	public static void main(String[] args) throws Exception {
		P2pUdpClient client=new P2pUdpClient();
		client.start();
		client.login();
		Thread.sleep(5000);
		client.list();
	}
}
