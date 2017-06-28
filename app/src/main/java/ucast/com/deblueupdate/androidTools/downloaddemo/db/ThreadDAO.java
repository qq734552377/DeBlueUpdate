package ucast.com.deblueupdate.androidTools.downloaddemo.db;

import java.util.List;


import ucast.com.deblueupdate.androidTools.downloaddemo.entities.ThreadInfo;

/**
 * 数据访问接口i
 * @author Just
 *
 */
public interface ThreadDAO {
	/**
	 * 插入线程信息
	 * @param threadInfo
	 */
	public void insertThreadInfo(ThreadInfo threadInfo);
	/**
	 * 删除线程信息
	 * @param url 文件的url
	 * @param id 线程的id
	 */
	public void deleteThreadInfo(String url,int id);
	/**
	 * 更新线程下载进度
	 */
	public void updateThreadInfo(String url,int threadId,int finished);
	/**
	 * 查询下载文件的线程信息
	 */
	public List<ThreadInfo> getThreads(String url);
	/**
	 * 判断指定线程信息是否已经存在数据库中
	 */
	public boolean isExists(String url,int threadId);
}
