package copyFilePack;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class svnOp {

	private static SVNClientManager ourClientManager;

	/***
	 * 初始化库
	 */
	private static void setupLibrary() {
		/*
		 * For using over http:// and https://
		 */
		DAVRepositoryFactory.setup();
		/*
		 * For using over svn:// and svn+xxx://
		 */
		SVNRepositoryFactoryImpl.setup();

		/*
		 * For using over file:///
		 */
		FSRepositoryFactory.setup();
	}
	
	public void getStatus()
	{
		String userName = "jihaibo";
		String pwd = "jihaibo";
		setupLibrary();
		ISVNOptions options=SVNWCUtil.createDefaultOptions(true);
		//System.out.println("显示的状态:");
		ourClientManager=SVNClientManager.newInstance(
				(DefaultSVNOptions)options,userName,pwd);
		File commitFile=new File("D:/android/22/ZMAndroidTest/testng.xml");
		SVNStatus status=null;
		try {
			status=ourClientManager.getStatusClient().doStatus(commitFile, true);
			if(status.getContentsStatus()==SVNStatusType.STATUS_UNVERSIONED){
			
			System.out.println("显示好啊");
			}
			//System.out.println("显示的状态:"+status.getContentsStatus());
			System.out.println("显示的状态1："+SVNStatusType.STATUS_UNVERSIONED);
			System.out.println("显示的值"+SVNStatusType.STATUS_CONFLICTED);
			System.out.println("显示的值是："+SVNStatusType.STATUS_MODIFIED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/***
	 * 获取svn版本
	 */
	public static void getSvnVer() {
		String userName = "test1";
		String pwd = "123456";
		try {
			SVNURL url = SVNURL
					.parseURIEncoded("http://192.168.0.102/svn/zhangmen/");
			DAVRepositoryFactory.setup();
			ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
			ISVNAuthenticationManager aum = SVNWCUtil
					.createDefaultAuthenticationManager(userName, pwd);
			SVNRepository repo = SVNRepositoryFactory.create(url);
			repo.setAuthenticationManager(aum);
			System.out.print("当前路径:" + repo.getLocation() + "\n");
			System.out.print("当前版本:" + repo.getLatestRevision());
		} catch (Exception e) {

		}

	}
	

	/****
	 * 从SVN checkout文件（代码）到本地
	 * @param httpUrl  #访问SVN地址
	 * @param desPath  #checkout文件到某个目录下
	 * @param name     #svn 用户名
	 * @param password #svn 用户密码
	 */
	public static void svnChekout(String httpUrl,String desPath,String name,String password ) {
		//初始化库。 必须先执行此操作。具体操作封装在setupLibrary方法中。
		setupLibrary();
		//相关变量赋值
		//String desPath="f:/checkout"; //checkout到某个文件下
		SVNURL repositoryURL = null;
		try {
			repositoryURL = SVNURL.parseURIEncoded(httpUrl);
		} catch (SVNException e) {
			System.out.println(e.getMessage());
		}	 	
		ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
		//实例化客户端管理类
		ourClientManager = SVNClientManager.newInstance(
				(DefaultSVNOptions) options, name, password);
		//要把版本库的内容check out到的目录
		File wcDir = new File(desPath);
		//通过客户端管理类获得updateClient类的实例。
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		//执行check out 操作，返回工作副本的版本号。
		long workingVersion = -1;
		try {
			workingVersion = updateClient
					.doCheckout(repositoryURL, wcDir, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY,false);
		} catch (SVNException e) { 
			e.printStackTrace();
		}
		System.out.println("把版本："+workingVersion+"以check out到目录："+wcDir+"中...");
	}
}
