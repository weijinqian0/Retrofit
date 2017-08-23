package com.example.weijinqian.retrofit;

/**
 * Created by weijinqian on 2017/8/23.
 */

class FilesManager {
    public static final String DIR_EXTERNAL_DOWNLOAD = "/.download/";

    private FilesManager(){}
    private volatile static FilesManager instance=null;
    public static FilesManager getInstance() {
        if (instance==null){
            synchronized (FilesManager.class){
                if (instance==null){
                    instance=new FilesManager();
                }
            }
        }
        return instance;
    }

    /**
     * 返回完整的私有外部文件的路径
     *
     * @param path 相对私有外部根目录的完整路径
     * @return 完整的文件的路径
     */
    public String getPrivateExternalPath(String path) {
//        return getDirProvider().getPrivateExternalRootDir() + deleteFileSeparatorInFirst(path);
        return "";
    }

}
