package novel.tool;
/**
 * 客户端和服务器之间的指令集
 * @author 22442
 *
 */
public enum Instruct {
	// 获取小说列表
	NOVEL_LIST,
	// 获取小说内容
	NOVEL_CONTENT,
	// 关闭连接
	CLOSE,
	// 文件发送结束
	FILE_EOF,
	// 上传图片
	UPLOAD_IMAGE,
	// 回复指令
	RETURN_UPLOAD_IMAGE,
	// 操作成功
	SUCCESS,
	// 操作失败
	FAILD
}
