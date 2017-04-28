# P2PChat(使用UDP打洞技术实现P2P聊天)
======

本机调试方法
==============

1.启动P2pUdpServer 服务端

	[main] INFO io.itit.p2p.P2pUdpServer - Server Start port:9999

2.启动P2pUdpClient 客户端1

	[main] INFO io.itit.p2p.P2pUdpClient - Client Start
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"LOGIN","requestId":1,"rsp":"1"} from /127.0.0.1:9999
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"LIST","requestId":2,"rsp":"{\"127.0.0.1:57561\":\"1\"}"} from /127.0.0.1:9999


3.启动P2pUdpClient 客户端2

	[main] INFO io.itit.p2p.P2pUdpClient - Client Start
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"LOGIN","requestId":1,"rsp":"2"} from /127.0.0.1:9999
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"LIST","requestId":2,"rsp":"{\"127.0.0.1:57561\":\"1\",\"127.0.0.1:57562\":\"2\"}"} from /127.0.0.1:9999
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561

4.客户端2收到P2PCHAT包后，就可以关闭服务端,两个客户端每隔5秒进行通信

	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561
	[Thread-0] INFO io.itit.p2p.P2pUdpClient - onReceieveMessage data:{"cmd":"P2PCHAT","requestId":3} from /127.0.0.1:57561

