package io.itit.p2p;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author skydu
 *
 */
public class P2pUdpServer{
	//
	private static Logger logger=LoggerFactory.getLogger(P2pUdpServer.class);
	//
	public static final String HOST = "127.0.0.1";
	public static final int PORT = 9999;
	//
	private final static int MAX_PACKET_SIZE = 1024;
	//
	private DatagramSocket server;
	private Map<String,String> clients;
	private AtomicInteger clientId=new AtomicInteger(1);
	//
	public P2pUdpServer(){
		clients=new ConcurrentHashMap<>();
	}
	//
	public void start() throws SocketException{
		server = new DatagramSocket(PORT);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						byte[] buffer = new byte[MAX_PACKET_SIZE];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						server.receive(packet);
						onReceievePacket(packet);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		//
		logger.info("Server Start port:{}",PORT);
	}
	//
	public void onReceievePacket(DatagramPacket packet) throws Exception{
		if(logger.isInfoEnabled()){
			logger.info("onReceieveMessage data:{} from {}:{}",new String(packet.getData()),
					packet.getAddress(),packet.getPort());
		}
		InetAddress clientAddress = packet.getAddress(); 
        int clientPort = packet.getPort();
        String key=clientAddress.getHostAddress()+":"+clientPort;
        if(!clients.containsKey(key)){//新增一个客户端
        	clients.put(key,clientId.getAndIncrement()+"");
        }
        String clientId=clients.get(key);
        String json=new String(packet.getData(),"UTF-8");
        Message message=JsonUtil.fromJson(json,Message.class);
        String cmd=message.cmd;
        //
        if(cmd.equals(Message.CMD_LOGIN)){
        	message.rsp=clientId;
        }else if(cmd.equals(Message.CMD_LIST)){
        	message.rsp=JsonUtil.toJson(clients);
        }else{
        	throw new IllegalArgumentException(cmd+" not support");
        }
        byte[] data=JsonUtil.toJson(message).getBytes();
		packet = new DatagramPacket(data,data.length,clientAddress,clientPort);
		server.send(packet);
	}
	//
	public static void main(String[] args) throws SocketException {
		P2pUdpServer server=new P2pUdpServer();
		server.start();
	}
}
