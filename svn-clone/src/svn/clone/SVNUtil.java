package svn.clone;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtil {

    /*第二步：
    *声明客户端管理类SVNClientManager。
    */
    private static SVNClientManager ourClientManager;

    /**
     * 获取SVN的版本库
     * @param userName
     * @param password
     * @return
     * @throws SVNException 
     */
    public static SVNRepository getRepository(String repositoryURL, String userName,
                                              String password) throws SVNException {
        SVNRepository repository = SVNRepositoryFactory.create(getSVNURL(repositoryURL));
        ISVNAuthenticationManager authManager = SVNWCUtil
            .createDefaultAuthenticationManager(userName, password);
        repository.setAuthenticationManager(authManager);
        return repository;
    }

    /**
     * 获取SVN的URL,地址格式为"http://xxx.xxx.xx/xxx"
     * @param repositoryURL
     * @return
     * @throws SVNException 
     */
    public static SVNURL getSVNURL(String repositoryURL) throws SVNException {
        if (repositoryURL == null || "".equals(repositoryURL)) {
            return null;
        }
        SVNURL parseURIEncoded = SVNURL.parseURIEncoded(repositoryURL);
        return parseURIEncoded;
    }

    /*
     * 此函数递归的获取版本库中某一目录下的所有条目。
     */
    public static List<SVNDirEntry> getDir(String repositoryURL, String userName, String password,
                                           String path) throws SVNException {
        SVNRepository repository = getRepository(repositoryURL, userName, password);
        return getDir(repository, path);
    }

    public static List<SVNDirEntry> getDir(SVNRepository repository,
                                           String path) throws SVNException {
        if (repository == null) {
            return null;
        }
        //获取版本库的path目录下的所有条目。参数－1表示是最新版本。
        Collection<SVNDirEntry> entries = repository.getDir(path, -1, null, SVNDirEntry.DIRENT_ALL,
            (Collection<?>) null);
        ArrayList<SVNDirEntry> arrayList = new ArrayList<>(entries);
        Collections.sort(arrayList);
        return arrayList;
    }

    /**
     * 返回版本库的Tag版本号
     * @param repositoryURL
     * @param userName
     * @param password
     * @return
     * @throws SVNException
     */
    public static List<String> getTags(String repositoryURL, String userName,
                                       String password) throws SVNException {
        List<SVNDirEntry> dir = getDir(repositoryURL, userName, password, "tags");
        if (dir == null || dir.isEmpty()) {
            return null;
        }
        List<String> tagList = new ArrayList<>();
        for (SVNDirEntry svnDirEntry : dir) {
            tagList.add(svnDirEntry.getName());
        }
        return tagList;
    }

    /**
     * 把SVN文件checkout到本地路径
     * @param repositoryURL
     * @param userName
     * @param password
     * @param path
     * @throws SVNException 
     * @throws FileExistsException 
     */
    public static void checkout(String repositoryURL, String userName, String password,
                                String path) throws SVNException {
        if (

        repositoryURL == null || "".equals(repositoryURL)) {
            return;
        }
        //工作副本目录
        /*工作副本目录创建*/
        File wcDir = new File(path);
        if (wcDir.exists()) {
            System.err.println(
                "the destination directory '" + wcDir.getAbsolutePath() + "' already exists!");
        }
        wcDir.mkdirs();
        //驱动选项
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        SVNClientManager svnClientManager = SVNClientManager
            .newInstance((DefaultSVNOptions) options, userName, password);
        SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
        updateClient.setIgnoreExternals(false);
        updateClient.doCheckout(getSVNURL(repositoryURL), wcDir, SVNRevision.HEAD, SVNRevision.HEAD,
            SVNDepth.INFINITY, true);
    }

    /**
     *
     * @param args
     * @throws SVNException
     * @throws FileExistsException 
     */
    public static void main(String[] args) throws SVNException {

    }
    //        /*第三步：
    //         * 对版本库进行初始化操作 (在用版本库进行其他操作前必须进行初始化) 
    //         * 对于通过使用 http:// 和 https:// 访问，执行DAVRepositoryFactory.setup();
    //         * 对于通过使用svn:// 和 svn+xxx://访问，执行SVNRepositoryFactoryImpl.setup();
    //         * 对于通过使用file:///访问，执行FSRepositoryFactory.setup();
    //         * 本程序框架用svn://来访问
    //         */
    //
    //        SVNRepositoryFactoryImpl.setup();
    //
    //        /*第四步：
    //         * 要访问版本库的相关变量设置
    //         */
    //        //版本库的URL地址
    //        SVNURL repositoryURL = getSVNURL(REPOSITORY_URL);
    //        if (repositoryURL == null) {
    //            return;
    //        }
    //        //工作副本目录
    //        String myWorkingCopyPath = "D:/MyWorkingCopy";
    //        //驱动选项
    //        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
    //        /*第五步：
    //         * 创建SVNClientManager的实例。提供认证信息（用户名，密码）
    //         * 和驱动选项。
    //         */
    //        ourClientManager = SVNClientManager
    //            .newInstance((DefaultSVNOptions) options, NAME, PASSWORD);
    //        /*第六步：
    //         * 通过SVNClientManager的实例获取要进行操作的client实例（如             *  SVNUpdateClient）
    //         * 通过client实例来执行相关的操作。
    //         * 此框架以check out操作来进行说明，其他操作类似。
    //         */
    //
    //        /*工作副本目录创建*/
    //        File wcDir = new File(myWorkingCopyPath);
    //        if (wcDir.exists()) {
    //            logger.error("the destination directory '" + wcDir.getAbsolutePath()
    //                         + "' already exists!");
    //        }
    //        wcDir.mkdirs();
    //
    //        try {
    //            /*
    //             * 递归的把工作副本从repositoryURL check out 到 wcDir目录。
    //             * SVNRevision.HEAD 意味着把最新的版本checked out出来。 
    //             */
    //            /*
    //             * 对版本库设置认证信息。
    //             */
    //            SVNRepository repository = SVNRepositoryFactory.create(repositoryURL);
    //            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
    //                NAME, PASSWORD);
    //            repository.setAuthenticationManager(authManager);
    //            //打印版本库的根
    //            System.out.println("Repository Root: " + repository.getRepositoryRoot(true));
    //            //打印出版本库的UUID
    //            System.out.println("Repository UUID: " + repository.getRepositoryUUID(true));
    //            System.out.println("" + repository.getLatestRevision());
    //            List<String> tags = getTags(REPOSITORY_URL, NAME, PASSWORD);
    //            for (String e : tags) {
    //                System.out.println(e);
    //            }
    //            //            打印版本库的目录树结构
    //            //            listEntries(repository, "src/test");
    //            //            SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
    //            //            updateClient.setIgnoreExternals(false);
    //            //            //检出资源
    //            //            updateClient.doCheckout(repositoryURL, wcDir, SVNRevision.HEAD, SVNRevision.HEAD,
    //            //                SVNDepth.INFINITY, true);
    //
    //        } catch (SVNException svne) {
    //            //
    //            logger.error(svne.toString());
    //        }
    //    }
}
