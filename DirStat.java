public class DirStat {
    public long totalSize;
    public long totalNumberOfSubdirectories;
    public long totalNumberOfFiles;
    public long bigFiles;
    public long specialFiles;

    public DirStat() {
        totalSize = 0;
        totalNumberOfSubdirectories = 0;
        totalNumberOfFiles = 0;
        bigFiles = 0;
        specialFiles = 0;
    }

    public DirStat(long size, long subDir, long files, long big, long spec) {
        totalSize = size;
        totalNumberOfSubdirectories = subDir;
        totalNumberOfFiles = files;
        bigFiles = big;
        specialFiles = spec;
    }

    public DirStat add(DirStat other) {
        return new DirStat(
            totalSize + other.totalSize,
            totalNumberOfSubdirectories + other.totalNumberOfSubdirectories,
            totalNumberOfFiles + other.totalNumberOfFiles,
            bigFiles + other.bigFiles, 
            specialFiles + other.specialFiles
        );
    }

    public String toString() {
        double size = totalSize;
        String[] codes = {"B", "KB", "MB", "GB", "TB"};
        int code;
        for(code = 0; code < codes.length; code++) {
            if(size >= 1024) {
                size = size / 1024;
            } else {
                break;
            }
        }
        return String.format("Size : %f %s \n", size, codes[code])
             + String.format("Number of Subdirectories : %d \n", totalNumberOfSubdirectories)
             + String.format("Number of Files : %d \n", totalNumberOfFiles)
             + String.format("Number of Big Files : %d \n", bigFiles)
             + String.format("Number of Special Files : %d \n", specialFiles);
    }
}
