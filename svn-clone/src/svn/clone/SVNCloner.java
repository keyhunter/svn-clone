package svn.clone;
import org.tmatesoft.svn.core.SVNException;

public class SVNCloner {

    //这里填写你要克隆的SVN地址
    public static final String SVN_ADDRESS  = "SVN_URL1,SVN_URL2,SVN_URL3";

    //版本库的用户名
    private static String      SVN_NAME     = "user";
    //版本库的用户名密码
    private static String      SVN_PASSWORD = "password";
    //版本库的用户名密码
    private static String      TARGET_PATH  = "C:/svn/output/";

    public void cloneResources() {
        String[] addresses = SVN_ADDRESS.split(",");
        try {
            int i = 0;
            for (String address : addresses) {
                i++;
                System.out.println(address);
                SVNUtil.checkout(address, SVN_NAME, SVN_PASSWORD, TARGET_PATH + i);
            }
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SVNCloner svnCloner = new SVNCloner();
        svnCloner.cloneResources();
    }

}
