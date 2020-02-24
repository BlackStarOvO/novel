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
 * ��װsocket����,�����������֮���ͨ��
 * 
 * @author 22442
 *
 */
public class ClientNet {

	public Socket socket;

	public DataInputStream dis;
	public DataOutputStream dos;
	// ��������ַ
	public String address;
	// �˿ں�
	public int port;

	public ClientNet(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public Socket getConnect() {
		return this.getConnect(address, port);
	}

	/**
	 * ��ȡ�����������������
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
	 * ��ȡioͨ��
	 * 
	 * @return
	 */
	public DataInputStream getDataInputStream() {
		if (dis == null) {
			/*
			 * ����socket
			 */
			if (getConnect(address, port) == null) {
				return null;
			}
			/*
			 * ��ȡio����
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
	 * ��ȡio����
	 * 
	 * @return
	 */
	public DataOutputStream getDataOutputStream() {
		if (dos == null) {
			/*
			 * ��ȡsocket����
			 */
			if (getConnect(address, port) == null) {
				return null;
			}
			/*
			 * ��ȡio
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
	 * �����������ָ��
	 * 
	 * @param instruct ָ��
	 * @param padding  ָ�����
	 * @return
	 */
	public boolean send(Instruct instruct, String padding) {
		/*
		 * ��ȡio
		 */
		DataOutputStream out = getDataOutputStream();
		// ����ʧ��
		if (out == null) {
			return false;
		}
		// ���ӳɹ�
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
	 * ���������������
	 * 
	 * @param instruct ָ��
	 * @param file  Ҫ���͵�����
	 * @return
	 */
	public boolean send(Instruct ins, File file) {
		/*
		 * ����ͷָ��,�з�����׼���ý����ļ�
		 */
		if(send(ins, "")) {
			System.out.println("���ڵȴ�������ȷ��...");
			// �ȴ��ظ�ָ��
			if(recv().equals(Instruct.RETURN_UPLOAD_IMAGE.name())) {
				// �����ļ�
				try {
					System.out.println("�����ϴ��ļ�...");
					byte[] bArr = new byte[1024];
					// ��ȡ������
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					while(bis.read(bArr) > 0) {
						dos.write(bArr);
					}
					dos.write(-1);
					dos.flush();
					bis.close();
					System.out.println("���ڵȴ���ִ��Ϣ...");
					// �ȴ���ִ
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
	 * ���շ��������͵�����
	 * 
	 * @return
	 */
	public String recv() {
		/*
		 * ���ӷ�����,��ȡioͨ��
		 */
		DataInputStream in = getDataInputStream();
		// ����ʧ��
		if (in == null) {
			return "����������ʧ��,����ϵ����Ա";
		}
		/*
		 * ��������
		 */
		try {
			return in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			return "���ݽ���ʧ��,����ϵ����Ա";
		}
	}

	/**
	 * �ر�����
	 * 
	 * @return
	 */
	public boolean close() {
		// ���ͽ���ָ��
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
