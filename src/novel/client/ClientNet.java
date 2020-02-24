package novel.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import novel.tool.Instruct;

/**
 * 封装socket方法,处理与服务器之间的通信
 * 
 * @author 22442
 *
 */
public class ClientNet {

	public Socket socket;

	public DataInputStream dis;
	public DataOutputStream dos;
	// 服务器地址
	public String address;
	// 端口号
	public int port;

	public ClientNet(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public Socket getConnect() {
		return this.getConnect(address, port);
	}

	/**
	 * 获取与服务器创建的连接
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	public Socket getConnect(String address, int port) {
		if (socket == null || !socket.isConnected()) {
			try {
				socket = new Socket(address, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				return null;
			}
			this.address = address;
			this.port = port;
		}
		return socket;
	}

	/**
	 * 获取io通道
	 * 
	 * @return
	 */
	public DataInputStream getDataInputStream() {
		if (dis == null) {
			/*
			 * 连接socket
			 */
			if (getConnect(address, port) == null) {
				return null;
			}
			/*
			 * 获取io连接
			 */
			try {
				InputStream in = socket.getInputStream();
				dis = new DataInputStream(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return dis;
	}

	/**
	 * 获取io连接
	 * 
	 * @return
	 */
	public DataOutputStream getDataOutputStream() {
		if (dos == null) {
			/*
			 * 获取socket连接
			 */
			if (getConnect(address, port) == null) {
				return null;
			}
			/*
			 * 获取io
			 */
			try {
				OutputStream out = socket.getOutputStream();
				dos = new DataOutputStream(out);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return dos;
	}

	/**
	 * 
	 * 向服务器发送指令
	 * 
	 * @param instruct 指令
	 * @param padding  指令填充
	 * @return
	 */
	public boolean send(Instruct instruct, String padding) {
		/*
		 * 获取io
		 */
		DataOutputStream out = getDataOutputStream();
		// 连接失败
		if (out == null) {
			return false;
		}
		// 连接成功
		try {
			out.writeUTF(instruct.name() + "," + padding);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 向服务器发送数据
	 * 
	 * @param instruct 指令
	 * @param file  要发送的数据
	 * @return
	 */
	public boolean send(Instruct ins, File file) {
		/*
		 * 发送头指令,叫服务器准备好接收文件
		 */
		if(send(ins, "")) {
			System.out.println("正在等待服务器确认...");
			// 等待回复指令
			if(recv().equals(Instruct.RETURN_UPLOAD_IMAGE.name())) {
				// 发送文件
				try {
					System.out.println("正在上传文件...");
					byte[] bArr = new byte[1024];
					// 读取输入流
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					while(bis.read(bArr) > 0) {
						dos.write(bArr);
					}
					dos.write(-1);
					dos.flush();
					bis.close();
					System.out.println("正在等待回执信息...");
					// 等待回执
					return recv().equals(Instruct.SUCCESS.name());
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 接收服务器发送的数据
	 * 
	 * @return
	 */
	public String recv() {
		/*
		 * 连接服务器,获取io通道
		 */
		DataInputStream in = getDataInputStream();
		// 连接失败
		if (in == null) {
			return "服务器连接失败,请联系管理员";
		}
		/*
		 * 接收数据
		 */
		try {
			return in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			return "数据接收失败,请联系管理员";
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @return
	 */
	public boolean close() {
		// 发送结束指令
		if (send(Instruct.CLOSE, "")) {
			System.out.println("client close success...");
		}
		try {
			if (dos != null)
				dos.close();
			if (dis != null)
				dis.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
